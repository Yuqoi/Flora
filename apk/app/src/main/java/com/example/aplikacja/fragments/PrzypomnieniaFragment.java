package com.example.aplikacja.fragments;


import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Parcel;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.aplikacja.R;
import com.example.aplikacja.helpers.FlowerNotification;
import com.example.aplikacja.helpers.FlowerViewModel;
import com.example.aplikacja.models.Flower;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class PrzypomnieniaFragment extends Fragment {


    FlowerViewModel flowerViewModel;

    TextView nazwaKwiata;

    ConstraintLayout ostatniaData;
    TextView data;

    AppCompatButton godzinaUstaw;
    TextView godzina;

    TextView oszacowanyCzas;

    AppCompatButton przyciskPowiadom;

    AppCompatButton usunAlarm;

    private static final String CHANNEL_ID = "flower_channel";
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;
    private static final String PREFERENCES_FILE = "com.example.notifications.preferences";
    private static final String NOTIFICATION_ID_KEY = "notification_id_key";
    private static AtomicInteger notificationCounter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_przypomnienia, container, false);

        flowerViewModel = new ViewModelProvider(requireActivity()).get(FlowerViewModel.class);
        Flower flower = flowerViewModel.getFlower();


        nazwaKwiata = view.findViewById(R.id.przypomnienia_name);
        ostatniaData = view.findViewById(R.id.przypomnienia_data);
        data = view.findViewById(R.id.przypomnienia_ostatnia_data);
        godzinaUstaw = view.findViewById(R.id.przypomnienia_ustaw_godzine);
        godzina = view.findViewById(R.id.przypomnienia_godzina);
        oszacowanyCzas = view.findViewById(R.id.przypomnenia_oszacowany_czas);
        przyciskPowiadom = view.findViewById(R.id.przypomnienia_przycisk);
        usunAlarm = view.findViewById(R.id.usun_alarm);

        nazwaKwiata.setText(flower.getName());

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        int notificationId = sharedPreferences.getInt(NOTIFICATION_ID_KEY, 0);
        notificationCounter = new AtomicInteger(notificationId);

        ostatniaData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog(view);
            }
        });

        godzinaUstaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog(view);
            }
        });

        przyciskPowiadom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] timeParts = godzina.getText().toString().split(":");
                String[] dataParts = godzina.getText().toString().split("/");

                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{android.Manifest.permission.POST_NOTIFICATIONS},NOTIFICATION_PERMISSION_REQUEST_CODE);
                            Toast.makeText(getContext(), "Włącz powiadomienia", Toast.LENGTH_SHORT).show();
                }else{

                    scheduleNotification(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]), flower);
                    Toast.makeText(getContext(), "Pomyślnie ustawiono powiadomienie", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return view;
    }


    private void scheduleNotification(int hourOfDay, int minute, Flower flower) {
        // Set the time for the notification
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();
        if (delay < 0) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            delay = calendar.getTimeInMillis() - System.currentTimeMillis();
        }


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(flower);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        Data data = new Data.Builder()
                .putByteArray("flowerclass", byteArray)
                .build();

        WorkRequest notificationWork = new OneTimeWorkRequest.Builder(FlowerNotification.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build();

        WorkManager.getInstance(requireContext()).enqueue(notificationWork);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NOTIFICATION_ID_KEY, notificationCounter.get());
        editor.apply();
    }

    private void openDateDialog(View view){
        List<Integer> currentDate = getCurrentDate();
        DatePickerDialog datePicker = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                data.setText(String.format("%d/%d/%d", dayOfMonth, month+1, year));
                updateOszacowanyCzas();
            }
        }, currentDate.get(2), currentDate.get(1) - 1, currentDate.get(0));
        datePicker.show();
    }

    private void openTimeDialog(View view){
        List<Integer> currentTime = getCurrentTime();
        TimePickerDialog timePicker = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                godzina.setText(String.format("%02d:%02d", hourOfDay, minute));
                updateOszacowanyCzas();
            }
        }, currentTime.get(0), currentTime.get(1), true);
        timePicker.show();
    }

    private boolean checkIfNull(TextView v1, TextView v2){
        if (v1.getText().toString().isEmpty() || v2.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }

    private void updateOszacowanyCzas(){
        if(!godzina.getText().toString().isEmpty() && !data.getText().toString().isEmpty()){
            String[] dateParts = data.getText().toString().split("/");
            String[] timeParts = godzina.getText().toString().split(":");
            oszacowanyCzas.setText(String.format("Nawodnienie w dniu: %s/%s/%s, %s:%s",dateParts[0], dateParts[1], dateParts[2], timeParts[0], timeParts[1]));
        }
    }

    private List<Integer> getCurrentDate(){
        return Arrays.asList(
                LocalDate.now().getDayOfMonth(),
                LocalDate.now().getMonthValue(),
                LocalDate.now().getYear()
        );
    }
    private List<Integer> getCurrentTime(){
        return Arrays.asList(
                LocalTime.now().getHour(),
                LocalTime.now().getMinute()
        );
    }
}