package com.indahouse.skylab.calendarupmc.classes;

import java.util.ArrayList;

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

    public String getCode(){
        return code;
    }

    public int getColor(){
        return color;
    }

    public String getType() {
        return type;
    }
}

