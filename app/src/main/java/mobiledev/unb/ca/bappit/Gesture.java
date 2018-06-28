package mobiledev.unb.ca.bappit;

/**
 * Created by Mathieu on 4/8/2018.
 */

import java.util.Random;

public enum Gesture {//TODO Does this need to be in an interface or abtract class?
    BAPP (R.mipmap.bappit),
    FLING (R.mipmap.fling_it),
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
        return values()[rand.nextInt(Gesture.numGestures())];
    }
}
