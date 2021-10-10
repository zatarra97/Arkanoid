package com.example.android.arkanoid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class Main_Menu extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

    }

    public void startGame(View view) {
        Intent intent1 = new Intent (this, MainActivity.class);
        startActivity(intent1);
    }

    public void openScorePage(View view) {
        Intent intent1 = new Intent (this, ScorePage.class);
        startActivity(intent1);
    }
}