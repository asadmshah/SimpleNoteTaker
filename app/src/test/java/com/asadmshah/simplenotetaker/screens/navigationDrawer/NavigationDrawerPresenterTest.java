package com.asadmshah.simplenotetaker.screens.navigationDrawer;

import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.database.InMemoryDatabase;
import com.asadmshah.simplenotetaker.models.Tag;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NavigationDrawerPresenterTest {

    @Rule public RxResetRule rxRule = new RxResetRule();

    @Mock private NavigationDrawerContract.Parent parent;
    @Mock private NavigationDrawerContract.View view;

    @Captor private ArgumentCaptor<List<Tag>> tagsListCaptor;
    @Captor private ArgumentCaptor<Boolean> loadingStateCaptor;

    private Database database;
    private NavigationDrawerContract.Presenter presenter;

    @Before
    public void setUp() {
        database = new InMemoryDatabase();
        database.insertTag("T3", 3);
        database.insertTag("T2", 2);
        database.insertTag("T1", 1);

        presenter = new NavigationDrawerPresenter(view, parent, database);
    }

    @After
    public void tearDown() {
        presenter.unSubscribeFromUpdates();
    }

    @Test
    public void reloadTags() {
        presenter.reloadTags();

        verify(view, times(2)).setTagsStateLoading(loadingStateCaptor.capture());
        verify(view).tagsLoaded();

        assertThat(presenter.getDataSource().getCount(), is(3));
        assertThat(presenter.getDataSource().getTag(0).label(), is("T1"));
        assertThat(presenter.getDataSource().getTag(2).label(), is("T3"));

        assertThat(loadingStateCaptor.getAllValues(), contains(true, false));
    }

    @Test
    public void onAllNotesSelected() {
        presenter.onAllNotesSelected();

        verify(parent).onAllNotesSelected();
    }

    @Test
    public void onTagSelected() {
        presenter.onTagSelected(3);

        verify(parent).onTagSelected(3);
    }

    @Test
    public void onSettingsSelected() {
        presenter.onSettingsSelected();

        verify(parent).onSettingsSelected();
    }

    @Test
    public void unSubscribeFromUpdates() {
        presenter.reloadTags();
        database.insertTag("T4", 4);
    }

}
