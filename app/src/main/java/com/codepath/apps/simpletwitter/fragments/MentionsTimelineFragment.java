package com.codepath.apps.simpletwitter.fragments;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MentionsTimelineFragment extends TweetsListFragment {

    @Override
    protected void getTimeLine(RequestParams params,
                               AsyncHttpResponseHandler handler) {
        client.getMentionsTimeLine(params, handler);
    }
}
