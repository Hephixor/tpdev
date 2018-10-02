package java_ics_parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
		//System.out.println("Parsing UE : " + ue);
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

	public void calCon() {
		try {
			URL url = new URL(urls);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(30000);
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type",  "text/calendar");
			con.setDoOutput(true);

			String user_pass = "student.master:guest";

			String encoded_pass = Base64.encodeBase64String(user_pass.getBytes());
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
			System.out.println(calendar);

			ICalendar ical = Biweekly.parse(calendar).first();
			//			System.out.println(ical);
			events = ical.getEvents();

			for (VEvent vEvent : events) {
				System.out.println("---------------------");
				parseUE(vEvent.getSummary().getValue());
				parseStartTime(vEvent.getDateStart().getValue().toString());
				parseEndTime(vEvent.getDateEnd().getValue().toString());
				System.out.println("---------------------");
				System.out.println();
			}


		} catch (MalformedURLException e) {
			System.out.println("Malformed URL");
		} catch (IOException e) {
			System.out.println("IO error");
			e.printStackTrace();
		}
	}

	
}
