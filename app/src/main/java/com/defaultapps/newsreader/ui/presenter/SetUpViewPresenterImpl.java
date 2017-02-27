package com.defaultapps.newsreader.ui.presenter;

import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.defaultapps.newsreader.data.interactor.SetUpViewInteractor;
import com.defaultapps.newsreader.ui.fragment.SetUpViewImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SetUpViewPresenterImpl implements SetUpViewPresenter, SetUpViewInteractor.MainViewInteractorCallback {

    private SetUpViewImpl view;
    private SetUpViewInteractor setUpViewInteractor;

    private boolean taskRunning = false;
    private boolean errorVisible = false;

    private List<List<String>> savedData;

    @Inject
    public SetUpViewPresenterImpl(SetUpViewInteractor setUpViewInteractor) {
        this.setUpViewInteractor = setUpViewInteractor;
        Log.d("PRESENTER", "Constructor");
    }

    @Override
    public void setView(SetUpViewImpl view) {
        this.view = view;
        setUpViewInteractor.bindInteractor(this);
    }

    @Override
    public void detachView() {
        view = null;
        setUpViewInteractor.bindInteractor(null);
    }

    @Override
    public void destroy() {
        //TODO: Stop loading of any data at this point ?
    }


    @Override
    public void requestSourceUpdate(String languageCode) {
        setTaskStatus(true);
        setErrorVisibilityStatus(false);
        view.showLoading();
        view.hideSourcesList();
        view.hideError();
        setUpViewInteractor.loadSourcesData(languageCode);
    }


    @Override
    public void onSuccess(List<String> sourcesName, List<String> sourcesDescription, List<String> sourcesUrl) {
        savedData = new ArrayList<>();
        savedData.add(sourcesName);
        savedData.add(sourcesDescription);
        savedData.add(sourcesUrl);
        if (view != null) {
            view.hideLoading();
            view.hideError();
            view.showSourcesList();
            view.updateView(sourcesName, sourcesDescription, sourcesUrl);
        }
        setTaskStatus(false);
    }

    @Override
    public void onFailure() {
        if (view != null) {
            view.hideLoading();
            view.hideSourcesList();
            view.showError();
        }
        setErrorVisibilityStatus(true);
        setTaskStatus(false);
    }

    public void restoreViewState() {
        if (view != null) {
            if (taskRunning) {
                view.showLoading();
                view.hideSourcesList();
                view.hideError();
            } else if(errorVisible) {
                view.showError();
                view.hideSourcesList();
                view.hideLoading();
            } else {
                if (savedData != null)
                view.updateView(savedData.get(0), savedData.get(1), savedData.get(2));
            }
        }
    }

    @VisibleForTesting
    void setTaskStatus(boolean status) {
        this.taskRunning = status;
    }

    @VisibleForTesting
    void setErrorVisibilityStatus(boolean status) {
        this.errorVisible = status;
    }

    public boolean isTaskRunning() {
        return taskRunning;
    }

    public boolean isErrorVisible() {
        return errorVisible;
    }

}
