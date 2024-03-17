package com.example.bidaionak;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class MenuHorizontal extends Fragment {

    private Button buttonDestinos;
    private Button buttonIdioma;


    public MenuHorizontal() {
        // Constructor vacío requerido por Fragment.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño asociado al fragmento.
        View rootView = inflater.inflate(R.layout.menu_horizontal, container, false);

        // Inicializar los botones
        buttonDestinos = rootView.findViewById(R.id.buttonDestinos);


        // Configurar el click listener para el botón Destinos
        buttonDestinos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar ListsDestinesActivity
                Intent intent = new Intent(getActivity(), ListsDestinos.class);
                startActivity(intent);
            }
        });




        return rootView;
    }
}
