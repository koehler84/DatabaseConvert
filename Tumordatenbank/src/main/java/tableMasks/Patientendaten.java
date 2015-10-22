package tableMasks;

public class Patientendaten {

	String geburtsdatum;
	String vorname;
	String name;
//	String strasse;
//	String hausnummer;
//	String land;
//	String plz;
//	String Ort;
	
	public Patientendaten(String geburtsdatum, String vorname, String name) {
		this.geburtsdatum = geburtsdatum;
		this.vorname = vorname;
		this.name = name;
	}

	public String getGeburtsdatum() {
		return geburtsdatum;
	}

	public void setGeburtsdatum(String geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
