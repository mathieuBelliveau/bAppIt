package mobiledev.unb.ca.bappit;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;

public class TutorialVideoActivity extends AppCompatActivity {

    public final static String GESTURE_VIDEO_ID = "gestureVideoId";
    public final static String GESTURE_NAME = "gestureName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_video);

        Intent intent = getIntent();
        int gestureID = intent.getIntExtra(GESTURE_VIDEO_ID, 0);
        String gestureName = intent.getStringExtra(GESTURE_NAME);

        TextView gestureNameView = (TextView) findViewById(R.id.action_name);
        gestureNameView.setText(gestureName);

        VideoView video = (VideoView)findViewById(R.id.tutorial_video);
        Uri uri = Uri.parse("android.resource://" + getPackageName()+ "/" + gestureID);
        video.setVideoURI(uri);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(false);
                mp.setVolume(0, 0);
            }
        });
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                TutorialVideoActivity.this.finish();
            }
        });
        video.start();
    }
}
