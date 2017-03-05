package com.defaultapps.newsreader.ui.presenter;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.defaultapps.newsreader.data.interactor.MainViewInteractor;
import com.defaultapps.newsreader.ui.fragment.MainViewImpl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MainViewPresenterImpl implements MainViewPresenter, MainViewInteractor.MainViewInteractorCallback {

    private MainViewInteractor mainViewInteractor;
    private MainViewImpl view;

    private boolean taskStatus = false;
    private boolean errorVisibility = false;

    @Inject
    public MainViewPresenterImpl(MainViewInteractor mainViewInteractor) {
        this.mainViewInteractor = mainViewInteractor;
        Log.d("MainViewPresenterImpl", "constructor");
    }

    @Override
    public void requestUpdate() {
        setTaskStatus(true);
        setErrorVisibility(false);
        view.showLoading();
        view.hideError();
        view.hideArticlesList();
        mainViewInteractor.loadArticlesData();
    }

    @Override
    public void requestCache() {
        setTaskStatus(true);
        setErrorVisibility(false);
        view.showLoading();
        view.hideError();
        view.hideArticlesList();
        mainViewInteractor.loadDataFromCache();
    }

    @Override
    public void setView(MainViewImpl view) {
        mainViewInteractor.bindInteractor(this);
        this.view = view;
    }

    @Override
    public void detachView() {
        mainViewInteractor.bindInteractor(null);
        this.view = null;

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onFailure() {
        if (view != null) {
            view.hideLoading();
            view.hideArticlesList();
            view.showError();
        }
        setTaskStatus(false);
        setErrorVisibility(true);
    }

    @Override
    public void onSuccess(List<String> articlesTitle, List<String> articlesDescription, List<String> articlesImageUrl, List<String> articlesDirectUrl) {
        if (view != null) {
            view.hideLoading();
            view.hideError();
            view.updateView(articlesTitle, articlesDescription, articlesImageUrl, articlesDirectUrl);
            view.showArticlesList();
        }
        setTaskStatus(false);
        setErrorVisibility(false);
    }

    public void restoreViewState() {
        if (view != null) {
            if (taskStatus) {
                view.hideError();
                view.hideArticlesList();
                view.showLoading();
            } else if (errorVisibility) {
                view.hideLoading();
                view.hideArticlesList();
                view.showError();
            } else {
                mainViewInteractor.loadDataFromCache();
            }
        }
    }

    @VisibleForTesting
    void setTaskStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    @VisibleForTesting
    void setErrorVisibility(boolean errorVisibility) {
        this.errorVisibility = errorVisibility;
    }
}
