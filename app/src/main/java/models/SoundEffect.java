package models;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.example.andre.informaticsquiz.R;

import activities.GameActivity;

/**
 * Created by andre
 */

public class SoundEffect {

    private SoundPool.Builder soundPoolBuilder;
    private AudioAttributes atributes;
    private AudioAttributes.Builder atributesBuilder;

    private static SoundPool soundPool;
    private static int soundIdArray[];

    public SoundEffect(GameActivity gameActivity) {
        initSoundPool();
        soundIdArray = new int[9];
        soundIdArray[0] = soundPool.load(gameActivity, R.raw.right_answer_sound,1);
        soundIdArray[1] = soundPool.load(gameActivity,R.raw.wrong_answer_sound,1);
        soundIdArray[2] = soundPool.load(gameActivity,R.raw.win_game_audience_aplause,1);
        soundIdArray[3] = soundPool.load(gameActivity,R.raw.lose_game_sad_trombone,1);
        soundIdArray[4] = soundPool.load(gameActivity,R.raw.tick_sound,1);
    }

    private void initSoundPool() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            atributesBuilder = new AudioAttributes.Builder();
            atributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
            atributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
            atributes = atributesBuilder.build();

            soundPoolBuilder = new SoundPool.Builder();
            soundPoolBuilder.setAudioAttributes(atributes);
            soundPool = soundPoolBuilder.build();
        } else
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC,0);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                boolean loaded = true;
            }
        });
    }

    public static void playRightAnswerSound() {
        soundPool.play(soundIdArray[0], 1, 1, 0, 0, 1);
    }

    public static void playWrongAnswerSound() {
        soundPool.play(soundIdArray[1], 1, 1, 0, 0, 1);
    }

    public static void playWinGameSound() { soundPool.play(soundIdArray[2], 1, 1, 0, 0, 1); }

    public static void playLoseGameSound() {
        soundPool.play(soundIdArray[3], 1, 1, 0, 0, 1);
    }

    public static void playTickSound() { soundPool.play(soundIdArray[4], 1, 1, 0, 0, 1); }

    public static void stopAllSounds() {
        for(int i : soundIdArray)
            soundPool.stop(i);
    }
}
