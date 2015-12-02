package com.asadmshah.simplenotetaker.injection.components;

import com.asadmshah.simplenotetaker.activities.MainActivity;
import com.asadmshah.simplenotetaker.injection.modules.MainActivityModule;
import com.asadmshah.simplenotetaker.injection.modules.NavigationDrawerScreenModule;
import com.asadmshah.simplenotetaker.injection.modules.NotesListScreenModule;
import com.asadmshah.simplenotetaker.injection.scopes.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(
        modules = {
                MainActivityModule.class,
                NavigationDrawerScreenModule.class,
                NotesListScreenModule.class
        },
        dependencies = {BaseApplicationComponent.class}
)
public interface MainActivityComponent extends NavigationDrawerScreenComponent, NotesListScreenComponent {
    void inject(MainActivity activity);

    MainActivity activity();
}
