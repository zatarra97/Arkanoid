package com.example.android.arkanoid;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ScorePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);

        SharedPreferences sp = this.getSharedPreferences("com.example.android.arkanoid", Context.MODE_PRIVATE);

        //Salvare Shared preferences
        //sp.edit().putString("primo", "10000").apply();
        //sp.edit().putString("secondo", "9000").apply();
        //sp.edit().putString("terzo", "8000").apply();

        //Leggere Shared preferences
        String primo = sp.getString("primo", "0");   //Se non dovesse esistere utilizza il secondo argomeno (stringa vuota)
        String secondo = sp.getString("secondo", "0");
        String terzo = sp.getString("terzo", "0");

        TextView tw1 = (TextView)findViewById(R.id.textViewScore1);
        TextView tw2 = (TextView)findViewById(R.id.textViewScore2);
        TextView tw3 = (TextView)findViewById(R.id.textViewScore3);

        //Popola le label con i dati corretti
        tw1.setText(primo);
        tw2.setText(secondo);
        tw3.setText(terzo);
    }
}