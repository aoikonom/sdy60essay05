package streetmarker.aoikonom.sdy.streetmarker.activities;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

import streetmarker.aoikonom.sdy.streetmarker.R;
import streetmarker.aoikonom.sdy.streetmarker.model.UserInfo;

public class RegionActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView[] mCardsImageView;
    private EditText mAnswer;
    private ImageView mPhotoImageView;
    private Button mInstructionsBtn;
    private Button mTryBtn;
    private MediaPlayer mMediaPlayer;
    private static final int CARD_COUNT = 9;

    private UserInfo mUser;
    private Target mTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        mCardsImageView = new ImageView[CARD_COUNT];

        for (int i = 0; i < CARD_COUNT; i++) {
            int resID = getResources().getIdentifier("card" + String.format("%02d", i+1), "id", getPackageName());
            mCardsImageView[i] = findViewById(resID);
        }

        mAnswer = findViewById(R.id.answer);
        mPhotoImageView = findViewById(R.id.photo);
        mTryBtn = findViewById(R.id.try_btn);
        mInstructionsBtn = findViewById(R.id.instructions_btn);

        mUser = (UserInfo) getIntent().getSerializableExtra("user");
        mTarget = (Target) getIntent().getSerializableExtra("target");

        for (int i = 0; i < mUser.getTargetsCompleted(); i++)
            mCardsImageView[i].setVisibility(View.VISIBLE);

        if (mTarget != null && mTarget.getPhotoUrl() != null)
            new DownloadImageTask(mPhotoImageView).execute(mTarget.getPhotoUrl());

        mTryBtn.setOnClickListener(this);
        mInstructionsBtn.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaPlayer != null)
            mMediaPlayer.release();
    }

    @Override
    public void onClick(View v) {
        if (v == mTryBtn) {
          onAnswerBtnClicked();
        } else if (v == mInstructionsBtn) {
            onInstrctionsBtnClicked();
        }
    }
/*
    private void onInstrctionsBtnClicked() {
        String url = mTarget.getVideoUrl();
        mMediaPlayer = new MediaPlayer();
//        mMediaPlayer.setVideoScalingMode(VideoManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                RegionActivity.this.mMediaPlayer.start();
            }
        });
        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
        }
        catch (IOException e) {
            Toast.makeText(this, "Video not avalitable", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void onInstrctionsBtnClicked() {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("target", mTarget);
        startActivity(intent);
    }

    private void onAnswerBtnClicked() {
        if (mAnswer.getText().toString().equals(mTarget.getCorrectAnswer()))
            onTargetCompleted();
        else
            Toast.makeText(this, "Wrong answer", Toast.LENGTH_SHORT).show();
    }

    private void onTargetCompleted() {
        mUser.setTargetsCompleted(mUser.getTargetsCompleted() + 1);
        mCardsImageView[mUser.getTargetsCompleted() - 1].setVisibility(View.VISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Congratulations!\n\nYou successfully completed this target\n\nLet's go for next target")
                .setTitle("Target Completed")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        RegionActivity.this.finish();
                    }
                });
        builder.create().show();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}


