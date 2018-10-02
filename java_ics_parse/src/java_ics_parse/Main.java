package java_ics_parse;

public class Main {

	public static void main(String[] args) {
		String urls = new String("https://cal.ufr-info-p6.jussieu.fr/caldav.php/STL/M1_STL");
		
		
		
		CalendarMaker cm = new CalendarMaker(urls);
		
		cm.calCon();
		cm.caldown();
		
		
	}

}
