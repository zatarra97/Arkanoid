package com.example.android.arkanoid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Locale;

public class SettingsPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SwitchCompat mSwitch;
    private Button lbtn;
    private TextView language;
    protected static boolean flag_sounds = false; //flag per attivare/disattivare i suoni del gioco

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocate();
        setContentView(R.layout.settings_page_layout);

        // instanza delle SharedPreferences per salvare le impostazioni
        SharedPreferences sp = getSharedPreferences("com.example.android.arkanoid", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //per far comparire la freccia in alto a sinistra
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner dropdown = findViewById(R.id.spinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"Arrows", "Accelerometer"};
        //create an adapter to describe how the items are displayed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

        // inserimento nickname utente
        final EditText input = findViewById(R.id.editTextTextPersonName);
        // recupera il nickname salvato
        String saved_nickname = sp.getString("nickname", "");
        input.setText(saved_nickname);
        // registra i cambiamenti al nickname e li salva
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("nickname", s.toString());
                editor.apply();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        input.addTextChangedListener(textWatcher);
        // TODO aggiungere massimo caratteri, salvare punteggio su db con nickname in GAME


        //associazione switchcompact
        mSwitch = (SwitchCompat)findViewById(R.id.switch_music);
        //associazione tasto al tasto del layout corrispondente
        lbtn = (Button) findViewById(R.id.btnLang);


        //metodo che verrà avviato al click del pulsante per la lingua
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLang();
            }
        });

        //Setto lo switchcompact per il suono, salvo nella SharedPReferences
        mSwitch.setChecked(sp.getBoolean("switch_sounds", false));

        //setto l'ascoltatore dello switchcompact
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("switch_sounds", true);
                    editor.apply();

                } else if(!mSwitch.isChecked()){
                    editor.putBoolean("switch_sounds", false);
                    editor.apply();

                }
                editor.commit();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences sp = this.getSharedPreferences("com.example.android.arkanoid", Context.MODE_PRIVATE);
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

    //viene chiamato quando si preme il tasto indietro in alto a sinistra
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

    //funzione che imposta nelle preferenze dell'app la lingua selezionata
    private void setLocate(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(
                config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor= getSharedPreferences("com.example.android.arkanoid",Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    //funzione che esegue l'impostazione della lingua
    protected void loadLocate(){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.arkanoid",Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("My_Lang", "");
        if(language!=""){
            setLocate(language);
        }
    }

    //funzione che permette di aprire un DIalogAlert con l'elenco delle lingue selezionabili
    private void showChangeLang(){
        String listItems[]={"English","Italiano"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle(R.string.selectLangTitle);

        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        setLocate("en");
                        recreate();
                        break;
                    case 1:
                        setLocate("it");
                        recreate();
                        break;
                }
                dialog.dismiss();
            }
        });
        DialogInterface mDialog = mBuilder.create();
        mBuilder.show();

    }

}