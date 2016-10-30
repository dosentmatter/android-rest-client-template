package com.codepath.apps.simpletwitter.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitter.R;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

    private static class ViewHolder {
        ImageView ivProfileImage;
        TextView tvScreenName;
        TextView tvText;
    }

    public TweetArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.item_tweet, tweets);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_tweet, parent, false);

            viewHolder.ivProfileImage
                    = (ImageView)convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvScreenName
                    = (TextView)convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvText
                    = (TextView)convertView.findViewById(R.id.tvText);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        String profileImageUrl = tweet.getUser().getProfileImageUrl();
        Picasso.with(getContext()).load(profileImageUrl)
               .placeholder(R.drawable.ic_launcher)
               .into(viewHolder.ivProfileImage);

        viewHolder.tvScreenName.setText(tweet.getUser().getScreenName());
        viewHolder.tvText.setText(tweet.getText());

        return convertView;
    }
}

