package tableMasks;

import java.time.LocalDate;

public abstract class Person {

	private LocalDate geburtsdatum;
	private String vorname;
	private String name;
	
	public Person(LocalDate geburtsdatum, String vorname, String name) {
		this.geburtsdatum = geburtsdatum;
		this.vorname = vorname;
		this.name = name;
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
}
