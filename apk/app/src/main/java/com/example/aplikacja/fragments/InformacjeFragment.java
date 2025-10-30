package com.example.aplikacja.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aplikacja.R;
import com.example.aplikacja.helpers.FlowerViewModel;
import com.example.aplikacja.models.Flower;

import java.util.Iterator;


public class InformacjeFragment extends Fragment {



    TextView flowerName;
    TextView flowerScietificName;
    TextView flowerDesc;
    TextView flowerColors;
    TextView flowerFamily;
    TextView flowerBloomTime;
    TextView flowerAvgHeight;
    TextView flowerAvgSpread;
    TextView flowerRegion;

    TextView flowerSoil;
    TextView flowerSun;
    TextView flowerWater;

    TextView flowerUsability;
    TextView flowerPests;
    TextView flowerDifficulty;
    TextView flowerToxicity;


    private FlowerViewModel flowerViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informacje, container, false);

        flowerViewModel = new ViewModelProvider(requireActivity()).get(FlowerViewModel.class);
        Flower flower = flowerViewModel.getFlower();

        asignFields(view);
        setInformation(flower);




        return view;
    }
    public void asignFields(View view){
        flowerName = view.findViewById(R.id.info_flowername);
        flowerDesc = view.findViewById(R.id.info_flowerdesc);
        flowerScietificName = view.findViewById(R.id.info_flowerscientificname);

        flowerColors = view.findViewById(R.id.info_flower_colors);
        flowerFamily = view.findViewById(R.id.info_flower_family);
        flowerBloomTime = view.findViewById(R.id.info_flower_bloom);
        flowerAvgHeight = view.findViewById(R.id.info_flower_height);
        flowerAvgSpread = view.findViewById(R.id.info_flower_spread);
        flowerRegion = view.findViewById(R.id.info_flower_region);

        flowerSoil = view.findViewById(R.id.info_flower_soil);
        flowerSun = view.findViewById(R.id.info_flower_sun);
        flowerWater = view.findViewById(R.id.info_flower_watering);

        flowerDifficulty = view.findViewById(R.id.info_difficulty);
        flowerToxicity = view.findViewById(R.id.info_toxicity);
        flowerUsability = view.findViewById(R.id.info_usability);
        flowerPests = view.findViewById(R.id.info_susceptibility_to_pests);
    }

    public void setInformation(Flower flower){
        StringBuilder colors = new StringBuilder();
        for (String s : flower.getColors()) {
            colors.append(s).append(" ");
        }

        flowerName.setText(flower.getName());
        flowerDesc.setText(flower.getDesc());
        flowerScietificName.setText(flower.getScientificName());

        flowerFamily.setText(flower.getFamily());
        flowerColors.setText(colors);
        flowerBloomTime.setText(flower.getBloomTime());
        flowerAvgSpread.setText(flower.getAvgSpread());
        flowerAvgHeight.setText(flower.getAvgHeight());
        flowerRegion.setText(flower.getNativeRegion());

        flowerSoil.setText(flower.getSoilType());
        flowerSun.setText(flower.getSunRequirements());
        flowerWater.setText(flower.getWateringNeeds());

        flowerDifficulty.setText(flower.getDifficulty());
        flowerToxicity.setText(flower.getToxicity());
        flowerUsability.setText(flower.getUsability());
        flowerPests.setText(flower.getSusceptibilityToPests());
    }
}