package com.codepath.apps.simpletwitter.fragments;

import android.os.Bundle;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Map;

public class UserTimelineFragment extends TweetsListFragment {

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Override
    protected Map<String, String> getParams() {
        Map <String, String> params = super.getParams();
        params.put("screen_name", getArguments().getString("screenName"));
        return params;
    }

    @Override
    protected void getTimeLine(RequestParams params,
                               AsyncHttpResponseHandler handler) {
        client.getUserTimeline(params, handler);
    }
}
