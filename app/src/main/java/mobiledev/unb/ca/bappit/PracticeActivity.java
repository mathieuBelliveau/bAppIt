package mobiledev.unb.ca.bappit;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * @author Mathieu Belliveau
 * An activity to practice the set of gestures through prompts and user responses.
 *
 */
/*FIXME - Should it begin as a cycle, or have a series of static images that prompt that gesture?
    Probably the former*/
public class PracticeActivity extends GestureCompatActivity {
    private View gameView;

    private Button quitButton;
    private ImageView practiceGestureImage;

    private Sounds sounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        gameView = getWindow().getDecorView();
        hideSystemUI();

        //TODO - Add static layout similar to TutorialMenuActivity.java

        /*TODO
        ArrayList<Gesture> gestures = new ArrayList<>();
        gestures.add(new Gesture("Fling It!", R.raw.vid_fling_it, R.id.guide_swipe_btn));
        gestures.add(new Gesture("bApp It!", R.raw.vid_tap_it, R.id.guide_bappit_btn));
        gestures.add(new Gesture("Shake It!", R.raw.vid_shake_it, R.id.guide_shake_btn));
        gestures.add(new Gesture("Twist It!", R.raw.vid_twist_it, R.id.guide_twist_btn));
        gestures.add(new Gesture("Zoom It!", R.raw.vid_zoom_it, R.id.guide_zoom_btn));

        TODO for (final Gesture gesture : gestures) {
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

        TODO
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
         */

        quitButton = (Button) (findViewById(R.id.quit_btn));
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Intent to send back to GameActivity
            }
        });

        practiceGestureImage = (ImageView) findViewById(R.id.practice_gesture_img);

        quitButton.setVisibility(View.GONE);
        practiceGestureImage.setVisibility(View.GONE);
        loadResources();
    }

    public void startInstance()//FIXME - may abstract in super class
    {
        SharedPreferences prefs = this.getSharedPreferences();//TODO - sound preferences

        quitButton.setVisibility(View.VISIBLE);
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

    private void hideSystemUI() {//FIXME which flags can go?
        gameView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void loadResources()
    {
        LoadResourcesTask task = new LoadResourcesTask();
        task.execute();
    }

    private class LoadResourcesTask extends AsyncTask<Void, Integer, Void>//FIXME - not set in stone
    {
        @Override
        protected void doInBackground(Void... params)
        {
            //TODO
        }

        @Override
        protected void onPostExecute()
        {
            //TODO
            startInstance();
        }
    }
}
