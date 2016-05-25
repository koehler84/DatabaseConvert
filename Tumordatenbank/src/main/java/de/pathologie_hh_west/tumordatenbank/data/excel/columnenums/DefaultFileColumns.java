package de.pathologie_hh_west.tumordatenbank.data.excel.columnenums;

public enum DefaultFileColumns {

	EINGANGSDATUM("Spalte A"),
	E_NUMMER(""),
	EINSENDER(""),
	GEBURTSDATUM(""),
	VORNAME(""),
	NAME(""),
	STRASSE(""),
	HAUSNUMMER(""),
	LAND(""),
	PLZ(""),
	ORT(""),
	WHO(""),
	G(""),
	T(""),
	N(""),
	L(""),
	V(""),
	R(""),
	HER2(""),
	HER2_SCORE(""),
	KI_67(""),
	ER(""),
	PR(""),
	REZEPTOREN(""),
	IRS(""),
	TUMORKLASSIFIKATION(""),
	BEFUNDTYP(""),
	DOKUMENTNAME(""),
	BEFUNDTEXT("");
	
	private String title;
	
	private DefaultFileColumns(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public static DefaultFileColumns fromString(String title) {
		switch (title.toLowerCase()) {
		case "eingangsdatum":
			return EINGANGSDATUM;
		case "e.-nummer":
			return E_NUMMER;
		case "einsender":
			return DefaultFileColumns.EINSENDER;
		case "geburtsdatum":
			return DefaultFileColumns.GEBURTSDATUM;
		case "vorname":
			return DefaultFileColumns.VORNAME;
		case "name":
			return DefaultFileColumns.NAME;
		case "strasse": case "straﬂe":
			return DefaultFileColumns.STRASSE;
		case "hausnummer":
			return HAUSNUMMER;
		case "land":
			return LAND;
		case "plz":
			return DefaultFileColumns.PLZ;
		case "ort":
			return ORT;
		case "who":
			return DefaultFileColumns.WHO;
		case "g":
			return DefaultFileColumns.G;
		case "t":
			return T;
		case "n":
			return N;
		case "l":
			return L;
		case "v":
			return V;
		case "r":
			return R;
		case "her2":
			return HER2;
		case "her2 score":
			return DefaultFileColumns.HER2_SCORE;
		case "ki-67":
			return KI_67;
		case "er":
			return DefaultFileColumns.ER;
		case "pr":
			return DefaultFileColumns.PR;
		case "rezeptoren":
			return DefaultFileColumns.REZEPTOREN;
		case "irs":
			return DefaultFileColumns.IRS;
		case "tumorklassifikation":
			return DefaultFileColumns.TUMORKLASSIFIKATION;
		case "befundtyp":
			return DefaultFileColumns.BEFUNDTYP;
		case "dokumentname":
			return DefaultFileColumns.DOKUMENTNAME;
		case "befundtext":
			return BEFUNDTEXT;
		default:
			throw new IllegalArgumentException("Spalte " + title + " konnte nicht gefunden werden.");
		}
	}
	
}
