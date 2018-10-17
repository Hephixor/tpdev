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
            for (CalendarEntry calEvent : calEvents) {
                WeekViewEvent tmpEvent = CalendarEntryParser.parseEntry(context,calEvent);
                    if(tmpEvent!=null){
                        if(calEvent.getMatiere().toUpperCase().equals("SVP")) {
                            Log.e("XXX", String.valueOf(calEvent.getStartH())+"H"+String.valueOf(calEvent.getStartM()) + " -- " + String.valueOf(calEvent.getEndH())+"H"+String.valueOf(calEvent.getEndM()));
                            this.events.add(tmpEvent);
                        }
                    }
                }

        delegate.processFinish(events);
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }
}

