package com.codepath.apps.simpletwitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ComposeTweetActivity extends AppCompatActivity {

    private EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);

        setupViews();
    }

    private void setupViews() {
        etText = (EditText)findViewById(R.id.etText);
    }

    public void onCancel(View view) {
        finish();
    }

    public void onTweet(View view) {
        if (etText.getText().toString().length() <=
            getResources().getInteger(R.integer.tweet_max_length)) {
            Intent tweetIntent = new Intent();
            tweetIntent.putExtra("tweetText", etText.getText().toString());
            setResult(RESULT_OK, tweetIntent);
            finish();
        } else {
            Toast.makeText(this, "Tweet is too long",
                           Toast.LENGTH_SHORT).show();
        }
    }
}
