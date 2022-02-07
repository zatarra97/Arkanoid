package com.example.android.arkanoid;

import android.content.Intent;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsPage extends AppCompatActivity {
    Switch mySwitch = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);


    }

    //metodo per governare il tasto back del sistema operativo
    @Override
    public void onBackPressed(){
        Intent toMainMenu = new Intent(this, Main_Menu.class);
        startActivity(toMainMenu);
        finish();
    }


}