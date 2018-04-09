package mobiledev.unb.ca.bappit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import java.util.Random;

/**
 * Created by Mathieu on 4/7/2018.
 */

abstract class GestureManager{
    private static Random rand;
    private Context currActivity;

    private Gesture currentGesture;
    private boolean gestureComplete;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private GestureDetectorCompat mDetector;
    private ShakeDetector mShakeDetector;
    private TiltDetector mTiltDetector;
    private ScaleGestureDetector mScaleDetector;

    //assuming the context is an activity extending AppCompatActivity
    public GestureManager(AppCompatActivity activity)
    {
        currActivity = activity;

        /* initializations*/
        mDetector = new GestureDetectorCompat(currActivity, new MyGestureListener());
        mScaleDetector = new ScaleGestureDetector(currActivity, new ScaleListener());

        // ShakeDetector initialization
        mSensorManager = (SensorManager) currActivity.getSystemService(Context.SENSOR_SERVICE);
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
        }

    abstract void checkGesture(Gesture gesture);

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mScaleDetector.onTouchEvent(event);
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
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
}
