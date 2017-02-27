package com.defaultapps.newsreader.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.defaultapps.newsreader.App;
import com.defaultapps.newsreader.R;
import com.defaultapps.newsreader.ui.adapter.SourcesAdapter;
import com.defaultapps.newsreader.ui.presenter.SetUpViewPresenterImpl;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;


public class SetUpViewImpl extends Fragment implements SetUpView{

    @Inject
    SetUpViewPresenterImpl mainViewPresenter;

    @Inject
    SourcesAdapter sourcesAdapter;

    private Unbinder unbinder;

    @BindView(R.id.errorTextView)
    TextView errorText;

    @BindView(R.id.errorButton)
    Button errorButton;

    @BindView(R.id.sourcesRecyclerView)
    RecyclerView sourcesRecycler;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.setupRadioGroup)
    RadioGroup radioGroup;

    private final String TAG = "SetUpViewImpl";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent(getActivity()).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        mainViewPresenter.setView(this);
        initRecyclerView();
//        radioGroup.setOnClickListener(this);
        if (savedInstanceState != null) {
            mainViewPresenter.restoreViewState();
        } else {
//            mainViewPresenter.requestSourceUpdate();
        }


        super.onViewCreated(view, savedInstanceState);
    }

//    @Override
//    public void onClickListener(RadioGroup radioGroup, int i) {
//        switch (radioGroup.getCheckedRadioButtonId()) {
//            case R.id.setupEngRadio:
//                Log.d(TAG, "ENGLISH");
//                mainViewPresenter.requestSourceUpdate("en");
//                break;
//            case R.id.setupDeRadio:
//                mainViewPresenter.requestSourceUpdate("de");
//                break;
//            case R.id.setupFrRadio:
//                mainViewPresenter.requestSourceUpdate("fr");
//                break;
//        }
//    }

    @OnClick({R.id.setupEngRadio, R.id.setupDeRadio, R.id.setupFrRadio})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.setupEngRadio:
                Log.d(TAG, "ENGLISH");
                mainViewPresenter.requestSourceUpdate("en");
                break;
            case R.id.setupDeRadio:
                mainViewPresenter.requestSourceUpdate("de");
                break;
            case R.id.setupFrRadio:
                mainViewPresenter.requestSourceUpdate("fr");
                break;
        }
    }



    @OnClick(R.id.errorButton)
    void onClick() {
//        mainViewPresenter.requestSourceUpdate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sourcesRecycler.setAdapter(null);
        mainViewPresenter.detachView();
        unbinder.unbind();
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void showError() {
        errorText.setVisibility(View.VISIBLE);
        errorButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
        errorText.setVisibility(View.GONE);
        errorButton.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateView(List<String> sourcesName, List<String> sourcesDescription, List<String> sourcesUrl) {
        sourcesAdapter.setSourcesData(sourcesName, sourcesDescription, sourcesUrl);
    }

    @Override
    public void hideSourcesList() {
        sourcesRecycler.setVisibility(View.GONE);
    }

    @Override
    public void showSourcesList() {
        sourcesRecycler.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1);
        sourcesRecycler.setLayoutManager(gridLayoutManager);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(sourcesAdapter);
        scaleInAnimationAdapter.setFirstOnly(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(sourcesRecycler.getContext(), gridLayoutManager.getOrientation());
        sourcesRecycler.addItemDecoration(dividerItemDecoration);
        sourcesRecycler.setAdapter(scaleInAnimationAdapter);
    }

}
