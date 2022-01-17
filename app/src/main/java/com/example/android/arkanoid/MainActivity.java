package com.example.android.arkanoid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // imposta l'orientamento dello schermo
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String custom_level = "";
        if (getIntent().getExtras() != null) {
            custom_level = getIntent().getExtras().getString("custom_level");
        }

        // crea un nuovo gioco
        game = new Game(this, 3, 0, custom_level);
        setContentView(game);

        // vytvori handler a thread
        //crea un gestore di thread
        VytvorHandler();
        myThread = new UpdateThread(updateHandler);
        myThread.start();
    }

    //metodo per governare il tasto back del sistema operativo
    @Override
    public void onBackPressed(){
        Intent toMainMenu = new Intent(this, Main_Menu.class);
        startActivity(toMainMenu);
        finish();
    }


    private void VytvorHandler() {
        updateHandler = new Handler() {
            public void handleMessage(Message msg) {
                game.invalidate();
                game.update();
                super.handleMessage(msg);
            }
        };
    }
    //zastavSnimanie = smetti si sparare
    protected void onPause() {
        super.onPause();
        game.smettiDiSparare();
    }

    //spusti Snimanie = Esegui scansione
    protected void onResume() {
        super.onResume();
        game.iniziaSparare();
    }

}
