package com.codepath.apps.simpletwitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitter.ProfileActivity;
import com.codepath.apps.simpletwitter.R;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

    private static class ViewHolder {
        ImageView ivProfileImage;
        TextView tvName;
        TextView tvScreenName;
        TextView tvRelativeTimestamp;
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
            viewHolder.tvName
                    = (TextView)convertView.findViewById(R.id.tvName);
            viewHolder.tvScreenName
                    = (TextView)convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvRelativeTimestamp
                    = (TextView)convertView.findViewById(R.id.tvRelativeTimestamp);
            viewHolder.tvText
                    = (TextView)convertView.findViewById(R.id.tvText);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        final String profileImageUrl = tweet.getUser().getProfileImageUrl();
        Picasso.with(getContext()).load(profileImageUrl)
               .placeholder(R.drawable.ic_launcher)
               .into(viewHolder.ivProfileImage);

        View.OnClickListener profileImageOnClickListener
            = new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      if (!(getContext() instanceof ProfileActivity)) {
                          Intent profileIntent
                                  = new Intent(getContext(),
                                               ProfileActivity.class);
                          Tweet tweet = (Tweet)view.getTag();
                          profileIntent
                          .putExtra("screenName",
                                    tweet.getUser().getScreenName());
                          getContext().startActivity(profileIntent);
                      }
                  }
              };
        viewHolder.ivProfileImage.setTag(tweet);
        viewHolder.ivProfileImage
                  .setOnClickListener(profileImageOnClickListener);

        viewHolder.tvName.setText(tweet.getUser().getName());
        viewHolder
        .tvScreenName
        .setText(tweet.getUser().getPrefixedScreenName());
        viewHolder.tvRelativeTimestamp.setText(tweet.getRelativeTimeStamp());
        viewHolder.tvText.setText(tweet.getText());

        return convertView;
    }
}

