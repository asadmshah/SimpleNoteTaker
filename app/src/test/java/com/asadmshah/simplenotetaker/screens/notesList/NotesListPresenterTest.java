package com.asadmshah.simplenotetaker.screens.notesList;

import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.database.InMemoryDatabase;
import com.asadmshah.simplenotetaker.models.Note;
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

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NotesListPresenterTest {

    @Rule public RxResetRule rxResetRule = new RxResetRule();

    @Mock private NotesListContract.View view;
    @Mock private NotesListContract.Parent parent;

    @Captor private ArgumentCaptor<List<Note>> noteListCaptor;
    @Captor private ArgumentCaptor<Boolean> loadingStateCaptor;

    private Database database;
    private NotesListContract.Presenter presenter;

    @Before
    public void setUp() {
        database = new InMemoryDatabase();
        database.insertNote("A1", "B1");
        database.insertTag("T1", 1);
        database.insertNote("A2", "B2");
        database.insertTag("T2", 2);
        database.insertNote("A3", "B3");
        database.insertTag("T3", 3);

        database.tagNote(1, 1);
        database.tagNote(1, 2);
        database.tagNote(1, 3);
        database.tagNote(2, 2);
        database.tagNote(2, 3);

        presenter = new NotesListPresenter(view, parent, database);
    }

    @After
    public void tearDown() {
        presenter.unSubscribeFromUpdates();
    }

    @Test
    public void test_reloadNotes() {
        presenter.reloadNotes();

        verify(view, times(2)).setNotesListStateLoading(loadingStateCaptor.capture());
        verify(view).notesLoaded();
        verify(view, never()).showError(any(ErrorConstants.class));

        assertThat(presenter.getDataSource().getCount(), is(3));
        assertThat(presenter.getDataSource().getNote(0).title(), is("A3"));
        assertThat(presenter.getDataSource().getNote(2).title(), is("A1"));

        assertThat(loadingStateCaptor.getAllValues(), contains(true, false));
    }

    @Test
    public void test_reloadNotesOfTag() {
        presenter.reloadNotes(2);

        verify(view, times(2)).setNotesListStateLoading(loadingStateCaptor.capture());
        verify(view).notesLoaded();
        verify(view, never()).showError(any(ErrorConstants.class));

        assertThat(presenter.getDataSource().getCount(), is(2));
        assertThat(presenter.getDataSource().getNote(0).title(), is("A2"));
        assertThat(presenter.getDataSource().getNote(1).title(), is("A1"));

        assertThat(loadingStateCaptor.getAllValues(), contains(true, false));
    }

    @Test
    public void test_addNewNoteClicked() {
        presenter.onAddNewNoteClicked();

        verify(parent).showNote(4);
    }

    @Test
    public void test_onNoteSelected() {
        presenter.onNoteSelected(3);

        verify(parent).showNote(3);
    }

    @Test
    public void test_unSubscribeFromUpdates() {
        presenter.reloadNotes();
        presenter.unSubscribeFromUpdates();
        database.insertNote("A4", "B4");

        verify(view, times(2)).setNotesListStateLoading(anyBoolean());
        verify(view, times(1)).notesLoaded();
    }

}
