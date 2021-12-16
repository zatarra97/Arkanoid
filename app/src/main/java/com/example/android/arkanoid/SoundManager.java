package com.example.android.arkanoid;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.provider.MediaStore;

public class SoundManager {

    private static final int NR_OF_SIMULTANEOUS_SOUND = 3;
    //private static SoundPool soundPoolHit;
    //private static SoundPool soundPoolBomb;
    //private static SoundPool soundPoolPUp;
    private static SoundPool soundBrick;
    private static int hit_mattoncino;
    private static int bomb_mattoncino;
    private static int powerUp_mattoncino;

    //Carichiamo i file audio
    public static void init(Context context){

        //oggetto SoundPool
        soundBrick = new SoundPool(NR_OF_SIMULTANEOUS_SOUND, AudioManager.STREAM_MUSIC, 100);

        hit_mattoncino = soundBrick.load(context,R.raw.hit_mattoncino, 1);

        //soundPoolBomb = new SoundPool(1,AudioManager.STREAM_MUSIC,100);
        bomb_mattoncino = soundBrick.load(context ,R.raw.mattoncino_bomba, 1);

        //soundPoolPUp = new SoundPool(1, AudioManager.STREAM_MUSIC,100);
        powerUp_mattoncino = soundBrick.load(context, R.raw.power_up,1);

    }

    //Eseguiamo i file audio
    public static void playHit(){
        soundBrick.play(hit_mattoncino, 1f, 1f, 1, 0, 1f);
    }

    public static void playBomb(){
        soundBrick.play(bomb_mattoncino, 1f, 1f, 1, 0, 1f);
    }

    public static void playPup(){
        soundBrick.play(powerUp_mattoncino, 1f, 1f, 1, 0, 1f);
    }
}



