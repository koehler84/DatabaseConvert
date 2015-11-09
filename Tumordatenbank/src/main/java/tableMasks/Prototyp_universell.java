package tableMasks;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class Prototyp_universell {

	LocalDate geburtsdatum;
	String vorname;
	String name;
	String ENummer;
	String er;
	String pr;
	String her2_neu;
	String tumorart;
	String t;
	String n;
	String m;
	int g;
	int l;
	int r;
	int v;
	
	public Prototyp_universell(LocalDate geburtsdatum, String vorname, String name, String eNummer, String er,
			String pr, String her2_neu, String tumorart, String t, String n, String m, int g, int l, int r, int v) {
		super();
		this.geburtsdatum = geburtsdatum;
		this.vorname = vorname;
		this.name = name;
		this.ENummer = eNummer;
		this.er = er;
		this.pr = pr;
		this.her2_neu = her2_neu;
		this.tumorart = tumorart;
		this.t = t;
		this.n = n;
		this.m = m;
		this.g = g;
		this.l = l;
		this.r = r;
		this.v = v;
	}
	
	/**
	 * Use this method to get columns to add or set to a <i>TableView</i>.
	 * @return ArrayList including <i>TableColumns</i>. To set the Columns in the <i>TableView</i> use: 
	 * <i>table.getColumns().addAll(Prototyp_universell.getColumns());</i>
	 */
	public static ArrayList<TableColumn<Prototyp_universell, ?>> getColumns() {
		
		ArrayList<TableColumn<Prototyp_universell, ?>> columns = new ArrayList<TableColumn<Prototyp_universell, ?>>();
		
		TableColumn<Prototyp_universell, LocalDate> col_gebDatum = new TableColumn<Prototyp_universell, LocalDate>("Geburtsdatum");
		col_gebDatum.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, LocalDate>("geburtsdatum"));
		TableColumn<Prototyp_universell, String> col_vorname = new TableColumn<Prototyp_universell, String>("Vorname");
		col_vorname.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, String>("vorname"));
		TableColumn<Prototyp_universell, String> col_name = new TableColumn<Prototyp_universell, String>("Nachname");
		col_name.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, String>("name"));
		TableColumn<Prototyp_universell, String> col_ENummer = new TableColumn<Prototyp_universell, String>("E.-Nummer");
		col_ENummer.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, String>("ENummer"));
		TableColumn<Prototyp_universell, String> col_er = new TableColumn<Prototyp_universell, String>("Östrogen Rezeptor");
		col_er.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, String>("er"));
		TableColumn<Prototyp_universell, String> col_pr = new TableColumn<Prototyp_universell, String>("Progesteron Rezeptor");
		col_pr.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, String>("pr"));
		TableColumn<Prototyp_universell, String> col_her2_neu = new TableColumn<Prototyp_universell, String>("Her2/neu");
		col_her2_neu.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, String>("her2_neu"));
		TableColumn<Prototyp_universell, String> col_tumorart = new TableColumn<Prototyp_universell, String>("Tumorart");
		col_tumorart.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, String>("tumorart"));
		TableColumn<Prototyp_universell, String> col_T = new TableColumn<Prototyp_universell, String>("T");
		col_T.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, String>("t"));
		TableColumn<Prototyp_universell, String> col_N = new TableColumn<Prototyp_universell, String>("N");
		col_N.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, String>("n"));
		TableColumn<Prototyp_universell, String> col_M = new TableColumn<Prototyp_universell, String>("M");
		col_M.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, String>("m"));
		TableColumn<Prototyp_universell, String> col_G = new TableColumn<Prototyp_universell, String>("G");
		col_G.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, String>("g"));
		TableColumn<Prototyp_universell, String> col_L = new TableColumn<Prototyp_universell, String>("L");
		col_L.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, String>("l"));
		TableColumn<Prototyp_universell, String> col_R = new TableColumn<Prototyp_universell, String>("R");
		col_R.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, String>("r"));
		TableColumn<Prototyp_universell, String> col_V = new TableColumn<Prototyp_universell, String>("V");
		col_V.setCellValueFactory(new PropertyValueFactory<Prototyp_universell, String>("v"));
		
		columns.add(col_gebDatum);
		columns.add(col_vorname);
		columns.add(col_name);
		columns.add(col_ENummer);
		columns.add(col_er);
		columns.add(col_pr);
		columns.add(col_her2_neu);
		columns.add(col_tumorart);
		columns.add(col_T);
		columns.add(col_N);
		columns.add(col_M);
		columns.add(col_G);
		columns.add(col_L);
		columns.add(col_R);
		columns.add(col_V);
		
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
	public String getENummer() {
		return ENummer;
	}
	public void setENummer(String eNummer) {
		ENummer = eNummer;
	}
	public String getEr() {
		return er;
	}
	public void setEr(String er) {
		this.er = er;
	}
	public String getPr() {
		return pr;
	}
	public void setPr(String pr) {
		this.pr = pr;
	}
	public String getHer2_neu() {
		return her2_neu;
	}
	public void setHer2_neu(String her2_neu) {
		this.her2_neu = her2_neu;
	}
	public String getTumorart() {
		return tumorart;
	}
	public void setTumorart(String tumorart) {
		this.tumorart = tumorart;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getN() {
		return n;
	}
	public void setN(String n) {
		this.n = n;
	}
	public String getM() {
		return m;
	}
	public void setM(String m) {
		this.m = m;
	}
	public int getG() {
		return g;
	}
	public void setG(int g) {
		this.g = g;
	}
	public int getL() {
		return l;
	}
	public void setL(int l) {
		this.l = l;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	public int getV() {
		return v;
	}
	public void setV(int v) {
		this.v = v;
	}
	
}
