package com.codepath.apps.simpletwitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.simpletwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.simpletwitter.interfaces.EndlessScrollListener;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private static final String TAG = TimelineActivity.class.getSimpleName();
    private static final int TWEET_COUNT = 25;
    private static final int REQUEST_COMPOSE_TWEET = 0;

    private TwitterClient client;

    private ListView lvTweets;
    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter tweetsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApplication.getRestClient();

        setupViews();

        RequestParams params = new RequestParams();
        params.put("count", TWEET_COUNT);
        populateTimeline(params);
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

        return super.onOptionsItemSelected(item);
    }

    private void onComposeTweet() {
        Intent composeTweetIntent = new Intent(getApplicationContext(),
                                               ComposeTweetActivity.class);
        startActivityForResult(composeTweetIntent, REQUEST_COMPOSE_TWEET);
    }

    private void setupViews() {
        lvTweets = (ListView)findViewById(R.id.lvTweets);

        tweets = new ArrayList<>();
        tweetsAdapter = new TweetArrayAdapter(this, tweets);
        lvTweets.setAdapter(tweetsAdapter);

        setupListViewListeners();
    }

    private void setupListViewListeners() {
        EndlessScrollListener listViewOnScrollListener
            = new EndlessScrollListener() {
                  @Override
                  public boolean onLoadMore(int page, int totalItemsCount) {
                      RequestParams params = new RequestParams();
                      params.put("count", TWEET_COUNT);
                      long oldestTweetId
                          = tweets.get(tweets.size() - 1).getId();
                      params.put("max_id", oldestTweetId - 1);
                      populateTimeline(params);
                      return true;
                  }
              };
        lvTweets.setOnScrollListener(listViewOnScrollListener);
    }

    private void populateTimeline(RequestParams params) {
        JsonHttpResponseHandler timelineHandler
            = new JsonHttpResponseHandler() {
                  @Override
                  public void onSuccess(int statusCode, Header[] headers,
                                        JSONArray response) {
                      ArrayList<Tweet> tweetResults
                          = Tweet.fromJSONArray(response);
                      tweetsAdapter.addAll(tweetResults);
                  }

                  @Override
                  public void onFailure(int statusCode, Header[] headers,
                                        String responseString,
                                        Throwable throwable) {
                      super.onFailure(statusCode, headers, responseString,
                                      throwable);
                  }
              };

        client.getHomeTimeLine(params, timelineHandler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_COMPOSE_TWEET) {
            tweets.clear();
            tweetsAdapter.notifyDataSetChanged();
            RequestParams params = new RequestParams();
            params.put("count", TWEET_COUNT);
            populateTimeline(params);
        }
    }
}