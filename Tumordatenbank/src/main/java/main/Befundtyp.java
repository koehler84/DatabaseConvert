package main;

/**
 * Enum contains possible values for <i>Befundtyp</i> inside the windows' panels. <i>Befundtyp</i> is stored as <i>Integer</i> inside the Database.
 * The class helps transition between <i>Integer</i> and <i>String</i> values.
 * @author Eike Wichern
 */
public enum Befundtyp {
	
	Hauptbefund(0),
	Nachbericht_1(1),
	Nachbericht_2(2),
	Korrekturbefund_1(3),
	Korrekturbefund_2(4),
	Korrekturbefund_3(5),
	Konsiliarbericht_1(6),
	Unbekannt(7),
	Fehler(9);
	private int value;
		
	Befundtyp (int val) {
		this.value = val;
	}
	
	/**
	 * @return <i>Integer</i> value from a certain <i>Befundtyp</i>-Element.
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * Method returns a <i>Befundtyp</i>-Element for a given <i>int</i>-value.
	 * @param a <i>Integer</i>-value you want to convert to <i>Befundtyp</i>. 
	 * @return <i>Befundtyp</i> that matches given <i>int</i>-value.
	 */
	public static Befundtyp getBefundtyp(int a) {
		
		switch (a) {
		case 0:
			return Befundtyp.Hauptbefund;
		case 1:
			return Befundtyp.Nachbericht_1;
		case 2:
			return Befundtyp.Nachbericht_2;
		case 3:
			return Befundtyp.Korrekturbefund_1;
		case 4:
			return Befundtyp.Korrekturbefund_2;
		case 5:
			return Befundtyp.Korrekturbefund_3;
		case 6:
			return Befundtyp.Konsiliarbericht_1;
		case 9:
			return Befundtyp.Fehler;
		}
		
		return null;
	}
	
	/**
	 * Method returns a <i>Befundtyp</i>-Element for a given <i>String</i>.
	 * @param a <i>String</i> you want to convert to <i>Befundtyp</i>. 
	 * @return <i>Befundtyp</i> that matches given <i>String</i>.
	 */
	public static Befundtyp getBefundtyp(String a) {
		
		if (a == null) a = "Fehler";
		
		switch (a) {
		case "Hauptbefund":
			return Befundtyp.Hauptbefund;
		case "Nachbericht_1":
			return Befundtyp.Nachbericht_1;
		case "Nachbericht_2":
			return Befundtyp.Nachbericht_2;
		case "Korrekturbefund_1":
			return Befundtyp.Korrekturbefund_1;
		case "Korrekturbefund_2":
			return Befundtyp.Korrekturbefund_2;
		case "Korrekturbefund_3":
			return Befundtyp.Korrekturbefund_3;
		case "Konsiliarbericht_1":
			return Befundtyp.Konsiliarbericht_1;
		case "Fehler":
			return Befundtyp.Fehler;
		}
		
		return null;
	}
	
}
