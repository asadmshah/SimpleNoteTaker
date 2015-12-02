package com.asadmshah.simplenotetaker.injection.components;

import com.asadmshah.simplenotetaker.injection.modules.NoteDetailScreenModule;
import com.asadmshah.simplenotetaker.injection.scopes.ActivityScope;
import com.asadmshah.simplenotetaker.screens.noteDetail.NoteDetailContract;
import com.asadmshah.simplenotetaker.screens.noteDetail.NoteDetailFragment;

import dagger.Component;

@ActivityScope
@Component(
        modules = {NoteDetailScreenModule.class},
        dependencies = {BaseApplicationComponent.class}
)
public interface NoteDetailScreenComponent {
    void inject(NoteDetailFragment fragment);

    NoteDetailContract.Parent noteDetailParent();
    NoteDetailContract.View noteDetailView();
    NoteDetailContract.Presenter noteDetailPresenter();
}
