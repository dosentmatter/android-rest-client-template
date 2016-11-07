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

    @Column
    private String description;

    @Column
    private int followersCount;

    @Column
    private int friendsCount;

    public User() {
        super();
    }

    public User(JSONObject jsonObject) throws JSONException {
        super();

        this.name = jsonObject.getString("name");
        this.id = jsonObject.getLong("id");
        this.screenName = jsonObject.getString("screen_name");
        this.profileImageUrl = jsonObject.getString("profile_image_url");
        this.description = jsonObject.getString("description");
        this.followersCount = jsonObject.getInt("followers_count");
        this.friendsCount = jsonObject.getInt("friends_count");
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

    public String getPrefixedScreenName() {
        return "@" + screenName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public String getSuffixedFollowersCount() {
        return followersCount + " Followers";
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public String getSuffixedFriendsCount() {
        return friendsCount + " Following";
    }
    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }
}
