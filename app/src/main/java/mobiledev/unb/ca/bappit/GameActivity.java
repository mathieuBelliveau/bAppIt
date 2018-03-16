package mobiledev.unb.ca.bappit;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "debug";

    private View gameView;
    private int score;

    private Button quitButton;
    private TextView scoreText;
    private TextView currentGestureText;
    private ImageView checkMarkImage;

    private final static String[] gestures = new String[] {"Swipe It!", "Tap It!", "Shake It!"};

    private final static int SWIPE = 0;
    private final static int TAP = 1;
    private final static int SHAKE = 2;
    private final static int NUM_GESTURES = 3;

    private int timeForGesture;
    private int currentGesture;
    private boolean gestureComplete;
    private CountDownTimer gestureTimer;
    private ProgressBar timerProgressBar;
    private Sounds sounds;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    Random rand;

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = getWindow().getDecorView();
        hideSystemUI();

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        rand = new Random();

        score = 0;

        quitButton = (Button) (findViewById(R.id.quit_btn));
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gestureTimer.cancel();
                gameOver();
            }
        });

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                Log.d(DEBUG_TAG, "onShake: " + count);
                checkGesture(SHAKE);
            }
        });

        scoreText = (TextView) findViewById(R.id.score_txt);
        currentGestureText = (TextView) (findViewById(R.id.current_gesture_txt));
        timerProgressBar = (ProgressBar) findViewById(R.id.timeProgressBar);
        checkMarkImage = (ImageView) findViewById(R.id.check_mark_img);


        timeForGesture = 3000;
        timerProgressBar.setMax(timeForGesture);

        changeGesture();
    }

    private void initSound()
    {
        //Hardware buttons setting to adjust the media sound
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        sounds = new Sounds(audioManager, this);
        sounds.startMusic();
    }

    private void increaseScore() {
        score++;
        scoreText.setText("Score: " + score);
    }

    private void gameOver() {
        sounds.stopMusic();
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
            checkGesture(SWIPE);
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d(DEBUG_TAG, "onTap: " + e.toString());
            checkGesture(TAP);
            return true;
        }

    }

    private void checkGesture(int gesture) {
        if(currentGesture == gesture && !gestureComplete) {
            increaseScore();
            gestureComplete = true;

            timerProgressBar.setVisibility(View.INVISIBLE);
            currentGestureText.setVisibility(View.GONE);
            checkMarkImage.setVisibility(View.VISIBLE);

            sounds.playSound(currentGesture);
        }
        else {
            gestureTimer.cancel();
            gameOver();
        }
    }

    private void changeGesture() {
        currentGesture = rand.nextInt(NUM_GESTURES);

        currentGestureText.setText(gestures[currentGesture]);

        gestureComplete = false;
        RunTextAnimation();
        timerProgressBar.setVisibility(View.VISIBLE);
        currentGestureText.setVisibility(View.VISIBLE);
        checkMarkImage.setVisibility(View.GONE);

        timeForGesture -= timeForGesture*0.03; //decrease logarithmically
        timerProgressBar.setMax(timeForGesture);
        
        gestureTimer = new CountDownTimer(timeForGesture, 50) {
            public void onTick(long millisUntilFinished) {
                if(!gestureComplete) {
                    timerProgressBar.setProgress((int)millisUntilFinished);
                }
            }

            public void onFinish() {
                if(gestureComplete) {
                    changeGesture();
                }
                else{
                    gameOver();
                }
            }
        }.start();
    }

    private void RunTextAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale_text);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.current_gesture_txt);
        tv.clearAnimation();
        tv.startAnimation(a);
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

    @Override
    public void onResume() {
        initSound();
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        sounds.stopMusic();
        super.onPause();
    }
}
