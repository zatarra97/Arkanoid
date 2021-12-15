package com.example.android.arkanoid;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.provider.MediaStore;

public class SoundManager {

    private static SoundPool soundPoolHit;
    private static SoundPool soundPoolBomb;
    private static SoundPool soundPoolPUp;
    private static int hit_mattoncino;
    private static int bomb_mattoncino;
    private static int powerUp_mattoncino;

    //Carichiamo i file audio
    public static void init(Context context){

        soundPoolHit=new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
        hit_mattoncino = soundPoolHit.load(context,R.raw.hit_mattoncino, 0);

        soundPoolBomb = new SoundPool(1,AudioManager.STREAM_MUSIC,100);
        bomb_mattoncino = soundPoolBomb.load(context ,R.raw.mattoncino_bomba, 100);

        soundPoolPUp = new SoundPool(1, AudioManager.STREAM_MUSIC,100);
        powerUp_mattoncino = soundPoolPUp.load(context, R.raw.power_up,100);

    }

    //Eseguiamo i file audio
    public static void playHit(){
        soundPoolHit.play(hit_mattoncino, 1f, 1f, 1, 0, 1f);
    }

    public static void playBomb(){
        soundPoolBomb.play(bomb_mattoncino, 1f, 1f, 1, 0, 1f);
    }

    public static void playPup(){
        soundPoolPUp.play(powerUp_mattoncino, 1f, 1f, 1, 0, 1f);
    }
}



