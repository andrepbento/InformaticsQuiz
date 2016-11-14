package com.example.andre.informaticsquiz;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import activities.GameActivity;

/**
 * Created by andre on 14/11/2016.
 */

public class SoundEffect {
    private SoundPool.Builder soundPoolBuilder;
    private AudioAttributes atributes;
    private AudioAttributes.Builder atributesBuilder;

    private static SoundPool soundPool;
    private static int soundIdArray[];

    public SoundEffect(Game game) {
        initSoundPool();
        soundIdArray = new int[9];
        soundIdArray[0] =  soundPool.load(game, R.raw.right_answer_sound,1);
        soundIdArray[1] =  soundPool.load(game,R.raw.wrong_answer_sound,1);
        soundIdArray[3] =  soundPool.load(game,R.raw.lose_game_sad_trombone,1);
    }

    public SoundEffect(GameActivity gameActivity) {
        initSoundPool();
        soundIdArray = new int[9];
        soundIdArray[0] =  soundPool.load(gameActivity, R.raw.right_answer_sound,1);
        soundIdArray[1] =  soundPool.load(gameActivity,R.raw.wrong_answer_sound,1);
        soundIdArray[3] =  soundPool.load(gameActivity,R.raw.lose_game_sad_trombone,1);
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
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
    }

    public static void playSound(int soundId) {
        switch (soundId) {
            case 0:
                soundPool.play(soundIdArray[0], 1, 1, 0, 0, 1);
                break;
            case 1:
                soundPool.play(soundIdArray[1], 1, 1, 0, 0, 1);
                break;
            case 3:
                soundPool.play(soundIdArray[3], 1, 1, 0, 0, 1);
                break;
        }
    }

    public static void playRightAnswerSound() {
        soundPool.play(soundIdArray[0], 1, 1, 0, 0, 1);
    }

    public static void playWrongAnswerSound() {
        soundPool.play(soundIdArray[1], 1, 1, 0, 0, 1);
    }

    public static void playLoseGameSound() {
        soundPool.play(soundIdArray[3], 1, 1, 0, 0, 1);
    }
}
