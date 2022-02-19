package com.example.android.arkanoid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Main_Menu extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private String selectedLevel;
    private SettingsPage settings;
    private String selectedController;
    private String match_id;
    private String device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Main_Menu obj = new Main_Menu();
    }

    @Override
    public void onBackPressed(){
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences sp = this.getSharedPreferences("com.example.android.arkanoid", Context.MODE_PRIVATE);

        // Recupera il controller salvato
        String selectedController = sp.getString("saved_controller", "");
        if (selectedController.isEmpty()) {
            selectedController = "Arrows";
        }
        this.selectedController = selectedController;

        // Recupera deviceID o lo inizializza
        device_id = sp.getString("device_id", "");
        if (device_id.isEmpty()) {
            device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("device_id", device_id);
            editor.apply();
            if (device_id.isEmpty()) {
                Toast.makeText(this, "Impossibile recuperare il deviceID", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void startGame(View view) {
        Intent intent1 = new Intent (this, MainActivity.class);
        intent1.putExtra("saved_controller", this.selectedController);
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
                intent1.putExtra("saved_controller", selectedController);
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

    public void openMultiplayer(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.matchId);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                match_id = input.getText().toString();
                // Cerca dati partita su Firebase Realtime Database
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://arkanoid2022-default-rtdb.europe-west1.firebasedatabase.app");
                DatabaseReference multiplayerRootRef = database.getReference("Multiplayer");
                multiplayerRootRef.child(match_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                            Toast.makeText( Main_Menu.this, "Errore nel matchmaking", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            if (task.getResult().getValue() != null) {
                                HashMap result;
                                try {
                                    result = (HashMap) task.getResult().getValue();
                                    Intent intent1 = new Intent (Main_Menu.this, MainActivity.class);
                                    intent1.putExtra("saved_controller", Main_Menu.this.selectedController);
                                    intent1.putExtra("match_id", Main_Menu.this.match_id);
                                    startActivity(intent1);
                                } catch (RuntimeException e) {
                                    Toast.makeText( Main_Menu.this, "ID partita non esistente", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("firebase", "Error getting data", task.getException());
                                Toast.makeText( Main_Menu.this, "ID partita non esistente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
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