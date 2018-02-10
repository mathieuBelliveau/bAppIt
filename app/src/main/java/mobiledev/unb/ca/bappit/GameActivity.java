package mobiledev.unb.ca.bappit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "debug";

    private View gameView;
    private int score;

    private Button gameButton;
    private Button finishButton;
    private TextView scoreText;

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = getWindow().getDecorView();
        hideSystemUI();

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        score = 0;

        gameButton = (Button) (findViewById(R.id.game_btn));
        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseScore();
            }
        });

        finishButton = (Button) (findViewById(R.id.finish_btn));
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameOver();
            }
        });

        scoreText = (TextView) findViewById(R.id.score_txt);

    }

    private void increaseScore() {
        score++;
        scoreText.setText("Score: " + score);
    }

    private void gameOver() {
        Intent finishIntent = new Intent(GameActivity.this, FinalScoreActivity.class);
        finishIntent.putExtra(FinalScoreActivity.FINAL_SCORE, score);
        startActivity(finishIntent);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d(DEBUG_TAG, "onTap: " + e.toString());
            return true;
        }
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        gameView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
