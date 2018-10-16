package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import android.util.Log;

public class CalendarEntryParser {

    public CalendarEntryParser(){

    }

    public static void parseEntry(String string){
        // IL,4I502,TME5,Salle 14.15.408,Thu 27 Sep,16:00:00,Thu 27 Sep,18:00:00,true
        Log.e("Entry", string);
        String[] values = string.split(",");

    }


}
