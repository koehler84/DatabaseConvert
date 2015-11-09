package tableMasks;

import java.time.LocalDate;

public class Prototyp_universell {

	LocalDate geburtsdatum;
	String vorname;
	String name;
	String ENummer;
	String er;
	String pr;
	String her2_neu;
	String tumorart;
	String T;
	String N;
	String M;
	int G;
	int L;
	int R;
	int V;
	
	public Prototyp_universell(LocalDate geburtsdatum, String vorname, String name, String eNummer, String er,
			String pr, String her2_neu, String tumorart, String t, String n, String m, int g, int l, int r, int v) {
		super();
		this.geburtsdatum = geburtsdatum;
		this.vorname = vorname;
		this.name = name;
		ENummer = eNummer;
		this.er = er;
		this.pr = pr;
		this.her2_neu = her2_neu;
		this.tumorart = tumorart;
		T = t;
		N = n;
		M = m;
		G = g;
		L = l;
		R = r;
		V = v;
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
		return T;
	}
	public void setT(String t) {
		T = t;
	}
	public String getN() {
		return N;
	}
	public void setN(String n) {
		N = n;
	}
	public String getM() {
		return M;
	}
	public void setM(String m) {
		M = m;
	}
	public int getG() {
		return G;
	}
	public void setG(int g) {
		G = g;
	}
	public int getL() {
		return L;
	}
	public void setL(int l) {
		L = l;
	}
	public int getR() {
		return R;
	}
	public void setR(int r) {
		R = r;
	}
	public int getV() {
		return V;
	}
	public void setV(int v) {
		V = v;
	}
	
}
