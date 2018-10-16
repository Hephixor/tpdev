package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

//a mettre quelquepart
/*ListView list_data = fragment_view.findViewById(R.id.list_data);
        new AsyncTaskGetEventsEntries(getActivity(), list_data).execute("");*/


public class AsyncTaskGetEventsEntries extends AsyncTask<String, String, ArrayList<String>> {

    Activity context;
    ListView view;

    public AsyncTaskGetEventsEntries(Activity ctx, ListView v){
        context = ctx;
        view = v;
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, strings);
        view.setAdapter(adapter);
    }
}

