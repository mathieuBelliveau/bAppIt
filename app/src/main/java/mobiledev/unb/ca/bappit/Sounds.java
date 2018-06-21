package mobiledev.unb.ca.bappit;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Debug;
import android.util.Log;

public class Sounds {
    private SoundPool soundPool;
    private int[] soundFXIds;
    private int[] announcerIds;
    private Context context;


    private int backMusic;
    private int backStream;
    private  float musicRate;

    boolean loaded = false, musicPlaying = false;
    float musicVolume, soundFXvolume, announcerVolume;
    int counter;


    public Sounds(AudioManager audioManager, Context context) {
        this.context = context;

        SharedPreferences prefs = context.getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE);

        float musicVol = prefs.getInt(SettingsActivity.MUSIC, 100);
        float fxVol = prefs.getInt(SettingsActivity.FX, 100);
        float voiceVol = prefs.getInt(SettingsActivity.VOICE, 100);

        float actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        musicVolume = actVolume*(musicVol/100) / maxVolume;
        soundFXvolume = actVolume*(fxVol/100) / maxVolume;
        announcerVolume = actVolume*(voiceVol/100) / maxVolume;

        musicRate = 0.5f;
        // Load the sounds
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        soundPool = new SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                counter++;
                if(counter == 11) {
                    loaded = true;
                }
            }
        });

        backMusic = soundPool.load(context,R.raw.background_music_modified, 1);

        announcerIds = new int[5];
        announcerIds[0] = soundPool.load(context, R.raw.voice_fling_it, 1);
        announcerIds[1] = soundPool.load(context, R.raw.voice_bapp_it, 1);
        announcerIds[2] = soundPool.load(context, R.raw.voice_shake_it, 1);
        announcerIds[3] = soundPool.load(context, R.raw.voice_twist_it, 1);
        announcerIds[4] = soundPool.load(context, R.raw.voice_zoom_it, 1);

        soundFXIds = new int[5];
        soundFXIds[0] = soundPool.load(context, R.raw.fx_fling_it, 1);
        soundFXIds[1] = soundPool.load(context, R.raw.fx_tap_it, 1);
        soundFXIds[2] = soundPool.load(context, R.raw.fx_shake_it, 1);
        soundFXIds[3] = soundPool.load(context, R.raw.fx_twist_it, 1);
        soundFXIds[4] = soundPool.load(context, R.raw.fx_zoom_it, 1);

    }

    public void playSound(Gesture gesture, boolean isSoundEffect) {
        if (loaded) {
            int sID = 0;
            if(gesture == Gesture.FLING)
                sID = 0;
            else if (gesture == Gesture.BAPP)
                sID = 1;
            else if (gesture == Gesture.SHAKE)
                sID = 2;
            else if (gesture == Gesture.TWIST)
                sID = 3;
            else if (gesture == Gesture.ZOOM)
                sID = 4;

            if(isSoundEffect)
                soundPool.play(soundFXIds[sID], soundFXvolume, soundFXvolume, 1, 0, 1f);
            else
                soundPool.play(announcerIds[sID], announcerVolume, announcerVolume, 1, 0, 1f);
        }
    }

    public void startBackgroundMusic()
    {
        if(loaded) {
            musicPlaying = true;
            backStream = soundPool.play(backMusic, musicVolume, musicVolume, 1, -1, musicRate);
        }
    }

    public void stopAllMusic()
    {
        soundPool.stop(backStream);
        soundPool.release();
    }


    public void incrementMusicRate(float rateChange){
        musicRate += rateChange;
        soundPool.setRate(backStream, musicRate);
    }
}
