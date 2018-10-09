package java_ics_parse;

import java.net.URISyntaxException;
import java.util.ArrayList;


public class Main {

	public static void main(String[] args) {
		String urls = new String("https://cal.ufr-info-p6.jussieu.fr/caldav.php/STL/M1_STL");	

		CalendarMaker cm = new CalendarMaker(urls);
		ArrayList<CalendarEntry> cal = cm.getEventsEntries(cm.getEventCollection());
		System.out.println("created cal with collection size "+cal.size());
		for (CalendarEntry calendarEntry : cal) {
			System.out.println(calendarEntry.toString());
		}
//		
//		//cm.caldown();
		
		
	}

}
