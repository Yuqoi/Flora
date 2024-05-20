package com.example.aplikacja.helpers;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.aplikacja.R;
import com.example.aplikacja.models.Flower;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class FlowerNotification extends Worker {


    private static String CHANNEL_ID = "flower_channel";
    private static final String PREFERENCES_FILE = "com.example.notifications.preferences";
    private static final String NOTIFICATION_DATA_KEY_PREFIX = "notification_data_";
    private static final AtomicInteger notificationCounter = new AtomicInteger(0);

    public FlowerNotification(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Create and display the notification

        byte[] byteArray = getInputData().getByteArray("flowerclass");

        // Deserialize the Serializable object
        Flower flower = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            flower = (Flower) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        createNotificationChannel(flower);
        displayNotification(flower);
        return Result.success();
    }



    private void createNotificationChannel(Flower flower) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = flower.getName()+"_name" ;
            String description = "Notyfikacje";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void displayNotification(Flower flower) {
        int notificationId = notificationCounter.incrementAndGet();


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_flora)
                .setContentTitle("Czas podlać kwiata!")
                .setContentText(flower.getName() + ", oczekuje na nawodnienie")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(notificationId, builder.build());
    }


}
