package com.asadmshah.simplenotetaker.screens.editNoteTags;

import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.models.Tag;
import com.asadmshah.simplenotetaker.utils.ErrorConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditNoteTagsPresenter implements EditNoteTagsContract.Presenter {

    private final EditNoteTagsContract.Parent parent;
    private final EditNoteTagsContract.View view;
    private final Database database;
    private final long noteId;
    private final NoteTagsDataSource noteTagsDataSource = new NoteTagsDataSource() {
        @Override
        public NoteTag getNoteTag(int position) {
            return noteTagsList.get(position);
        }

        @Override
        public int getCount() {
            return noteTagsList != null ? noteTagsList.size() : 0;
        }
    };

    private List<NoteTag> noteTagsList;
    private String newTagLabel = "";
    private int newTagColor = -1;

    public EditNoteTagsPresenter(EditNoteTagsContract.Parent parent, EditNoteTagsContract.View view, Database database, long noteId) {
        this.parent = parent;
        this.view = view;
        this.database = database;
        this.noteId = noteId;
    }

    public NoteTagsDataSource getNoteTagsDataSource() {
        return noteTagsDataSource;
    }

    @Override
    public void reloadTags() {
        view.setTagsLoadingState(true);
        Observable<List<Tag>> observable1 = database.getTags();
        Observable<List<Tag>> observable2 = database.getTagsOfNote(noteId);
        Observable.combineLatest(observable1, observable2, (List<Tag> tags1, List<Tag> tags2) -> {
                    Set<Tag> tagged = new HashSet<>(tags2);
                    List<NoteTag> result = new ArrayList<>(tags1.size());
                    for (Tag tag : tags1) {
                        result.add(NoteTag.create(tag, tagged.contains(tag)));
                    }
                    return result;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .first()
                .subscribe(
                        (List<NoteTag> tags) -> {
                            noteTagsList = tags;
                            view.tagsDataSetChanged();
                            view.setTagsLoadingState(false);
                        },
                        (Throwable e) -> {
                            view.setTagsLoadingState(false);
                            view.showTagCreateButton(false);
                            view.showError(ErrorConstants.UNABLE_TO_LOAD_TAGS);
                        }
                );
    }

    @Override
    public void onToggleTag(int position) {
        NoteTag oldNoteTag = noteTagsList.get(position);
        NoteTag newNoteTag;
        if (oldNoteTag.isTagged()) {
            if (!database.unTagNote(noteId, oldNoteTag.getTag().id())) {
                view.showError(ErrorConstants.UNABLE_TO_UNTAG_NOTE);
                return;
            } else {
                newNoteTag = NoteTag.create(oldNoteTag.getTag(), false);
            }
        } else {
            if (!database.tagNote(noteId, oldNoteTag.getTag().id())) {
                view.showError(ErrorConstants.UNABLE_TO_TAG_NOTE);
                return;
            } else {
                newNoteTag = NoteTag.create(oldNoteTag.getTag(), true);
            }
        }
        noteTagsList.remove(position);
        noteTagsList.add(position, newNoteTag);
        view.tagChanged(position);
    }

    @Override
    public void onCreateTagLabelChanged(String newText) {
        newTagLabel = newText;
        view.showTagCreateButton(newTagLabel.length() > 0 && newTagColor != -1);
    }

    @Override
    public void onCreateTagColorChanged(int color) {
        newTagColor = color;
        view.showTagCreateButton(newTagLabel.length() > 0);
        view.selectTagColor(color);
    }

    @Override
    public void onCreateTag() {
        if (newTagLabel.length() == 0) {
            view.showError(ErrorConstants.NEW_TAG_REQUIRES_LABEL);
            return;
        }
        if (newTagColor == -1) {
            view.showError(ErrorConstants.NEW_TAG_REQUIRES_COLOR);
            return;
        }
        Tag tag = database.insertTag(newTagLabel, newTagColor);
        if (tag == null) {
            view.showError(ErrorConstants.UNABLE_TO_CREATE_TAG);
            return;
        }
        view.clearNewTagColor();
        view.clearNewTagLabel();
        view.showTagCreateButton(false);
        newTagLabel = "";
        newTagColor = -1;
        NoteTag noteTag = NoteTag.create(tag, database.tagNote(noteId, tag.id()));
        int i;
        for (i = 0; i < noteTagsList.size(); i++) {
            if (noteTagsList.get(i).getTag().label().compareTo(noteTag.getTag().label()) > 0) {
                break;
            }
        }
        noteTagsList.add(i, noteTag);
        view.tagAdded(i);
    }

    @Override
    public void onBackPressed() {
        parent.closeEditNoteTagsScreen();
    }

}
