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

public class Main {

	public static void main(String[] args) {

		calCon();
		
//		String str =
//				"BEGIN:VCALENDAR\n" + 
//						"PRODID:-//davical.org//NONSGML AWL Calendar//EN\n" + 
//						"VERSION:2.0\n" + 
//						"CALSCALE:GREGORIAN\n" + 
//						"X-WR-CALNAME:M1_STL\n" + 
//						"BEGIN:VEVENT\n" + 
//						"TRANSP:OPAQUE\n" + 
//						"DTEND;TZID=Europe/Paris:20170126T154500\n" + 
//						"UID:9500DA8A-755C-4CDC-94A1-431FC1708041\n" + 
//						"EXDATE;TZID=Europe/Paris:20170427T134500\n" + 
//						"EXDATE;TZID=Europe/Paris:20170316T134500\n" + 
//						"EXDATE;TZID=Europe/Paris:20170406T134500\n" + 
//						"EXDATE;TZID=Europe/Paris:20170309T134500\n" + 
//						"EXDATE;TZID=Europe/Paris:20170413T134500\n" + 
//						"DTSTAMP:20170123T081938Z\n" + 
//						"LOCATION:Salle 55.65.103\n" + 
//						"SEQUENCE:0\n" + 
//						"SUMMARY:4I506-TD1-CPS\n" + 
//						"DTSTART;TZID=Europe/Paris:20170126T134500\n" + 
//						"X-APPLE-TRAVEL-ADVISORY-BEHAVIOR:AUTOMATIC\n" + 
//						"CREATED:20161110T092233Z\n" + 
//						"RRULE:FREQ=WEEKLY;INTERVAL=1;UNTIL=20170428T215959Z\n" + 
//						"END:VEVENT\n" + 
//						"BEGIN:VEVENT\n" + 
//						"CREATED:20170331T070552Z\n" + 
//						"UID:F567E2DB-2521-411C-93CD-FDCA86C9AC15\n" + 
//						"DTEND;TZID=Europe/Paris:20170613T130000\n" + 
//						"TRANSP:OPAQUE\n" + 
//						"X-APPLE-TRAVEL-ADVISORY-BEHAVIOR:AUTOMATIC\n" + 
//						"SUMMARY:4I504-Exam2ndeSession-CA\n" + 
//						"DTSTART;TZID=Europe/Paris:20170613T110000\n" + 
//						"DTSTAMP:20170524T124422Z\n" + 
//						"LOCATION:Salle 24.34.207\n" + 
//						"SEQUENCE:0\n" + 
//						"END:VEVENT\n" + 
//						"BEGIN:VEVENT\n" + 
//						"CREATED:20170529T122542Z\n" + 
//						"UID:11A2D7A2-694E-43A0-8F29-49CB1B6BF83A\n" + 
//						"DTEND;TZID=Europe/Paris:20170530T120000\n" + 
//						"TRANSP:OPAQUE\n" + 
//						"X-APPLE-TRAVEL-ADVISORY-BEHAVIOR:AUTOMATIC\n" + 
//						"SUMMARY:4I505-Consultation Copies-CPA\n" + 
//						"DTSTART;TZID=Europe/Paris:20170530T110000\n" + 
//						"DTSTAMP:20170529T122643Z\n" + 
//						"LOCATION:Salle 24.25.405\n" + 
//						"SEQUENCE:0\n" + 
//						"END:VEVENT\n" + 
//						"BEGIN:VEVENT\n" + 
//						"CREATED:20170331T070552Z\n" + 
//						"UID:17A5B6FB-98C9-4B6A-BD95-4139CECE9079\n" + 
//						"DTEND;TZID=Europe/Paris:20170615T103000\n" + 
//						"TRANSP:OPAQUE\n" + 
//						"X-APPLE-TRAVEL-ADVISORY-BEHAVIOR:AUTOMATIC\n" + 
//						"SUMMARY:4I505-Exam2ndeSession-CPA\n" + 
//						"DTSTART;TZID=Europe/Paris:20170615T083000\n" + 
//						"DTSTAMP:20170529T063510Z\n" + 
//						"LOCATION:AMPHI 45B\n" + 
//						"SEQUENCE:0\n" + 
//						"END:VEVENT\n" + 
//						"BEGIN:VEVENT\n" + 
//						"CREATED:20170331T070552Z\n" + 
//						"UID:96B0736B-C2EC-4A29-8B7B-BC76E0755BBB\n" + 
//						"DTEND;TZID=Europe/Paris:20170614T183000\n" + 
//						"TRANSP:OPAQUE\n" + 
//						"X-APPLE-TRAVEL-ADVISORY-BEHAVIOR:AUTOMATIC\n" + 
//						"SUMMARY:4I503-Exam2ndeSession-APS\n" + 
//						"DTSTART;TZID=Europe/Paris:20170614T163000\n" + 
//						"DTSTAMP:20170529T063629Z\n" + 
//						"LOCATION:Salle 24.34.101\n" + 
//						"SEQUENCE:0\n" + 
//						"END:VEVENT\n" + 
//						"BEGIN:VEVENT\n" + 
//						"CREATED:20170208T093148Z\n" + 
//						"UID:FE9B2B6E-DF00-4859-8EE6-E0CB26ACDBB0\n" + 
//						"DTEND;TZID=Europe/Paris:20170516T154500\n" + 
//						"TRANSP:OPAQUE\n" + 
//						"X-APPLE-TRAVEL-ADVISORY-BEHAVIOR:AUTOMATIC\n" + 
//						"SUMMARY:4XAN1-Examen-ANGLAIS\n" + 
//						"DTSTART;TZID=Europe/Paris:20170516T131500\n" + 
//						"DTSTAMP:20170221T084259Z\n" + 
//						"SEQUENCE:0\n" + 
//						"END:VEVENT\n" + 
//						"BEGIN:VTIMEZONE\n" + 
//						"TZID:Europe/Paris\n" + 
//						"BEGIN:DAYLIGHT\n" + 
//						"TZOFFSETFROM:+0100\n" + 
//						"RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=-1SU\n" + 
//						"DTSTART:19810329T020000\n" + 
//						"TZNAME:UTC+2\n" + 
//						"TZOFFSETTO:+0200\n" + 
//						"END:DAYLIGHT\n" + 
//						"BEGIN:STANDARD\n" + 
//						"TZOFFSETFROM:+0200\n" + 
//						"RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU\n" + 
//						"DTSTART:19961027T030000\n" + 
//						"TZNAME:UTC+1\n" + 
//						"TZOFFSETTO:+0100\n" + 
//						"END:STANDARD\n" + 
//						"END:VTIMEZONE\n" + 
//						"END:VCALENDAR\n";
//
//		ICalendar ical = Biweekly.parse(str).first();
//		List<VEvent> events = ical.getEvents();
//		//				VEvent event = ical.getEvents().get(0);
//		//				String summary = event.getSummary().getValue();
//		//				System.out.println(summary);
//
//
//		for (VEvent vEvent : events) {
//			parseUE(vEvent.getSummary().getValue());
////			System.out.println(vEvent.getSummary().getValue());
//			parseStartTime(vEvent.getDateStart().getValue().toString());
////			System.out.println(vEvent.getDateStart().getValue());
//			parseEndTime(vEvent.getDateEnd().getValue().toString());
////			System.out.println(vEvent.getDateEnd().getValue());
//			System.out.println();
//		}




	}
	
	public static void parseUE(String ue) {
		String[] pue = ue.split("-");
		System.out.print("Matière : " + pue[pue.length-1] + " || Code : " + pue[0] +" || Type : " + pue[1]);
		System.out.println("");
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
	
	public static void calCon() {
		try {
			URL url = new URL("https://cal.ufr-info-p6.jussieu.fr/caldav.php/STL/M2_STL");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(30000);
			con.setRequestMethod("GET");
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
			}
			
			in.close();
			
			con.disconnect();

			String calendar = content.toString();
			System.out.println(calendar);
			
			ICalendar ical = Biweekly.parse(calendar).first();
//			System.out.println(ical);
			List<VEvent> events = ical.getEvents();

			for (VEvent vEvent : events) {
				parseUE(vEvent.getSummary().getValue());
//			System.out.println(vEvent.getSummary().getValue());
			parseStartTime(vEvent.getDateStart().getValue().toString());
//			System.out.println(vEvent.getDateStart().getValue());
			parseEndTime(vEvent.getDateEnd().getValue().toString());
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
