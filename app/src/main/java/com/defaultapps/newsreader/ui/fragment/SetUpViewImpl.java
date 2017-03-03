package com.defaultapps.newsreader.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.defaultapps.newsreader.App;
import com.defaultapps.newsreader.R;
import com.defaultapps.newsreader.data.local.sp.SharedPreferencesManager;
import com.defaultapps.newsreader.ui.adapter.SourcesAdapter;
import com.defaultapps.newsreader.ui.presenter.SetUpViewPresenterImpl;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import icepick.Icepick;
import icepick.State;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;


public class SetUpViewImpl extends Fragment implements SetUpView, SourcesAdapter.Listener{

    @Inject
    SetUpViewPresenterImpl setUpViewPresenter;

    @Inject
    SourcesAdapter sourcesAdapter;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    private Unbinder unbinder;
    private SourcesListener sourcesListener;

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

    @State
    ArrayList<List<String>> data;

    @State
    ArrayList<List<String>> sortsAvailable;

    public interface SourcesListener {
        void sourceClicked();
    }


    @Override
    public void onAttach(Context context) {
        if (context instanceof SourcesListener) {
            sourcesListener = (SourcesListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent(getActivity()).inject(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        setUpViewPresenter.setView(this);
        initRecyclerView();
        if (data != null) {
            sourcesAdapter.setSourcesData(data.get(0), data.get(1), data.get(2));
            sourcesRecycler.setVisibility(View.VISIBLE);
        }
        if (savedInstanceState != null) {
            setUpViewPresenter.restoreViewState();
        }
        sourcesAdapter.setListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Icepick.saveInstanceState(this, outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setUpViewPresenter.detachView();
        sourcesRecycler.setAdapter(null);
        unbinder.unbind();
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @OnClick({R.id.setupEngRadio, R.id.setupDeRadio, R.id.setupFrRadio})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.setupEngRadio:
                setUpViewPresenter.requestSourceUpdate("en");
                break;
            case R.id.setupDeRadio:
                setUpViewPresenter.requestSourceUpdate("de");
                break;
            case R.id.setupFrRadio:
                setUpViewPresenter.requestSourceUpdate("fr");
                break;
        }
    }

    @Override
    public void onSourceClick(int position) {
        Toast.makeText(getActivity(), "Position " + String.valueOf(position), Toast.LENGTH_SHORT).show();
        sharedPreferencesManager.setSource(data.get(3).get(position));
        sharedPreferencesManager.setSort("top");
        sourcesListener.sourceClicked();
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
    public void updateView(List<String> sourcesName,
                           List<String> sourcesDescription,
                           List<String> sourcesUrl,
                           List<String> sourcesId,
                           List<List<String>> sourceSortAvailable) {
        data = new ArrayList<>();
        sortsAvailable = new ArrayList<>();
        data.add(sourcesName);
        data.add(sourcesDescription);
        data.add(sourcesUrl);
        data.add(sourcesId);
        sortsAvailable.addAll(sourceSortAvailable);
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
