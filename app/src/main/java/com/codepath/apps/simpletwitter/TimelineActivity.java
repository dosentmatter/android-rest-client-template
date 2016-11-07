package com.codepath.apps.simpletwitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.simpletwitter.adapters.SmartFragmentStatePagerAdapter;
import com.codepath.apps.simpletwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.simpletwitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.codepath.apps.simpletwitter.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private static final String TAG = TimelineActivity.class
                                                      .getSimpleName();

    private static final int REQUEST_COMPOSE_TWEET = 0;

    private TwitterClient client;

    private ViewPager viewPager;
    private SmartFragmentStatePagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApplication.getRestClient();

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        PagerSlidingTabStrip tabStrip
            = (PagerSlidingTabStrip)findViewById(R.id.tabstrip);
        tabStrip.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_compose_tweet) {
            onComposeTweet();
            return true;
        }
        if (id == R.id.action_profile) {
            onProfile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class TweetsPagerAdapter
    extends SmartFragmentStatePagerAdapter {

        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

    private void onProfile() {
        Intent profileIntent = new Intent(getApplicationContext(),
                                          ProfileActivity.class);
        startActivity(profileIntent);
    }

    private void onComposeTweet() {
        Intent composeTweetIntent = new Intent(getApplicationContext(),
                                               ComposeTweetActivity.class);
        startActivityForResult(composeTweetIntent, REQUEST_COMPOSE_TWEET);
    }

    private void composeTweet(RequestParams params) {
        JsonHttpResponseHandler composeTweetHandler
            = new JsonHttpResponseHandler() {
                  @Override
                  public void onSuccess(int statusCode, Header[] headers,
                                        JSONObject response) {
                      try {
                          Tweet tweet = new Tweet(response);
                          Tweet.saveTweet(tweet);
                          HomeTimelineFragment homeTimelineFragment
                              = (HomeTimelineFragment)viewPagerAdapter
                                .getRegisteredFragment(0);
                          homeTimelineFragment.insert(tweet, 0);
                          viewPager.setCurrentItem(0);
                      } catch (JSONException e) {
                          Log.e(TAG, e.getMessage());
                      }
                  }

                  @Override
                  public void onFailure(int statusCode, Header[] headers,
                                        String responseString,
                                        Throwable throwable) {
                      super.onFailure(statusCode, headers, responseString,
                                      throwable);
                  }
              };

        client.postUpdate(params, composeTweetHandler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_COMPOSE_TWEET) {
            String tweetText = data.getStringExtra("tweetText");
            RequestParams params = new RequestParams();
            params.put("status", tweetText);
            composeTweet(params);
        }
    }
}
