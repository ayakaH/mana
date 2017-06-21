package me.sunzheng.mana.home.search.wrapper;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.sunzheng.mana.BangumiDetailsActivity;
import me.sunzheng.mana.R;

/**
 * Created by Sun on 2017/6/20.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    List<Datum> mValues;

    public SearchResultAdapter(List<Datum> list) {
        this.mValues = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_onairfragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BangumiDetailsActivity.newInstance((Activity) v.getContext(), mValues.get(position).getId(), mValues.get(position).getImage(), mValues.get(position).getNameCn(), holder.mImageView);
            }
        });
        Glide.with(holder.itemView.getContext())
                .load(mValues.get(position).getImage())
                .placeholder(new ColorDrawable(Color.parseColor(mValues.get(position).getCoverColor())))
                .into(holder.mImageView);
        holder.mTitleTextView.setText(mValues.get(position).getNameCn());
        holder.mSummaryTextView.setText(mValues.get(position).getSummary());
        holder.mEtcTextView.setText(mValues.get(position).getAirDate());
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    protected final static class ViewHolder extends RecyclerView.ViewHolder {
        public final CardView mView;
        public final TextView mTitleTextView, mSummaryTextView, mEtcTextView;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = (CardView) view;
            mTitleTextView = (TextView) view.findViewById(R.id.item_title_textview);
            mImageView = (ImageView) view.findViewById(R.id.item_album);
            mSummaryTextView = (TextView) view.findViewById(R.id.item_subtitle_textview);
            mEtcTextView = (TextView) view.findViewById(R.id.item_etc_textview);
        }
    }
}
