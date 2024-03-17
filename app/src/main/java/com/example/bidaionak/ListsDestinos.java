package com.example.bidaionak;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListsDestinos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DestinoAdap adapter;
    private ArrayList<Destino> destinosList;
    private static final String CHANNEL_ID = "DestinoChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_destinos);

        destinosList = new ArrayList<>();

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewDestinos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DestinoAdap(this, destinosList);
        recyclerView.setAdapter(adapter);

        // Configurar el botón de añadir
        Button buttonAdd = findViewById(R.id.buttonAnadir);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoAñadir();
            }
        });

        // Cargar destinos existentes al iniciar la actividad
        cargarDestinos();
    }

    private void mostrarDialogoAñadir() {
        // Inflar el diseño del diálogo personalizado
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.diag_anadir_destino, null);

        // Obtener referencias a los elementos del diálogo
        final EditText editTextDestino = dialogView.findViewById(R.id.editTextDestino);
        final EditText editTextCoste = dialogView.findViewById(R.id.editTextCoste);
        final EditText editTextTransporte = dialogView.findViewById(R.id.editTextTransporte);

        // Crear el diálogo de alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Añadir Destino");
        builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener los datos ingresados por el usuario
                String nuevoDestinoNombre = editTextDestino.getText().toString();
                String nuevoDestinoCoste = editTextCoste.getText().toString();
                String nuevoDestinoTransporte = editTextTransporte.getText().toString();

                long nuevoDestinoId = System.currentTimeMillis();

                // Crear un nuevo objeto Destino con los datos ingresados
                Destino nuevoDestino = new Destino(nuevoDestinoId, nuevoDestinoNombre, nuevoDestinoCoste, nuevoDestinoTransporte);

                // Añadir el nuevo destino a la lista
                destinosList.add(nuevoDestino);

                // Notificar al adaptador del cambio en los datos
                adapter.notifyDataSetChanged();

                // Agregar el nuevo destino a la base de datos
                Bd bd = new Bd(ListsDestinos.this);
                long id = bd.nuevoDestino(nuevoDestinoNombre, String.valueOf(nuevoDestinoCoste), nuevoDestinoTransporte);

                showNotification();
            }
        });
        builder.setNegativeButton("Cancelar", null);

        // Mostrar el diálogo
        builder.create().show();
    }

    private void showNotification() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 11);
        } else {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icono)
                    .setContentTitle("Nuevo Destino Agregado")
                    .setContentText("Se ha agregado un nuevo destino a la lista.")
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Destinos", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, builder.build());
        }
    }

    private void cargarDestinos() {
        Bd bd = new Bd(this);
        destinosList.clear(); // Limpiar la lista actual
        destinosList.addAll(bd.getAllTasks()); // Agregar todos los destinos de la base de datos
        adapter.notifyDataSetChanged(); // Notificar al adaptador de la actualización
    }
}
