package mobiledev.unb.ca.bappit;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
    private VideoView practiceGestureVideo;

    private int gestureID;
    private String gestureName;
    private int gestureCode;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_gesture);

        Intent intent = getIntent();
        gestureID = intent.getIntExtra(GESTURE_ID, 0);
        gestureName = intent.getStringExtra(GESTURE_NAME);
        gestureCode = intent.getIntExtra(GESTURE_CODE, 0);

        TextView gestureNameView = (TextView) findViewById(R.id.practice_action_name);
        gestureNameView.setText(gestureName);

        practiceGestureImage = (ImageView) findViewById(R.id.practice_gesture_img);
        practiceGestureImage.setVisibility(View.INVISIBLE);

        practiceGestureVideo = (VideoView)findViewById(R.id.practice_tutorial_video);
        practiceGestureVideo.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse("android.resource://" + getPackageName()+ "/" + gestureID);
        practiceGestureVideo.setVideoURI(uri);
        practiceGestureVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(false);
                mp.setVolume(0, 0);
            }
        });
        practiceGestureVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.setVolume(100,100);//FIXME - sharedprefs instead
                mp.stop();//FIXME - unsure if proper to stop video here.
                PracticeGestureActivity context = (PracticeGestureActivity) getApplicationContext();
                context.beginPractice();
                //TODO - Play after the listener is pressed and the video completes
            }
        });
        practiceGestureVideo.start();
    }

    public void beginPractice(){
//        practiceGestureVideo.stopPlayback();//FIXME - stop here or at 55?
        practiceGestureVideo.setVisibility(View.INVISIBLE);
        practiceGestureImage.setVisibility(View.VISIBLE);

        setCurrentGesture(Gesture.values()[gestureCode]);
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
