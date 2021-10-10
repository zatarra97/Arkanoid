package com.example.android.arkanoid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExitActivity extends AppCompatActivity {

    Button btnSi;
    Button btnNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);

        btnNo = findViewById(R.id.btnNo);
        btnSi = findViewById(R.id.btnSi);

    }

    //metodo per uscire dall'applicazione
    public void onClickExit(View view) {

        if(view.getId() == R.id.btnSi){
            moveTaskToBack(true);
            finish();

        }else if(view.getId() == R.id.btnNo){
            Intent toMainMenu = new Intent(this,Main_Menu.class);
            startActivity(toMainMenu);
        }

    }
}