package com.example.bidaionak;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

        //Obtener el nombre y la foto
        Intent intent = getIntent();
        String name = intent.getStringExtra("usuario");
        cargarUsuario(name);

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewDestinos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DestinoAdap(this, destinosList);
        recyclerView.setAdapter(adapter);

        // Configurar el botón para añadir
        Button buttonAdd = findViewById(R.id.buttonAnadir);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoAñadir();
            }
        });

        cargarDestinos();
    }

    private void cargarUsuario(String name){
        TextView textViewUser = findViewById(R.id.usuario);
        textViewUser.setText(name);
        // URL del php para cargar la imagen
        String url = "http://34.44.136.78:81/get_image.php";
        String parametros = "usuario=" + Uri.encode(name);
        Log.d("Datos enviados", parametros);

        // Ejecutar la tarea para recuperar la imagen
        new ObtenerImagenTask().execute(url, parametros);
    }

    private void mostrarImagen(Bitmap imagen) {
        ImageView imageView = findViewById(R.id.avatarUsuario);
        imageView.setImageBitmap(imagen);
    }

    private class ObtenerImagenTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            String urlString = params[0];
            String parametros = params[1];
            Bitmap imagen = null;

            try {
                Log.e("prueba", "entra en el try");
                URL url = new URL(urlString);
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                conexion.setRequestMethod("POST");
                conexion.setDoOutput(true);

                // Escribir los parámetros en la solicitud
                OutputStream outputStream = conexion.getOutputStream();
                outputStream.write(parametros.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();


                // Establecer conexión HTTP y recibir la respuesta del servidor
                int responseCode = conexion.getResponseCode();
                Log.e("Respuesta", String.valueOf(responseCode));

                // Verificar si la conexión fue exitosa
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = conexion.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    // Construir el StringBuilder
                    while ((line = bufferedReader.readLine()) != null) {
                        Log.d("Buff",line);
                        stringBuilder.append(line);
                    }

                    bufferedReader.close();
                    inputStream.close();

                    // Obtener el string de la respuesta del servidor
                    String base64String = stringBuilder.toString();
                    Log.d("Data","Image: "+base64String);

                    byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
                    imagen = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);


                } else {
                    Log.d("Error","Imagen no recibida correctamente");
                }

                conexion.disconnect();

            } catch (Exception e) {
                Log.e("Error", "Error al recuperar la imagen: " + e.getMessage());
            }

            return imagen;
        }

        @Override
        protected void onPostExecute(Bitmap imagen) {
            if (imagen != null) {
                mostrarImagen(imagen);
            } else {
                Log.e("Error", "No se pudo obtener la imagen o está vacia");
            }
        }
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
            Log.e("1","prueba");

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
