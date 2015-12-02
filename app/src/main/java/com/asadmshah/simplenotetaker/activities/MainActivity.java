package com.asadmshah.simplenotetaker.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.asadmshah.simplenotetaker.BaseApplication;
import com.asadmshah.simplenotetaker.R;
import com.asadmshah.simplenotetaker.injection.components.DaggerMainActivityComponent;
import com.asadmshah.simplenotetaker.injection.components.MainActivityComponent;
import com.asadmshah.simplenotetaker.injection.modules.MainActivityModule;
import com.asadmshah.simplenotetaker.injection.modules.NavigationDrawerScreenModule;
import com.asadmshah.simplenotetaker.injection.modules.NotesListScreenModule;
import com.asadmshah.simplenotetaker.screens.navigationDrawer.NavigationDrawerContract;
import com.asadmshah.simplenotetaker.screens.navigationDrawer.NavigationDrawerFragment;
import com.asadmshah.simplenotetaker.screens.notesList.NotesListContract;
import com.asadmshah.simplenotetaker.screens.notesList.NotesListFragment;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements NavigationDrawerContract.Parent, NotesListContract.Parent {

    private DrawerLayout viewDrawerLayout;
    private NotesListFragment notesListFragment;
    private NavigationDrawerFragment navigationDrawerFragment;

    @Inject NavigationDrawerContract.Presenter navigationDrawerPresenter;
    @Inject NotesListContract.Presenter notesListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24px);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        viewDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        viewDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        notesListFragment = (NotesListFragment) getSupportFragmentManager().findFragmentByTag(NotesListFragment.TAG);
        if (notesListFragment == null) {
            notesListFragment = new NotesListFragment();
            transaction.add(R.id.content_notes_list, notesListFragment, NotesListFragment.TAG);
        }
        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentByTag(NavigationDrawerFragment.TAG);
        if (navigationDrawerFragment == null) {
            navigationDrawerFragment = new NavigationDrawerFragment();
            transaction.add(R.id.content_navigation_drawer, navigationDrawerFragment, NavigationDrawerFragment.TAG);
        }
        transaction.commit();

        doInjections();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                viewDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void closeDrawer() {
        viewDrawerLayout.closeDrawers();
    }

    @Override
    public void onAllNotesSelected() {
        notesListPresenter.reloadNotes();
    }

    @Override
    public void onEditTagsSelected() {

    }

    @Override
    public void onTagSelected(long tagId) {
        notesListPresenter.reloadNotes(tagId);
    }

    @Override
    public void onCreateTagSelected() {

    }

    @Override
    public void onSettingsSelected() {

    }

    @Override
    public void showNote(long id) {
        startActivity(NoteDetailActivity.buildIntent(this, id));
    }

    private void doInjections() {
        MainActivityComponent component = DaggerMainActivityComponent.builder()
                .baseApplicationComponent(BaseApplication.getComponent(this))
                .mainActivityModule(new MainActivityModule(this))
                .navigationDrawerScreenModule(new NavigationDrawerScreenModule(this, navigationDrawerFragment))
                .notesListScreenModule(new NotesListScreenModule(this, notesListFragment))
                .build();
        component.inject(this);
        component.inject(navigationDrawerFragment);
        component.inject(notesListFragment);
    }

}
