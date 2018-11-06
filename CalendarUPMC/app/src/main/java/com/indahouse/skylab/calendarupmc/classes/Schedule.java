package com.indahouse.skylab.calendarupmc.classes;

import java.util.Calendar;

public class Schedule {

    private Calendar startTime;
    private Calendar endTime;

    //This class is used to represent an occurence of a repeated event
    public Schedule(Calendar startTime, Calendar endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public String getStartTimeDMY(){
        return String.format("%02d/%02d/%02d",
            startTime.get(Calendar.DAY_OF_MONTH),
            ((startTime.get(Calendar.MONTH)+1)),
            startTime.get(Calendar.YEAR));
    }

    public String getEndTimeDMY(){
        return String.format("%02d/%02d/%02d",
            endTime.get(Calendar.DAY_OF_MONTH),
            ((endTime.get(Calendar.MONTH)+1)),
            endTime.get(Calendar.YEAR));
    }

    public String getStartTimeHM(){
        return String.format("%02d:%02d",
            startTime.get(Calendar.HOUR_OF_DAY),
            startTime.get(Calendar.MINUTE));
    }

    public String getEndTimeHM(){
        return String.format("%02d:%02d",
            endTime.get(Calendar.HOUR_OF_DAY),
            endTime.get(Calendar.MINUTE));
    }

    public String toString(){
        return getStartTimeHM()+" "+getStartTimeDMY()+" -> "+
        getEndTimeHM()+" "+getEndTimeDMY();
    }
}
