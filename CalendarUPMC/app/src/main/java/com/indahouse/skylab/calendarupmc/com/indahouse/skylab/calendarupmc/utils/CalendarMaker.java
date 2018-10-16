package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
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

	public static void parseUE(String ue) {
		String[] pue = ue.split("-");
		System.out.println("Matière : " + pue[pue.length-1]);
		if(pue.length>1)
			System.out.println("Code : " + pue[0]);
		
		if(pue.length>2)
			System.out.println("Type : " + pue[1]);
	}

	public static void parseStartTime(String ue) {
		String[] pue = ue.split(" ");
		System.out.println("Start day : " + pue[0] + " " + pue[2] + " " + pue[1]);
		System.out.println("Start time : " + pue[3]);
	}

	public static void parseEndTime(String ue) {
		String[] pue = ue.split(" ");
		System.out.println("End day : " + pue[0] + " " + pue[2] + " " + pue[1]);
		System.out.println("End time : " + pue[3]);
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
			//System.out.println(calendar);

			ICalendar ical = Biweekly.parse(calendar).first();
			events = ical.getEvents();

//			for (VEvent vEvent : events) {
//				System.out.println("---------------------");
//				parseUE(vEvent.getSummary().getValue());
//				parseStartTime(vEvent.getDateStart().getValue().toString());
//				parseEndTime(vEvent.getDateEnd().getValue().toString());
//				System.out.println("---------------------");
//				System.out.println();
//			}


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
			String startD = tue[0] + " " + tue[2] + " " + tue[1];
			String startT = tue[3];
			
			//End Time
			String[] eue = vEvent.getDateEnd().getValue().toString().split(" ");
			String endD = eue[0] + " " + eue[2] + " " + eue[1];
			String endT = eue[3];
			
			CalendarEntry tmpCal = new CalendarEntry(matiere,code,type,location, startD,startT,endD,endT);
			CalEvents.add(tmpCal);
		}
		
		return CalEvents;
		
	}
	

	
}
