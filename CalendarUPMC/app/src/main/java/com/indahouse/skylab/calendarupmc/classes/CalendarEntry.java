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

    //Constructor
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

    //Get event repetitions
    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    //Add event repetition
    public void addSchedule(Schedule sched) {
        schedules.add(sched);
    }

    //Get the place of the event
    public String getLocation() {
        return location;
    }

    //Get the matiere of the event
    public String getMatiere() {
        return matiere;
    }

    //Get the code of the matiere of the event
    public String getCode(){
        return code;
    }

    //Get the color param of the event
    public int getColor(){
        return color;
    }

    //Get the type of the event
    public String getType() {
        return type;
    }
}

