package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import java.util.Arrays;

public class CalendarEntry {
	private String matiere;
	private String code;
	private String type;
	private String location;
	private String startD;
	private String startT;
	private String startM;
	private String startY;
	private String endD;
	private String endT;
	private String endM;
	private String endY;
	private boolean suivi;
	// color ?
	
	
	public CalendarEntry(String matiere, String code, String type, String location,String startY, String startM, String startD, String startT, String endY, String endM, String endD, String endT) {
		this.matiere=matiere;
		this.code=code;
		this.type=type;
		this.location=location;
		this.startY=startY;
		this.startM=startM;
		this.startD=startD;
		this.startT=startT;
		this.endY=endY;
		this.endM=endM;
		this.endD=endD;
		this.endT=endT;
		this.suivi=true;
	}
	
	

	@Override
	public String toString() {
		return matiere + "," + code + "," + type + ","+location+"," + startD+"," + startT + "," + startM+"," + startY + "," + endM + "," + endY + "," + endD + "," + endT + "," + suivi;
	}

	public static int monthAsInt(String month){
		int iMonth;
		switch(month.toUpperCase().substring(0, Math.min(month.length(), 3))){
			case "JAN":
			iMonth = 1;
			break;
			case "FEB":
			iMonth = 2;
			break;
			case "MAR":
			iMonth = 3;
			break;
			case "APR":
			iMonth = 4;
			break;
			case "MAY":
			iMonth = 5;
			break;
			case "JUN":
			iMonth = 6;
			break;
			case "JUL":
			iMonth = 7;
			break;
			case "AUG":
			iMonth = 8;
			break;
			case "SEP":
			iMonth = 9;
			break;
			case "OCT":
			iMonth = 10;
			break;
			case "NOV":
			iMonth = 11;
			break;
			case "DEC":
			iMonth = 12;
			break;
			default:
			iMonth = 0;
			break;

		}

		return iMonth;
	}

	public int getStartY(){
		return Integer.parseInt(startY);
	}

	public String getStartMo(){
		return startM;
	}

	public int getStartH(){
		String[] sHour = this.startT.split(":");
		int hour = Integer.parseInt(sHour[0]);
		return hour;
	}

	public int getStartM(){
		String[] sMinute = this.startT.split(":");
		String ssMinute = Arrays.toString(sMinute);
		if(ssMinute.length()==1){
			ssMinute+="0";
		}
		int minute = Integer.parseInt(sMinute[1]);
		return minute;
	}

	public int getEndY(){
		return Integer.parseInt(endY);
	}

	public String getEndMo(){
		return endM;
	}

	public int getEndH(){
		String[] sHour = this.endT.split(":");
		int hour = Integer.parseInt(sHour[0]);
		return hour;
	}

	public int getEndM(){
		String[] sMinute = this.endT.split(":");
		String ssMinute = Arrays.toString(sMinute);
		if(ssMinute.length()==1){
			ssMinute+="0";
		}
		int minute = Integer.parseInt(sMinute[1]);
		return minute;
	}
	
	public boolean getSuivi() {
		return suivi;
	}
	
	public void setSuivi(boolean suivi) {
		this.suivi = suivi;
	}

	public String getMatiere() {
		return matiere;
	}

	public void setMatiere(String matiere) {
		this.matiere = matiere;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStartD() {
		return startD;
	}

	public void setStartD(String startD) {
		this.startD = startD;
	}

	public String getStartT() {
		return startT;
	}

	public void setStartT(String startT) {
		this.startT = startT;
	}

	public String getEndD() {
		return endD;
	}

	public void setEndD(String endD) {
		this.endD = endD;
	}

	public String getEndT() {
		return endT;
	}

	public void setEndT(String endT) {
		this.endT = endT;
	}

	public String getLocation(){ return this.location ;}

	public void setLocation(String location){ this.location = location; }
}
