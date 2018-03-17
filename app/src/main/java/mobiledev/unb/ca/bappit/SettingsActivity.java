package mobiledev.unb.ca.bappit;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor = null;
    private SeekBar musicBar = null;
    private SeekBar effectsBar = null;
    private SeekBar voiceBar = null;
    private Switch vibeToggle = null;
    //TODO - Vibration toggle

    private int[] vol = new int[3];//holds volumes until write in "onPause()"



    private String prefs = "SettingsPrefs";//FIXME - recommend making "public static final" in the main activity

    //TODO - Put in GameActivity
    /*
    SharedPreferences prefs = getSharedPreferences("SettingsPrefs", MODE_PRIVATE);
    String restoredText = prefs.getString("MUSIC", null);
    if (restoredText != null) {
      int idName = prefs.getInt("MUSIC", 50); //50 is the default value.
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initControls();
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        applyChanges();
    }

    private void applyChanges()
    {
        editor.putInt("MUSIC", vol[0]);
        editor.putInt("FX", vol[1]);
        editor.putInt("VOICE", vol[2]);
        editor.putBoolean("VIBRATE",);

        editor.apply();
    }

    private void initControls()//can make less redundant @Mathieu
    {
        editor = getSharedPreferences(prefs, MODE_PRIVATE).edit();

        musicBar = (SeekBar)findViewById(R.id.MusicSeekBar);
        effectsBar = (SeekBar)findViewById(R.id.SFXSeekBar);
        voiceBar = (SeekBar)findViewById(R.id.AnnouncerSeekBar);

        vibeToggle = (Switch)findViewById(R.id.VibrationSwitch);



        musicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                vol[0] = i;
            }//Notification the progress level changed.

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }//User started touch gesture

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }//User finished touch gesture
        });

        effectsBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                vol[1] = i;
            }//Notification the progress level changed.

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }//User started touch gesture

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }//User finished touch gesture
        });

        voiceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                vol[2] = i;
            }//Notification the progress level changed.

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }//User started touch gesture

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }//User finished touch gesture
        });


    }



}
