package com.codepath.apps.simpletwitter.models;

import com.codepath.apps.simpletwitter.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

@Table(database = MyDatabase.class)
public class User extends BaseModel {

    @Column
    private String name;

    @Column
    @PrimaryKey
    private long id;

    @Column
    private String screenName;

    @Column
    private String profileImageUrl;

    public User() {
        super();
    }

    public User(JSONObject jsonObject) throws JSONException {
        super();

        this.name = jsonObject.getString("name");
        this.id = jsonObject.getLong("id");
        this.screenName = jsonObject.getString("screen_name");
        this.profileImageUrl = jsonObject.getString("profile_image_url");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
