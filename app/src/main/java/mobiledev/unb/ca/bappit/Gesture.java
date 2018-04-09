package mobiledev.unb.ca.bappit;

/**
 * Created by Mathieu on 4/8/2018.
 */

import java.util.Random;

public enum Gesture {
    FLING (R.mipmap.fling_it),
    BAPP (R.mipmap.bappit),
    SHAKE (R.mipmap.shake_it),
    TWIST (R.mipmap.twist_it),
    ZOOM (R.mipmap.zoom_it);

    private int gestureImageId;
    private static Random rand;

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
        return values()[rand.nextInt(GestureManager.Gesture.numGestures())];
    }
}
