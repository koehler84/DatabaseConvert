package tableMasks;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Class with elements matching the Database table "Patientendaten".
 * @author Eike Wichern
 */
public class Patientendaten {

	LocalDate geburtsdatum;
	String vorname;
	String name;
	String strasse;
	String hausnummer;
	String land;
	String plz;
	String ort;
	
	public Patientendaten(LocalDate geburtsdatum, String vorname, String name) {
		this.geburtsdatum = geburtsdatum;
		this.vorname = vorname;
		this.name = name;
	}
	
	public Patientendaten(LocalDate geburtsdatum, String vorname, String name, String strasse, String hausnummer,
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
		
		if (this.geburtsdatum == null) ;
		if (this.vorname == null) this.vorname = "";
		if (this.name == null) this.name = "";
		if (this.strasse == null) this.strasse = "";
		if (this.hausnummer == null) this.hausnummer = "";
		if (this.land == null) this.land = "";
		if (this.plz == null) this.plz = "";
		if (this.ort == null) this.ort = "";
	}
	
	/**
	 * Use this method to get columns to add or set to a <i>TableView</i>.
	 * @return ArrayList including <i>TableColumns</i>. To set the Columns in the <i>TableView</i> use: <i>table.getColumns().addAll(Patientendaten.getColumns());</i>
	 */
	public static ArrayList<TableColumn<Patientendaten, ?>> getColumns() {
		
		ArrayList<TableColumn<Patientendaten, ?>> columns = new ArrayList<TableColumn<Patientendaten, ?>>();
		
		TableColumn<Patientendaten, LocalDate> col_gebDatum = new TableColumn<Patientendaten, LocalDate>("Geburtsdatum");
		col_gebDatum.setCellValueFactory(new PropertyValueFactory<Patientendaten, LocalDate>("geburtsdatum"));
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

	public LocalDate getGeburtsdatum() {
		return geburtsdatum;
	}

	public void setGeburtsdatum(LocalDate geburtsdatum) {
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
