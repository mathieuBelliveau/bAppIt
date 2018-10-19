package mobiledev.unb.ca.bappit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import java.util.Random;

/**
 * Created by Mathieu on 4/7/2018.
 */

public abstract class GestureCompatActivity extends AppCompatActivity{

    private Gesture currentGesture;
    private boolean gestureComplete;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private GestureDetectorCompat mDetector;
    private ShakeDetector mShakeDetector;
    private TiltDetector mTiltDetector;
    private static ScaleGestureDetector mScaleDetector;

    private static Random rand;

    abstract void gestureMatch(Gesture gesture);
    abstract void gestureMismatch(Gesture gesture);
    abstract void completeGesture();
//    abstract void checkGesture(Gesture gesture);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());//FIXME - may need to use getCurrentContext(), or whatever
        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
        rand = new Random();

        initShakeDetect();
        initTiltDetect();
    }

    public void registerListeners()
    {
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mTiltDetector, mGyroscope, SensorManager.SENSOR_DELAY_GAME);
    }

    public void unregisterListeners()
    {
        mSensorManager.unregisterListener(mShakeDetector);
        mSensorManager.unregisterListener(mTiltDetector);
    }

    public void setTouchEvent(MotionEvent event)
    {
        this.mScaleDetector.onTouchEvent(event);
        this.mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        setTouchEvent(event);
        return super.onTouchEvent(event);
    }




    public boolean isGestureComplete() {
        return gestureComplete;
    }

    public void setGestureComplete(boolean gestureComplete) {
        this.gestureComplete = gestureComplete;
    }

    public Gesture getCurrentGesture() {
        return currentGesture;
    }

    public void setCurrentGesture(Gesture currentGesture) {
        this.currentGesture = currentGesture;
    }

    public void checkGesture(Gesture gesture) {
        if(currentGesture == gesture && !gestureComplete) {
            gestureMatch(gesture);
        }
        else {
            gestureMismatch(gesture);
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            checkGesture(Gesture.ZOOM);
        }
    }

    private void initShakeDetect()
    {
        // ShakeDetector initialization
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                checkGesture(Gesture.SHAKE);
            }
        });
    }

    private void initTiltDetect()
    {
        //Tilt Detector initialization
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mTiltDetector = new TiltDetector();
        mTiltDetector.setOnTiltListener(new TiltDetector.OnTiltListener() {
            @Override
            public void onTilt(float tilt) {
                checkGesture(Gesture.TWIST);
            }
        });
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

    public enum Gesture {
        BAPP (R.mipmap.bapp_it),
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
