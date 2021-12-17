package com.example.android.arkanoid;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.provider.MediaStore;

public class SoundManager {

    //costante per numero di suoni simultanei
    private static final int NR_OF_SIMULTANEOUS_SOUND = 3;
    //variabili membro di SoudPool
    private static SoundPool soundBrick;
    private static int hit_mattoncino;
    private static int bomb_mattoncino;
    private static int powerUp_mattoncino;

    //Carichiamo i file audio
    public static void init(Context context){

        //Gestione costruttore SoundPool che è deprecato dalla versione API 21
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundBrick = new SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(NR_OF_SIMULTANEOUS_SOUND).build();
        }else{
            //oggetto SoundPool per altre versioni
            soundBrick = new SoundPool(NR_OF_SIMULTANEOUS_SOUND, AudioManager.STREAM_MUSIC, 0);
        }

        hit_mattoncino = soundBrick.load(context,R.raw.hit_mattoncino, 1);
        bomb_mattoncino = soundBrick.load(context ,R.raw.mattoncino_bomba, 1);
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



