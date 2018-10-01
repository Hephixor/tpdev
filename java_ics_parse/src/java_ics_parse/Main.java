package java_ics_parse;
import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;

public class Main {

	public static void main(String[] args) {
		String str =
				"BEGIN:VCALENDAR\r\n" +
				  "VERSION:2.0\r\n" +
				  "PRODID:-//Microsoft Corporation//Outlook 14.0 MIMEDIR//EN\r\n" +
				  "BEGIN:VEVENT\n" + 
				  "TRANSP:OPAQUE\n" + 
				  "DTEND;TZID=Europe/Paris:20170126T154500\n" + 
				  "UID:9500DA8A-755C-4CDC-94A1-431FC1708041\n" + 
				  "EXDATE;TZID=Europe/Paris:20170427T134500\n" + 
				  "EXDATE;TZID=Europe/Paris:20170316T134500\n" + 
				  "EXDATE;TZID=Europe/Paris:20170406T134500\n" + 
				  "EXDATE;TZID=Europe/Paris:20170309T134500\n" + 
				  "EXDATE;TZID=Europe/Paris:20170413T134500\n" + 
				  "DTSTAMP:20170123T081938Z\n" + 
				  "LOCATION:Salle 55.65.103\n" + 
				  "SEQUENCE:0\n" + 
				  "SUMMARY:4I506-TD1-CPS\n" + 
				  "DTSTART;TZID=Europe/Paris:20170126T134500\n" + 
				  "X-APPLE-TRAVEL-ADVISORY-BEHAVIOR:AUTOMATIC\n" + 
				  "CREATED:20161110T092233Z\n" + 
				  "RRULE:FREQ=WEEKLY;INTERVAL=1;UNTIL=20170428T215959Z\n" + 
				  "END:VEVENT\n" +
				"END:VCALENDAR\r\n";

				ICalendar ical = Biweekly.parse(str).first();
				VEvent event = ical.getEvents().get(0);
				String summary = event.getSummary().getValue();
				System.out.println(summary);

	}

}
