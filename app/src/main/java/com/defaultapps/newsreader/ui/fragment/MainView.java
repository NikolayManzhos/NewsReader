package com.defaultapps.newsreader.ui.fragment;

import com.defaultapps.newsreader.ui.base.MvpView;

import java.util.List;


public interface MainView extends MvpView {
    void showArticlesList();
    void hideArticlesList();
    void updateView(List<String> articlesTitle,
                    List<String> articlesDescription,
                    List<String> articlesImageUrl);
}
