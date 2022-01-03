package com.example.android.arkanoid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ScorePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);

        SharedPreferences sp = this.getSharedPreferences("com.example.android.arkanoid", Context.MODE_PRIVATE);

        //Leggere Shared preferences
        final String primo = sp.getString("primo", "0");   //Se non dovesse esistere utilizza il secondo argomeno (stringa vuota)
        final String secondo = sp.getString("secondo", "0");
        final String terzo = sp.getString("terzo", "0");

        TextView tw1 = (TextView)findViewById(R.id.textViewScore1);
        TextView tw2 = (TextView)findViewById(R.id.textViewScore2);
        TextView tw3 = (TextView)findViewById(R.id.textViewScore3);

        //Popola le label con i dati corretti
        tw1.setText(primo);
        tw2.setText(secondo);
        tw3.setText(terzo);

        Button buttonMatchHistory = (Button)findViewById(R.id.buttonMatchHistory);
        buttonMatchHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        FloatingActionButton shareScoreButton = (FloatingActionButton) findViewById(R.id.shareScoreButton); //FIND THE BUTTON
        shareScoreButton.setOnClickListener(new View.OnClickListener() { //SET ON CLICK LISTENER
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                //shareIntent.setPackage("com.whatsapp");
                String topScores = "1. ".concat(primo).concat("\n2. ").concat(secondo).concat("\n3. ").concat(terzo);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Check out my last Arkanoid top scores:\n" + topScores);
                try {
                    //startActivity(shareIntent);
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.bestScores)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText( ScorePage.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //metodo per governare il tasto back del sistema operativo
    @Override
    public void onBackPressed(){
        Intent toMainMenu = new Intent(this, Main_Menu.class);
        startActivity(toMainMenu);
    }

    public void openDialog(){
        ArrayList<String> matches = readMatchHistoryFile();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(matches.toArray(new String[matches.size()]), -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int selectedItem) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public ArrayList<String> readMatchHistoryFile(){
        ArrayList<String> results = new ArrayList<String>();
        String state = Environment.getExternalStorageState();

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            // If it isn't mounted - we can't read from it.
            return results;
        }

        try {
            Scanner scanner = new Scanner(new File(this.getExternalFilesDir(null), "partite.txt")); //.useDelimiter("\\Z");
            String s = "";
            while (scanner.hasNext()) {
                s = scanner.nextLine().toString();
                results.add(s.split(";")[0]);
            }
            scanner.close();
        }
        catch (IOException e) {
            // You'll need to add proper error handling here
            Log.i("FILE", "error reading file:" + e.toString());
        }
        return results;
    }
}