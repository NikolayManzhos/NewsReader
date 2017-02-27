package com.defaultapps.newsreader.ui.fragment;

import com.defaultapps.newsreader.ui.base.MvpView;

import java.util.List;


public interface SetUpView extends MvpView {
    void updateView(List<String> sourcesName, List<String> sourcesDescription, List<String> sourcesUrl);
    void hideSourcesList();
    void showSourcesList();
}
