package com.example.statefull;

public class Reminder {
    int _id;
    int hour;
    int minute;
    String meridian;
    boolean active = false;

    Reminder(int h, int m, String merd) {
        hour = h;
        minute = m;
        active = true;
        meridian = merd;
    }

    Reminder(int h, int m, String merd, Boolean active) {
        hour = h;
        minute = m;
        this.active = active;
        meridian = merd;
    }

    int getHour() {
        return hour;
    }

    int getMinute() {
        return minute;
    }

    boolean isActive() {
        return active;
    }
}
