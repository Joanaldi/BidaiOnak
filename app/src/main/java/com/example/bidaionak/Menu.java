package com.example.bidaionak;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class Menu extends AppCompatActivity {

    private Button buttonDestinos;
    private Button buttonES;
    private Button buttonEU;
    private Button buttonEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        // Inicializar los botones
        buttonDestinos = findViewById(R.id.buttonDestinos);
        buttonES = findViewById(R.id.buttonES);
        buttonEU = findViewById(R.id.buttonEU);
        buttonEN = findViewById(R.id.buttonEN);

        // Configurar el click listener para el botón Destinos
        buttonDestinos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar ListsDestinos
                Intent intent = new Intent(Menu.this, ListsDestinos.class);
                startActivity(intent);
            }
        });

        // Configurar el click listener para el botón ES
        buttonES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar el idioma a español
                setLocale("es");
            }
        });

        // Configurar el click listener para el botón EU
        buttonEU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar el idioma a euskera
                setLocale("eu");
            }
        });

        // Configurar el click listener para el botón EN
        buttonEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar el idioma a inglés
                setLocale("en");
            }
        });

        // Cargar el fragmento correspondiente según la orientación del dispositivo
        loadMenuFragment();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Cuando cambia la orientación, volver a cargar el fragmento correspondiente
        loadMenuFragment();
    }

    private void loadMenuFragment() {
        Fragment fragment;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragment = new MenuVertical();
        } else {
            fragment = new MenuHorizontal();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        // Forzar la actualización de la interfaz de usuario
        findViewById(android.R.id.content).invalidate();
    }

    // Método para cambiar el idioma de la aplicación
    private void setLocale(String languageCode) {
        // Actualizar la configuración de idioma
        Locale nuevaloc = new Locale(languageCode);
        Locale.setDefault(nuevaloc);
        Configuration configuration =
                getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context =
                getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());


        // Recargar la actividad para aplicar los cambios de idioma
        finish();
        startActivity(getIntent());
    }
}
