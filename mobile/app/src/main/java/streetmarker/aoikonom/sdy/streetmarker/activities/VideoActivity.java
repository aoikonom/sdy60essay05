package streetmarker.aoikonom.sdy.streetmarker.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import streetmarker.aoikonom.sdy.streetmarker.R;

public class VideoActivity extends AppCompatActivity {
    private Target mTarget;
    private VideoView mVideoView;
    private MediaController mMediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mVideoView = findViewById(R.id.targetVideo);

        mTarget = (Target) getIntent().getSerializableExtra("target");

        mMediaController = new MediaController(this);
        mMediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mMediaController);

        playVideo();
    }

    void playVideo() {
        String url = mTarget.getVideoUrl();
        if (url != null) {
            Uri videoUri = Uri.parse(url);
            mVideoView.setVideoURI(videoUri);
            mVideoView.start();

        }
    }
}
