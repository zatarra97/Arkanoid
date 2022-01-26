package com.example.android.arkanoid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import androidx.navigation.ui.AppBarConfiguration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Main_Menu extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private String selectedLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Main_Menu obj = new Main_Menu();
    }

    @Override
    public void onBackPressed(){
    }



    public void startGame(View view) {
        Intent intent1 = new Intent (this, MainActivity.class);
        startActivity(intent1);
    }

    public void loadCustomLevel(View view) {
        final SharedPreferences sp = this.getSharedPreferences("com.example.android.arkanoid", Context.MODE_PRIVATE);
        Set<String> levelsStringSet = new HashSet<>();
        levelsStringSet = sp.getStringSet("custom_levels", levelsStringSet);

        final HashMap<String,String> livelli = new HashMap<>();
        for (String s : levelsStringSet) {
            String[] splitted = s.split(":");
            livelli.put(splitted[0], splitted[1]);
        }
        CharSequence[] levels_names = livelli.keySet().toArray(new CharSequence[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setSingleChoiceItems(levels_names, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int selectedItem) {
                String sel = (String) livelli.keySet().toArray()[selectedItem];
                selectedLevel = livelli.get(sel);
                Intent intent1 = new Intent (Main_Menu.this, MainActivity.class);
                intent1.putExtra("custom_level", selectedLevel);
                dialog.dismiss();
                Main_Menu.this.startActivity(intent1);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void openScorePage(View view) {
        Intent intent1 = new Intent (this, ScorePage.class);
        startActivity(intent1);
    }

    public void openLevelEditor(View view){
        Intent intent1 = new Intent(this, LevelEditor.class);
        startActivity(intent1);
    }

    public void quitGame(View view){
        Intent toExit = new Intent(this, ExitActivity.class);
        startActivity(toExit);
    }

    public void openSettings(View view) {
        Intent intent1 = new Intent(this, SettingsPage.class);
        startActivity(intent1);
    }
}