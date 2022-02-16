package com.example.android.arkanoid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.View;
import android.widget.Toast;
import java.util.HashSet;

public class SettingsPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Switch mySwitch = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        Spinner dropdown = findViewById(R.id.spinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"Arrows", "Accelerometer"};
        //create an adapter to describe how the items are displayed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

        //per far comparire la freccia in alto a sinistra
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final SharedPreferences sp = this.getSharedPreferences("com.example.android.arkanoid", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected controller: " + item, Toast.LENGTH_LONG).show();
        try {
            editor.putString("saved_controller", item);
            editor.apply();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText( SettingsPage.this, "Errore salvataggio impostazioni", Toast.LENGTH_SHORT).show();
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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

    //metodo per governare il tasto back del sistema operativo
    @Override
    public void onBackPressed(){
        Intent toMainMenu = new Intent(this, Main_Menu.class);
        startActivity(toMainMenu);
        finish();
    }


}