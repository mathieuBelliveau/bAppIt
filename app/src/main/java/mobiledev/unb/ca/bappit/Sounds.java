package mobiledev.unb.ca.bappit;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.util.Log;
import java.util.concurrent.TimeUnit;

public class Sounds {
    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;
    private int[] soundID;
    private Context context;

    private int backMusic;
    private int backStream;

    boolean plays = false, loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;


    public Sounds(AudioManager audioManager, Context context) {
        this.context = context;
        soundID = new int[3];

        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        // Load the sounds
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        soundPool = new SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(audioAttributes)
            .build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        soundID[0] = soundPool.load(context, R.raw.swipe, 1);
        soundID[1] = soundPool.load(context, R.raw.tap, 1);
        soundID[2] = soundPool.load(context, R.raw.shake, 1);
        backMusic = soundPool.load(context,R.raw.background, 1);
    }

    public void playSound(int sID) {
        // Is the sound loaded does it already play?
        if (loaded) {
            soundPool.play(soundID[sID], volume, volume, 1, 0, 1f);
        }
    }

    public void startMusic()
    {
        Log.i("debug", "starting music");

        StartMusicTask task = new StartMusicTask();
        task.execute();

        /*//Enable looping background music
        mediaPlayer = MediaPlayer.create(context, R.raw.background);
        if(mediaPlayer != null)
        {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }*/

    }

    public void stopMusic()
    {
        soundPool.stop(backStream);
        /*mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;*/
    }

    /*public void stopSound(View v, int sID) {
        if (plays) {
            soundPool.stop(soundID[sID]);
            soundID = soundPool.load(this, R.raw.beep, counter);
            Toast.makeText(context, "Stop sound", Toast.LENGTH_SHORT).show();
            plays = false;
        }
    }*/

    private class StartMusicTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("debug", "starting thread");
            if (loaded) {
                backStream = soundPool.play(backMusic, volume, volume, 1, -1, 1f);
                return true;
            } else {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean startedMusic) {
            super.onPostExecute(startedMusic);
            if(!startedMusic) {
                startMusic();
            }
        }
    }
}
