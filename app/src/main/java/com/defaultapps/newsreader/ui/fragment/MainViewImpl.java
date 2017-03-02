package com.defaultapps.newsreader.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.defaultapps.newsreader.App;
import com.defaultapps.newsreader.R;
import com.defaultapps.newsreader.ui.adapter.ArticlesAdapter;
import com.defaultapps.newsreader.ui.presenter.MainViewPresenterImpl;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;


public class MainViewImpl extends Fragment implements MainView, SwipeRefreshLayout.OnRefreshListener {

    private Unbinder unbinder;

    @Inject
    MainViewPresenterImpl mainViewPresenter;

    @Inject
    ArticlesAdapter articlesAdapter;

    @BindView(R.id.articlesRecyclerView)
    RecyclerView articlesRecycler;

    @BindView(R.id.swipeRefreshMainView)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.errorTextView)
    TextView errorText;

    @BindView(R.id.errorButton)
    Button errorButton;

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
        if (savedInstanceState!= null) {
            mainViewPresenter.restoreViewState();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_view, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(getActivity(), "SETTINGS", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        mainViewPresenter.requestUpdate();
    }

    @Override
    public void updateView(List<String> articlesTitle, List<String> articlesDescription, List<String> articlesImageUrl) {
        articlesAdapter.setArticlesData(articlesTitle, articlesDescription, articlesImageUrl);
    }

    @Override
    public void onDestroyView() {
        articlesRecycler.setAdapter(null);
        mainViewPresenter.detachView();
        unbinder.unbind();
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
        super.onDestroyView();
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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(articlesRecycler.getContext(), gridLayoutManager.getOrientation());
        articlesRecycler.addItemDecoration(dividerItemDecoration);
        articlesRecycler.setAdapter(scaleInAnimationAdapter);
    }
}
