package com.codepath.apps.simpletwitter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.simpletwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.simpletwitter.fragments.MentionsTimelineFragment;

public class TimelineActivity extends AppCompatActivity {
//
//    private static final int REQUEST_COMPOSE_TWEET = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager
        .setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
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
//        if (id == R.id.action_compose_tweet) {
//            onComposeTweet();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {

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

//    private void onComposeTweet() {
//        Intent composeTweetIntent = new Intent(getApplicationContext(),
//                                               ComposeTweetActivity.class);
//        startActivityForResult(composeTweetIntent, REQUEST_COMPOSE_TWEET);
//    }
//
//    private void composeTweet(RequestParams params) {
//        JsonHttpResponseHandler composeTweetHandler
//            = new JsonHttpResponseHandler() {
//                  @Override
//                  public void onSuccess(int statusCode, Header[] headers,
//                                        JSONObject response) {
//                      try {
//                          Tweet tweet = new Tweet(response);
//                          Tweet.saveTweet(tweet);
//                          tweetsAdapter.insert(tweet, 0);
//                      } catch (JSONException e) {
//                          Log.e(TAG, e.getMessage());
//                      }
//                  }
//
//                  @Override
//                  public void onFailure(int statusCode, Header[] headers,
//                                        String responseString,
//                                        Throwable throwable) {
//                      super.onFailure(statusCode, headers, responseString,
//                                      throwable);
//                  }
//              };
//
//        client.postUpdate(params, composeTweetHandler);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent data) {
//        if (resultCode == RESULT_OK && requestCode == REQUEST_COMPOSE_TWEET) {
//            String tweetText = data.getStringExtra("tweetText");
//            RequestParams params = new RequestParams();
//            params.put("status", tweetText);
//            composeTweet(params);
//        }
//    }
}
