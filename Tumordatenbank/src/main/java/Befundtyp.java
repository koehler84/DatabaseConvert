
public enum Befundtyp {
	
	Hauptbefund(0),
	Nachbericht_1(1),
	Nachbericht_2(2),
	Korrekturbefund_1(3),
	Korrekturbefund_2(4),
	Korrekturbefund_3(5),
	Konsiliarbericht_1(6),
	Fehler(9);
	private int value;
		
	Befundtyp (int val) {
		this.value = val;
	}
	
	public int getValue() {
		return this.value;
	}
	
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
