package tableMasks;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Class with elements matching the Database table "Patientendaten".
 * @author Eike Wichern
 */
public class Patient extends Person {

	String strasse;
	String hausnummer;
	String land;
	String plz;
	String ort;
	
	public Patient(LocalDate geburtsdatum, String vorname, String name) {
		super(geburtsdatum, vorname, name);
	}
	
	public Patient(LocalDate geburtsdatum, String vorname, String name, String strasse, String hausnummer,
			String land, String plz, String ort) {
		super(geburtsdatum, vorname, name);
		this.strasse = strasse;
		this.hausnummer = hausnummer;
		this.land = land;
		this.plz = plz;
		this.ort = ort;
		
		if (this.getGeburtsdatum() == null) ;
		if (this.getVorname() == null) this.setVorname("");
		if (this.getName() == null) this.setName("");
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
	public static ArrayList<TableColumn<Patient, ?>> getColumns() {
		
		ArrayList<TableColumn<Patient, ?>> columns = new ArrayList<TableColumn<Patient, ?>>();
		
		TableColumn<Patient, LocalDate> col_gebDatum = new TableColumn<Patient, LocalDate>("Geburtsdatum");
		col_gebDatum.setCellValueFactory(new PropertyValueFactory<Patient, LocalDate>("geburtsdatum"));
		TableColumn<Patient, String> col_vorname = new TableColumn<Patient, String>("Vorname");
		col_vorname.setCellValueFactory(new PropertyValueFactory<Patient, String>("vorname"));
		TableColumn<Patient, String> col_name = new TableColumn<Patient, String>("Nachname");
		col_name.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
		TableColumn<Patient, String> col_strasse = new TableColumn<Patient, String>("Straﬂe");
		col_strasse.setCellValueFactory(new PropertyValueFactory<Patient, String>("strasse"));
		TableColumn<Patient, String> col_hausnummer = new TableColumn<Patient, String>("Hausnummer");
		col_hausnummer.setCellValueFactory(new PropertyValueFactory<Patient, String>("hausnummer"));
		TableColumn<Patient, String> col_land = new TableColumn<Patient, String>("Land");
		col_land.setCellValueFactory(new PropertyValueFactory<Patient, String>("land"));
		TableColumn<Patient, String> col_plz = new TableColumn<Patient, String>("PLZ");
		col_plz.setCellValueFactory(new PropertyValueFactory<Patient, String>("plz"));
		TableColumn<Patient, String> col_ort = new TableColumn<Patient, String>("Ort");
		col_ort.setCellValueFactory(new PropertyValueFactory<Patient, String>("ort"));
		
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
