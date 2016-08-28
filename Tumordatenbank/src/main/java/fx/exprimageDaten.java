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
	private String her2NeuScoreExcel;
	private String her2NeuScoreDB;
	private String NotizenExcel;
	//	private String NotizenDB;
	private String chemoExcel;
	//	private String chemoDB;
	private int medAntihormonTamoxifenExcel=9999;
	//	private int medAntihormonTamoxifenDB=9999;
	private Date rDatumExcel;
	//	private Date rDatumDB;
	private Date rDatum2Excel;
	//	private Date rDatum2DB;
	private String EE2015StatusExcel;
	//	private String EE2015StatusDB;
	private Date EE2015DatumExcel;
	//	private Date EE2015DatumDB;
	private String quelleTodExcel;
	//	private String quelleTodDB;
	private Date todDatumExcel;
	//	private Date todDatumDB;
	//	private String HA_DB;
	//	private String FA_DB;
	private String ARZT_EXCEL;
	private int PatIDDB;
	private String Notizen2Excel;
	private String GExcel;
	private String TExcel;
	private String NExcel;
	private String MExcel;
	private String LExcel;
	private String VExcel;
	private String RExcel;
	private String GDB;
	private String TDB;
	private String NDB;
	private String MDB;
	private String LDB;
	private String VDB;
	private String RDB;
	private String medAntihormonAromataseHExcel;
	private String bestrahlung;
	private String expFU;


	public String getMedAntihormonAromataseHExcel() {
		if (medAntihormonAromataseHExcel==null){
			return null;
		} else {
			return "'"+medAntihormonAromataseHExcel.replace("'", "''")+"'";
		}
	}
	public void setMedAntihormonAromataseHExcel(String medAntihormonAromataseHExcel) {
		this.medAntihormonAromataseHExcel = medAntihormonAromataseHExcel;
	}
	public String getGExcel() {
		return GExcel;
	}
	public void setGExcel(String gExcel) {
		GExcel = gExcel;
	}
	public String getTExcel() {
		return TExcel;
	}
	public void setTExcel(String tExcel) {
		TExcel = tExcel;
	}
	public String getNExcel() {
		return NExcel;
	}
	public void setNExcel(String nExcel) {
		NExcel = nExcel;
	}
	public String getMExcel() {
		return MExcel;
	}
	public void setMExcel(String mExcel) {
		MExcel = mExcel;
	}
	public String getLExcel() {
		return LExcel;
	}
	public void setLExcel(String lExcel) {
		LExcel = lExcel;
	}
	public String getVExcel() {
		return VExcel;

	}
	public void setVExcel(String vExcel) {
		VExcel = vExcel;
	}
	public String getRExcel() {
		return RExcel;
	}
	public void setRExcel(String rExcel) {
		RExcel = rExcel;
	}
	public String getGDB() {
		return GDB;
	}
	public void setGDB(String gDB) {
		GDB = gDB;
	}
	public String getTDB() {
		return TDB;
	}
	public void setTDB(String tDB) {
		TDB = tDB;
	}
	public String getNDB() {
		return NDB;
	}
	public void setNDB(String nDB) {
		NDB = nDB;
	}
	public String getMDB() {
		return MDB;
	}
	public void setMDB(String mDB) {
		MDB = mDB;
	}
	public String getLDB() {
		return LDB;
	}
	public void setLDB(String lDB) {
		LDB = lDB;
	}
	public String getVDB() {
		return VDB;
	}
	public void setVDB(String vDB) {
		VDB = vDB;
	}
	public String getRDB() {
		return RDB;
	}
	public void setRDB(String rDB) {
		RDB = rDB;
	}
	public String getNotizen2Excel() {
		if (Notizen2Excel==null){
			return null;
		} else {
			return "'"+Notizen2Excel.replace("'", "''")+"'";
		}
	}
	public void setNotizen2Excel(String notizen2Excel) {
		Notizen2Excel = notizen2Excel;
	}
	public String getrDatumExcel() {
		if (rDatumExcel==null){
			return null;
		} else {
			return "'"+rDatumExcel+"'";
		}
	}
	public void setrDatumExcel(java.util.Date date) {
		this.rDatumExcel = new java.sql.Date(date.getTime());
	}
	//	public Date getrDatumDB() {
	//		return rDatumDB;
	//	}
	//	public void setrDatumDB(Date rDatumDB) {
	//		this.rDatumDB = rDatumDB;
	//	}
	public String getrDatum2Excel() {
		if (rDatum2Excel==null){
			return null;
		} else {
			return "'"+rDatum2Excel+"'";
		}
	}
	public void setrDatum2Excel(java.util.Date date) {
		this.rDatum2Excel = new java.sql.Date(date.getTime());
	}
	//	public Date getrDatum2DB() {
	//		return rDatum2DB;
	//	}
	//	public void setrDatum2DB(Date rDatum2DB) {
	//		this.rDatum2DB = rDatum2DB;
	//	}
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
	public String getHer2NeuScoreDB() {
		return her2NeuScoreDB;
	}
	public void setHer2NeuScoreDB(String her2NeuScoreDB) {
		this.her2NeuScoreDB = her2NeuScoreDB;
	}
	public String getNotizenExcel() {
		if (NotizenExcel==null){
			return null;
		} else {
			return "'"+NotizenExcel.replace("'", "''")+"'";
		}
	}
	public void setNotizenExcel(String notizenExcel) {
		NotizenExcel = notizenExcel;
	}
	//	public String getNotizenDB() {
	//		return NotizenDB;
	//	}
	//	public void setNotizenDB(String notizenDB) {
	//		NotizenDB = notizenDB.replace("'", "''");
	//	}
	public String getChemoExcel() {
		if (chemoExcel==null){
			return null;
		} else {
			return "'"+chemoExcel+"'";
		}
	}
	public void setChemoExcel(int chemoExcel) {
		if (chemoExcel==1){
			this.chemoExcel = "ja";
		} else if (chemoExcel== 0){
			this.chemoExcel = "nein";
		}
	}
	//	public String getChemoDB() {
	//		return chemoDB;
	//	}
	//	public void setChemoDB(String chemoDB) {
	//		this.chemoDB = chemoDB;
	//	}
	public Integer getMedAntihormonTamoxifenExcel() {
		if (medAntihormonTamoxifenExcel==9999){
			return null;
		} else {
			return medAntihormonTamoxifenExcel;
		}
	}
	public void setMedAntihormonTamoxifenExcel(int medAntihormonTamoxifenExcel) {
		this.medAntihormonTamoxifenExcel = medAntihormonTamoxifenExcel;
	}
	//	public int getMedAntihormonTamoxifenDB() {
	//		return medAntihormonTamoxifenDB;
	//	}
	//	public void setMedAntihormonTamoxifenDB(int medAntihormonTamoxifenDB) {
	//		this.medAntihormonTamoxifenDB = medAntihormonTamoxifenDB;
	//	}
	public String getEE2015StatusExcel() {
		if (EE2015StatusExcel==null){
			return null;
		} else {
			return "'"+EE2015StatusExcel.replace("'", "''")+"'";
		}
	}
	public void setEE2015StatusExcel(String eE2015StatusExcel) {
		EE2015StatusExcel = eE2015StatusExcel;
	}
	//	public String getEE2015StatusDB() {
	//		return EE2015StatusDB;
	//	}
	//	public void setEE2015StatusDB(String eE2015StatusDB) {
	//		EE2015StatusDB = eE2015StatusDB;
	//	}
	public String getEE2015DatumExcel() {
		if (EE2015DatumExcel==null){
			return null;
		} else {
			return "'"+EE2015DatumExcel+"'";
		}
	}
	public void setEE2015DatumExcel(java.util.Date date) {
		EE2015DatumExcel = new java.sql.Date(date.getTime());;
	}
	//	public Date getEE2015DatumDB() {
	//		return EE2015DatumDB;
	//	}
	//	public void setEE2015DatumDB(Date eE2015DatumDB) {
	//		EE2015DatumDB = eE2015DatumDB;
	//	}
	public String getQuelleTodExcel() {
		if (quelleTodExcel==null){
			return null;
		} else {
			return "'"+quelleTodExcel.replace("'", "''")+"'";
		}
	}
	public void setQuelleTodExcel(String quelleTodExcel) {
		this.quelleTodExcel = quelleTodExcel;
	}
	//	public String getQuelleTodDB() {
	//		return quelleTodDB;
	//	}
	//	public void setQuelleTodDB(String quelleTodDB) {
	//		this.quelleTodDB = quelleTodDB;
	//	}
	public String getTodDatumExcel() {
		if (todDatumExcel==null){
			return null;
		} else {
			return "'"+todDatumExcel+"'";
		}
	}
	public void setTodDatumExcel(java.util.Date date) {
		this.todDatumExcel = new java.sql.Date(date.getTime());
	}
	//	public Date getTodDatumDB() {
	//		return todDatumDB;
	//	}
	//	public void setTodDatumDB(Date todDatumDB) {
	//		this.todDatumDB = todDatumDB;
	//	}
	//	public String getHA_DB() {
	//		return HA_DB;
	//	}
	//	public void setHA_DB(String hA_DB) {
	//		HA_DB = hA_DB;
	//	}
	//	public String getFA_DB() {
	//		return FA_DB;
	//	}
	//	public void setFA_DB(String fA_DB) {
	//		FA_DB = fA_DB;
	//	}
	public String getARZT_EXCEL() {
		if (ARZT_EXCEL==null){
			return null;
		} else {
			return "'"+ARZT_EXCEL.replace("'", "''")+"'";
		}
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
		if (name==null){
			return null;
		} else {
			return "'"+this.name.replace("'", "''")+"'";
		}
	}
	public String getVorname(){
		if (vorname==null){
			return null;
		} else {
			return "'"+this.vorname.replace("'", "''")+"'";
		}
	}
	public String getGebDatum(){
		if (gebDatum==null){
			return null;
		} else {
			return "'"+this.gebDatum+"'";
		}
	}
	public void seteNR (String eNr){
		//Convertion old ENr to new Format (994/13085 = A/1999/413085)
		String jH;
		if (eNr=="F"){
			this.eNR="F";
		} else {
			if (Integer.parseInt(eNr.substring(0, 2))>20){
				jH = "19";
			} else {
				jH = "20";
			}

			this.eNR = "A/"+jH+eNr.substring(0, 2)+"/"+eNr.substring(2, 3)+eNr.substring(4, eNr.length());
		}
	}
	public String geteNR (){
		return this.eNR;
	}
	public void setHer2NeuScoreExcel(String her2NeuScore){
		this.her2NeuScoreExcel=her2NeuScore;
	}
	public String getHer2ScoreNeuExcel (){
		return this.her2NeuScoreExcel;
	}
	public String getBestrahlung() {
		if (bestrahlung==null){
			return null;
		} else {
			return "'"+bestrahlung.replace("'", "''")+"'";
		}
	}
	public void setBestrahlung(String bestrahlung) {
		this.bestrahlung = bestrahlung;
	}
	public String getExpFU() {
		if (expFU==null){
			return null;
		} else {
			return "'"+expFU.replace("'", "''")+"'";
		}
	}
	public void setExpFU(String expFU) {
		this.expFU = expFU;
	}
	public void setPatIDDB(int PatIDDB) {
		this.PatIDDB=PatIDDB;
	}
	public int getPatIDDB(){
		return PatIDDB;
	}
	public List<String> buildStatment() {

		List<String> sqlUpdateStatment = new ArrayList<String>();
		int x=0;
		boolean used = false;


		sqlUpdateStatment.add("update fall join klassifikation on klassifikation.`Fall_E.-Nummer` = fall.`E.-Nummer` and klassifikation.Fall_Befundtyp = fall.Befundtyp set ");
		used = false;
		if (einsenderDB==null && einsenderExcel!= null ){

			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "fall.Einsender = '"+ einsenderExcel.replace("'", "''") +"'");
			used =true;
		}
		if (erDB==null && erExcel!= null ){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "klassifikation.ER = '"+ erExcel.replace("'", "''") +"'");
		}
		if (prDB==null && prExcel!= null ){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "klassifikation.PR = '"+ prExcel.replace("'", "''") +"'");
		}
		if (her2NeuScoreDB==null && her2NeuScoreExcel!= null){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "klassifikation.`Her2/neu-Score` = '"+ her2NeuScoreExcel.replace("'", "''") +"'");
		}
		if (erDB==null && erExcel!= null ){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "klassifikation.ER = '"+ erExcel.replace("'", "''") +"'");
		}
		if (GDB==null && GExcel!= null){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "klassifikation.G = '"+ GExcel.replace("'", "''") +"'");
		}
		if (TDB==null && TExcel!= null){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "klassifikation.T = '"+ TExcel.replace("'", "''") +"'");
		}
		if (NDB==null && NExcel!= null){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "klassifikation.N = '"+ NExcel.replace("'", "''") +"'");
		}
		if (MDB==null && MExcel!= null){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "klassifikation.M = '"+ MExcel.replace("'", "''") +"'");
		}
		if (LDB==null && LExcel!= null){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "klassifikation.L = '"+ LExcel.replace("'", "''") +"'");
		}
		if (VDB==null && VExcel!= null){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "klassifikation.V = '"+ VExcel.replace("'", "''") +"'");
		}
		if (RDB==null && RExcel!= null){
			if (used) {
				sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + ", ");
			}
			used =true;
			sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + "klassifikation.R = '"+ RExcel.replace("'", "''") +"'");
		}


		sqlUpdateStatment.set(x, sqlUpdateStatment.get(x) + " where fall.`E.-Nummer` = '" +eNR +"';");

		if (!used){
			sqlUpdateStatment.remove(x);
			x--;
		}


		if (PatIDDB!=-1){
			sqlUpdateStatment.add("INSERT INTO mydb.patientenzusatz (EXPnotizen, EXPchemo,EXPtamoxifen,EXParomataseH,"
					+ "EXPradatio, EXPtumorprogress1, EXPtumorprogress2, EXPstatus, EXPdatum, EXPfollowUp, EXPtodQuelle, "
					+ "EXPtodDatum, EXPbemerkung, EXParzt, Patientendaten_PatientenID) "
					+ "VALUES ( "+  getNotizenExcel()+", "+getChemoExcel()+", "+getMedAntihormonTamoxifenExcel()+", "+getMedAntihormonAromataseHExcel()+", "
					+ getBestrahlung()+", "+getrDatumExcel()+", "+getrDatum2Excel()+", "+getEE2015StatusExcel()+", "+getEE2015DatumExcel()+","+getExpFU()+", "+getQuelleTodExcel()+", "
					+ getTodDatumExcel()+", "+ getNotizen2Excel()+","+getARZT_EXCEL()+", " + PatIDDB
					+ ") "
					+ "ON DUPLICATE KEY UPDATE EXPnotizen=values(EXPnotizen), EXPchemo=values(EXPchemo), EXPtamoxifen=values(EXPtamoxifen), "
					+ "EXParomataseH=values(EXParomataseH), EXPradatio=values(EXPradatio), EXPtumorprogress1=values(EXPtumorprogress1), "
					+ "EXPstatus=values(EXPstatus), EXPdatum=values(EXPdatum), EXPfollowUp=values(EXPfollowUp), EXPtodQuelle=values(EXPtodQuelle), "
					+ "EXPtodDatum=values(EXPtodDatum), EXPbemerkung=values(EXPbemerkung), EXParzt=values(EXParzt)  ,  "
					+ "patientendaten_PatientenID=values(patientendaten_PatientenID);");
		}
		return sqlUpdateStatment;
	}
	public void clear() {

		einsenderExcel=null;
		einsenderDB=null;
		eNR=null;
		name=null;
		vorname=null;
		gebDatum=null;
		erExcel=null;
		erDB=null;
		prExcel=null;
		prDB=null;
		her2NeuScoreExcel=null;
		her2NeuScoreDB=null;
		NotizenExcel=null;
		chemoExcel=null;
		medAntihormonTamoxifenExcel=9999;
		rDatumExcel=null;
		rDatum2Excel=null;
		EE2015StatusExcel=null;
		EE2015DatumExcel=null;
		quelleTodExcel=null;
		todDatumExcel=null;
		ARZT_EXCEL=null;
		PatIDDB=0;
		Notizen2Excel=null;
		GExcel=null;
		TExcel=null;
		NExcel=null;
		MExcel=null;
		LExcel=null;
		VExcel=null;
		RExcel=null;
		GDB=null;
		TDB=null;
		NDB=null;
		MDB=null;
		LDB=null;
		VDB=null;
		RDB=null;
		medAntihormonAromataseHExcel=null;
		bestrahlung=null;
		expFU=null;
	}
}
