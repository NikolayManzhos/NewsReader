package com.defaultapps.newsreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;


public class ArticlesAdapter extends RecyclerView.Adapter {

    private Context context; //Application context

    static class ArticleViewHolder extends RecyclerView.ViewHolder {

        ArticleViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Inject
    public ArticlesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setAdapterData() {

    }
}
