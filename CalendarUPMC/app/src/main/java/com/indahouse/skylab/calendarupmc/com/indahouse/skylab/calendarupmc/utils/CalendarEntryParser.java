package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.Random;

import com.alamkanak.weekview.WeekViewEvent;
import com.indahouse.skylab.calendarupmc.R;

public class CalendarEntryParser {

    public CalendarEntryParser(){

    }

    public static WeekViewEvent parseEntry(Context ctx, CalendarEntry calendarEntry){

            // IL,4I502,Consultation Copies,Salle SAR (14.15.508),02,13:00:00,Jul,2018,Jul,2018,02,14:00:00,true
            Random rand = new Random();
            WeekViewEvent wEvent;

            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(calendarEntry.getStartD()));
            startTime.set(Calendar.HOUR_OF_DAY, calendarEntry.getStartH());
            startTime.set(Calendar.MINUTE, calendarEntry.getStartM());
            startTime.set(Calendar.MONTH, CalendarEntry.monthAsInt(calendarEntry.getStartMo())-1);
            startTime.set(Calendar.YEAR, calendarEntry.getStartY());

        Calendar endTime = Calendar.getInstance();
            endTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(calendarEntry.getEndD()));
            endTime.set(Calendar.HOUR_OF_DAY, calendarEntry.getEndH());
            endTime.set(Calendar.MINUTE, calendarEntry.getEndM());
            endTime.set(Calendar.MONTH, calendarEntry.monthAsInt(calendarEntry.getEndMo())-1);
            endTime.set(Calendar.YEAR, calendarEntry.getEndY());


        wEvent = new WeekViewEvent(rand.nextInt(100000), getEventTitle(startTime), "\n\n"+calendarEntry.getLocation(), startTime, endTime);
        wEvent.setName(calendarEntry.getMatiere());

            switch (rand.nextInt(4)){
                case 0:
                wEvent.setColor(ctx.getResources().getColor(R.color.event_color_01));
                break;
                case 1:
                wEvent.setColor(ctx.getResources().getColor(R.color.event_color_02));
                break;
                case 2:
                wEvent.setColor(ctx.getResources().getColor(R.color.event_color_03));
                break;
                case 3:
                wEvent.setColor(ctx.getResources().getColor(R.color.event_color_04));
                break;
                default:
                break;
            }


        return wEvent;

    }

    private static String getEventTitle(Calendar time) {
     return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
 }

}
