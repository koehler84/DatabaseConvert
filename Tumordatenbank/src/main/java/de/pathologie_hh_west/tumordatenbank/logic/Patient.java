package de.pathologie_hh_west.tumordatenbank.logic;

import de.pathologie_hh_west.tumordatenbank.data.sql.DataAccessException;
import de.pathologie_hh_west.tumordatenbank.data.sql.PatientDAO;

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
	
//	public Patient(TransferObject transferObject) {
//		
//	}
	
	@SuppressWarnings("unused")
	private void save() throws DataAccessException {
		PatientDAO dao = new PatientDAO();
		dao.upsert(this);
	}

	public String getGeburtsdatum() {
		return geburtsdatum;
	}

	public String getVorname() {
		return vorname;
	}

	public String getName() {
		return name;
	}

	public String getStrasse() {
		return strasse;
	}

	public String getHausnummer() {
		return hausnummer;
	}

	public String getLand() {
		return land;
	}

	public String getPlz() {
		return plz;
	}

	public String getOrt() {
		return ort;
	}
	
}
