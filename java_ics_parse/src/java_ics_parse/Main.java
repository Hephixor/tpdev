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
				  "BEGIN:VEVENT\r\n" +
				    "UID:0123\r\n" +
				    "DTSTAMP:20130601T080000Z\r\n" +
				    "SUMMARY;LANGUAGE=en-us:Team Meeting\r\n" +
				    "DTSTART:20130610T120000Z\r\n" +
				    "DURATION:PT1H\r\n" +
				    "RRULE:FREQ=WEEKLY;INTERVAL=2\r\n" +
				  "END:VEVENT\r\n" +
				"END:VCALENDAR\r\n";

				ICalendar ical = Biweekly.parse(str).first();
				VEvent event = ical.getEvents().get(0);
				String summary = event.getSummary().getValue();
				System.out.println(summary);

	}

}
