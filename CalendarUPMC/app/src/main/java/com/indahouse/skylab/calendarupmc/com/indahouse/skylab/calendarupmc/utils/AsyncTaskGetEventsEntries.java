package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
import com.indahouse.skylab.calendarupmc.BaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AsyncTaskGetEventsEntries extends AsyncTask<Boolean , Integer, ArrayList<CalendarEntry>> {

    private ArrayList<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    private ArrayList<CalendarEntry> calEvents = new ArrayList<>();
    public AsyncResponse delegate = null;


    public AsyncTaskGetEventsEntries(AsyncResponse delegate){
        this.delegate=delegate;
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
                WeekViewEvent tmpEvent = CalendarEntryParser.parseEntry(calEvent);
                    if(tmpEvent!=null){
                     this.events.add(tmpEvent);
                    }
                }

            Log.e("Event List Size ", String.valueOf(events.size()));

        delegate.processFinish(events);
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }
}

