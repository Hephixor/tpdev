package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//a mettre quelquepart
/*ListView list_data = fragment_view.findViewById(R.id.list_data);
        new AsyncTaskGetEventsEntries(getActivity(), list_data).execute("");*/


public class AsyncTaskGetEventsEntries extends AsyncTask<Boolean , Integer, ArrayList<CalendarEntry>> {

    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    private WeekView mWeekView;
    private ArrayList<CalendarEntry> calEvents = new ArrayList<>();
    private ArrayList<String> data = new ArrayList<String>();



    Activity context;
    ListView view;

    public AsyncTaskGetEventsEntries(Activity ctx, ListView v, WeekView weekView){
        context = ctx;
        view = v;
        mWeekView = weekView;
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
                if(calEvent.getMatiere().toUpperCase().equals("PPC")) {
                    WeekViewEvent tmpEvent = CalendarEntryParser.parseEntry(calEvent);
                    if(tmpEvent!=null){
                      data.add(calEvent.toString());
                      this.events.add(tmpEvent);
                    }
                }
            }

            Log.e("Event List Size ", String.valueOf(events.size()));

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, data);
            view.setAdapter(adapter);

        WeekViewLoader wk = new WeekViewLoader() {
            int i=0;
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

