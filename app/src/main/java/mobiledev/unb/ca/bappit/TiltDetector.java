package mobiledev.unb.ca.bappit;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class TiltDetector implements SensorEventListener {

    /*
     * The gForce that is necessary to register as shake.
     * Must be greater than 1G (one earth gravity unit).
     * You can install "G-Force", by Blake La Pierre
     * from the Google Play Store and run it to see how
     *  many G's it takes to register a shake
     */

    private static final int TILT_SLOP_TIME_MS = 1000;

    private OnTiltListener mListener;
    private long mTiltTimestamp;

    public void setOnTiltListener(OnTiltListener listener) {
        this.mListener = listener;
    }

    public interface OnTiltListener {
        void onTilt(float tilt);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mListener != null) {
            float y = event.values[1];

            if(Math.abs(y) > 8f) {
                final long now = System.currentTimeMillis();

                if(mTiltTimestamp + TILT_SLOP_TIME_MS > now) {
                    return;
                }

                mTiltTimestamp = now;

                mListener.onTilt(y);
            }

        }
    }
}