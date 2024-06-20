package com.example.bidaionak;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Firebase extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "channel_id"; // ID del canal de notificación
    private static final String CHANNEL_NAME = "Nombre del Canal"; // Nombre del canal de notificación

    public Firebase() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.d("Noti","Notificacion recibida");

            // Mostrar la notificación
            mostrarNotificacion(remoteMessage.getData().get("titulo"), remoteMessage.getData().get("mensaje"));
        }
    }



    private void mostrarNotificacion(String titulo, String mensaje) {

        // Crear un canal de notificación si la versión de Android es mayor o igual a Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Construir la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Icono de la notificación
                .setContentTitle(titulo) // Título de la notificación
                .setContentText(mensaje) // Contenido de la notificación
                .setPriority(NotificationCompat.PRIORITY_DEFAULT); // Prioridad de la notificación


        // Mostrar la notificación
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(0, builder.build());
    }
}
