package fx;

import java.sql.Date;

public class exprimageDaten {
	String einsenderExcel;
	String einsenderDB;
	private String eNRExcel;
	private String name;
	private String vorname;
	private Date gebDatum;
	String erExcel;
	String erDB;
	String prExcel;
	String prDB;
	String her2NeuExcel;
	String her2NeuDB;
	String NotizenExcel;
	String NotizenDB;
	String chemoExcel;
	String chemoDB;
	int medAntihormonTamoxifenExcel=5;
	int medAntihormonTamoxifenDB=5;
	Date rDatum;
	Date rDatum2;
	String EE2015StatusExcel;
	String EE2015StatusDB;
	Date EE2015DatumExcel;
	Date EE2015DatumDB;
	String quelleTodExcel;
	String quelleTodDB;
	Date todDatumExcel;
	Date todDatumDB;
	String HA_DB;
	String FA_DB;
	String ARZT_EXCEL;
	
	public void setName(String name){
		this.name=name;
	}
	public void setVornameame(String vorname){
		this.vorname=vorname;
	}
	public void setGebDatumSQL(Date gebDatum){
		this.gebDatum=gebDatum;
	}
	public void setGebDatum(java.util.Date gebDatum){
		this.gebDatum=new java.sql.Date(gebDatum.getTime());
	}
	public String getName(){
		return this.name;
	}
	public String getVorname(){
		return this.vorname;
	}
	public Date getGebDatum(){
		return this.gebDatum;
	}
	
}
