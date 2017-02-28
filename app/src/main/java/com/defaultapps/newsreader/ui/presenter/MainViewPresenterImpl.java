package com.defaultapps.newsreader.ui.presenter;
import com.defaultapps.newsreader.data.interactor.MainViewInteractor;
import com.defaultapps.newsreader.ui.fragment.MainViewImpl;

import javax.inject.Inject;


public class MainViewPresenterImpl implements MainViewPresenter, MainViewInteractor.MainViewInteractorCallback {

    private MainViewInteractor mainViewInteractor;

    @Inject
    public MainViewPresenterImpl(MainViewInteractor mainViewInteractor) {
        this.mainViewInteractor = mainViewInteractor;
    }

    @Override
    public void requestUpdate(String languageCode) {

    }

    @Override
    public void setView(MainViewImpl view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onSuccess() {

    }
}
