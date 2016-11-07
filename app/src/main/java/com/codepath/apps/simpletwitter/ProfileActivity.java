package com.codepath.apps.simpletwitter;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitter.fragments.UserTimelineFragment;
import com.codepath.apps.simpletwitter.models.User;
import com.codepath.apps.simpletwitter.network.TwitterClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = UserTimelineFragment.class
                                      .getSimpleName();

    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApplication.getRestClient();
        AsyncHttpResponseHandler userHandler
            = new JsonHttpResponseHandler() {
                  @Override
                  public void onSuccess(int statusCode, Header[] headers,
                                        JSONObject response) {
                      try {
                          user = new User(response);
                      } catch (JSONException e) {
                          Log.e(TAG, e.getMessage());
                      }
                      getSupportActionBar().setTitle(user.getPrefixedScreenName());
                      populateProfileHeader(user);
                  }

                  @Override
                  public void onFailure(int statusCode, Header[] headers,
                                        String responseString,
                                        Throwable throwable) {
                      Log.d(TAG, responseString);
                  }
              };
        client.getUserInfo(null, userHandler);

        String screenName = getIntent().getStringExtra("screenName");

        if (savedInstanceState == null) {
            UserTimelineFragment userTimelineFragment
                = UserTimelineFragment.newInstance(screenName);
            FragmentTransaction fragmentTransaction
                = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flContainer, userTimelineFragment);
            fragmentTransaction.commit();
        }
    }

    private void populateProfileHeader(User user) {
        ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        TextView tvName = (TextView)findViewById(R.id.tvName);
        TextView tvTagLine = (TextView)findViewById(R.id.tvTagLine);
        TextView tvFollowers = (TextView)findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView)findViewById(R.id.tvFollowing);

        String profileImageUrl = user.getProfileImageUrl();
        Picasso.with(getContext()).load(profileImageUrl)
                .placeholder(R.drawable.ic_launcher)
                .into(ivProfileImage);

        tvName.setText(user.getName());
        tvTagLine.setText(user.getDescription());
        tvFollowers.setText(user.getSuffixedFollowersCount());
        tvFollowing.setText(user.getSuffixedFriendsCount());
    }
}
