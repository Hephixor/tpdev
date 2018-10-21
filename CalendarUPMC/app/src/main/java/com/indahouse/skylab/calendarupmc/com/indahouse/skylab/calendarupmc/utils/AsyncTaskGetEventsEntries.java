package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AsyncTaskGetEventsEntries extends AsyncTask<String, Integer, ArrayList<CalendarEntry>> {

    public AsyncResponse delegate;
    public Context context;
    private ArrayList<WeekViewEvent> weekViewEvents;


    public AsyncTaskGetEventsEntries(AsyncResponse delegate, Context ctx) {
        this.delegate = delegate;
        this.context = ctx;
        weekViewEvents = new ArrayList<WeekViewEvent>();
    }

    @Override
    protected ArrayList<CalendarEntry> doInBackground(String... unused) {
        String urls = new String("https://cal.ufr-info-p6.jussieu.fr/caldav.php/STL/M2_STL");
        CalendarMaker cm = new CalendarMaker(urls);
        ArrayList<CalendarEntry> cal = cm.toCalendarEntries(cm.downloadAndParseVEventsFromInternet());

        return cal;
    }

    protected void onProgressUpdate(Integer... progress) {
        // setProgressPercent(progress);
    }

    @Override
    protected void onPostExecute(ArrayList<CalendarEntry> calEntries) {
        super.onPostExecute(calEntries);

        weekViewEvents.clear();

        weekViewEvents = CalendarMaker.toWeekViewEvents(calEntries);

        /*Log.d("MYTAG","calEntries ("+calEntries.size()+"), weekViewEvents ("+
                weekViewEvents.size()+")");

        for(CalendarEntry calentry : calEntries){
            if(calentry.getSchedules().size() > 1){
                Log.d("MYTAG",calentry.toString());
                for(Schedule sched : calentry.getSchedules()){
                    Log.d("MYTAG",sched.toString());
                }
                break;
            }
        }*/

        delegate.processFinish(weekViewEvents);
    }
}

