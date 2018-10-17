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
    private ImageView checkMarkImage;
    private VideoView practiceGestureVideo;

    private int gestureID;
    private String gestureName;
    private int gestureCode;

    private int count = 0;//correct execution

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_gesture);

        TextView gestureNameView = (TextView) findViewById(R.id.practice_action_name);
        gestureNameView.setText(gestureName);

        practiceGestureImage = (ImageView) findViewById(R.id.practice_gesture_img);

        Intent intent = getIntent();
        gestureID = intent.getIntExtra(GESTURE_ID, 0);
        gestureName = intent.getStringExtra(GESTURE_NAME);
        gestureCode = intent.getIntExtra(GESTURE_CODE, 0);

        setCurrentGesture(Gesture.values()[gestureCode]);

        practiceGestureImage.setImageResource(gestureID);



        //practiceGestureImage.setVisibility(View.INVISIBLE);



        completeGesture();

    }

    public void completeGesture() {

    }

    public void gestureMatch(Gesture gesture)
    {
        //TODO - Checkmark, with n/3 successes shown
        setGestureComplete(true);
    }

    public void gestureMismatch(Gesture gesture)
    {
        //TODO - "X", with n/3 successes reset
    }
}
