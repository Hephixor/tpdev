package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import android.util.Log;

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
import java.util.Locale;
import java.util.TimeZone;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.RecurrenceDates;
import biweekly.util.Frequency;
import biweekly.util.com.google.ical.compat.javautil.DateIterator;
import biweekly.util.org.apache.commons.codec.binary.Base64;

public class CalendarMaker {
	private static String urls;
	private static List<VEvent> events;
	
	public List<VEvent> getEvents(){
		return this.events;
	}
	
	public CalendarMaker(String url) {
		urls = url;
	}
	
	
	public void caldown() {
		ICSDownloader icsdown = new ICSDownloader();	
		icsdown.downloadICS(urls);
	}

	public List<VEvent> getEventCollection() {
		try {
			String user_pass = "student.master:guest";
			String encoded_pass = Base64.encodeBase64String(user_pass.getBytes());

			URL url = new URL(urls);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(30000);
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type",  "text/calendar");
			con.setRequestProperty("Authorization", "Basic " + encoded_pass);

			System.out.println("Status: " + con.getResponseCode());

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while((inputLine = in.readLine()) != null) {
				content.append(inputLine);
				content.append("\r\n");
			}

			in.close();
			con.disconnect();

			String calendar = content.toString();
			ICalendar ical = Biweekly.parse(calendar).first();
			events = ical.getEvents();

		} catch (MalformedURLException e) {
			System.out.println("Malformed URL");
		} catch (IOException e) {
			System.out.println("IO error");
			e.printStackTrace();
		}
		
		return events;
	}
	
	public ArrayList<CalendarEntry> getEventsEntries(List<VEvent> events){
		
		ArrayList<CalendarEntry> CalEvents = new ArrayList<CalendarEntry>();
		
		for (VEvent vEvent : events) {
			//Details

			String[] pue = vEvent.getSummary().getValue().split("-");
			String matiere =  pue[pue.length-1];
			/* if(matiere.equals("TAS")){
			    Log.e("XXX PARSER", vEvent.getDateStart().getValue().toString());
            } */



			String code = "";
			String type = "";
			String location = "";
			if(pue.length>1)
				code = pue[0];
			if(pue.length>2)
				type = pue[1];
			
			if(vEvent.getLocation()!=null) {
				location = vEvent.getLocation().getValue();
			}
			

			//Start Time
			String[] tue = vEvent.getDateStart().getValue().toString().split(" ");
			String startD = tue[2];
			String startM = tue[1];
			String startT = tue[3];
			String startY = tue[5];



			//End Time
			String[] eue = vEvent.getDateEnd().getValue().toString().split(" ");
			String endD = eue[2];
			String endM = eue[1];
			String endT = eue[3];
			String endY = eue[5];
			
			CalendarEntry tmpCal = new CalendarEntry(matiere,code,type,location,startY,startM,startD,startT,endY,endM,endD,endT);
		//	Log.e("Entry" ,String.valueOf(tmpCal.toString()));
            if(vEvent.getRecurrenceRule()!=null){
                switch (vEvent.getRecurrenceRule().getValue().getFrequency()){
                    case DAILY:
                        tmpCal.setFrequency(Frequency.DAILY);
                        break;
                    case WEEKLY:
                        tmpCal.setFrequency(Frequency.WEEKLY);
                        break;
                    case MONTHLY:
                        tmpCal.setFrequency(Frequency.MONTHLY);
                        break;
                    case YEARLY:
                        tmpCal.setFrequency(Frequency.YEARLY);
                        break;
                    default:
                        break;
                }
            }

            if(matiere.equals("TAS") && startY.equals("2018")) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(startD));
                cal.set(Calendar.MONTH, CalendarEntry.monthAsInt(startM));
                cal.set(Calendar.YEAR, Integer.parseInt(startY));

                Date date = cal.getTime();

                if (vEvent.getRecurrenceRule() != null) {
                    DateIterator di = vEvent.getRecurrenceRule().getDateIterator(date, TimeZone.getDefault());
                    List<RecurrenceDates> listRec = vEvent.getRecurrenceDates();
                    for (RecurrenceDates rec : listRec) {
                        Log.e("XXX PARSING",rec.getDates().toString());
                    }
                   // Log.e("XXX PARSING", date.toString());
                    //Log.e("XXX PARSING", matiere + " " + startD + "/" + startM + "/" + startY);
                    //Log.e("XXX PARSING", "Repetitions " );
                }
            }

			CalEvents.add(tmpCal);
		}
		
		return CalEvents;
		
	}
	

	
}
