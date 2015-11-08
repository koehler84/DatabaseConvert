package tableMasks;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Befundtyp;

/**
 * Class with elements matching the Database table "Fall".
 * @author Eike Wichern
 */
public class Fall {

	String ENummer;
	Befundtyp befundtyp;
	String Arzt;
	LocalDate Eingangsdatum;
	String Einsender;
	LocalDate Geburtsdatum;
	String Vorname;
	String Name;
	
	public Fall(String eNummer, Befundtyp befundtyp, String arzt, LocalDate eingangsdatum, String einsender,
			LocalDate geburtsdatum, String vorname, String name) {
		super();
		this.ENummer = eNummer;
		this.befundtyp = befundtyp;
		Arzt = arzt;
		Eingangsdatum = eingangsdatum;
		Einsender = einsender;
		Geburtsdatum = geburtsdatum;
		Vorname = vorname;
		Name = name;
		
		if (this.Geburtsdatum == null) ;
		if (this.Vorname == null) this.Vorname = "";
		if (this.Name == null) this.Name = "";
		if (this.ENummer == null) this.ENummer = "";
		if (this.befundtyp == null) ;
		if (this.Eingangsdatum == null) ;
		if (this.Einsender == null) this.Einsender = "";
		if (this.Arzt == null) this.Arzt = "";
	}
	
	/**
	 * Use this method to get columns to add or set to a <i>TableView</i>.
	 * @return ArrayList including <i>TableColumns</i>. To set the Columns in the <i>TableView</i> use: <i>table.getColumns().addAll(Fall.getColumns());</i>
	 */
	public static ArrayList<TableColumn<Fall, ?>> getColumns() {
		
		ArrayList<TableColumn<Fall, ?>> columns = new ArrayList<TableColumn<Fall, ?>>();
		
		TableColumn<Fall, LocalDate> col_gebDatum = new TableColumn<Fall, LocalDate>("Geburtsdatum");
		col_gebDatum.setCellValueFactory(new PropertyValueFactory<Fall, LocalDate>("geburtsdatum"));
		TableColumn<Fall, String> col_vorname = new TableColumn<Fall, String>("Vorname");
		col_vorname.setCellValueFactory(new PropertyValueFactory<Fall, String>("vorname"));
		TableColumn<Fall, String> col_name = new TableColumn<Fall, String>("Nachname");
		col_name.setCellValueFactory(new PropertyValueFactory<Fall, String>("name"));
		
		TableColumn<Fall, String> col_eNummer = new TableColumn<Fall, String>("E.-Nummer");
		col_eNummer.setCellValueFactory(new PropertyValueFactory<Fall, String>("ENummer"));
		TableColumn<Fall, Befundtyp> col_befundtyp = new TableColumn<Fall, Befundtyp>("Befundtyp");
		col_befundtyp.setCellValueFactory(new PropertyValueFactory<Fall, Befundtyp>("befundtyp"));
		TableColumn<Fall, String> col_arzt = new TableColumn<Fall, String>("Arzt");
		col_arzt.setCellValueFactory(new PropertyValueFactory<Fall, String>("arzt"));
		TableColumn<Fall, LocalDate> col_eingangsdatum = new TableColumn<Fall, LocalDate>("Eingangsdatum");
		col_eingangsdatum.setCellValueFactory(new PropertyValueFactory<Fall, LocalDate>("eingangsdatum"));
		TableColumn<Fall, String> col_einsender = new TableColumn<Fall, String>("Einsender");
		col_einsender.setCellValueFactory(new PropertyValueFactory<Fall, String>("einsender"));
		
		columns.add(col_eNummer);
		columns.add(col_befundtyp);
		columns.add(col_arzt);
		columns.add(col_eingangsdatum);
		columns.add(col_einsender);
		columns.add(col_gebDatum);
		columns.add(col_vorname);
		columns.add(col_name);
		
		return columns;
	}
	
	public String getENummer() {
		return ENummer;
	}
	
	public void setENummer(String eNummer) {
		this.ENummer = eNummer;
	}
	
	public Befundtyp getBefundtyp() {
		return befundtyp;
	}
	
	public void setBefundtyp(Befundtyp befundtyp) {
		this.befundtyp = befundtyp;
	}
	
	public String getArzt() {
		return Arzt;
	}
	
	public void setArzt(String arzt) {
		Arzt = arzt;
	}
	
	public LocalDate getEingangsdatum() {
		return Eingangsdatum;
	}
	
	public void setEingangsdatum(LocalDate eingangsdatum) {
		Eingangsdatum = eingangsdatum;
	}
	
	public String getEinsender() {
		return Einsender;
	}
	
	public void setEinsender(String einsender) {
		Einsender = einsender;
	}
	
	public LocalDate getGeburtsdatum() {
		return Geburtsdatum;
	}
	
	public void setGeburtsdatum(LocalDate geburtsdatum) {
		Geburtsdatum = geburtsdatum;
	}
	
	public String getVorname() {
		return Vorname;
	}
	
	public void setVorname(String vorname) {
		Vorname = vorname;
	}
	
	public String getName() {
		return Name;
	}
	
	public void setName(String name) {
		Name = name;
	}
	
	
	
}
