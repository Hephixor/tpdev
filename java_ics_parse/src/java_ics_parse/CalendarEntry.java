package java_ics_parse;

public class CalendarEntry {
	private String matiere;
	private String code;
	private String type;
	private String location;
	private String startD;
	private String startT;
	private String endD;
	private String endT;
	private boolean suivi;
	// color ?
	
	
	public CalendarEntry(String matiere, String code, String type, String location, String startD, String startT, String endD, String endT) {
		this.matiere=matiere;
		this.code=code;
		this.type=type;
		this.location=location;
		this.startD=startD;
		this.startT=startT;
		this.endD=endD;
		this.endT=endT;
		this.suivi=true;
	}
	
	

	@Override
	public String toString() {
		return "CalendarEntry [matiere=" + matiere + ", code=" + code + ", type=" + type + ", location="+location+", startD=" + startD
				+ ", startT=" + startT + ", endD=" + endD + ", endT=" + endT + ", suivi=" + suivi+"]";
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
}
