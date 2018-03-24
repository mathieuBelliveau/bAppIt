package mobiledev.unb.ca.bappit;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
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
                loaded = true;
            }
        });

        backMusic = soundPool.load(context,R.raw.background_music_modified, 1);

        soundFXIds = new int[4];
        soundFXIds[0] = soundPool.load(context, R.raw.fling, 1);
        soundFXIds[1] = soundPool.load(context, R.raw.tap, 1);
        soundFXIds[2] = soundPool.load(context, R.raw.shake, 1);
        //TEMP
        soundFXIds[3] = soundPool.load(context, R.raw.shake, 1);

        announcerIds = new int[4];
        announcerIds[0] = soundPool.load(context, R.raw.flingit_voice, 1);
        announcerIds[1] = soundPool.load(context, R.raw.tapit_voice, 1);
        announcerIds[2] = soundPool.load(context, R.raw.shakeit_voice, 1);
        //TEMP
        announcerIds[3] = soundPool.load(context, R.raw.shakeit_voice, 1);

    }

    public void playSound(int sID, boolean isSoundEffect) {
        if (loaded) {
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
