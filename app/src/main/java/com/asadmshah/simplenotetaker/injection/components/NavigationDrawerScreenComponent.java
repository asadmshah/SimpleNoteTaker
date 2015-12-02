package com.asadmshah.simplenotetaker.injection.components;

import com.asadmshah.simplenotetaker.injection.modules.NavigationDrawerScreenModule;
import com.asadmshah.simplenotetaker.injection.scopes.ActivityScope;
import com.asadmshah.simplenotetaker.screens.navigationDrawer.NavigationDrawerContract;
import com.asadmshah.simplenotetaker.screens.navigationDrawer.NavigationDrawerFragment;

import dagger.Component;

@ActivityScope
@Component(
        modules = {NavigationDrawerScreenModule.class},
        dependencies = {BaseApplicationComponent.class}
)
public interface NavigationDrawerScreenComponent {
    void inject(NavigationDrawerFragment view);

    NavigationDrawerContract.View navigationDrawerView();
    NavigationDrawerContract.Parent navigationDrawerParent();
    NavigationDrawerContract.Presenter navigationDrawerPresenter();
}
