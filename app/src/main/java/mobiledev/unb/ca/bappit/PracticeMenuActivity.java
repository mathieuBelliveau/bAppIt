package mobiledev.unb.ca.bappit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * @author Mathieu Belliveau
 * A menu to practiceable gestures
 * Each button sends you to a GestureCompatActivity, which allows you to practice that gesture.
 */
public class PracticeMenuActivity extends AppCompatActivity {
    //private View gameView;

    private Button quitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_menu);

        ArrayList<GestureID> gestures = new ArrayList<>();
        gestures.add(new GestureID("bApp It!",R.mipmap.bapp_it, R.raw.vid_tap_it, R.id.practice_bappit_btn, 0));
        gestures.add(new GestureID("Fling It!",R.mipmap.fling_it, R.raw.vid_fling_it, R.id.practice_swipe_btn, 1));
        gestures.add(new GestureID("Shake It!",R.mipmap.shake_it,R.raw.vid_shake_it, R.id.practice_shake_btn, 2));
        gestures.add(new GestureID("Twist It!",R.mipmap.twist_it, R.raw.vid_twist_it, R.id.practice_twist_btn, 3));
        gestures.add(new GestureID("Zoom It!",R.mipmap.zoom_it, R.raw.vid_zoom_it, R.id.practice_zoom_btn, 4));



        for (final GestureID gesture : gestures) {
            Button btn = (Button) findViewById(gesture.buttonID);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent1 = new Intent(PracticeMenuActivity.this, TutorialVideoActivity.class);
//
//                    intent1.putExtra(TutorialVideoActivity.GESTURE_VIDEO_ID, gesture.videoID);
//                    intent1.putExtra(TutorialVideoActivity.GESTURE_NAME, gesture.name);
//
//                    startActivity(intent1);
                    Intent intent2 = new Intent(PracticeMenuActivity.this, PracticeGestureActivity.class);

                    intent2.putExtra(PracticeGestureActivity.GESTURE_ID, gesture.imageID);
                    intent2.putExtra(PracticeGestureActivity.GESTURE_NAME, gesture.name);
                    intent2.putExtra(PracticeGestureActivity.GESTURE_CODE, gesture.gestureCode);

                    intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent2);
                }
            });
        }
        quitButton = (Button) (findViewById(R.id.quit_practice_btn));
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PracticeMenuActivity.this, StartMenuActivity.class);
                startActivity(intent);
            }
        });


    }

    private class GestureID {
        private String name;
        private int imageID;
        private int videoID;
        private int buttonID;
        private int gestureCode;//identifies which Gesture enum value

        private GestureID (String name, int imageID, int videoId, int buttonID, int gestureCode) {
            this.name = name;
            this.imageID = imageID;
            this.videoID = videoId;
            this.buttonID = buttonID;
            this.gestureCode = gestureCode;
        }
    }
}
