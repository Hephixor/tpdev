package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;



public class AsyncTaskGetEventsEntries extends AsyncTask<Boolean , Integer, ArrayList<CalendarEntry>> {

    private ArrayList<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    private ArrayList<CalendarEntry> calEvents = new ArrayList<>();
    public AsyncResponse delegate = null;
    public Context context;


    public AsyncTaskGetEventsEntries(AsyncResponse delegate, Context ctx){
        this.delegate=delegate;
        this.context=ctx;
    }

    @Override
    protected ArrayList<CalendarEntry> doInBackground(Boolean... unused) {
        String urls = new String("https://cal.ufr-info-p6.jussieu.fr/caldav.php/STL/M2_STL");
        CalendarMaker cm = new CalendarMaker(urls);
        cm.caldown();
        ArrayList<CalendarEntry> cal = cm.getEventsEntries(cm.getEventCollection());

        return cal;
    }

    protected void onProgressUpdate(Integer... progress){
       // setProgressPercent(progress);
    }

    @Override
    protected void onPostExecute(ArrayList<CalendarEntry> calEvents) {
     super.onPostExecute(calEvents);
     ArrayList<String> strings = new ArrayList<>();
     events.clear();
        WeekViewEvent tmpEvent;
     for (CalendarEntry calEvent : calEvents) {
         if(calEvent.getStartY() == Calendar.getInstance().get(Calendar.YEAR) || calEvent.getStartY() == Calendar.getInstance().get(Calendar.YEAR) +1 ) {
            // if(calEvent.getMatiere().equals("TAS")) {
                 tmpEvent = CalendarEntryParser.parseEntry(context, calEvent);
                 this.events.add(tmpEvent);
            // }
         }
    }

    delegate.processFinish(events);
}

protected String getEventTitle(Calendar time) {
    return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
}
}

