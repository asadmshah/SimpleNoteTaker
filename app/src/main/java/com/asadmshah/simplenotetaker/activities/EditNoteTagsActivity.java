package com.asadmshah.simplenotetaker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.asadmshah.simplenotetaker.BaseApplication;
import com.asadmshah.simplenotetaker.R;
import com.asadmshah.simplenotetaker.injection.components.DaggerEditNoteTagsActivityComponent;
import com.asadmshah.simplenotetaker.injection.components.EditNoteTagsActivityComponent;
import com.asadmshah.simplenotetaker.injection.modules.EditNoteTagsActivityModule;
import com.asadmshah.simplenotetaker.injection.modules.EditNoteTagsScreenModule;
import com.asadmshah.simplenotetaker.screens.editNoteTags.EditNoteTagsContract;
import com.asadmshah.simplenotetaker.screens.editNoteTags.EditNoteTagsFragment;

import javax.inject.Inject;

public class EditNoteTagsActivity extends AppCompatActivity implements EditNoteTagsContract.Parent {

    public static final String KEY_NOTE_ID = "note_id";

    @Inject EditNoteTagsContract.Presenter editNoteTagsPresenter;

    private long noteId;
    private EditNoteTagsFragment editNoteTagsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note_tags);

        noteId = getIntent().getLongExtra(KEY_NOTE_ID, -1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(" ");
            ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        editNoteTagsFragment = (EditNoteTagsFragment) getSupportFragmentManager().findFragmentByTag(EditNoteTagsFragment.TAG);
        if (editNoteTagsFragment == null) {
            editNoteTagsFragment = new EditNoteTagsFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_edit_note_tags, editNoteTagsFragment, EditNoteTagsFragment.TAG);
            transaction.commit();
        }

        doInjections();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void closeEditNoteTagsScreen() {
        finish();
    }

    private void doInjections() {
        EditNoteTagsActivityComponent component = DaggerEditNoteTagsActivityComponent.builder()
                .baseApplicationComponent(BaseApplication.getComponent(this))
                .editNoteTagsActivityModule(new EditNoteTagsActivityModule(this))
                .editNoteTagsScreenModule(new EditNoteTagsScreenModule(this, editNoteTagsFragment, noteId))
                .build();
        component.inject(this);
        component.inject(editNoteTagsFragment);
    }

    public static Intent buildIntent(Context context, long noteId) {
        Intent intent = new Intent(context, EditNoteTagsActivity.class);
        intent.putExtra(KEY_NOTE_ID, noteId);
        return intent;
    }
}
