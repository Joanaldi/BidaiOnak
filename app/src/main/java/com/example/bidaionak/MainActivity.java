package com.example.bidaionak;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        // Configurar el click listener para cambiar a menu.xml al tocar la pantalla
        View rootView = findViewById(android.R.id.content);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a menu.xml
                setContentView(R.layout.menu);

                // Verificar la orientación del dispositivo y cargar el fragmento correspondiente
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    loadVerticalFragment();
                } else {
                    loadHorizontalFragment();
                }
            }
        });
    }

    private void loadVerticalFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MenuVertical())
                .commit();
    }

    private void loadHorizontalFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MenuHorizontal())
                .commit();
    }
}
