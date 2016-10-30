package com.codepath.apps.simpletwitter.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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

    public String getRelativeTimeStamp() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat twitterDateFormat
            = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        twitterDateFormat.setLenient(true);

        String relativeDate = "";
        try {
            long dateInMillis = twitterDateFormat.parse(createdAt).getTime();
            relativeDate
                = DateUtils
                  .getRelativeTimeSpanString(dateInMillis,
                                             System.currentTimeMillis(),
                                             DateUtils.SECOND_IN_MILLIS)
                  .toString();
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return relativeDate;
    }
}
