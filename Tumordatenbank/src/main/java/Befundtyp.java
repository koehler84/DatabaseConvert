
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
	
}
