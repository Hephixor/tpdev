package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;
import com.indahouse.skylab.calendarupmc.BaseActivity;
import com.indahouse.skylab.calendarupmc.R;

import java.util.Calendar;
import java.util.Random;

public class CalendarEntryParser {

    public CalendarEntryParser(){

    }

    public static WeekViewEvent parseEntry(Context ctx, CalendarEntry calendarEntry){

        if(calendarEntry.getStartY() == Calendar.getInstance().get(Calendar.YEAR) || calendarEntry.getStartY() == Calendar.getInstance().get(Calendar.YEAR) +1 ){
            // IL,4I502,Consultation Copies,Salle SAR (14.15.508),02,13:00:00,Jul,2018,Jul,2018,02,14:00:00,true
            Random rand = new Random();
            WeekViewEvent wEvent;

            Calendar startTime = Calendar.getInstance();

            startTime.set(Calendar.DATE, Integer.parseInt(calendarEntry.getStartD()));
            startTime.set(Calendar.HOUR_OF_DAY, calendarEntry.getStartH());
            startTime.set(Calendar.MINUTE, calendarEntry.getStartM());
            startTime.set(Calendar.MONTH, calendarEntry.monthAsInt(calendarEntry.getStartMo()));
            startTime.set(Calendar.YEAR, calendarEntry.getStartY());

            Calendar endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR, calendarEntry.getEndH());
            endTime.add(Calendar.MINUTE, calendarEntry.getEndM());
            endTime.set(Calendar.MONTH, calendarEntry.monthAsInt(calendarEntry.getEndMo()));

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
        else{
            return null;
        }

    }

    private static String getEventTitle(Calendar time) {
     return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
 }

}
