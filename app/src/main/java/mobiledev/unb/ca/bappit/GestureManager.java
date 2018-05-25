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

public interface GestureManager{

    abstract void shakeDetectorInit();
    abstract void tiltDetectorInit();
    abstract void checkGesture(Gesture gesture);

    //FIXME is this proper?
    abstract class MyGestureListener extends GestureDetector.SimpleOnGestureListener{};
    abstract class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {};
}
