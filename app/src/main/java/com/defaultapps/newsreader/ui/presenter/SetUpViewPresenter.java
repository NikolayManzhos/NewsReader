package com.defaultapps.newsreader.ui.presenter;

import com.defaultapps.newsreader.ui.base.MvpPresenter;
import com.defaultapps.newsreader.ui.fragment.SetUpViewImpl;


public interface SetUpViewPresenter extends MvpPresenter<SetUpViewImpl> {
    void requestSourceUpdate(String countryCode);
}
