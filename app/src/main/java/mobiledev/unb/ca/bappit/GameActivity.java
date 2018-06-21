package mobiledev.unb.ca.bappit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private View gameView;
    private int score;

    private Button quitButton;
    private TextView scoreText;
    private ImageView currentGestureImage;
    private ImageView checkMarkImage;

    private final int initialGestureTime = 3000;
    private float deltaPlayRate;

    private int timeForGesture;
    private Gesture currentGesture;
    private boolean gestureComplete;
    private CountDownTimer gestureTimer;
    private ProgressBar timerProgressBar;
    private Sounds sounds;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private GestureDetectorCompat mDetector;
    private ShakeDetector mShakeDetector;
    private TiltDetector mTiltDetector;
    private ScaleGestureDetector mScaleDetector;

    private static Random rand;

    private boolean isVibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = getWindow().getDecorView();
        hideSystemUI();

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
        rand = new Random();

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
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                checkGesture(Gesture.SHAKE);
            }
        });

        //Tilt Detector initialization
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mTiltDetector = new TiltDetector();
        mTiltDetector.setOnTiltListener(new TiltDetector.OnTiltListener() {
            @Override
            public void onTilt(float tilt) {
                checkGesture(Gesture.TWIST);
            }
        });

        scoreText = (TextView) findViewById(R.id.score_txt);
        currentGestureImage = (ImageView) (findViewById(R.id.current_gesture_img));
        timerProgressBar = (ProgressBar) findViewById(R.id.timeProgressBar);
        checkMarkImage = (ImageView) findViewById(R.id.check_mark_img);

        scoreText.setVisibility(View.GONE);
        quitButton.setVisibility(View.GONE);
        currentGestureImage.setVisibility(View.GONE);
        timerProgressBar.setVisibility(View.GONE);
        checkMarkImage.setVisibility(View.GONE);

        loadResources();
    }

    public void startGame() {
        score = 0;
        deltaPlayRate = 0.015f;

        SharedPreferences prefs = GameActivity.this.getSharedPreferences(SettingsActivity.PREFS, Context.MODE_PRIVATE);
        isVibrate = prefs.getBoolean(SettingsActivity.VIBRATE,true);

        timeForGesture = initialGestureTime;
        timerProgressBar.setMax(timeForGesture);

        scoreText.setVisibility(View.VISIBLE);
        quitButton.setVisibility(View.VISIBLE);

        introCycle();
        //randomGesture();
    }

    private void loadResources()
    {
        LoadResourcesTask task = new LoadResourcesTask();
        task.execute();
    }

    private void increaseScore() {
        score++;
        scoreText.setText("Score: " + score);
    }

    private void gameOver() {
        sounds.stopAllMusic();
        vibrate();//FIXME - Probably want it on a separate thread
        Intent finishIntent = new Intent(GameActivity.this, FinalScoreActivity.class);
        finishIntent.putExtra(FinalScoreActivity.FINAL_SCORE, score);
        startActivity(finishIntent);
    }

    //FIXME Figure out better vibration patterns
    private void vibrate()
    {
        if(isVibrate){
           if (Build.VERSION.SDK_INT >= 26) {//To accommodate deprecation across builds
                ((Vibrator) getSystemService(VIBRATOR_SERVICE))
                        .vibrate(VibrationEffect.createOneShot(750,255));
            } else {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(750);
            }}
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mScaleDetector.onTouchEvent(event);
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void checkGesture(Gesture gesture) {
        if(currentGesture == gesture && !gestureComplete) {
            increaseScore();
            gestureComplete = true;

            timerProgressBar.setVisibility(View.INVISIBLE);
            //currentGestureText.setVisibility(View.GONE);
            currentGestureImage.setVisibility(View.GONE);
            checkMarkImage.setVisibility(View.VISIBLE);

            //Play sound effect for correct gesture
            sounds.playSound(currentGesture, true);
        }
        else {
            gestureTimer.cancel();
            if(gestureComplete)
                gestureHint();

            correctionMessage(gesture);
            gameOver();
        }
    }

    private void correctionMessage(Gesture gesture){
        String mismatchMessage = "";//TODO Get a better message
        if(gesture == Gesture.SHAKE)
            mismatchMessage = "Oops! You shook it!";
        else
            mismatchMessage = "Oops! You " + gesture.toString().toLowerCase() + "ed it!";
        Toast.makeText(this, mismatchMessage,
                Toast.LENGTH_SHORT).show();
    }

    /*TODO a method that will display a popup window
    This popup tell the user to stop during the grace period
    Have some friendly reminders with little drawings.*/
    private void gestureHint()
    {

    }

    private void  introCycle()
    {
        for (Gesture gesture : Gesture.values())
        {
            currentGesture = gesture;
            completeGesture();
            break;
        }
    }

    private void randomGesture() {
        currentGesture = Gesture.getRandomGesture();
        completeGesture();
    }

    private void completeGesture()
    {
        gestureComplete = false;

        //Play announcer sound clip for correct gesture
        if(sounds != null)
            sounds.playSound(currentGesture, false);

        //Update UI elements
        currentGestureImage.setImageResource(currentGesture.getGestureImageId());
        RunTextAnimation();
        timerProgressBar.setVisibility(View.VISIBLE);
        currentGestureImage.setVisibility(View.VISIBLE);
        checkMarkImage.setVisibility(View.GONE);

        //Set new time limit
        timeForGesture -= initialGestureTime * deltaPlayRate;
        timerProgressBar.setMax(timeForGesture);

        //Decrease rate of change after a certain point
        if(score == 25)
            deltaPlayRate = 0.01f;
        if(score == 50)
            deltaPlayRate = 0.005f;
        sounds.incrementMusicRate(deltaPlayRate);

        gestureTimer = new CountDownTimer(timeForGesture, 50) {
            public void onTick(long millisUntilFinished) {
                if(!gestureComplete) {
                    timerProgressBar.setProgress((int)millisUntilFinished);
                }
            }

            public void onFinish() {
                if(gestureComplete) {
                    randomGesture();
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
        currentGestureImage.clearAnimation();
        currentGestureImage.startAnimation(a);
    }

    private void hideSystemUI() {
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
        loadResources();
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mTiltDetector, mGyroscope, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        mSensorManager.unregisterListener(mTiltDetector);
        sounds.stopAllMusic();
        gestureTimer.cancel();
        super.onPause();
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            if (currentGesture != Gesture.ZOOM) {
                checkGesture(Gesture.FLING);
                return true;
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            checkGesture(Gesture.BAPP);
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            checkGesture(Gesture.ZOOM);
        }
    }

    private enum MusicState {JUST_STARTED, PLAYING, STILL_LOADING};

    private class LoadResourcesTask extends AsyncTask<Void, Integer, MusicState> {
        @Override
        protected MusicState doInBackground(Void... params) {
            if(sounds == null) {
                //Hardware buttons setting to adjust the media sound
                GameActivity.this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
                AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                sounds = new Sounds(audioManager,  GameActivity.this);
            }

            if(sounds.loaded && sounds.musicPlaying) {
                return MusicState.PLAYING;
            }
            else if (sounds.loaded) {
                sounds.startBackgroundMusic();
                return MusicState.JUST_STARTED;
            } else {
                try {
                   Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return MusicState.STILL_LOADING;
            }
        }

        @Override
        protected void onPostExecute(MusicState state) {
            super.onPostExecute(state);
            if(state == MusicState.JUST_STARTED) {
                startGame();
            }
            else if (state == MusicState.STILL_LOADING) {
                loadResources();
            }
        }
    }

    public enum Gesture {
        BAPP (R.mipmap.bappit),
        FLING (R.mipmap.fling_it),
        SHAKE (R.mipmap.shake_it),
        TWIST (R.mipmap.twist_it),
        ZOOM (R.mipmap.zoom_it);

        private int gestureImageId;

        Gesture (int imageId) {
            this.gestureImageId = imageId;
        }

        public int getGestureImageId() {
            return gestureImageId;
        }

        public static int numGestures() {
            return values().length;
        }

        public static Gesture getRandomGesture() {
            return values()[rand.nextInt(Gesture.numGestures())];
        }
    }
}
