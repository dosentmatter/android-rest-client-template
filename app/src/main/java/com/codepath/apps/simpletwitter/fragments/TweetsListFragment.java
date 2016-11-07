package com.codepath.apps.simpletwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.simpletwitter.R;
import com.codepath.apps.simpletwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.simpletwitter.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetsListFragment extends Fragment {

    private ListView lvTweets;
    private ArrayList<Tweet> tweetsList;
    private TweetArrayAdapter tweetsAdapter;

    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweetsList = new ArrayList<>();
        tweetsAdapter = new TweetArrayAdapter(getActivity(), tweetsList);
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

//        setupSwipeContainerListeners();

        lvTweets = (ListView)view.findViewById(R.id.lvTweets);

        lvTweets.setAdapter(tweetsAdapter);

//        setupListViewListeners();
    }
//
//    private void setupSwipeContainerListeners() {
//        SwipeRefreshLayout.OnRefreshListener swipeContainerRefreshListener
//            = new SwipeRefreshLayout.OnRefreshListener() {
//                  @Override
//                  public void onRefresh() {
//                      tweetsList.clear();
//                      tweetsAdapter.notifyDataSetChanged();
//                      Map<String, String> params = new HashMap<>();
//                      params.put("count", String.valueOf(TWEET_COUNT));
//                      populateTimeline(params);
//                      swipeContainer.setRefreshing(false);
//                  }
//              };
//
//        swipeContainer.setOnRefreshListener(swipeContainerRefreshListener);
//
//        swipeContainer
//        .setColorSchemeResources(android.R.color.holo_blue_bright);
//
//    }
//
//    private void setupListViewListeners() {
//        EndlessScrollListener listViewOnScrollListener
//            = new EndlessScrollListener() {
//                  @Override
//                  public boolean onLoadMore(int page, int totalItemsCount) {
//                      Map<String, String> params = new HashMap<>();
//                      params.put("count", String.valueOf(TWEET_COUNT));
//                      long oldestTweetId
//                          = tweetsList.get(tweetsList.size() - 1).getId();
//                      params.put("max_id", String.valueOf(oldestTweetId - 1));
//                      populateTimeline(params);
//                      return true;
//                  }
//              };
//        lvTweets.setOnScrollListener(listViewOnScrollListener);
//    }

    public void addAll(List<Tweet> tweets) {
        tweetsAdapter.addAll(tweets);
    }
}
