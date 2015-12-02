package com.asadmshah.simplenotetaker.database;

import android.support.v4.util.Pair;

import com.asadmshah.simplenotetaker.models.Note;
import com.asadmshah.simplenotetaker.models.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import rx.Observable;
import rx.subjects.PublishSubject;

public class InMemoryDatabase implements Database {

    private final AtomicLong notesCounter = new AtomicLong();
    private final AtomicLong tagsCounter = new AtomicLong();
    private final AtomicLong noteTagsCounter = new AtomicLong();

    private final Map<Long, Note> notesMap = new HashMap<>();
    private final Map<Long, Tag> tagsMap = new HashMap<>();
    private final Map<Long, Pair<Long, Long>> noteTagsMap = new HashMap<>();

    private final PublishSubject<Object> notesChangePublisher = PublishSubject.create();
    private final PublishSubject<Object> tagsChangePublisher = PublishSubject.create();
    private final PublishSubject<Object> noteTagsChangePublisher = PublishSubject.create();

    @Override
    public Observable<List<Note>> getNotes() {
        Observable<List<Note>> observable1 = Observable.just(_getNotesList());
        Observable<List<Note>> observable2 = notesChangePublisher.asObservable()
                .flatMap(o -> Observable.just(_getNotesList()));
        return Observable.merge(observable1, observable2);
    }

    @Override
    public Observable<List<Note>> getNotesOfTag(final long tagId) {
        Observable<List<Note>> observable1 = Observable.just(_getNotesOfTag(tagId));
        Observable<List<Note>> observable2 = noteTagsChangePublisher.asObservable()
                .flatMap(o -> Observable.just(_getNotesOfTag(tagId)));
        return Observable.merge(observable1, observable2);
    }

    @Override
    public Observable<Note> getNote(final long noteId) {
        Observable<Note> observable1 = Observable.just(notesMap.get(noteId));
        Observable<Note> observable2 = notesChangePublisher.asObservable()
                .flatMap(o -> Observable.just(notesMap.get(noteId)));
        return Observable.merge(observable1, observable2);
    }

    @Override
    public Observable<List<Tag>> getTags() {
        Observable<List<Tag>> observable1 = Observable.just(_getTagsList());
        Observable<List<Tag>> observable2 = tagsChangePublisher.asObservable()
                .flatMap(o -> Observable.just(_getTagsList()));
        return Observable.merge(observable1, observable2);
    }

    @Override
    public Observable<List<Tag>> getTagsOfNote(final long noteId) {
        Observable<List<Tag>> observable1 = Observable.just(_getTagsOfNote(noteId));
        Observable<List<Tag>> observable2 = noteTagsChangePublisher.asObservable()
                .flatMap(o -> Observable.just(_getTagsOfNote(noteId)));
        return Observable.merge(observable1, observable2);
    }

    @Override
    public Note insertNote(String title, String text) {
        long id = notesCounter.incrementAndGet();
        Note note = Note.create(id, title, text, System.currentTimeMillis());
        notesMap.put(id, note);
        notesChangePublisher.onNext(null);
        return note;
    }

    @Override
    public boolean deleteNote(long noteId) {
        if (notesMap.remove(noteId) == null) {
            return false;
        }
        Iterator<Map.Entry<Long, Pair<Long, Long>>> iterator = noteTagsMap.entrySet().iterator();
        Map.Entry<Long, Pair<Long, Long>> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (entry.getValue().first == noteId) {
                iterator.remove();
            }
        }
        noteTagsMap.remove(noteId);
        notesChangePublisher.onNext(null);
        return true;
    }

    @Override
    public boolean updateNote(long noteId, String title, String text) {
        Note oldNote = notesMap.remove(noteId);
        if (oldNote == null) {
            return false;
        }
        notesMap.put(noteId, Note.create(noteId, title, text, oldNote.createdOn()));
        notesChangePublisher.onNext(null);
        noteTagsChangePublisher.onNext(null);
        return true;
    }

    @Override
    public Tag insertTag(String label, int color) {
        long id = tagsCounter.incrementAndGet();
        Tag tag = Tag.create(id, label, color);
        tagsMap.put(id, tag);
        tagsChangePublisher.onNext(null);
        return tag;
    }

    @Override
    public boolean deleteTag(long tagId) {
        if (tagsMap.remove(tagId) == null) {
            return false;
        }
        Iterator<Map.Entry<Long, Pair<Long, Long>>> iterator = noteTagsMap.entrySet().iterator();
        Map.Entry<Long, Pair<Long, Long>> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (entry.getValue().second == tagId) {
                iterator.remove();
            }
        }
        tagsChangePublisher.onNext(null);
        noteTagsChangePublisher.onNext(null);
        return true;
    }

    @Override
    public boolean updateTag(long tagId, String label, int color) {
        Tag oldTag = tagsMap.remove(tagId);
        if (oldTag == null) {
            return false;
        }
        tagsMap.put(tagId, Tag.create(tagId, label, color));
        tagsChangePublisher.onNext(null);
        noteTagsChangePublisher.onNext(null);
        return true;
    }

    @Override
    public boolean tagNote(long noteId, long tagId) {
        if (notesMap.containsKey(noteId) && tagsMap.containsKey(tagId)) {
            for (Pair<Long, Long> pair : noteTagsMap.values()) {
                if (pair.first == noteId && pair.second == tagId) {
                    return false;
                }
            }
            noteTagsMap.put(noteTagsCounter.incrementAndGet(), new Pair<>(noteId, tagId));
            noteTagsChangePublisher.onNext(null);
            return true;
        }
        return false;
    }

    @Override
    public boolean unTagNote(long noteId, long tagId) {
        Iterator<Map.Entry<Long, Pair<Long, Long>>> iterator = noteTagsMap.entrySet().iterator();
        Map.Entry<Long, Pair<Long, Long>> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (entry.getValue().first == noteId && entry.getValue().second == tagId) {
                iterator.remove();
                noteTagsChangePublisher.onNext(null);
                return true;
            }
        }
        return false;
    }

    private List<Note> _getNotesList() {
        List<Note> notes = new ArrayList<>(notesMap.values());
        Collections.sort(notes, (lhs, rhs) -> {
            Long lhsDate = lhs.createdOn();
            Long rhsDate = rhs.createdOn();
            return lhsDate.compareTo(rhsDate);
        });
        Collections.reverse(notes);
        return notes;
    }

    private List<Tag> _getTagsList() {
        List<Tag> tags = new ArrayList<>(tagsMap.values());
        Collections.sort(tags, (lhs, rhs) -> lhs.label().compareTo(rhs.label()));
        return tags;
    }

    private List<Tag> _getTagsOfNote(long noteId) {
        if (!notesMap.containsKey(noteId)) {
            return null;
        }
        List<Tag> tags = new ArrayList<>();
        for (Pair<Long, Long> pair : noteTagsMap.values()) {
            if (pair.first == noteId) {
                tags.add(tagsMap.get(pair.second));
            }
        }
        Collections.sort(tags, (lhs, rhs) -> lhs.label().compareTo(rhs.label()));
        return tags;
    }

    private List<Note> _getNotesOfTag(long tagId) {
        if (!tagsMap.containsKey(tagId)) {
            return null;
        }
        List<Note> notes = new ArrayList<>();
        for (Pair<Long, Long> pair : noteTagsMap.values()) {
            if (pair.second == tagId) {
                notes.add(notesMap.get(pair.first));
            }
        }
        Collections.sort(notes, (lhs, rhs) -> {
            Long l = lhs.createdOn();
            Long r = rhs.createdOn();
            return l.compareTo(r);
        });
        Collections.reverse(notes);
        return notes;
    }

}
