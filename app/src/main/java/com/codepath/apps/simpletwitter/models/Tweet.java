package com.codepath.apps.simpletwitter.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tweet {

    private static final String TAG = Tweet.class.getSimpleName();

    private String text;
    private long id;
    private User user;
    private String createdAt;

    public Tweet(JSONObject jsonObject) throws JSONException {
        this.text = jsonObject.getString("text");
        this.id = jsonObject.getLong("id");
        this.user = new User(jsonObject.getJSONObject("user"));
        this.createdAt = jsonObject.getString("created_at");
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray array) {
        ArrayList<Tweet> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Tweet(array.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return results;
    }

    public String getText() {
        return text;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
