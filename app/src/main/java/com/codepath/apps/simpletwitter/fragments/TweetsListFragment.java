package com.codepath.apps.simpletwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.simpletwitter.R;
import com.codepath.apps.simpletwitter.TwitterApplication;
import com.codepath.apps.simpletwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.simpletwitter.interfaces.EndlessScrollListener;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.codepath.apps.simpletwitter.network.NetworkTools;
import com.codepath.apps.simpletwitter.network.TwitterClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public abstract class TweetsListFragment extends Fragment {

    protected static final String TAG = TweetsListFragment.class
                                                          .getSimpleName();

    protected static final int TWEET_COUNT = 25;

    protected TwitterClient client;

    protected ListView lvTweets;
    protected ArrayList<Tweet> tweetsList;
    protected TweetArrayAdapter tweetsAdapter;

    protected SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();

        tweetsList = new ArrayList<>();
        tweetsAdapter = new TweetArrayAdapter(getActivity(), tweetsList);

        populateTimeline();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container,
                                  false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setupViews(view);
    }

    private void setupViews(View view) {
        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);

        setupSwipeContainerListeners();

        lvTweets = (ListView)view.findViewById(R.id.lvTweets);

        lvTweets.setAdapter(tweetsAdapter);

        setupListViewListeners();
    }

    protected void populateTimeline() {
        Map<String, String> params = getParams();
        populateTimeline(params);
    }

    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("count", String.valueOf(TWEET_COUNT));
        return params;
    }

    protected void populateTimeline(Map<String, String> params) {
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
            getTimeLine(requestParams, timelineHandler);
        } else {
//            QueryTransaction.QueryResultListCallback<Tweet> timelineHandler
//                = new QueryTransaction.QueryResultListCallback<Tweet>() {
//                      @Override
//                      public void
//                      onListQueryResult(QueryTransaction transaction,
//                                        @Nullable List<Tweet> tResult) {
//                          addAll(tResult);
//                      }
//                  };
//            Tweet.getHomeTimeLine(params, timelineHandler);
        }
    }

    abstract protected void getTimeLine(RequestParams params,
                                        AsyncHttpResponseHandler handler);

    private void setupSwipeContainerListeners() {
        SwipeRefreshLayout.OnRefreshListener swipeContainerRefreshListener
            = new SwipeRefreshLayout.OnRefreshListener() {
                  @Override
                  public void onRefresh() {
                      tweetsList.clear();
                      tweetsAdapter.notifyDataSetChanged();
                      Map<String, String> params = getParams();
                      populateTimeline(params);
                      swipeContainer.setRefreshing(false);
                  }
              };

        swipeContainer.setOnRefreshListener(swipeContainerRefreshListener);

        swipeContainer
        .setColorSchemeResources(android.R.color.holo_blue_bright);

    }

    private void setupListViewListeners() {
        EndlessScrollListener listViewOnScrollListener
            = new EndlessScrollListener() {
                  @Override
                  public boolean onLoadMore(int page, int totalItemsCount) {
                      Map<String, String> params = getParams();
                      long oldestTweetId
                          = tweetsList.get(tweetsList.size() - 1).getId();
                      params.put("max_id", String.valueOf(oldestTweetId - 1));
                      populateTimeline(params);
                      return true;
                  }
              };
        lvTweets.setOnScrollListener(listViewOnScrollListener);
    }

    public void addAll(List<Tweet> tweets) {
        tweetsAdapter.addAll(tweets);
    }

    public void insert(Tweet tweet, int index) {
        tweetsAdapter.insert(tweet, index);
    }

    public void smoothScrollToPosition(int position) {
        lvTweets.smoothScrollToPosition(position);
    }
}
