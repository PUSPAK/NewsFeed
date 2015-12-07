package com.app.adapter;


import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.databasehandler.DatabaseHandler;
import com.example.divum.newsfeed.R;
import com.datamodel.RssItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FeedListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<RssItem> feedItems;
    ProgressBar progress;
    DatabaseHandler db;

    ImageView likeImageView;

    public FeedListAdapter(Context context, List<RssItem> feedItems) {
        this.context = context;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.feed_item, null);
            db = new DatabaseHandler(context);
            holder = new ViewHolder();


            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.type = (TextView) convertView
                    .findViewById(R.id.type);
            holder.news = (TextView) convertView
                    .findViewById(R.id.news);
            holder.feedImageView = (ImageView) convertView
                    .findViewById(R.id.feedimage);
            holder.likeImageView = (ImageView) convertView
                    .findViewById(R.id.like);
            holder.progress = (ProgressBar) convertView
                    .findViewById(R.id.progressimage);
            holder.shareImageView = (ImageView) convertView
                    .findViewById(R.id.share);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        RssItem item = feedItems.get(position);

        holder.name.setText(Html.fromHtml(item.getTitle()));
        holder.news.setText(Html.fromHtml(item.getDescription()));
        if (item.getLikebyme().equals("YES")) {
            holder.likeImageView.setBackgroundResource(R.drawable.like_red);

        } else {
            holder.likeImageView.setBackgroundResource(R.drawable.like);

        }
        holder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.updateContact(feedItems.get(position));
                feedItems.get(position).setLikebyme("YES");
                notifyDataSetChanged();

            }
        });

        holder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareTextUrl(feedItems.get(position).getLink());
            }
        });
        if (item.getType() != null) {
            holder.type.setText(item.getType());

        } else {
            holder.type.setVisibility(View.GONE);
        }
        // Feed image
        if (item.getImageUrl() != null) {

            final ViewHolder holder2 = holder;

            Picasso.with(context).load(item.getImageUrl()).into(holder.feedImageView, new Callback() {
                @Override
                public void onSuccess() {
                    holder2.progress.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    // TODO Auto-generated method stub
                    holder2.progress.setVisibility(View.GONE);
                }
            });
        } else {
            holder.feedImageView.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView name, type, news;
        ImageView feedImageView, likeImageView, shareImageView;
        ProgressBar progress;
    }

    private void shareTextUrl(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        context.startActivity(intent);
    }
}
