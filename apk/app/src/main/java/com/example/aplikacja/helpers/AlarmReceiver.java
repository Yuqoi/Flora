package com.example.aplikacja.helpers;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.aplikacja.R;
import com.example.aplikacja.activities.WaterNotification;
import com.example.aplikacja.models.Flower;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notificationIntent = new Intent(context, WaterNotification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);


        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, proceed with showing the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarm_channel")
                    .setSmallIcon(R.drawable.flower_icon)
                    .setContentTitle("kwiat")
                    .setContentText("podlij")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(0, builder.build());



            Toast.makeText(context, "dzialaxd" , Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(context, "NIE DZIALA" , Toast.LENGTH_LONG).show();
        }




    }
}
