package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TimeUtils;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import biweekly.util.Frequency;
import biweekly.util.Recurrence;
import biweekly.util.com.google.ical.compat.javautil.DateIterator;


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
        //cm.caldown();
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
            //     Log.e("XXX ", calEvent.toString());
                 tmpEvent = CalendarEntryParser.parseEntry(context, calEvent);
                 this.events.add(tmpEvent);

                 if(calEvent.hasRepetition()){
                     ArrayList<Date> repetitions = computeRepetitionDates(calEvent, tmpEvent.getStartTime());
                     for (Date date : repetitions) {
                         Calendar cal = Calendar.getInstance();
                         cal.setTime(date);
                         int day = cal.get(Calendar.DAY_OF_MONTH);
                         int month = cal.get(Calendar.MONTH);
                         int year = cal.get(Calendar.YEAR);

                         WeekViewEvent copy = new WeekViewEvent(0,tmpEvent.getName(),year,month,day,
                                 tmpEvent.getStartTime().get(Calendar.HOUR_OF_DAY),
                                 tmpEvent.getStartTime().get(Calendar.MINUTE),year,month,day,
                                 tmpEvent.getEndTime().get(Calendar.HOUR_OF_DAY),tmpEvent.getEndTime().get(Calendar.MINUTE));
                         this.events.add(copy);
                         Log.e("RECUR ", copy.getName()+" "+copy.getStartTime().get(Calendar.DAY_OF_MONTH)
                                 +"/"+copy.getStartTime().get(Calendar.MONTH)+"/"+copy.getStartTime().get(Calendar.YEAR)+" "+copy.getStartTime().get(Calendar.HOUR_OF_DAY)
                                 +":"+copy.getStartTime().get(Calendar.MINUTE));
                     }
                 }

            //}
         }
    }

    delegate.processFinish(events);
}


private ArrayList<Date> computeRepetitionDates(CalendarEntry calEvent, Calendar startTime){
        Date st = startTime.getTime();
        ArrayList<Date> dates = new ArrayList<Date>();
        Recurrence recurrence = new Recurrence.Builder(calEvent.getFrequency()).interval(1).build();
        DateIterator dateIterator = recurrence.getDateIterator(st, startTime.getTimeZone());

        Boolean pre = true;
        Calendar tmpCalendar = Calendar.getInstance();
        tmpCalendar.set(2019,01,01);

        while(pre && dateIterator.hasNext()) {
              Date next = dateIterator.next();
                if (next.after(tmpCalendar.getTime())) {
                    pre = false;
                }

                dates.add(next);

        }



        return dates;
}


protected String getEventTitle(Calendar time) {
    return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
}
}

