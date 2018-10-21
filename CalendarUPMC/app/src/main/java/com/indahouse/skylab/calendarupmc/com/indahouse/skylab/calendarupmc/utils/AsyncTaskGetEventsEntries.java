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
    private String url;


    public AsyncTaskGetEventsEntries(AsyncResponse delegate, Context ctx, String urls) {
        this.delegate = delegate;
        this.context = ctx;
        this.url = urls;
        weekViewEvents = new ArrayList<WeekViewEvent>();
    }

    @Override
    protected ArrayList<CalendarEntry> doInBackground(String... unused) {
        CalendarMaker cm = new CalendarMaker(url);
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

        delegate.processFinish(weekViewEvents);
    }
}

