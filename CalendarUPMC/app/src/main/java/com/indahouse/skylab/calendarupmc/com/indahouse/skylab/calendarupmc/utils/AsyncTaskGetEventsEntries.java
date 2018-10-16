package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
import com.indahouse.skylab.calendarupmc.R;
import com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils.apiclient.Event;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//a mettre quelquepart
/*ListView list_data = fragment_view.findViewById(R.id.list_data);
        new AsyncTaskGetEventsEntries(getActivity(), list_data).execute("");*/


public class AsyncTaskGetEventsEntries extends AsyncTask<String, String, ArrayList<String>> {

    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    private WeekView mWeekView;

    Activity context;
    ListView view;

    public AsyncTaskGetEventsEntries(Activity ctx, ListView v, WeekView weekView){
        context = ctx;
        view = v;
        mWeekView = weekView;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        String urls = new String("https://cal.ufr-info-p6.jussieu.fr/caldav.php/STL/M1_STL");

        CalendarMaker cm = new CalendarMaker(urls);
        ArrayList<CalendarEntry> cal = cm.getEventsEntries(cm.getEventCollection());

        ArrayList<String> data = new ArrayList<String>();
        for (CalendarEntry calendarEntry : cal) {
            data.add(calendarEntry.toString());
        }

        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);

        // parser ici les Strings en Event


        for(String string : strings){
            CalendarEntryParser.parseEntry(string);
        }

        events.clear();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, strings);
        view.setAdapter(adapter);


        //for (Event event : events) {
         //   this.events.add(event.toWeekViewEvent());
        //}
        WeekViewLoader wk = new WeekViewLoader() {
            @Override
            public double toWeekViewPeriodIndex(Calendar instance) {
                return 0;
            }

            @Override
            public List<? extends WeekViewEvent> onLoad(int periodIndex) {
                return events;
            }
        };

        mWeekView.setWeekViewLoader(wk);
        mWeekView.notifyDatasetChanged();
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }
}

