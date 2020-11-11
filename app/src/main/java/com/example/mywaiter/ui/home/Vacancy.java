package com.example.mywaiter.ui.home;

public class Vacancy {
    public String key, name, time, date, reward, hirer;

    public Vacancy() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Vacancy(String key, String date, String hirer, String name, String reward, String time) {
        this.key = key;
        this.name = name;
        this.time = time;
        this.date = date;
        this.hirer = hirer;
        this.reward = reward;
    }

    public String getTime() {
        return time;
    }
}
