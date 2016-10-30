package com.codepath.apps.simpletwitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private String name;
    private long id;
    private String screenName;
    private String profileImageUrl;

    public User(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString("name");
        this.id = jsonObject.getLong("id");
        this.screenName = jsonObject.getString("screen_name");
        this.profileImageUrl = jsonObject.getString("profile_image_url");
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
