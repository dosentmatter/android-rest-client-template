package com.codepath.apps.simpletwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.simpletwitter.TwitterApplication;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.codepath.apps.simpletwitter.network.NetworkTools;
import com.codepath.apps.simpletwitter.network.TwitterClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class HomeTimelineFragment extends TweetsListFragment {

    private static final String TAG = HomeTimelineFragment.class
                                                          .getSimpleName();
    private static final int TWEET_COUNT = 25;

    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();

        Map<String, String> params = new HashMap<>();
        params.put("count", String.valueOf(TWEET_COUNT));
        populateTimeline(params);
    }

    private void populateTimeline(Map<String, String> params) {
        if (NetworkTools.isOnline()) {
            AsyncHttpResponseHandler timelineHandler
                = new JsonHttpResponseHandler() {
                      @Override
                      public void onSuccess(int statusCode, Header[] headers,
                                            JSONArray response) {
                          List<Tweet> tweetResults
                              = Tweet.fromJSONArray(response);
                          Tweet.saveTweets(tweetResults);
                          addAll(tweetResults);
                      }

                      @Override
                      public void onFailure(int statusCode, Header[] headers,
                                            String responseString,
                                            Throwable throwable) {
                          Log.d(TAG, responseString);
                      }
                  };

            RequestParams requestParams
                = NetworkTools.convertToRequestParams(params);
            client.getHomeTimeLine(requestParams, timelineHandler);
        } else {
            QueryTransaction.QueryResultListCallback<Tweet> timelineHandler
                = new QueryTransaction.QueryResultListCallback<Tweet>() {
                      @Override
                      public void
                      onListQueryResult(QueryTransaction transaction,
                                        @Nullable List<Tweet> tResult) {
                          addAll(tResult);
                      }
                  };
            Tweet.getHomeTimeLine(params, timelineHandler);
        }
    }
}
