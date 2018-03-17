package mobiledev.unb.ca.bappit;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.util.Log;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class Sounds {
    private SoundPool soundPool;
    private int[] soundFXIds;
    private int[] announcerIds;
    private Context context;


    private int backMusic;
    private int backStream;
    private  float musicRate;

    boolean loaded = false, musicPlaying = false;
    float actVolume, maxVolume, volume;
    int counter;


    public Sounds(AudioManager audioManager, Context context) {
        this.context = context;

        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;
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

        soundFXIds = new int[3];
        soundFXIds[0] = soundPool.load(context, R.raw.fling, 1);
        soundFXIds[1] = soundPool.load(context, R.raw.tap, 1);
        soundFXIds[2] = soundPool.load(context, R.raw.shake, 1);

        announcerIds = new int[3];
        announcerIds[0] = soundPool.load(context, R.raw.flingit_voice, 1);
        announcerIds[1] = soundPool.load(context, R.raw.tapit_voice, 1);
        announcerIds[2] = soundPool.load(context, R.raw.shakeit_voice, 1);

    }

    public void playSound(int sID, boolean isSoundEffect) {
        // Is the sound loaded does it already play?
        if (loaded) {
            if(isSoundEffect)
                soundPool.play(soundFXIds[sID], volume, volume, 1, 0, 1f);
            else
                soundPool.play(announcerIds[sID], volume, volume, 1, 0, 1f);
        }
    }

    public void startBackgroundMusic()
    {
        if(loaded) {
            musicPlaying = true;
            backStream = soundPool.play(backMusic, volume, volume, 1, -1, musicRate);
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
        Log.i(TAG, "this is the rate: " + musicRate);
    }

}
