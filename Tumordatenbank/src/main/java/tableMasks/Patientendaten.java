package tableMasks;

import java.util.ArrayList;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class Patientendaten {

	String geburtsdatum;
	String vorname;
	String name;
	String strasse;
	String hausnummer;
	String land;
	String plz;
	String ort;
	
	public Patientendaten(String geburtsdatum, String vorname, String name) {
		this.geburtsdatum = geburtsdatum;
		this.vorname = vorname;
		this.name = name;
	}
	
	public Patientendaten(String geburtsdatum, String vorname, String name, String strasse, String hausnummer,
			String land, String plz, String ort) {
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

	public static ArrayList<TableColumn<Patientendaten, ?>> getColumns() {
		
		ArrayList<TableColumn<Patientendaten, ?>> columns = new ArrayList<TableColumn<Patientendaten, ?>>();
		
		TableColumn<Patientendaten, String> col_gebDatum = new TableColumn<Patientendaten, String>("Geburtsdatum");
		col_gebDatum.setCellValueFactory(new PropertyValueFactory<Patientendaten, String>("geburtsdatum"));
		TableColumn<Patientendaten, String> col_vorname = new TableColumn<Patientendaten, String>("Vorname");
		col_vorname.setCellValueFactory(new PropertyValueFactory<Patientendaten, String>("vorname"));
		TableColumn<Patientendaten, String> col_name = new TableColumn<Patientendaten, String>("Nachname");
		col_name.setCellValueFactory(new PropertyValueFactory<Patientendaten, String>("name"));
		TableColumn<Patientendaten, String> col_strasse = new TableColumn<Patientendaten, String>("Straﬂe");
		col_strasse.setCellValueFactory(new PropertyValueFactory<Patientendaten, String>("strasse"));
		TableColumn<Patientendaten, String> col_hausnummer = new TableColumn<Patientendaten, String>("Hausnummer");
		col_hausnummer.setCellValueFactory(new PropertyValueFactory<Patientendaten, String>("hausnummer"));
		TableColumn<Patientendaten, String> col_land = new TableColumn<Patientendaten, String>("Land");
		col_land.setCellValueFactory(new PropertyValueFactory<Patientendaten, String>("land"));
		TableColumn<Patientendaten, String> col_plz = new TableColumn<Patientendaten, String>("PLZ");
		col_plz.setCellValueFactory(new PropertyValueFactory<Patientendaten, String>("plz"));
		TableColumn<Patientendaten, String> col_ort = new TableColumn<Patientendaten, String>("Ort");
		col_ort.setCellValueFactory(new PropertyValueFactory<Patientendaten, String>("ort"));
		
		columns.add(col_gebDatum);
		columns.add(col_vorname);
		columns.add(col_name);
		columns.add(col_strasse);
		columns.add(col_hausnummer);
		columns.add(col_land);
		columns.add(col_plz);
		columns.add(col_ort);
		
		return columns;
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

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getHausnummer() {
		return hausnummer;
	}

	public void setHausnummer(String hausnummer) {
		this.hausnummer = hausnummer;
	}

	public String getLand() {
		return land;
	}

	public void setLand(String land) {
		this.land = land;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}
	
}
