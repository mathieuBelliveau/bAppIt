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
        gestures.add(new GestureID("Fling It!", R.raw.vid_fling_it, R.id.guide_swipe_btn));
        gestures.add(new GestureID("bApp It!", R.raw.vid_tap_it, R.id.guide_bappit_btn));
        gestures.add(new GestureID("Shake It!", R.raw.vid_shake_it, R.id.guide_shake_btn));
        gestures.add(new GestureID("Twist It!", R.raw.vid_twist_it, R.id.guide_twist_btn));
        gestures.add(new GestureID("Zoom It!", R.raw.vid_zoom_it, R.id.guide_zoom_btn));

        for (final GestureID gesture : gestures) {
            Button btn = (Button) findViewById(gesture.buttonID);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PracticeMenuActivity.this, PracticeGestureActivity.class);
                    intent.putExtra(PracticeGestureActivity.GESTURE_ID, gesture.resourceID);
                    intent.putExtra(PracticeGestureActivity.GESTURE_NAME, gesture.name);
                    startActivity(intent);
                }
            });
        }
        quitButton = (Button) (findViewById(R.id.quit_btn));
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Intent to send back to main menu
            }
        });

        quitButton.setVisibility(View.GONE);
    }

    private class GestureID {
        private String name;
        private int resourceID;
        private int buttonID;

        private GestureID (String name, int resourceId, int buttonID) {
            this.name = name;
            this.resourceID = resourceId;
            this.buttonID = buttonID;
        }
    }
}
