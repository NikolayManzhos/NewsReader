package com.defaultapps.newsreader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.defaultapps.newsreader.App;
import com.defaultapps.newsreader.R;
import com.defaultapps.newsreader.ui.presenter.MainViewPresenterImpl;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MainViewImpl extends Fragment implements MainView {

    private Unbinder unbinder;

    @Inject
    MainViewPresenterImpl mainViewPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        App.getAppComponent(getActivity()).inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        mainViewPresenter.setView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        mainViewPresenter.setView(null);
        super.onDestroyView();
    }

    @Override
    public void updateView() {

    }

    @Override
    public void showTopicList() {

    }

    @Override
    public void hideTopicList() {

    }
}
