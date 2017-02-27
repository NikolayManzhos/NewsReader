package com.defaultapps.newsreader.di.component;

import com.defaultapps.newsreader.di.modules.AppModule;
import com.defaultapps.newsreader.ui.activity.MainActivity;
import com.defaultapps.newsreader.ui.fragment.MainViewImpl;
import com.defaultapps.newsreader.ui.fragment.SetUpViewImpl;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(SetUpViewImpl mainFragment);
    void inject(MainViewImpl mainView);
    void inject(MainActivity mainActivity);
}
