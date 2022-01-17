package com.example.android.arkanoid;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ScorePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);

        //per far comparire la freccia in alto a sinistra
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //per la memorizzazione locale
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
        finish();
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


}