package com.indahouse.skylab.calendarupmc.classes;

import android.content.Context;
import android.os.AsyncTask;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;


public class AsyncTaskGetEventsEntries extends AsyncTask<String, Integer, ArrayList<CalendarEntry>> {

    public Controller delegate;
    public Context context;
    private ArrayList<WeekViewEvent> weekViewEvents;
    private String url;


    public AsyncTaskGetEventsEntries(Controller delegate, Context ctx, String urls) {
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

        //callBack evens
        weekViewEvents.clear();
        weekViewEvents = CalendarMaker.toWeekViewEvents(calEntries);
        delegate.processFinish(weekViewEvents);
    }
}

