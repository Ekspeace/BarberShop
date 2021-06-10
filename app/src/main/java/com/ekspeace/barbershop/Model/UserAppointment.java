package com.ekspeace.barbershop.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.File;

public class UserAppointment {
    private String Name, Number, Service, TimeDate, Barber,  specialServiceInfo;
    private File haircutImage, specialServiceImage;

    public UserAppointment() {
    }

    public UserAppointment(String name, String number, String service, String timeDate,String barber) {
        Name = name;
        Number = number;
        Service = service;
        TimeDate = timeDate;
        Barber = barber;
    }

    public UserAppointment(String name, String number, String service, String timeDate, String barber, String specialServiceInfo, File haircutImage, File specialServiceImage) {
        Name = name;
        Number = number;
        Service = service;
        TimeDate = timeDate;
        Barber = barber;
        this.specialServiceInfo = specialServiceInfo;
        this.haircutImage = haircutImage;
        this.specialServiceImage = specialServiceImage;
    }

    public String getBarber() {
        return Barber;
    }

    public void setBarber(String barber) {
        Barber = barber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getTimeDate() {
        return TimeDate;
    }

    public void setTimeDate(String timeDate) {
        TimeDate = timeDate;
    }

}
