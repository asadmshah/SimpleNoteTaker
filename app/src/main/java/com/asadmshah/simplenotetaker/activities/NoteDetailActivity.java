package com.asadmshah.simplenotetaker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.asadmshah.simplenotetaker.BaseApplication;
import com.asadmshah.simplenotetaker.R;
import com.asadmshah.simplenotetaker.injection.components.DaggerNoteDetailActivityComponent;
import com.asadmshah.simplenotetaker.injection.components.NoteDetailActivityComponent;
import com.asadmshah.simplenotetaker.injection.modules.NoteDetailActivityModule;
import com.asadmshah.simplenotetaker.injection.modules.NoteDetailScreenModule;
import com.asadmshah.simplenotetaker.screens.noteDetail.NoteDetailContract;
import com.asadmshah.simplenotetaker.screens.noteDetail.NoteDetailFragment;

import javax.inject.Inject;

public class NoteDetailActivity extends AppCompatActivity implements NoteDetailContract.Parent {

    public static final String KEY_NOTE_ID = "note_id";

    @Inject NoteDetailContract.Presenter noteDetailPresenter;

    private long noteId;
    private NoteDetailFragment noteDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        noteId = getIntent().getLongExtra(KEY_NOTE_ID, -1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        noteDetailFragment = (NoteDetailFragment) getSupportFragmentManager().findFragmentByTag(NoteDetailFragment.TAG);
        if (noteDetailFragment == null) {
            noteDetailFragment = new NoteDetailFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_note_detail, noteDetailFragment, NoteDetailFragment.TAG);
            transaction.commit();
        }

        doInjections();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                noteDetailPresenter.deleteNote();
                return true;
            case R.id.action_tag:
                requestEditTagsScreen(noteId);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void requestEditTagsScreen(long noteId) {
        startActivity(EditNoteTagsActivity.buildIntent(this, noteId));
    }

    @Override
    public void closeNoteDetailScreen() {
        finish();
    }

    private void doInjections() {
        NoteDetailActivityComponent component = DaggerNoteDetailActivityComponent.builder()
                .baseApplicationComponent(BaseApplication.getComponent(this))
                .noteDetailActivityModule(new NoteDetailActivityModule(this))
                .noteDetailScreenModule(new NoteDetailScreenModule(this, noteDetailFragment, noteId))
                .build();
        component.inject(this);
        component.inject(noteDetailFragment);
    }

    public static Intent buildIntent(Context context, long noteId) {
        Intent intent = new Intent(context, NoteDetailActivity.class);
        intent.putExtra(KEY_NOTE_ID, noteId);
        return intent;
    }

}
