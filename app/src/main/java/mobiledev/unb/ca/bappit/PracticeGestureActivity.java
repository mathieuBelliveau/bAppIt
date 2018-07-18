package mobiledev.unb.ca.bappit;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * @author MagnumIT-Admin
 * This activity will prompt a video demonstrating the given gesture,
 * then prompt the user to practice the gesture.
 * TODO Should it repeat until the user terminates, or should it continue until x successes?
 */
public class PracticeGestureActivity extends GestureCompatActivity{

    public final static String GESTURE_ID = "gestureId";
    public final static String GESTURE_NAME = "gestureName";
    public final static String GESTURE_CODE = "gestureCode";

    private ImageView practiceGestureImage;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_gesture);

        Intent intent = getIntent();
        int gestureID = intent.getIntExtra(GESTURE_ID, 0);
        String gestureName = intent.getStringExtra(GESTURE_NAME);
        int gestureCode = intent.getIntExtra(GESTURE_CODE, 0);

        TextView gestureNameView = (TextView) findViewById(R.id.practice_action_name);
        gestureNameView.setText(gestureName);

        VideoView video = (VideoView)findViewById(R.id.practice_tutorial_video);
        Uri uri = Uri.parse("android.resource://" + getPackageName()+ "/" + gestureID);
        video.setVideoURI(uri);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.setVolume(0, 0);
            }
        });
        video.start();
        //TODO - Disappear video and start practice
    }

    public void gestureMatch(Gesture gesture)
    {
        //TODO
        setGestureComplete(true);
    }

    public void gestureMismatch(Gesture gesture)
    {
        //TODO
    }
}
