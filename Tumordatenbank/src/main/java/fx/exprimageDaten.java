package fx;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class exprimageDaten {
	private String einsenderExcel;
	private String einsenderDB;
	private String eNR;
	private String name;
	private String vorname;
	private Date gebDatum;
	private String erExcel;
	private String erDB;
	private String prExcel;
	private String prDB;
	private String her2NeuExcel;
	private String her2NeuDB;
	private String NotizenExcel;
	private String NotizenDB;
	private String chemoExcel;
	private String chemoDB;
	private int medAntihormonTamoxifenExcel=9999;
	private int medAntihormonTamoxifenDB=9999;
	private Date rDatumExcel;
	private Date rDatumDB;
	private Date rDatum2Excel;
	private Date rDatum2DB;
	private String EE2015StatusExcel;
	private String EE2015StatusDB;
	private Date EE2015DatumExcel;
	private Date EE2015DatumDB;
	private String quelleTodExcel;
	private String quelleTodDB;
	private Date todDatumExcel;
	private Date todDatumDB;
	private String HA_DB;
	private String FA_DB;
	private String ARZT_EXCEL;
	private int PatIDDB;

	public Date getrDatumExcel() {
		return rDatumExcel;
	}
	public void setrDatumExcel(java.util.Date date) {
		this.rDatumExcel = new java.sql.Date(date.getTime());
	}
	public Date getrDatumDB() {
		return rDatumDB;
	}
	public void setrDatumDB(Date rDatumDB) {
		this.rDatumDB = rDatumDB;
	}
	public Date getrDatum2Excel() {
		return rDatum2Excel;
	}
	public void setrDatum2Excel(java.util.Date date) {
		this.rDatum2Excel = new java.sql.Date(date.getTime());;
	}
	public Date getrDatum2DB() {
		return rDatum2DB;
	}
	public void setrDatum2DB(Date rDatum2DB) {
		this.rDatum2DB = rDatum2DB;
	}
	public String getEinsenderExcel() {
		return einsenderExcel;
	}
	public void setEinsenderExcel(String einsenderExcel) {
		this.einsenderExcel = einsenderExcel;
	}
	public String getEinsenderDB() {
		return einsenderDB;
	}
	public void setEinsenderDB(String einsenderDB) {
		this.einsenderDB = einsenderDB;
	}
	public String getErExcel() {
		return erExcel;
	}
	public void setErExcel(String erExcel) {
		this.erExcel = erExcel;
	}
	public String getErDB() {
		return erDB;
	}
	public void setErDB(String erDB) {
		this.erDB = erDB;
	}
	public String getPrExcel() {
		return prExcel;
	}
	public void setPrExcel(String prExcel) {
		this.prExcel = prExcel;
	}
	public String getPrDB() {
		return prDB;
	}
	public void setPrDB(String prDB) {
		this.prDB = prDB;
	}
	public String getHer2NeuDB() {
		return her2NeuDB;
	}
	public void setHer2NeuDB(String her2NeuDB) {
		this.her2NeuDB = her2NeuDB;
	}
	public String getNotizenExcel() {
		return NotizenExcel;
	}
	public void setNotizenExcel(String notizenExcel) {
		NotizenExcel = notizenExcel;
	}
	public String getNotizenDB() {
		return NotizenDB;
	}
	public void setNotizenDB(String notizenDB) {
		NotizenDB = notizenDB;
	}
	public String getChemoExcel() {
		return chemoExcel;
	}
	public void setChemoExcel(int chemoExcel) {
		if (chemoExcel==1){
			this.chemoExcel = "ja";
		} else if (chemoExcel== 0){
			this.chemoExcel = "nein";
		}
	}
	public String getChemoDB() {
		return chemoDB;
	}
	public void setChemoDB(String chemoDB) {
		this.chemoDB = chemoDB;
	}
	public int getMedAntihormonTamoxifenExcel() {
		return medAntihormonTamoxifenExcel;
	}
	public void setMedAntihormonTamoxifenExcel(int medAntihormonTamoxifenExcel) {
		this.medAntihormonTamoxifenExcel = medAntihormonTamoxifenExcel;
	}
	public int getMedAntihormonTamoxifenDB() {
		return medAntihormonTamoxifenDB;
	}
	public void setMedAntihormonTamoxifenDB(int medAntihormonTamoxifenDB) {
		this.medAntihormonTamoxifenDB = medAntihormonTamoxifenDB;
	}
	public String getEE2015StatusExcel() {
		return EE2015StatusExcel;
	}
	public void setEE2015StatusExcel(String eE2015StatusExcel) {
		EE2015StatusExcel = eE2015StatusExcel;
	}
	public String getEE2015StatusDB() {
		return EE2015StatusDB;
	}
	public void setEE2015StatusDB(String eE2015StatusDB) {
		EE2015StatusDB = eE2015StatusDB;
	}
	public Date getEE2015DatumExcel() {
		return EE2015DatumExcel;
	}
	public void setEE2015DatumExcel(java.util.Date date) {
		EE2015DatumExcel = new java.sql.Date(date.getTime());;
	}
	public Date getEE2015DatumDB() {
		return EE2015DatumDB;
	}
	public void setEE2015DatumDB(Date eE2015DatumDB) {
		EE2015DatumDB = eE2015DatumDB;
	}
	public String getQuelleTodExcel() {
		return quelleTodExcel;
	}
	public void setQuelleTodExcel(String quelleTodExcel) {
		this.quelleTodExcel = quelleTodExcel;
	}
	public String getQuelleTodDB() {
		return quelleTodDB;
	}
	public void setQuelleTodDB(String quelleTodDB) {
		this.quelleTodDB = quelleTodDB;
	}
	public Date getTodDatumExcel() {
		return todDatumExcel;
	}
	public void setTodDatumExcel(java.util.Date date) {
		this.todDatumExcel = new java.sql.Date(date.getTime());
	}
	public Date getTodDatumDB() {
		return todDatumDB;
	}
	public void setTodDatumDB(Date todDatumDB) {
		this.todDatumDB = todDatumDB;
	}
	public String getHA_DB() {
		return HA_DB;
	}
	public void setHA_DB(String hA_DB) {
		HA_DB = hA_DB;
	}
	public String getFA_DB() {
		return FA_DB;
	}
	public void setFA_DB(String fA_DB) {
		FA_DB = fA_DB;
	}
	public String getARZT_EXCEL() {
		return ARZT_EXCEL;
	}
	public void setARZT_EXCEL(String aRZT_EXCEL) {
		ARZT_EXCEL = aRZT_EXCEL;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setVorname(String vorname){
		this.vorname=vorname;
	}
	public void setGebDatumSQL(Date gebDatum){
		this.gebDatum=gebDatum;
	}
	public void setGebDatum(java.util.Date date){
		this.gebDatum=new java.sql.Date(date.getTime());
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
	public void seteNR (String eNr){
		//Convertion old ENr to new Format (994/13085 = A/1999/413085)
		String jH;
		if (Integer.parseInt(eNr.substring(0, 2))>20){
			jH = "19";
		} else {
			jH = "20";
		}

		this.eNR = "A/"+jH+eNr.substring(0, 2)+"/"+eNr.substring(2, 3)+eNr.substring(4, eNr.length());
	}
	public String geteNR (){
		return this.eNR;
	}
	public void setHer2NeuExcel(String her2Neu){
		if (her2Neu=="0"){
			this.her2NeuExcel="-";
		} else if (her2Neu=="1+" || her2Neu=="2+" || her2Neu=="3+"){
			this.her2NeuExcel="+";
		} else {
			//TODO Exception
		}
	}
	public String getHer2NeuExcel (){
		return this.her2NeuExcel;
	}
	public List<String> buildStatment() {
		List<String> sqlUpdateStatment = new ArrayList<String>();
		int x=0;
		boolean used = false;
		sqlUpdateStatment.add("update einverstaendnis2011 set ");
		if (rDatumDB==null && rDatumExcel!= null ){
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "einverstaendnis2011.RDatum = "+ rDatumExcel);
			used = true;
		} else if (rDatum2DB==null && rDatum2Excel!= null ){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "einverstaendnis2011.RDatum2 = "+ rDatum2Excel);
		} else if (false){
			//TODO Handling FA, HA, Arzt
		} else if (x==9){
			sqlUpdateStatment.remove(0);
			x=-1;
		}

		sqlUpdateStatment.add("update fall join klassifikation on klassifikation.`Fall_E.-Nummer` = fall.`E.-Nummer` and klassifikation.Fall_Befundtyp = fall.Befundtyp set ");
		used = false;
		x++;
		if (einsenderDB==null && einsenderExcel!= null ){

			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "fall.Einsender = "+ einsenderExcel);
			used =true;
		} else if (erDB==null && erExcel!= null ){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "klassifikation.ER = "+ erExcel);
		} else if (prDB==null && prExcel!= null ){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "klassifikation.PR = "+ prExcel);
		} else if (her2NeuDB==null && her2NeuExcel!= null){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "klassifikation.`Her2/neu` = "+ her2NeuExcel);

		}else if (x==9){
			sqlUpdateStatment.remove(0);
		}





		if (medAntihormonTamoxifenDB == 9999 && medAntihormonTamoxifenExcel != 9999){
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "med_antihormon_tamoxifen = "+ medAntihormonTamoxifenExcel);
		}




		return sqlUpdateStatment;
	}
	public void setPatIDDB(int PatIDDB) {
		this.PatIDDB=PatIDDB;
	}
	public int getPatIDDB(){
		return PatIDDB;
	}
}
