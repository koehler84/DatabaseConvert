package tableMasks;

import java.sql.Date;
import java.util.ArrayList;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Befundtyp;

public class Fall {

	String ENummer;
	Befundtyp befundtyp;
	String Arzt;
	Date Eingangsdatum;
	String Einsender;
	Date Geburtsdatum;
	String Vorname;
	String Name;
	
	public Fall(String eNummer, Befundtyp befundtyp, String arzt, Date eingangsdatum, String einsender,
			Date geburtsdatum, String vorname, String name) {
		super();
		this.ENummer = eNummer;
		this.befundtyp = befundtyp;
		Arzt = arzt;
		Eingangsdatum = eingangsdatum;
		Einsender = einsender;
		Geburtsdatum = geburtsdatum;
		Vorname = vorname;
		Name = name;
	}
	
	public static ArrayList<TableColumn<Fall, ?>> getColumns() {
		
		ArrayList<TableColumn<Fall, ?>> columns = new ArrayList<TableColumn<Fall, ?>>();
		
		TableColumn<Fall, Date> col_gebDatum = new TableColumn<Fall, Date>("Geburtsdatum");
		col_gebDatum.setCellValueFactory(new PropertyValueFactory<Fall, Date>("geburtsdatum"));
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
		TableColumn<Fall, Date> col_eingangsdatum = new TableColumn<Fall, Date>("Eingangsdatum");
		col_eingangsdatum.setCellValueFactory(new PropertyValueFactory<Fall, Date>("eingangsdatum"));
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
	
	public Date getEingangsdatum() {
		return Eingangsdatum;
	}
	
	public void setEingangsdatum(Date eingangsdatum) {
		Eingangsdatum = eingangsdatum;
	}
	
	public String getEinsender() {
		return Einsender;
	}
	
	public void setEinsender(String einsender) {
		Einsender = einsender;
	}
	
	public Date getGeburtsdatum() {
		return Geburtsdatum;
	}
	
	public void setGeburtsdatum(Date geburtsdatum) {
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
