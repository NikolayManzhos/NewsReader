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
import butterknife.OnClick;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.PhotosViewHolder>  {

    private ArrayList<String> sourcesName;
    private ArrayList<String> sourcesDescription;
    private ArrayList<String> sourcesUrl;
    private Context context;

    private OnClickListener onClickListener;

    @Inject
    public SourcesAdapter(Context context) {
        this.context = context;
//        this.data.addAll(data);
    }


    static class PhotosViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sourceImage)
        ImageView sourceImage;

        @BindView(R.id.sourceName)
        TextView sourceNameTextView;

        @BindView(R.id.sourceDescription)
        TextView sourceDescriptionTextView;

        @BindView(R.id.sourceItem)
        RelativeLayout sourceItem;

        PhotosViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public SourcesAdapter.PhotosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SourcesAdapter.PhotosViewHolder holder, int position) {
        if (sourcesName != null && sourcesDescription != null && sourcesUrl != null) {
            holder.sourceNameTextView.setText(sourcesName.get(holder.getAdapterPosition()));
            holder.sourceDescriptionTextView.setText(sourcesDescription.get(holder.getAdapterPosition()));
            Glide
                    .with(context)
                    .load(sourcesUrl.get(holder.getAdapterPosition()))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.sourceImage);
        }
    }

    @Override
    public int getItemCount() {
        return sourcesUrl != null ? sourcesUrl.size() : 0;
    }

    @OnClick(R.id.sourceItem)
    void onClick() {
        onClickListener.onClick();
    }


    public void setSourcesData(List<String> sourcesName, List<String> sourcesDescription, List<String> sourcesUrl) {
        this.sourcesName = new ArrayList<>(sourcesName);
        this.sourcesDescription = new ArrayList<>(sourcesDescription);
        this.sourcesUrl = new ArrayList<>(sourcesUrl);
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick();
    }
}
