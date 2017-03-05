package com.defaultapps.newsreader.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.TextView;

import com.defaultapps.newsreader.App;
import com.defaultapps.newsreader.R;
import com.defaultapps.newsreader.data.local.sp.SharedPreferencesManager;
import com.defaultapps.newsreader.ui.adapter.ArticlesAdapter;
import com.defaultapps.newsreader.ui.presenter.MainViewPresenterImpl;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;


public class MainViewImpl extends Fragment implements MainView, ArticlesAdapter.ArticleListener, SwipeRefreshLayout.OnRefreshListener {

    private Unbinder unbinder;
    private MainViewListener mainViewListener;

    @Inject
    MainViewPresenterImpl mainViewPresenter;

    @Inject
    ArticlesAdapter articlesAdapter;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @BindView(R.id.articlesRecyclerView)
    RecyclerView articlesRecycler;

    @BindView(R.id.swipeRefreshMainView)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.errorTextView)
    TextView errorText;

    @BindView(R.id.errorButton)
    Button errorButton;

    private ArrayList<String> articlesDirectUrl;

    private final String TAG = "MainViewImpl";


    public interface MainViewListener {
        void settingsClicked();
        void openWebView(String url);
    }

    @Override
    public void onAttach(Context context) {
        mainViewListener = (MainViewListener) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        App.getAppComponent(getActivity()).inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        mainViewPresenter.setView(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        initRecyclerView();
        articlesAdapter.setListener(this);
        if (sharedPreferencesManager.getForceLoadStatus()) {
            mainViewPresenter.requestUpdate();
            sharedPreferencesManager.setForceLoadStatus(false);
        }else if (savedInstanceState!= null) {
            mainViewPresenter.restoreViewState();
        } else {
            mainViewPresenter.requestCache();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_view, menu);
        Set<String> sorting = sharedPreferencesManager.getSortingAvailable();
        if (sorting.contains("top")) {
            menu.findItem(R.id.sortTop).setVisible(true);
        }
        if (sorting.contains("latest")) {
            MenuItem latestItem = menu.findItem(R.id.sortLatest);
            latestItem.setVisible(true);
        }
        if(sorting.contains("popular")) {
            menu.findItem(R.id.sortPopular).setVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                mainViewListener.settingsClicked();
                break;
            case R.id.sortTop:
                sharedPreferencesManager.setSort("top");
                Log.i(TAG, "Top selected");
                mainViewPresenter.requestUpdate();
                break;
            case R.id.sortLatest:
                sharedPreferencesManager.setSort("latest");
                mainViewPresenter.requestUpdate();
                break;
            case R.id.sortPopular:
                sharedPreferencesManager.setSort("popular");
                mainViewPresenter.requestUpdate();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        mainViewPresenter.requestUpdate();
    }

    @Override
    public void onArticleClick(int position) {
        mainViewListener.openWebView(articlesDirectUrl.get(position));
    }

    @OnClick(R.id.errorButton)
    void onClick() {
        mainViewPresenter.requestUpdate();
    }

    @Override
    public void updateView(List<String> articlesTitle, List<String> articlesDescription, List<String> articlesImageUrl, List<String> articlesDirectUrl) {
        articlesAdapter.setArticlesData(articlesTitle, articlesDescription, articlesImageUrl);
        this.articlesDirectUrl = new ArrayList<>(articlesDirectUrl);
        articlesRecycler.scrollToPosition(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainViewPresenter.detachView();
        articlesRecycler.setAdapter(null);
        unbinder.unbind();
        Log.d(TAG, "View destroyed");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainViewListener = null;
    }

    @Override
    public void showArticlesList() {
        articlesRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideArticlesList() {
        articlesRecycler.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideError() {
        errorText.setVisibility(View.GONE);
        errorButton.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        errorText.setVisibility(View.VISIBLE);
        errorButton.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1);
        articlesRecycler.setLayoutManager(gridLayoutManager);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(articlesAdapter);
        scaleInAnimationAdapter.setFirstOnly(false);
        articlesRecycler.setAdapter(scaleInAnimationAdapter);
    }
}
