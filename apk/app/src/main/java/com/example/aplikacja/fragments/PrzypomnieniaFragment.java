package com.example.aplikacja.fragments;




import static android.content.Context.ALARM_SERVICE;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.aplikacja.R;
import com.example.aplikacja.activities.WaterNotification;
import com.example.aplikacja.helpers.AlarmReceiver;
import com.example.aplikacja.helpers.FlowerViewModel;
import com.example.aplikacja.models.Flower;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


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

    private static final int REQUEST_CODE_NOTIFICATION = 101;

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
                if(!checkIfNull(data, godzina)) {
                    int howManyDays = flower.getWhenToWater();
                    if (checkNotificationPermission(view)) {
                        setWateringReminder(view.getContext(), 20, 27);
                        Toast.makeText(v.getContext(), "Reminder set for 7 days at 8:00 AM", Toast.LENGTH_SHORT).show();
                    } else {
                        requestNotificationPermission(view);
                    }
                }
            }
        });

        usunAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_NOTIFICATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setWateringReminder(requireContext(), 20, 27);
                Toast.makeText(requireContext(), "Reminder set for 7 days at 8:00 AM", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkNotificationPermission(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestNotificationPermission(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATION);
        }
    }

    public void setWateringReminder(Context context, int hourOfDay, int minute) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, LocalDate.now().getDayOfYear()); // Set the reminder 7 days from now
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        try {
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(context, "Permission to set exact alarms denied", Toast.LENGTH_SHORT).show();
        }
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
                godzina.setText(String.format("%d:%d", hourOfDay, minute));
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