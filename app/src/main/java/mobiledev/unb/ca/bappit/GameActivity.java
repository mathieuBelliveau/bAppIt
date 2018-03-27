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
import android.util.Log;
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

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "debug";

    private View gameView;
    private int score;

    private Button quitButton;
    private TextView scoreText;
    private TextView currentGestureText;
    private ImageView checkMarkImage;

    private final static String[] gestures = new String[] {"Fling It!", "Tap It!", "Shake It!", "Twist It!", "Zoom It!"};

    private final static int FLING = 0;
    private final static int TAP = 1;
    private final static int SHAKE = 2;
    private final static int TWIST = 3;
    private final static int ZOOM = 4;
    private final static int NUM_GESTURES = 5;
    private final int initialGestureTime = 3000;
    private float deltaPlayRate;

    private int timeForGesture;
    private int currentGesture;
    private boolean gestureComplete;
    private CountDownTimer gestureTimer;
    private ProgressBar timerProgressBar;
    private Sounds sounds;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private Random rand;

    private boolean isVibrate;

    private Sensor mGyroscope;
    private TiltDetector mTiltDetector;



    private GestureDetectorCompat mDetector;
    private ScaleGestureDetector mScaleDetector;

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

        //Tilt Detector
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mTiltDetector = new TiltDetector();
        mTiltDetector.setOnTiltListener(new TiltDetector.OnTiltListener() {
            @Override
            public void onTilt(float tilt) {
                Log.d(DEBUG_TAG, "onTilt" + tilt);
                checkGesture(TWIST);
            }
        });

        scoreText = (TextView) findViewById(R.id.score_txt);
        currentGestureText = (TextView) (findViewById(R.id.current_gesture_txt));
        timerProgressBar = (ProgressBar) findViewById(R.id.timeProgressBar);
        checkMarkImage = (ImageView) findViewById(R.id.check_mark_img);

        scoreText.setVisibility(View.GONE);
        quitButton.setVisibility(View.GONE);
        currentGestureText.setVisibility(View.GONE);
        timerProgressBar.setVisibility(View.GONE);
        checkMarkImage.setVisibility(View.GONE);

        loadResources();
    }

    public void startGame() {
        Log.i("debug", "Starting game");
        score = 0;
        deltaPlayRate = 0.015f;

        SharedPreferences prefs = GameActivity.this.getSharedPreferences(SettingsActivity.PREFS, Context.MODE_PRIVATE);
        isVibrate = prefs.getBoolean(SettingsActivity.VIBRATE,true);

        timeForGesture = initialGestureTime;
        timerProgressBar.setMax(timeForGesture);

        scoreText.setVisibility(View.VISIBLE);
        quitButton.setVisibility(View.VISIBLE);

        changeGesture();
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

    private void vibrate()
    {
        if(isVibrate){
            /*int[] arr = new int[18];
            Arrays.fill(arr,255);
            ((Vibrator) getSystemService(VIBRATOR_SERVICE))
                    .vibrate(VibrationEffect.createWaveform(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500}, arr,-1));*/
            if (Build.VERSION.SDK_INT >= 26) {//To accommodate deprecation across builds
                ((Vibrator) getSystemService(VIBRATOR_SERVICE))
                        .vibrate(VibrationEffect.createOneShot(750,255));
            } else {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(750);
            }}
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
//        boolean retVal = this.mScaleDetector.onTouchEvent(event);
//        retVal = this.mDetector.onTouchEvent(event) || retVal;
//
//        return retVal || super.onTouchEvent(event);
        this.mScaleDetector.onTouchEvent(event);
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
            if (currentGesture != ZOOM) {
                checkGesture(FLING);
                Log.i(DEBUG_TAG, "I'm flingin'!");
                return true;
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            checkGesture(TAP);
            return true;
        }

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            Log.i(DEBUG_TAG, "scale factor: " + detector.getScaleFactor());
            checkGesture(ZOOM);
        }
    }

    private void checkGesture(int gesture) {
        if(currentGesture == gesture && !gestureComplete) {
            increaseScore();
            gestureComplete = true;

            timerProgressBar.setVisibility(View.INVISIBLE);
            currentGestureText.setVisibility(View.GONE);
            checkMarkImage.setVisibility(View.VISIBLE);

            //Play sound effect for correct gesture
            sounds.playSound(currentGesture, true);
        }
        else {
            gestureTimer.cancel();
            gameOver();
        }
    }

    private void changeGesture() {
        //Generate new random gesture
        currentGesture = rand.nextInt(NUM_GESTURES);
        gestureComplete = false;

        //Play announcer sound clip for correct gesture
        if(sounds != null)
            sounds.playSound(currentGesture, false);

        //Update UI elements
        currentGestureText.setText(gestures[currentGesture]);
        RunTextAnimation();
        timerProgressBar.setVisibility(View.VISIBLE);
        currentGestureText.setVisibility(View.VISIBLE);
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
        loadResources();
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
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
}
