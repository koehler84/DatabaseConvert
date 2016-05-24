package de.pathologie_hh_west.tumordatenbank.logic;

public class Patient {

	private String geburtsdatum;
	private String vorname;
	private String name;
	private String strasse;
	private String hausnummer;
	private String land;
	private String plz;
	private String ort;
	
	public Patient(String geburtsdatum, String vorname, String name, String strasse, String hausnummer, String land,
			String plz, String ort) {
		super();
		this.geburtsdatum = geburtsdatum;
		this.vorname = vorname;
		this.name = name;
		this.strasse = strasse;
		this.hausnummer = hausnummer;
		this.land = land;
		this.plz = plz;
		this.ort = ort;
	}
	
//	public Patient(TransferObject) {
//		
//	}
	
	private void save() {
		//TODO
	}
	
	
	
}
