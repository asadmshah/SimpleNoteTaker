package com.asadmshah.simplenotetaker.screens.noteDetail;

import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.database.InMemoryDatabase;
import com.asadmshah.simplenotetaker.models.Note;
import com.asadmshah.simplenotetaker.utils.ErrorConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.plugins.RxResetRule;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NoteDetailPresenterTest {

    @Rule public RxResetRule rxRule = new RxResetRule();

    @Mock private NoteDetailContract.Parent parent;
    @Mock private NoteDetailContract.View view;

    @Captor private ArgumentCaptor<Boolean> stateEnabledCaptor;
    @Captor private ArgumentCaptor<Boolean> stateLoadingCaptor;
    @Captor private ArgumentCaptor<Note> noteCaptor;
    @Captor private ArgumentCaptor<ErrorConstants> errorCaptor;

    private Database database;
    private NoteDetailContract.Presenter presenter;

    @Before
    public void setUp() {
        database = new InMemoryDatabase();
        database.insertNote("A1", "B1");
        database.insertNote("A2", "B2");
        database.insertTag("T1", 1);
        database.insertTag("T2", 2);
        database.insertTag("T3", 3);
        database.tagNote(1, 1);
        database.tagNote(1, 3);
    }

    @Test
    public void loadNoteSuccess() {
        presenter = new NoteDetailPresenter(view, parent, database, 1);
        presenter.loadNote();

        verify(view, times(2)).setNoteStateEnabled(stateEnabledCaptor.capture());
        verify(view, times(2)).setNoteStateLoading(stateLoadingCaptor.capture());
        verify(view).noteLoaded(noteCaptor.capture());
        verify(view, never()).showError(any());

        assertThat(stateEnabledCaptor.getAllValues(), contains(false, true));
        assertThat(stateLoadingCaptor.getAllValues(), contains(true, false));
        assertThat(noteCaptor.getValue().title(), is("A1"));
    }

    @Test
    public void loadNoteFail() {
        presenter = new NoteDetailPresenter(view, parent, database, 3);
        presenter.loadNote();

        verify(view, times(2)).setNoteStateEnabled(stateEnabledCaptor.capture());
        verify(view, times(2)).setNoteStateLoading(stateLoadingCaptor.capture());
        verify(view, never()).noteLoaded(any());
        verify(view).showError(errorCaptor.capture());

        assertThat(stateEnabledCaptor.getAllValues(), contains(false, false));
        assertThat(stateLoadingCaptor.getAllValues(), contains(true, false));
        assertThat(errorCaptor.getValue(), is(ErrorConstants.NOTE_DOES_NOT_EXIST));
    }

    @Test
    public void updateNoteSuccess() {
        presenter = new NoteDetailPresenter(view, parent, database, 1);
        presenter.loadNote();
        presenter.updateNote("Updated A1", "");

        verify(view, times(2)).noteLoaded(noteCaptor.capture());
        verify(view, never()).showError(any());

        assertThat(noteCaptor.getValue().title(), is("Updated A1"));
    }

    @Test
    public void updateNoteFail() {
        presenter = new NoteDetailPresenter(view, parent, database, 3);
        presenter.updateNote("Updated A3", "");

        verify(view).showError(errorCaptor.capture());

        assertThat(errorCaptor.getValue(), is(ErrorConstants.UNABLE_TO_UPDATE_NOTE));
    }

    @Test
    public void deleteNoteSuccess() {
        presenter = new NoteDetailPresenter(view, parent, database, 1);
        presenter.deleteNote();

        verify(view).setNoteStateEnabled(stateEnabledCaptor.capture());
        verify(view, never()).showError(any());

        assertThat(stateEnabledCaptor.getAllValues(), contains(false, false));
    }

    @Test
    public void deleteNoteFail() {
        presenter = new NoteDetailPresenter(view, parent, database, 3);
        presenter.deleteNote();

        verify(view).setNoteStateEnabled(stateEnabledCaptor.capture());
        verify(view).showError(errorCaptor.capture());

        assertThat(stateEnabledCaptor.getAllValues(), contains(false, false));
        assertThat(errorCaptor.getValue(), is(ErrorConstants.UNABLE_TO_DELETE_NOTE));
    }

    @Test
    public void editTags() {
        presenter = new NoteDetailPresenter(view, parent, database, 1);
        presenter.editTags();

        verify(parent).requestEditTagsScreen(1);
    }

    @Test
    public void unSubscribeFromNoteUpdates() {
        presenter = new NoteDetailPresenter(view, parent, database, 1);
        presenter.loadNote();
        presenter.unSubscribeFromNoteUpdates();
        presenter.updateNote("Updated A1", "");

        verify(view, times(1)).noteLoaded(any());
    }

}
