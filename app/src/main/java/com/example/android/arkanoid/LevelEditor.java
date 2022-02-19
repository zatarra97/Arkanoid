package com.example.android.arkanoid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;

public class LevelEditor extends AppCompatActivity {

    final CharSequence[] items = {"Disabilitato", "Rosso", "Bomba", "Incrementa Vita"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //per far comparire la freccia in alto a sinistra
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SharedPreferences sp = this.getSharedPreferences("com.example.android.arkanoid", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_level_editor);

        final TextView speedLevel = findViewById(R.id.selectedSpeed);
        SeekBar speedSelection = findViewById(R.id.seekBar);
        speedSelection.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Integer value = (Integer)i;
                speedLevel.setText(value.toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        FloatingActionButton saveLevelButton = (FloatingActionButton) findViewById(R.id.saveLevel); //FIND THE BUTTON
        saveLevelButton.setOnClickListener(new View.OnClickListener() { //SET ON CLICK LISTENER
            @Override
            public void onClick(View v) {
                try {
                    String serializedLevel = serializeLevel(v);
                    HashSet<String> levelsStringSet = new HashSet<String>();
                    levelsStringSet.add(serializedLevel);
                    sp.edit().putStringSet("custom_levels", levelsStringSet).apply();
                    Toast.makeText( LevelEditor.this, "Livello salvato", Toast.LENGTH_SHORT).show();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText( LevelEditor.this, "Errore salvataggio", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String serializeLevel(View v) {
        StringBuilder sb = new StringBuilder();
        TextView levelName = findViewById(R.id.editTextLevelName);
        sb.append(levelName.getText()).append(":");
        View tableLayout = findViewById(R.id.tableLayout);
        ArrayList<View> buttons = new ArrayList<>();
        for(int index = 0; index < ((ViewGroup) tableLayout).getChildCount(); index++) {
            View tableRow = ((ViewGroup) tableLayout).getChildAt(index);
            for(int j = 0; j < ((ViewGroup) tableRow).getChildCount(); j++) {
                View b = ((ViewGroup) tableRow).getChildAt(j);
                if (b instanceof ImageButton) {
                    buttons.add(b);
                    String s = (String)b.getTag();
                    sb.append(s).append(",");
                }
            }
        }
        TextView ballSpeed = findViewById(R.id.selectedSpeed);
        sb.append(ballSpeed.getText());
        return sb.toString();
    }

    public void showCustomDialog(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int selectedItem) {
                setButtonDrawable(view.getId(), selectedItem);
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setButtonDrawable(Integer buttonId, Integer selectedType) {
        switch(selectedType) {
            case 0:
                changeImageButtonDrawable(buttonId, R.drawable.brick_transparent, "trasparent");
                break;
            case 1:
                changeImageButtonDrawable(buttonId, R.drawable.brick_red, "red");
                break;
            case 2:
                changeImageButtonDrawable(buttonId, R.drawable.brick_bomb, "bomb");
                break;
            case 3:
                changeImageButtonDrawable(buttonId, R.drawable.brick_lifeup, "lifeup");
                break;
        }
    }

    private void changeImageButtonDrawable(Integer buttonId, Integer drawableId, String tag) {
        Drawable d = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), drawableId));
        ImageButton imageButton = (ImageButton) findViewById(buttonId);
        imageButton.setImageDrawable(d);
        imageButton.setTag(tag);
    }

    //viene chiamato quando si preme il tasto indietro in altro a sinistra
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent toMainMenu = new Intent(this, Main_Menu.class);
                startActivity(toMainMenu);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    //tornare indietro al menu principale tramite il tasto indietro del sistema
    @Override
    public void onBackPressed(){
        Intent toMainMenu = new Intent(this, Main_Menu.class);
        startActivity(toMainMenu);
        finish();
    }
}