package com.defaultapps.newsreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.defaultapps.newsreader.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {

    private Context context; //Application context
    private List<String> articlesTitle, articlesDescription, articlesImageUrl;
    private ArticleListener articleListener;

    public interface ArticleListener {
        void onArticleClick(int position);
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.articleItem)
        RelativeLayout articleItem;

        @BindView(R.id.articleTitle)
        TextView articleTitleTextView;

        @BindView(R.id.articleImage)
        ImageView articleImageView;

        @BindView(R.id.articleDescription)
        TextView articleDescriptionTextView;

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
    public ArticlesAdapter.ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArticlesAdapter.ArticleViewHolder holder, int position) {
        if (articlesTitle != null && articlesDescription != null && articlesImageUrl!= null) {
            holder.articleTitleTextView.setText(articlesTitle.get(holder.getAdapterPosition()));
            holder.articleDescriptionTextView.setText(articlesDescription.get(holder.getAdapterPosition()));

            Glide
                    .with(context)
                    .load(articlesImageUrl.get(holder.getAdapterPosition()))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .into(holder.articleImageView);
            holder.articleItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    articleListener.onArticleClick(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return articlesImageUrl != null ? articlesImageUrl.size() : 0;
    }

    public void setListener(ArticleListener articleListener) {
        this.articleListener = articleListener;
    }

    public void setArticlesData(List<String> articlesTitle, List<String> articlesDescription, List<String> articlesImageUrl) {
        this.articlesTitle = new ArrayList<>(articlesTitle);
        this.articlesDescription = new ArrayList<>(articlesDescription);
        this.articlesImageUrl = new ArrayList<>(articlesImageUrl);
        notifyDataSetChanged();
    }
}
