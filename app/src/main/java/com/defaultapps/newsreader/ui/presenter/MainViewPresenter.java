package com.defaultapps.newsreader.ui.presenter;

import com.defaultapps.newsreader.ui.base.MvpPresenter;
import com.defaultapps.newsreader.ui.fragment.MainViewImpl;


public interface MainViewPresenter extends MvpPresenter<MainViewImpl> {
    void requestUpdate();
    void requestCache();
}
