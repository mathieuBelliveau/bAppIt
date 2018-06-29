package mobiledev.unb.ca.bappit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

public class TutorialMenuActivity extends GestureCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        ArrayList<Gesture> gestures = new ArrayList<>();
        gestures.add(new Gesture("Fling It!", R.raw.vid_fling_it, R.id.guide_swipe_btn));
        gestures.add(new Gesture("bApp It!", R.raw.vid_tap_it, R.id.guide_bappit_btn));
        gestures.add(new Gesture("Shake It!", R.raw.vid_shake_it, R.id.guide_shake_btn));
        gestures.add(new Gesture("Twist It!", R.raw.vid_twist_it, R.id.guide_twist_btn));
        gestures.add(new Gesture("Zoom It!", R.raw.vid_zoom_it, R.id.guide_zoom_btn));

        for (final Gesture gesture : gestures) {
            Button btn = (Button) findViewById(gesture.buttonID);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TutorialMenuActivity.this, TutorialVideoActivity.class);
                    intent.putExtra(TutorialVideoActivity.GESTURE_VIDEO_ID, gesture.resourceID);
                    intent.putExtra(TutorialVideoActivity.GESTURE_NAME, gesture.name);
                    startActivity(intent);
                }
            });
        }
    }

    public void gestureSuccess(GestureCompatActivity.Gesture gesture){}

    public void gestureFailure(GestureCompatActivity.Gesture gesture){}

    public void checkGesture(GestureCompatActivity.Gesture gesture){}


    private class Gesture {
        private String name;
        private int resourceID;
        private int buttonID;

        private Gesture (String name, int resourceId, int buttonID) {
            this.name = name;
            this.resourceID = resourceId;
            this.buttonID = buttonID;
        }
    }
}
