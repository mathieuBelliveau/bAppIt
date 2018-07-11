package mobiledev.unb.ca.bappit;

import android.os.Bundle;
import android.widget.ImageView;

public class PracticeGestureActivity extends GestureCompatActivity{

    public final static String GESTURE_ID = "gestureVideoId";
    public final static String GESTURE_NAME = "gestureName";

    private ImageView practiceGestureImage;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //TODO
    }

    public void gestureMatch(Gesture gesture)
    {
        //TODO
        setGestureComplete(true);
    }

    public void gestureMismatch(Gesture gesture)
    {
        //TODO
    }
}
