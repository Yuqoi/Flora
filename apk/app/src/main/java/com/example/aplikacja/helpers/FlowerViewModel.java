package com.example.aplikacja.helpers;

import androidx.lifecycle.ViewModel;

import com.example.aplikacja.models.Flower;

public class FlowerViewModel extends ViewModel {

    private Flower flower;

    public Flower getFlower(){
        return flower;
    }
    public void setFlower(Flower flower){
        this.flower = flower;
    }

}
