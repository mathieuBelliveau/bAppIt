package mobiledev.unb.ca.bappit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ViewSwitcher;
import android.view.animation.AnimationUtils;

import java.util.concurrent.TimeUnit;

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

    private ImageSwitcher practiceGestureSwitcher;
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

        practiceGestureSwitcher = (ImageSwitcher) findViewById(R.id.practice_gesture_switcher);
        initSwitcher();

        Intent intent = getIntent();
        gestureID = intent.getIntExtra(GESTURE_ID, 0);
        gestureName = intent.getStringExtra(GESTURE_NAME);
        gestureCode = intent.getIntExtra(GESTURE_CODE, 0);

        setCurrentGesture(Gesture.values()[gestureCode]);

        practiceGestureSwitcher.setImageResource(gestureID);



        //practiceGestureSwitcher.setVisibility(View.INVISIBLE);



        completeGesture();

    }

    public void completeGesture() {
        setGestureComplete(false);


        //TODO - sounds

        //TODO - animation
    }

    public void gestureMatch(Gesture gesture)
    {
        //TODO - Checkmark, with n/3 successes show
        try {
            practiceGestureSwitcher.setImageResource(0);
            practiceGestureSwitcher.setImageResource(R.mipmap.check_mark_transparent);
            count++;

            //TimeUnit.SECONDS.sleep(5); FIXME - If you still want this, thne you need to put it in an AsyncTask

            practiceGestureSwitcher = (ImageSwitcher) findViewById(R.id.practice_gesture_switcher);
            practiceGestureSwitcher.setImageResource(gestureID);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void gestureMismatch(Gesture gesture)
    {
        practiceGestureSwitcher.setImageResource(gestureID);
        count = 0;
        //TODO - "X", with n/3 successes reset
    }

    private void initSwitcher() {
        practiceGestureSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(PracticeGestureActivity.this);
                return imageView;
            }
        });

        practiceGestureSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));//TODO - better animations
        practiceGestureSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
    }
}
