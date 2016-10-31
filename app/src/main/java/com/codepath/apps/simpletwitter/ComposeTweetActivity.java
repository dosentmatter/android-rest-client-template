package com.codepath.apps.simpletwitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

public class ComposeTweetActivity extends AppCompatActivity {

    private TwitterClient client;

    private EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);

        client = TwitterApplication.getRestClient();

        setupViews();
    }

    private void setupViews() {
        etText = (EditText)findViewById(R.id.etText);
    }

    public void onCancel(View view) {
        finish();
    }

    public void onTweet(View view) {
        if (etText.getText().length() <=
            getResources().getInteger(R.integer.tweet_max_length)) {
            RequestParams params = new RequestParams();
            params.put("status", etText.getText());
            composeTweet(params);
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Tweet is too long",
                           Toast.LENGTH_SHORT).show();
        }
    }

    private void composeTweet(RequestParams params) {
        JsonHttpResponseHandler composeTweetHandler
                = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONArray response) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString,
                        throwable);
            }
        };

        client.postUpdate(params, composeTweetHandler);
    }
}
