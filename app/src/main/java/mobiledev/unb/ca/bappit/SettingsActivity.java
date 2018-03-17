package mobiledev.unb.ca.bappit;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor = null;
    private SeekBar musicBar;
    private SeekBar effectsBar;
    private SeekBar voiceBar;
    private Switch vibeToggle;

    private TextView saveButton;
    //TODO - Vibration toggle

    private String prefsFile = "SettingsPrefs";//FIXME - recommend making "public static final" in the main activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editor = getSharedPreferences(prefsFile, MODE_PRIVATE).edit();

        musicBar = (SeekBar)findViewById(R.id.MusicSeekBar);
        effectsBar = (SeekBar)findViewById(R.id.SFXSeekBar);
        voiceBar = (SeekBar)findViewById(R.id.AnnouncerSeekBar);

        //Get initial values
        SharedPreferences prefs = getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
        musicBar.setProgress(prefs.getInt("MUSIC", 75)); //50 is the default value.
        effectsBar.setProgress(prefs.getInt("FX", 75)); //50 is the default value.
        voiceBar.setProgress(prefs.getInt("VOICE", 75)); //50 is the default value.

        vibeToggle = (Switch)findViewById(R.id.VibrationSwitch);

        saveButton = (TextView)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyChanges();
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    private void applyChanges()
    {
        editor.putInt("MUSIC", musicBar.getProgress());
        editor.putInt("FX", effectsBar.getProgress());
        editor.putInt("VOICE", voiceBar.getProgress());
//        editor.putBoolean("VIBRATE",);

        editor.apply();
    }

}
