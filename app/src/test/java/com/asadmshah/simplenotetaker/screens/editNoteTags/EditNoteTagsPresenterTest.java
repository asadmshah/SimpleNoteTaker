package com.asadmshah.simplenotetaker.screens.editNoteTags;

import android.content.res.Resources;
import android.graphics.Color;

import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.database.InMemoryDatabase;
import com.asadmshah.simplenotetaker.utils.ErrorConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import rx.plugins.RxResetRule;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EditNoteTagsPresenterTest {

    @Rule public RxResetRule rxRule = new RxResetRule();

    @Mock private EditNoteTagsContract.Parent parent;
    @Mock private EditNoteTagsContract.View view;
    @Mock private Resources resources;

    @Captor private ArgumentCaptor<List<NoteTag>> noteTagCaptor;
    @Captor private ArgumentCaptor<Boolean> tagsLoadingStateCaptor;
    @Captor private ArgumentCaptor<ErrorConstants> errorCaptor;

    private Database database;
    private EditNoteTagsContract.Presenter presenter;

    @Before
    public void setUp() {

        database = new InMemoryDatabase();
        database.insertNote("A1", "B1");
        database.insertTag("T1", 1);
        database.insertTag("T2", 2);
        database.insertTag("T3", 3);
        database.insertTag("T4", 4);
        database.tagNote(1, 1);
        database.tagNote(1, 3);

        presenter = new EditNoteTagsPresenter(parent, view, database, 1);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void reloadTags() {
        presenter.reloadTags();

        verify(view, times(2)).setTagsLoadingState(tagsLoadingStateCaptor.capture());
        verify(view).tagsDataSetChanged();
        verify(view, never()).showError(any());

        assertThat(tagsLoadingStateCaptor.getAllValues(), contains(true, false));

        assertThat(presenter.getNoteTagsDataSource().getCount(), is(4));
        assertThat(presenter.getNoteTagsDataSource().getNoteTag(0).isTagged(), is(true));
        assertThat(presenter.getNoteTagsDataSource().getNoteTag(1).isTagged(), is(false));
        assertThat(presenter.getNoteTagsDataSource().getNoteTag(2).isTagged(), is(true));
        assertThat(presenter.getNoteTagsDataSource().getNoteTag(3).isTagged(), is(false));
    }

    @Test
    public void onToggleTag() {
        presenter.reloadTags();
        presenter.onToggleTag(0);
        presenter.onToggleTag(1);

        ArgumentCaptor<Integer> positionCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(view, times(2)).tagChanged(positionCaptor.capture());

        assertThat(positionCaptor.getAllValues(), contains(0, 1));
        assertThat(presenter.getNoteTagsDataSource().getNoteTag(0).isTagged(), is(false));
        assertThat(presenter.getNoteTagsDataSource().getNoteTag(1).isTagged(), is(true));
    }

    @Test
    public void onCreateTagLabelChanged() {
        presenter.onCreateTagColorChanged(Color.BLACK);
        presenter.onCreateTagLabelChanged("");
        presenter.onCreateTagLabelChanged("new label");

        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        verify(view, times(3)).showTagCreateButton(captor.capture());

        assertThat(captor.getAllValues(), contains(false, false, true));
    }

    @Test
    public void onCreateTagColorChanged() {
        presenter.onCreateTagLabelChanged("");
        presenter.onCreateTagLabelChanged("new label");
        presenter.onCreateTagColorChanged(Color.BLACK);

        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        verify(view, times(3)).showTagCreateButton(captor.capture());

        assertThat(captor.getAllValues(), contains(false, false, true));
    }

    @Test
    public void onCreateTagSuccess() {
        presenter.reloadTags();
        presenter.onCreateTagLabelChanged("T0");
        presenter.onCreateTagColorChanged(Color.BLACK);
        presenter.onCreateTag();
        presenter.onCreateTagLabelChanged("T25");
        presenter.onCreateTagColorChanged(Color.BLACK);
        presenter.onCreateTag();
        presenter.onCreateTagLabelChanged("T5");
        presenter.onCreateTagColorChanged(Color.BLACK);
        presenter.onCreateTag();

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(view, times(3)).tagAdded(captor.capture());
        verify(view, times(3)).clearNewTagLabel();
        verify(view, times(3)).clearNewTagColor();

        List<Integer> captured = captor.getAllValues();
        assertThat(captured, contains(0, 3, 6));
        assertThat(presenter.getNoteTagsDataSource().getNoteTag(captured.get(0)).getTag().label(), is("T0"));
        assertThat(presenter.getNoteTagsDataSource().getNoteTag(captured.get(0)).isTagged(), is(true));
        assertThat(presenter.getNoteTagsDataSource().getNoteTag(captured.get(1)).getTag().label(), is("T25"));
        assertThat(presenter.getNoteTagsDataSource().getNoteTag(captured.get(1)).isTagged(), is(true));
        assertThat(presenter.getNoteTagsDataSource().getNoteTag(captured.get(2)).getTag().label(), is("T5"));
        assertThat(presenter.getNoteTagsDataSource().getNoteTag(captured.get(2)).isTagged(), is(true));
    }

    @Test
    public void onCreateTagFailLabelRequired() {
        presenter.onCreateTagColorChanged(Color.BLACK);
        presenter.onCreateTag();

        verify(view).showError(errorCaptor.capture());
        verify(view, never()).tagAdded(anyInt());

        assertThat(errorCaptor.getValue(), is(ErrorConstants.NEW_TAG_REQUIRES_LABEL));
    }

    @Test
    public void onCreateTagFailColorRequired() {
        presenter.onCreateTagLabelChanged("Label");
        presenter.onCreateTag();

        verify(view).showError(errorCaptor.capture());
        verify(view, never()).tagAdded(anyInt());

        assertThat(errorCaptor.getValue(), is(ErrorConstants.NEW_TAG_REQUIRES_COLOR));
    }

    @Test
    public void onBackPressed() {
        presenter.onBackPressed();
        verify(parent).closeEditNoteTagsScreen();
    }

}
