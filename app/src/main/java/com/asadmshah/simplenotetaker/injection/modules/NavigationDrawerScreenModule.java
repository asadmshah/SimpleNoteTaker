package com.asadmshah.simplenotetaker.injection.modules;

import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.injection.scopes.ActivityScope;
import com.asadmshah.simplenotetaker.screens.navigationDrawer.NavigationDrawerContract;
import com.asadmshah.simplenotetaker.screens.navigationDrawer.NavigationDrawerPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class NavigationDrawerScreenModule {

    private final NavigationDrawerContract.Parent parent;
    private final NavigationDrawerContract.View view;

    public NavigationDrawerScreenModule(NavigationDrawerContract.Parent parent, NavigationDrawerContract.View view) {
        this.parent = parent;
        this.view = view;
    }

    @Provides
    @ActivityScope
    NavigationDrawerContract.Parent provideParent() {
        return parent;
    }

    @Provides
    @ActivityScope
    NavigationDrawerContract.View provideView() {
        return view;
    }

    @Provides
    @ActivityScope
    NavigationDrawerContract.Presenter providePresenter(NavigationDrawerContract.View view, NavigationDrawerContract.Parent parent, Database database) {
        return new NavigationDrawerPresenter(view, parent, database);
    }

}
