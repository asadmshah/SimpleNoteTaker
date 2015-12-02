package com.asadmshah.simplenotetaker.screens.editNoteTags;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.asadmshah.simplenotetaker.R;
import com.asadmshah.simplenotetaker.utils.ErrorConstants;
import com.asadmshah.simplenotetaker.widgets.ColorsListView;
import com.asadmshah.simplenotetaker.widgets.RecyclerViewInteractions.OnViewHolderClicked;

import javax.inject.Inject;

public class EditNoteTagsFragment extends Fragment implements EditNoteTagsContract.View, OnViewHolderClicked,
        ColorsListView.OnColorSelectedListener {

    public static final String TAG = EditNoteTagsFragment.class.getSimpleName();

    @Inject EditNoteTagsContract.Presenter presenter;

    private EditText viewCreateTagLabel;
    private ImageView viewCreateTagButton;
    private ColorsListView viewColorsList;
    private RecyclerView viewTagsList;

    private NoteTagsAdapter noteTagsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteTagsAdapter = new NoteTagsAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note_tags, container, false);

        viewCreateTagLabel = (EditText) view.findViewById(R.id.create_tag_label);
        viewCreateTagLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.onCreateTagLabelChanged(s.toString());
            }
        });
        viewCreateTagButton = (ImageView) view.findViewById(R.id.create_tag_button);
        viewColorsList = (ColorsListView) view.findViewById(R.id.colors_list);
        viewColorsList.setOnColorSelectedListener(this);
        viewTagsList = (RecyclerView) view.findViewById(R.id.tags_list);
        viewTagsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        viewTagsList.setAdapter(noteTagsAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.reloadTags();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void tagsDataSetChanged() {
        noteTagsAdapter.setDataSource(presenter.getNoteTagsDataSource());
    }

    @Override
    public void tagAdded(int position) {
        noteTagsAdapter.notifyItemInserted(position);
    }

    @Override
    public void tagChanged(int position) {
        noteTagsAdapter.notifyItemChanged(position);
    }

    @Override
    public void setTagsLoadingState(boolean isLoading) {
        noteTagsAdapter.setOnViewHolderClickListener(isLoading ? null : this);
    }

    @Override
    public void showTagCreateButton(boolean show) {
        if (show) {
            viewCreateTagButton.setVisibility(View.VISIBLE);
            viewCreateTagButton.setOnClickListener(v -> presenter.onCreateTag());
        } else {
            viewCreateTagButton.setVisibility(View.INVISIBLE);
            viewCreateTagButton.setOnClickListener(null);
        }
    }

    @Override
    public void selectTagColor(int color) {
        viewColorsList.setSelectedColor(color);
    }

    @Override
    public void clearNewTagLabel() {
        viewCreateTagLabel.setText("");
    }

    @Override
    public void clearNewTagColor() {
        viewColorsList.setSelectedColor(-1);
    }

    @Override
    public void showError(ErrorConstants error) {
        View view = getView();
        if (view != null) {
            Snackbar.make(view, error.getString(getActivity()), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onViewHolderClicked(RecyclerView.ViewHolder viewHolder) {
        presenter.onToggleTag(viewHolder.getAdapterPosition());
    }

    @Override
    public void onColorSelected(@ColorInt int color) {
        presenter.onCreateTagColorChanged(color);
    }
}
