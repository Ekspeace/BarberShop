package com.ekspeace.barbershop.Model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class HairCut {
    private String hairCutName;
    private Bitmap hairCutImage;

    public HairCut(String hairCutName, Bitmap hairCutImage) {
        this.hairCutName = hairCutName;
        this.hairCutImage = hairCutImage;
    }

    public String getHairCutName() {
        return hairCutName;
    }

    public void setHairCutName(String hairCutName) {
        this.hairCutName = hairCutName;
    }

    public Bitmap getHairCutImage() {
        return hairCutImage;
    }

    public void setHairCutImage(Bitmap hairCutImage) {
        this.hairCutImage = hairCutImage;
    }

}
