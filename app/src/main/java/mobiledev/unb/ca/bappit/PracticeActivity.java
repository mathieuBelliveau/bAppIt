package mobiledev.unb.ca.bappit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PracticeActivity extends GestureCompatActivity {
    private View gameView;

    private Button quitButton;
    private ImageView practiceGestureImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        gameView = getWindow().getDecorView();
        hideSystemUI();

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
    }

    public void gestureMatch(Gesture gesture)
    {
        //TODO
        setGestureComplete(true);
    }

    public void gestureMismatch(Gesture gesture)
    {

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
}
