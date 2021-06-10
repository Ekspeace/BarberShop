package com.ekspeace.barbershop.Model;

public class UserInfo {
    private String Name, Number;
    private int Id;

    public UserInfo(String name, String number, int id) {
        Name = name;
        Number = number;
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
}
