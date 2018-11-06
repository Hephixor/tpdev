package com.indahouse.skylab.calendarupmc.classes;

import com.alamkanak.weekview.WeekViewEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.util.com.google.ical.compat.javautil.DateIterator;
import biweekly.util.org.apache.commons.codec.binary.Base64;

public class CalendarMaker {
    private static String urls;
    private static List<VEvent> vEvents;
    private static long WVEidCounter = 0;

    public CalendarMaker(String url) {
        urls = url;
    }

    //Connects to url and fetch ics file
    public List<VEvent> downloadAndParseVEventsFromInternet() {
        try {
            //General settings
            String user_pass = "student.master:guest";
            String encoded_pass = Base64.encodeBase64String(user_pass.getBytes());

            URL url = new URL(urls);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(30000);
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "text/calendar");
            con.setRequestProperty("Authorization", "Basic " + encoded_pass);

            //Fecth raw text
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                content.append("\r\n");
            }

            in.close();
            con.disconnect();

            //Parse raw text
            String calendar = content.toString();
            ICalendar ical = Biweekly.parse(calendar).first();
            vEvents = ical.getEvents();

        } catch (MalformedURLException e) {
            System.out.println("Malformed URL");
        } catch (IOException e) {
            System.out.println("IO error");
            e.printStackTrace();
        }

        return vEvents;
    }

    public ArrayList<CalendarEntry> toCalendarEntries(List<VEvent> vevents) {

        ArrayList<CalendarEntry> CalEntries = new ArrayList<CalendarEntry>();

        //Parse each event
        for (VEvent vEvent : vevents) {
            //Details
            String[] pue = vEvent.getSummary().getValue().split("-");
            String matiere = pue[pue.length - 1];
            String code = "";
            if (pue.length > 1)
                code = pue[0];
            String type = "";
            if (pue.length > 2)
                type = pue[1];
            String location = "";
            if (vEvent.getLocation() != null) {
                location = vEvent.getLocation().getValue();
            }

            CalendarEntry tmpCal = new CalendarEntry(matiere,code,type,location,0);
            CalEntries.add(tmpCal);

            /*compute vEvent duration*/
            //Start Time
            Calendar firstStartTime = Calendar.getInstance();
            firstStartTime.setTime(vEvent.getDateStart().getValue());
            int start_hour = firstStartTime.get(Calendar.HOUR_OF_DAY);
            int start_min = firstStartTime.get(Calendar.MINUTE);
            //End Time
            Calendar firstEndTime = Calendar.getInstance();
            firstEndTime.setTime(vEvent.getDateEnd().getValue());
            int end_hour = firstEndTime.get(Calendar.HOUR_OF_DAY);
            int end_min = firstEndTime.get(Calendar.MINUTE);
            //Duration
            int duree_hour = end_hour - start_hour;
            int duree_min = end_min - start_min;
            assert (duree_hour >= 0 && duree_min >= 0);

            //Iterator on vEvent repetitions (without exceptions)
            DateIterator iterator = vEvent.getDateIterator(Calendar.getInstance().getTimeZone());
            while(iterator.hasNext()) {
                Date startDate = iterator.next();

                Calendar startTime = Calendar.getInstance();
                startTime.setTime(startDate);

                Calendar endTime = Calendar.getInstance();
                endTime.setTime(startDate);
                endTime.add(Calendar.HOUR_OF_DAY, duree_hour);
                endTime.add(Calendar.MINUTE, duree_min);

                tmpCal.addSchedule(new Schedule(startTime,endTime));
            }

        }

        return CalEntries;
    }

    //Instantiate new WeekViewEvent from calendarEntry list
    public static ArrayList<WeekViewEvent> toWeekViewEvents(List<CalendarEntry> calentries) {
        ArrayList<WeekViewEvent> weekViewEvents = new ArrayList<WeekViewEvent>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        WeekViewEvent tmpWVEvent;
        for (CalendarEntry calEntry : calentries) {
            for (Schedule sched : calEntry.getSchedules()) {
                if (sched.getStartTime().get(Calendar.YEAR) == thisYear || sched.getStartTime().get(Calendar.YEAR) == thisYear + 1) {
                    tmpWVEvent = new WeekViewEvent(generateWVEId(),calEntry.getMatiere()+" - " + calEntry.getType()+"\n\n", "\n" + calEntry.getLocation()+"\n", sched.getStartTime(), sched.getEndTime());
                    weekViewEvents.add(tmpWVEvent);
                }
            }
        }
        return weekViewEvents;
    }

    //ensure that every single event has a different ID
    private static long generateWVEId() {
        WVEidCounter++;
        return WVEidCounter;
    }

}
