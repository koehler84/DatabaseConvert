package main;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringReader {

	String source;
	//Init with Failsafe
	//--Klassifikation--
	String Quadrant=null;
	String G;							
	//erkennbar (siehe Task 2)
	String T=null;						//Tumor [c,p,][1,2,3,4,X][a,b,c] or "mis" (see Task 2)
	String N=null;						//Metastasen [1,2,3,4,X][a,b,c] or "mis" (see Task 2) 
	//=> Task 5
	String M=null;						//Fernmetastasen [0,1]{later maybe localisation code} or 
	//"mis" (see Task 2)
	String L;							//9 - Not in Dataset
	String V;							//9 - Not in Dataset
	String R;
	int nSum;
	int nMeta;
	private String ER=null;
	String erIrs;
	private String PR=null;
	String prIrs;
	String her2_Neu=null;
	String her2_NeuScore=null;
	String ki67=null;
	//--Tumor--
	String localisation=null;
	String side=null;
	String Material=null;
	int size;
	java.sql.Date progressDate;
	String tumorart=null;
	String lage=null;
	int indexER=Integer.MAX_VALUE;
	int indexPR=Integer.MAX_VALUE;


	//TODO:	her2_Neu implementieren (X), SubString (V), R (V)

	public StringReader () {
		this.source = null;
	}

	public StringReader (String source) {

		if (!source.equals("")) {
			this.source = source;
			this.StringAnalyse();
			this.readyForDB();
		} else {
			this.source = null;
			this.readyForDB();
		}

	}

	private void StringAnalyse() {

		int makroskopStart=0,mikroskopStart=0,diagStart=0,tumorclassStart=0;
		String[] textSub = {"", "", "",this.source};

		for (int i = 0; i < this.source.length() - 20; i++) {
			if (source.substring(i, i+"Makroskopie:".length()).equals("Makroskopie:")) {
				makroskopStart = i;
			}
			if (source.substring(i, i+"Mikroskopie:".length()).equals("Mikroskopie:")) {
				mikroskopStart = i;
				textSub[0] = source.substring(makroskopStart, mikroskopStart);				
			}
			if (source.substring(i, i+"Diagnose:".length()).equals("Diagnose:")) {
				diagStart = i;
				textSub[1] = source.substring(mikroskopStart, diagStart);
				textSub[2] = source.substring(diagStart, this.source.length());
				textSub[3] = "";
			}
			if (i+"Tumorklassifikation".length() < this.source.length() && source.substring(i, i+"Tumorklassifikation:".length()).equals("Tumorklassifikation:")) {
				tumorclassStart = i;
				textSub[2] = source.substring(diagStart, tumorclassStart);
				textSub[3] = source.substring(tumorclassStart, this.source.length());
				break;
			}
		}

		this.source = null;


		//TODO Überarbeiten Durchläufe ineffizient as fuck
		for (int i = 0; i < textSub[3].length(); i++) {
			FindKlassifikation(textSub[3], i);
			if (lage==null || tumorart==null ) {
				FindTumorart(textSub[3], i);

			}
			if (her2_Neu==null) {
				FindHer2_neu(textSub[3], i);
			}
		}

		for (int i = 0; i < textSub[2].length(); i++) {
			if (textSub[3]==""){
				FindKlassifikation(textSub[2], i);
			}
			if (lage==null || tumorart==null ) {
				FindTumorart(textSub[2], i);
			}
			if (her2_Neu==null) {
				FindHer2_neu(textSub[2], i);
			}
		}

		int a=3;
		while (a>-1) {
			for (int i = 0; i < textSub[a].length(); i++) {
				if (getER()==null || getPR()==null) {
					FindER_PR(textSub[a], i);
				}
			}
			a--;
		}
	}

	private void FindTumorart (String textSub, int i) {
		if (Pattern.matches(".*M[ ]?8[\\d]{3}/3.*", textSub) || Pattern.matches(".*NST.*", textSub) || Pattern.matches(".*NOS.*", textSub)){
			lage="invasiv";
			tumorart="duktal";
		}
		if (Pattern.matches(".*M[ ]?8[\\d]{3}/2.*", textSub)|| Pattern.matches(".*CLIS.*", textSub) || Pattern.matches(".DCIS.*", textSub) || Pattern.matches(".*insitu.*", textSub.toLowerCase()) || Pattern.matches(".*in situ.*", textSub.toLowerCase())){
			lage="in situ";

		}
		if ((Pattern.matches(".*M[ ]?8500.*", textSub) || Pattern.matches(".*duktal.*", textSub.toLowerCase()) || Pattern.matches(".DCIS.*", textSub)) && tumorart==null) {
			tumorart="duktal";
		}
		if ((Pattern.matches(".*M[ ]?8520.*", textSub) || Pattern.matches(".*lobulär.*", textSub.toLowerCase()) || Pattern.matches(".*CLIS.*", textSub)) && tumorart==null) {
			tumorart="lobulär";
		}
		if ((Pattern.matches(".*M[ ]?8211.*", textSub) || Pattern.matches(".*tubulär.*", textSub.toLowerCase())) && tumorart==null) {
			tumorart="tubulär";
		}
		if ((Pattern.matches(".*M[ ]?8480.*", textSub) || Pattern.matches(".*muzinös.*", textSub.toLowerCase())) && tumorart==null) {
			tumorart="muzinös";
		}
		if ((Pattern.matches(".*M[ ]?8503.*", textSub) || Pattern.matches(".*papillär.*", textSub.toLowerCase())) && tumorart==null) {
			tumorart="papillär";
		}
		if ((Pattern.matches(".*M[ ]?8201.*", textSub) || Pattern.matches(".*kribriform.*", textSub.toLowerCase())) && tumorart==null) {
			tumorart="kribriform";
		}
		if ((Pattern.matches(".*M[ ]?8510.*", textSub) || Pattern.matches(".*medullär.*", textSub.toLowerCase())) && tumorart==null) {
			tumorart="medullär";
		}
		if ((Pattern.matches(".*M[ ]?8575.*", textSub) || Pattern.matches(".*metaplastisch.*", textSub.toLowerCase())) && tumorart==null) {
			tumorart="metaplastisch";
		}

	}

	private void FindHer2_neu (String textSub, int i) {
		if ((i < textSub.length() - 8) && (textSub.substring(i, i + 5)).toUpperCase().equals("SCORE")) {
			if (textSub.substring(i + 5, i + 6).equals("2") && !textSub.substring(i + 6, i + 7).equals("/") || textSub.substring(i + 6, i + 7).equals("2") && !textSub.substring(i + 7, i + 8).equals("/")) {
				her2_NeuScore="2";
			}
			if (textSub.substring(i + 5, i + 6).equals("3") && !textSub.substring(i + 6, i + 7).equals("/") || textSub.substring(i + 6, i + 7).equals("3") && !textSub.substring(i + 7, i + 8).equals("/")) {
				her2_NeuScore="3";
			}
			if (textSub.substring(i + 5, i + 6).equals("1") && !textSub.substring(i + 6, i + 7).equals("/") || textSub.substring(i + 6, i + 7).equals("1") && !textSub.substring(i + 7, i + 8).equals("/")) {
				her2_NeuScore="1";
			}
			if (textSub.substring(i + 5, i + 6).equals("0") && !textSub.substring(i + 6, i + 7).equals("/") || textSub.substring(i + 6, i + 7).equals("0") && !textSub.substring(i + 7, i + 8).equals("/")) {
				her2_NeuScore="0";
			}
		}
		if ((i < textSub.length() - 6) && (textSub.substring(i, i + 5)).toUpperCase().equals("NEU -") || her2_NeuScore!=null && (her2_NeuScore.equals("0") || her2_NeuScore.equals("1"))) {
			her2_Neu="-";
		} else if ((i < textSub.length() - 5) && (textSub.substring(i, i + 5)).toUpperCase().equals("NEU +")|| her2_NeuScore!=null && her2_NeuScore.equals("3")) {
			her2_Neu="+";
		} else if (her2_NeuScore!=null && her2_NeuScore.equals("2")) {
			her2_Neu="?";
		}
	}

	private void FindER_PR (String textSub, int i) {

		String[] receptor = {" er", "östrogen", " pr", "progesteron"};
		String[] value = {"-", "\\+", "negativ", "positiv"};


		for (int l = 0; l < receptor.length; l++) {
			if ((i < textSub.length() - receptor[l].length()) && textSub.substring(i, i + receptor[l].length()).toLowerCase().equals(receptor[l])) {
				for (int m = 0; m < value.length; m++) {


					/* Überarbeitung 24.08
					 * [ER+,ER-,PR+,PR-] 
					 * 			(l==0 || l==2) && textSub.substring(i+receptor[l].length(), i+receptor[l].length()+value[m].length()).equals(value[m])
					 * [ER {value}, PR {value}] 
					 * 			(l==0 || l==2) && textSub.substring(i+receptor[l].length()+1, i+receptor[l].length()+value[m].length()+1).equals(value[m])
					 * [östrogen???{value} unterscheiungen:
					 * 		value nur mit führenden leerzeichen? nein ..rezeptor-positiv
					 * 		gesonderter Fall "-", alles andere generalisiert erstes vorkommen
					 * 		östrogenrezeoptor{???}{value}
					 * 		östrogen- und progesteronrezeptor-{???}{value}
					 * 			(l==1 || l ==3) && (m!=0)&& (Pattern.matches("(?i).*"+value[m]+".*",textSub.substring(i + 1 + receptor[l].length())))
					 * 		"-" östrogen- und progesteronrezeptor
					 * 			(m==0) && textSub.substring(i+receptor[l].length()+1, i+receptor[l].length()+value[m].length()+1).equals("-")))
					 * 		
					 * 		
					 */
					//if (Pattern.matches("(?i).*"+value[m]+".*",textSub.substring(i + 1 + receptor[l].length())))

					//Überarbeitung 19.08
					if (((l==0 || l==2) && (m!=0) && (Pattern.matches("(?i).*"+value[m]+".*",textSub.substring(i + receptor[l].length())))) ||
							(l==1 || l ==3) && (m!=0)&& (Pattern.matches("(?i).*"+value[m]+".*",textSub.substring(i + 3, i + 4)))
							) {
						if ((l==0 || l==1)) {
							setER(m,i);

						}
						if ((l==2 || l==3)){
							setPR(m,i);

						}
					} else if ((m==0) && (Pattern.matches("(?i).* "+value[m]+".*",textSub.substring(i + 1 + receptor[l].length()))) ||
							((l==0 || l==2) && (Pattern.matches("(?i).*"+value[m]+" .*",textSub.substring(i + 3, i + 4))))
							){
						if ((l==0 || l==1)) {
							setER(m,i);

						}
						if ((l==2 || l==3)){
							setPR(m,i);
						}
					}



					/*
					//Ursprung
					int end60 =0;
					int end40 =0;
					int end20 =0;
					if (textSub.length()-i- receptor[l].length()<60){
						end60 = textSub.length() - i - receptor[l].length();
					}else {
						end60 =60;
					}
					if (textSub.length()-i- receptor[l].length()<40){
						end40 = textSub.length() - i - receptor[l].length();
					}else {
						end40 =40;
					}
					if (textSub.length()-i- receptor[l].length()<20){
						end20 = textSub.length() - i - receptor[l].length();
					}else {
						end20 =20;
					}
						if (((l==1) && (!(Pattern.matches(".*nkoprotein.*", textSub.substring(i + receptor[l].length(),i + receptor[l].length() +end60)))) 
								&& (Pattern.matches("(?i).*"+value[m]+".*",textSub.substring(i + 1 + receptor[l].length(),i + receptor[l].length()+end60)))) ||
							((l==2 || l==3 || l==0) && !((Pattern.matches(".*nkoprotein.*", textSub.substring(i + receptor[l].length(),i + receptor[l].length()+end40))) )
								&& (Pattern.matches("(?i).*"+value[m]+".*",textSub.substring(i + receptor[l].length(),i + receptor[l].length()+end20))))
								) {

							if ((m==0 && textSub.substring(i + receptor[l].length(),i + receptor[l].length()+1).equals("-") && !(textSub.substring(i + receptor[l].length() +1,i + receptor[l].length()+2).equals(" ") || textSub.substring(i + receptor[l].length() +1,i + receptor[l].length()+2).equals(".")))){
							 continue;
							}
							if ((l==0 || l==1) && (m==1 || m==3)) {
								ER="+";
							}
							if ((l==2 || l==3) && (m==1 || m==3)){
								PR="+";
							}
							if ((l==0 || l==1) && (m==0 || m==2)) {
								ER="-";
							} 
							if ((l==2 || l==3) && (m==0 || m==2)){
								PR="-";
							}
					 */
				}
			}
		}
	}

	//	}

	private void FindKlassifikation (String textSub,int i) {

		if (textSub.substring(i, i+1).equals("G") && (i < textSub.length()- 5)){
			if (textSub.substring(i+1, i+2).equals("1") || textSub.substring(i+1, i+2).equals("2") || 
					textSub.substring(i+1, i+2).equals("3") || textSub.substring(i+1, i+2).equals("0")){
				G=textSub.charAt(i+1)+"";
			} else if (textSub.substring(i+1, i+3).equals(" 1") || textSub.substring(i+1, i+3).equals(" 2") || 
					textSub.substring(i+1, i+3).equals(" 3")|| textSub.substring(i+1, i+3).equals(" 0")) {
				G=textSub.charAt(i+2)+"";
			}
			if (textSub.substring(i+1, i+4).toUpperCase().equals("III") || textSub.substring(i+1, i+5).toUpperCase().equals(" III")) {
				G="3";
			} else if (textSub.substring(i+1, i+3).toUpperCase().equals("II") || textSub.substring(i+1, i+4).toUpperCase().equals(" II")){
				G="2";
			} else if (textSub.substring(i+1, i+3).toUpperCase().equals("I ") || textSub.substring(i+1, i+4).toUpperCase().equals(" I ")
					|| textSub.substring(i+1, i+3).toUpperCase().equals("I,") || textSub.substring(i+1, i+4).toUpperCase().equals(" I,")){
				G="1";
			} else if (textSub.substring(i+1, i+2).toUpperCase().equals("X") || 
					textSub.substring(i+1, i+3).toUpperCase().equals(" X")){
				G="X";
			}
		}

		if ((textSub.substring(i, i+1).toUpperCase().equals("T")) && (i < textSub.length() - 2)) {
			if ((textSub.substring(i+1, i+2).equals("1") || textSub.substring(i+1, i+2).equals("2") || 
					textSub.substring(i+1, i+2).equals("3") || textSub.substring(i+1, i+2).equals("4")|| 
					textSub.substring(i+1, i+2).toUpperCase().equals("X"))){
				T=""+ textSub.charAt(i+1);

				switch (textSub.charAt(i-2)){
				case 'y': T="y"+T;
				break;
				default:
				}

				switch (textSub.charAt(i-1)){
				case 'p': T="p"+T;
				break;
				case 'c': T="c"+T;
				break;
				default:
				}

				switch (textSub.charAt(i+2)){
				case 'a':	T+="a";
				break;
				case 'b':	T+="b";
				break;
				case 'c':	T+="c";
				break;
				default:
				}
			}
		}

		if (textSub.substring(i, i+1).toUpperCase().equals("N") && (i < textSub.length() - 2)) {
			if ((textSub.substring(i+1, i+2).equals("0") || textSub.substring(i+1, i+2).equals("1") || textSub.substring(i+1, i+2).equals("2") || 
					textSub.substring(i+1, i+2).equals("3") || textSub.substring(i+1, i+2).toUpperCase().equals("X"))){
				N=""+ textSub.charAt(i+1);

				switch (textSub.charAt(i+2)){
				case 'a':	N+="a";
				break;
				case 'b':	N+="b";
				break;
				case 'c':	N+="c";
				break;
				case 'd':	N+="d";
				break;
				case 'e':	N+="e";
				break;
				default:
				}
			}
		}

		if (textSub.substring(i, i+1).toUpperCase().equals("M") && (i < textSub.length() - 2)) {
			if (textSub.substring(i+1, i+2).equals("1") || textSub.substring(i+1, i+2).equals("0") || textSub.substring(i+1, i+2).toUpperCase().equals("X")){
				M=textSub.substring(i+1, i+2).toUpperCase();
			}
		}

		if (textSub.substring(i, i+1).toUpperCase().equals("L") && (i < textSub.length() - 2)){
			if (textSub.substring(i+1, i+2).equals("/")){
				if (textSub.substring(i+3, i+4).equals("0")) {
					L=V="0";
				} else {
					L="1";
					V=textSub.substring(i+3, i+4).toUpperCase();
				}
				if (textSub.substring(i+2, i+3).toUpperCase().equals("V") && !textSub.substring(i+1, i+2).equals("/")){
					L=textSub.substring(i+1, i+2);
					V=textSub.substring(i+3, i+4);
				}
			}
		}

		if (textSub.substring(i, i+1).toUpperCase().equals("R") && (i < textSub.length() - 2)) {
			if (textSub.substring(i+1, i+2).equals("0") || textSub.substring(i+1, i+2).equals("1") ||
					textSub.substring(i+1, i+2).equals("X")){
				R=textSub.charAt(i+1)+"";
			}

		}


	}

	private void readyForDB() {

		if (this.progressDate != null && this.progressDate.toString().equals("")) {};

	}

	public static String validateN(String n) {
		if (Pattern.matches(".*n0.*", n.toLowerCase())) {
			return "0";
		}
		if (Pattern.matches(".*n1a.*", n.toLowerCase())) {
			return "1a";
		} else if (Pattern.matches(".*n1mi.*", n.toLowerCase())) {
			return "1mi";
		} else if (Pattern.matches(".*n1.*", n.toLowerCase())) {
			return "1";
		}
		if (Pattern.matches(".*nx.*", n.toLowerCase())) {
			return "X";
		}
		return null;
	}
	public static String validateT(String t) {
		if (Pattern.matches(".*t1a.*", t.toLowerCase())) {
			return "1a";
		}
		if (Pattern.matches(".*t1b.*", t.toLowerCase())) {
			return "1b";
		}
		if (Pattern.matches(".*t1c.*", t.toLowerCase())) {
			return "1c";
		}
		if (Pattern.matches(".*t1mi.*", t.toLowerCase())) {
			return "1mi";
		}
		if (Pattern.matches(".*t2.*", t.toLowerCase())) {
			return "2";
		}
		if (Pattern.matches(".*t3.*", t.toLowerCase())) {
			return "3";
		}
		if (Pattern.matches(".*t4.*", t.toLowerCase())) {
			return "4";
		}
		if (Pattern.matches(".*t4a.*", t.toLowerCase())) {
			return "4a";
		}
		if (Pattern.matches(".*t4b.*", t.toLowerCase())) {
			return "4b";
		}
		if (Pattern.matches(".*t4c.*", t.toLowerCase())) {
			return "4c";
		}
		if (Pattern.matches(".*t4d.*", t.toLowerCase())) {
			return "4d";
		}
		if (Pattern.matches(".*t4is.*", t.toLowerCase())) {
			return "4is";
		}
		if (Pattern.matches(".*tx.*", t.toLowerCase())) {
			return "X";
		}


		return null;
	}

	public String getER() {
		return ER;
	}

	public void setER(int m2, int i) {
		if (i<indexER) {
			indexER=i;
			if (m2 == 1 || m2 == 3) {
				ER="+";
			} else if (m2 == 0 || m2 == 2) {
				ER="-";
			}
		}
	}

	public String getPR() {
		return PR;
	}

	public void setPR(int m2, int i) {
		if (i<indexPR) {
			indexPR=i;
			if (m2 == 1 || m2 == 3) {
				PR="+";
			} else if (m2 == 0 || m2 == 2) {
				PR="-";
			}
		}
	}


}

