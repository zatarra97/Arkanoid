package com.example.android.arkanoid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class SettingsPage extends AppCompatActivity {

    private Button lbtn;
    private TextView language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocate();
        setContentView(R.layout.settings_page_layout);

        //per far comparire la freccia in alto a sinistra
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //associazione tasto al tasto del layout corrispondente
        lbtn = findViewById(R.id.btnLang);

        //metodo che verrà avviato al click del pulsante
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLang();
            }
        });

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

        SharedPreferences.Editor editor= getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    //funzione che esegue l'impostazione della lingua
    private void loadLocate(){
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
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