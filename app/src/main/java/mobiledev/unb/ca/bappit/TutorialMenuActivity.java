package mobiledev.unb.ca.bappit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

public class TutorialMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        ArrayList<GestureID> gestureIDS = new ArrayList<>();
        gestureIDS.add(new GestureID("Fling It!", R.raw.vid_fling_it, R.id.guide_swipe_btn));
        gestureIDS.add(new GestureID("bApp It!", R.raw.vid_tap_it, R.id.guide_bappit_btn));
        gestureIDS.add(new GestureID("Shake It!", R.raw.vid_shake_it, R.id.guide_shake_btn));
        gestureIDS.add(new GestureID("Twist It!", R.raw.vid_twist_it, R.id.guide_twist_btn));
        gestureIDS.add(new GestureID("Zoom It!", R.raw.vid_zoom_it, R.id.guide_zoom_btn));

        for (final GestureID gestureID : gestureIDS) {
            Button btn = (Button) findViewById(gestureID.buttonID);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TutorialMenuActivity.this, TutorialVideoActivity.class);
                    intent.putExtra(TutorialVideoActivity.GESTURE_VIDEO_ID, gestureID.resourceID);
                    intent.putExtra(TutorialVideoActivity.GESTURE_NAME, gestureID.name);
                    startActivity(intent);
                }
            });
        }
    }

    private class GestureID {
        private String name;
        private int resourceID;
        private int buttonID;

        private GestureID(String name, int resourceId, int buttonID) {
            this.name = name;
            this.resourceID = resourceId;
            this.buttonID = buttonID;
        }
    }
}
