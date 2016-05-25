package de.pathologie_hh_west.tumordatenbank.data.excel.columnenums;

public enum TestFileColumns {

	SPALTE_A("Spalte A"),
	SPALTE_B("Spalte B"),
	SPALTE_MAX("Spalte MAX");
	
	private String title;
	
	private TestFileColumns(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public static TestFileColumns fromString(String title) {
		switch (title.toLowerCase()) {
		case "spaltea":
			return SPALTE_A;
		case "spalteb":
			return SPALTE_B;
		case "spalte max":
			return SPALTE_MAX;
		default:
			throw new IllegalArgumentException("Spalte " + title + " konnte nicht gefunden werden.");
		}
	}
	
}
