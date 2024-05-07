package com.example.aplikacja.models;

import android.net.Uri;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Flower implements Serializable {
    String name;
    String image;
    String desc;
    String bloomTime;
    List<String> colors;
    String family;
    String nativeRegion;
    String scientificName;
    String soilType;
    String sunRequirements;
    String wateringNeeds;
    String avgHeight;
    String avgSpread;
    String toxicity;
    String susceptibilityToPests;
    String usability;
    String difficulty;

    public Flower(String name, String image, String desc, String bloomTime,
                  List<String> colors, String family, String nativeRegion, String scientificName,
                  String soilType, String sunRequirements, String wateringNeeds, String avgHeight,
                  String avgSpread, String toxicity, String susceptibilityToPests, String usability,
                  String difficulty) {
        this.name = name;
        this.image = image;
        this.desc = desc;
        this.bloomTime = bloomTime;
        this.colors = colors;
        this.family = family;
        this.nativeRegion = nativeRegion;
        this.scientificName = scientificName;
        this.soilType = soilType;
        this.sunRequirements = sunRequirements;
        this.wateringNeeds = wateringNeeds;
        this.avgHeight = avgHeight;
        this.avgSpread = avgSpread;
        this.toxicity = toxicity;
        this.susceptibilityToPests = susceptibilityToPests;
        this.usability = usability;
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "Flower{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", desc='" + desc + '\'' +
                ", bloomTime='" + bloomTime + '\'' +
                ", colors=" + colors +
                ", family='" + family + '\'' +
                ", nativeRegion='" + nativeRegion + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", soilType='" + soilType + '\'' +
                ", sunRequirements='" + sunRequirements + '\'' +
                ", wateringNeeds='" + wateringNeeds + '\'' +
                ", avgHeight='" + avgHeight + '\'' +
                ", avgSpread='" + avgSpread + '\'' +
                ", toxicity='" + toxicity + '\'' +
                ", susceptibilityToPests='" + susceptibilityToPests + '\'' +
                ", usability='" + usability + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDesc() {
        return desc;
    }

    public String getBloomTime() {
        return bloomTime;
    }

    public List<String> getColors() {
        return colors;
    }

    public String getFamily() {
        return family;
    }

    public String getNativeRegion() {
        return nativeRegion;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getSoilType() {
        return soilType;
    }

    public String getSunRequirements() {
        return sunRequirements;
    }

    public String getWateringNeeds() {
        return wateringNeeds;
    }

    public String getAvgHeight() {
        return avgHeight;
    }

    public String getAvgSpread() {
        return avgSpread;
    }

    public String getToxicity() {
        return toxicity;
    }

    public String getSusceptibilityToPests() {
        return susceptibilityToPests;
    }

    public String getUsability() {
        return usability;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
