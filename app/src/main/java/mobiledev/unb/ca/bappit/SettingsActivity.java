package mobiledev.unb.ca.bappit;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor = null;
    private SeekBar musicBar;
    private SeekBar effectsBar;
    private SeekBar voiceBar;
    private Switch vibeToggle;

    protected final static String MUSIC = "MUSIC";
    protected final static String FX = "FX";
    protected final static String VOICE = "VOICE";
    protected final static String VIBRATE = "VIBRATE";
    protected final static String PREFS = "SettingsPrefs";

    private TextView saveButton;
    //TODO - Vibration toggle



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();

        musicBar = (SeekBar)findViewById(R.id.MusicSeekBar);
        effectsBar = (SeekBar)findViewById(R.id.SFXSeekBar);
        voiceBar = (SeekBar)findViewById(R.id.AnnouncerSeekBar);

        vibeToggle = (Switch)findViewById(R.id.VibrationSwitch);


        //Get initial values
        SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        musicBar.setProgress(prefs.getInt(MUSIC, 75)); //50 is the default value.
        effectsBar.setProgress(prefs.getInt(FX, 75)); //50 is the default value.
        voiceBar.setProgress(prefs.getInt(VOICE, 75)); //50 is the default value.
        vibeToggle.setChecked(prefs.getBoolean(VIBRATE,true)); //True is the default value.


    }

    @Override
    protected void onPause()
    {
        super.onPause();
        applyChanges();
        Toast.makeText(this, "Settings saved!",
                Toast.LENGTH_LONG).show();
    }

    private void applyChanges()
    {
        editor.putInt(MUSIC, musicBar.getProgress());
        editor.putInt(FX, effectsBar.getProgress());
        editor.putInt(VOICE, voiceBar.getProgress());
        editor.putBoolean(VIBRATE,vibeToggle.isChecked());

        editor.apply();
    }


}
