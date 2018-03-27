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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.view.ViewGroup.LayoutParams;
import java.util.ArrayList;

public class TutorialActivity extends AppCompatActivity {

    public ListView mGesturesList;
    public PopupWindow popup;
    private LinearLayout containerLayout;
    private LinearLayout mainLayout;
    private boolean isClicked;
    private LayoutParams layoutParams;
    private int centerX;
    private int centerY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        ArrayList<Gesture> gestures = new ArrayList<Gesture>();
        gestures.add(new Gesture("Fling It", R.raw.fling_it_vid));
        gestures.add(new Gesture("Tap It", R.raw.tap_it_vid));
        gestures.add(new Gesture("Shake It", R.raw.shake_it_vid));
        gestures.add(new Gesture("Twist It", R.raw.twist_it_vid));
        gestures.add(new Gesture("Zoom It", R.raw.zoom_it_vid));

        mGesturesList = (ListView)findViewById(R.id.gestures_list);
        GestureAdaptor adaptor = new GestureAdaptor(this, gestures);

        mGesturesList.setAdapter(adaptor);

        popup = new PopupWindow(this);

        isClicked = false;

        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        centerX = mdispSize.x/2;
        centerY = mdispSize.y/2;

        layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);



    }


    private class GestureAdaptor extends ArrayAdapter<Gesture> {
        ArrayList<Gesture> gestures;

        public GestureAdaptor(Context context, ArrayList<Gesture> gestures) {
            super(context, 0, gestures);
            this.gestures = gestures;
        }

        // The newView method is used to inflate a new view and return it,
        // you don't bind any data to the view at this point.

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final Gesture gesture = gestures.get(position);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.gesture_item, parent, false);
            }
            // Lookup view for data population
            TextView gestureName = (TextView) convertView.findViewById(R.id.gesture_name);
            gestureName.setText(gesture.name);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!isClicked) {
                        isClicked = true;

                        Intent intent = new Intent(TutorialActivity.this, TutorialVideo.class);
                        intent.putExtra("gesture", gesture.resourceID);
                        startActivity(intent);
////                        VideoView video = new VideoView(TutorialActivity.this);
////                        Uri uri = Uri.parse("android.resource://" + getPackageName()+ "/" + gesture.resourceID);
////                        Log.i("debug", gesture.name+ " " + centerX + " " + centerY);
////                        video.setVideoURI(uri);
//
////                        View gestureVideoView = LayoutInflater.from(getContext()).inflate(R.layout.gesture_video, convertView, false);
//
////                        TextView test = new TextView(TutorialActivity.this);
////                        test.setText("testing!!!");
//
//                        LinearLayout containerLayout = new LinearLayout(TutorialActivity.this);
//                        containerLayout.setOrientation(LinearLayout.VERTICAL);
//                        containerLayout.addView(video, layoutParams);
//                        popup.setContentView(containerLayout);
//
////                        popup.showAtLocation(mGesturesList, Gravity.CENTER, 0, 0);
////                        video.requestFocus();
////                        video.start();
                    }
                    else {
                        Log.i("debug", gesture.name+ " " + isClicked);
                        isClicked = false;
                        popup.dismiss();
                    }


//                    video = LayoutInflater.from(getContext()).inflate(0, video);



                }
            });
            return convertView;
        }
    }


    private class Gesture {
        private String name;
        private int resourceID;

        private Gesture (String name, int resourceId) {
            this.name = name;
            this.resourceID = resourceId;
        }
    }

    @Override
    protected void onPause() {
        popup.dismiss();
        super.onPause();
    }
}
