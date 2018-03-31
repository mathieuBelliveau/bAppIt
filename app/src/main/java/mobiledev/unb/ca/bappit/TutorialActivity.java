package mobiledev.unb.ca.bappit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.view.ViewGroup.LayoutParams;
import java.util.ArrayList;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        ArrayList<Gesture> gestures = new ArrayList<Gesture>();
        gestures.add(new Gesture("Fling It!", R.raw.vid_fling_it, R.id.guide_swipe_btn));
        gestures.add(new Gesture("Tap It!", R.raw.vid_tap_it, R.id.guide_bappit_btn));
        gestures.add(new Gesture("Shake It!", R.raw.vid_shake_it, R.id.guide_shake_btn));
        gestures.add(new Gesture("Twist It!", R.raw.vid_twist_it, R.id.guide_twist_btn));
        gestures.add(new Gesture("Zoom It!", R.raw.vid_zoom_it, R.id.guide_zoom_btn));

        for (final Gesture gesture : gestures) {
            Button btn = (Button) findViewById(gesture.buttonID);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TutorialActivity.this, TutorialVideo.class);
                    intent.putExtra(TutorialVideo.GESTURE_VIDEO_ID, gesture.resourceID);
                    intent.putExtra(TutorialVideo.GESTURE_NAME, gesture.name);
                    startActivity(intent);
                }
            });
        }
    }


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
