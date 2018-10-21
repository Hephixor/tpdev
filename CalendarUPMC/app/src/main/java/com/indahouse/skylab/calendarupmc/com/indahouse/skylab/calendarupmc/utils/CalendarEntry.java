package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import android.util.Log;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import biweekly.util.Frequency;

public class CalendarEntry {

    private String matiere;
    private String code;
    private String type;
    private String location;
    private boolean suivi;
    private ArrayList<Schedule> schedules;
    private int color;

    public CalendarEntry(String matiere, String code, String type, String location, int color) {
        this.matiere = matiere;
        this.code = code;
        this.type = type;
        this.location = location;
        this.color = color;
        this.suivi = true;
        this.schedules = new ArrayList<Schedule>();
    }

    @Override
    public String toString() {
        String str = matiere + "," + code + "," + type + "," + location + ","
        + schedules.size() + " dates," + suivi + ",";
        return str;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void addSchedule(Schedule sched) {
        schedules.add(sched);
    }

    public String getLocation() {
        return location;
    }

    public String getMatiere() {
        return matiere;
    }
}

