package com.codepath.apps.simpletwitter.models;

import android.text.format.DateUtils;
import android.util.Log;

import com.codepath.apps.simpletwitter.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Table(database = MyDatabase.class)
public class Tweet extends BaseModel {

    private static final String TAG = Tweet.class.getSimpleName();
    private static final int DEFAULT_TWEET_COUNT = 200;

    @Column
    private String text;

    @Column
    @PrimaryKey
    private long id;

    @Column
    @ForeignKey(saveForeignKeyModel = true)
    private User user;

    @Column
    private String createdAt;

    public Tweet() {
        super();
    }

    public Tweet(JSONObject jsonObject) throws JSONException {
        super();

        this.text = jsonObject.getString("text");
        this.id = jsonObject.getLong("id");
        this.user = new User(jsonObject.getJSONObject("user"));
        this.createdAt = jsonObject.getString("created_at");
    }

    public static void saveTweet(Tweet tweet) {
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet);
        saveTweets(tweets);
    }

    public static void saveTweets(List<Tweet> tweets) {
        ProcessModelTransaction.ProcessModel processModel
            = new ProcessModelTransaction.ProcessModel<Tweet>() {
                  @Override
                  public void processModel(Tweet tweet) {
                      tweet.save();
                  }
              };
        ProcessModelTransaction<Tweet> processModelTransaction
            = new ProcessModelTransaction.Builder<>(processModel).addAll(tweets)
                                         .build();
        DatabaseDefinition database = FlowManager.getDatabase(MyDatabase.class);
        Transaction transaction
            = database.beginTransactionAsync(processModelTransaction)
                      .build();
        transaction.execute();
    }

    public static List<Tweet> fromJSONArray(JSONArray array) {
        List<Tweet> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Tweet(array.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return results;
    }

    public static void
    getHomeTimeLine(Map<String, String> params,
                    QueryTransaction.QueryResultListCallback<Tweet> handler) {
        String countString = params.get("count");
        String maxIdString = params.get("max_id");

        int count = countString != null ?
        Integer.valueOf(countString) : DEFAULT_TWEET_COUNT;

        Where where = SQLite.select().from(Tweet.class).where();
        if (maxIdString != null) {
            long maxId = Long.valueOf(maxIdString);
            where = where.andAll(Tweet_Table.id.lessThanOrEq(maxId));
        }
        where = where.orderBy(Tweet_Table.id, false).limit(count);
        where.async().queryListResultCallback(handler).execute();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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
