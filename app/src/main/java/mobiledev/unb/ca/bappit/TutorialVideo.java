package mobiledev.unb.ca.bappit;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class TutorialVideo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_video);

        Intent intent = getIntent();
        int gestureID = intent.getIntExtra("gesture", 0);

        VideoView video = (VideoView)findViewById(R.id.tutorial_video);
        Uri uri = Uri.parse("android.resource://" + getPackageName()+ "/" + gestureID);
        video.setVideoURI(uri);
        video.start();
    }
}
