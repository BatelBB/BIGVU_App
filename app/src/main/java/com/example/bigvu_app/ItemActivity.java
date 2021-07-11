package com.example.bigvu_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class ItemActivity extends AppCompatActivity {

    private static final String EXTRA_NAME = "com.example.bigvu_app.name";
    private static final String EXTRA_DESCRIPTION = "com.example.bigvu_app.description";
    private static final String EXTRA_TEXT = "com.example.bigvu_app.text";
    private static final String EXTRA_VIDEO = "com.example.bigvu_app.video";

    private String getExtraName;
    private String getExtraDescription;
    private String getExtraText;
    private String getExtraVideo;

    private VideoView mVideoView;
    private TextView mTextViewName;
    private TextView mTextViewDesc;
    private TextView mTextViewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_item);

        getExtraName = getIntent().getStringExtra(EXTRA_NAME);
        getExtraDescription = getIntent().getStringExtra(EXTRA_DESCRIPTION);
        getExtraText = getIntent().getStringExtra(EXTRA_TEXT);
        getExtraVideo = getIntent().getStringExtra(EXTRA_VIDEO);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        mTextViewName = (TextView) findViewById(R.id.nameText);
        mTextViewDesc = (TextView) findViewById(R.id.descText);
        mTextViewText = (TextView) findViewById(R.id.textText);

        mTextViewName.setText(getExtraName);
        mTextViewDesc.setText(getExtraDescription);
        mTextViewText.setText(getExtraText);

        mVideoView.setVideoURI(Uri.parse(getExtraVideo));
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();
        mVideoView.start();
    }
}