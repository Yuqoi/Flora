package com.example.aplikacja.fragments;




import static android.content.Context.ALARM_SERVICE;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
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

                    Toast.makeText(getContext(), "dziala", Toast.LENGTH_SHORT).show();
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