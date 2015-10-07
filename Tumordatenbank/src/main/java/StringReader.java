import java.util.regex.Pattern;

public class StringReader {

	String source;
	//Init with Failsafe
	//--Klassifikation--
	String Quadrant=null;
	int G=9;							//Klassifikation =0,1,2,3,5=X,9 Not in Dataset 
	//erkennbar (siehe Task 2)
	String T=null;						//Tumor [c,p,][1,2,3,4,X][a,b,c] or "mis" (see Task 2)
	String N=null;						//Metastasen [1,2,3,4,X][a,b,c] or "mis" (see Task 2) 
	//=> Task 5
	String M=null;						//Fernmetastasen [0,1]{later maybe localisation code} or 
	//"mis" (see Task 2)
	int L=9;							//9 - Not in Dataset
	int V=9;							//9 - Not in Dataset
	int R=9;
	int nSum;
	int nMeta;
	String ER=null;
	String erIrs;
	String PR=null;
	String prIrs;
	String her2_Neu=null;
	int her2_NeuScore;
	String ki67=null;
	//--Tumor--
	String localisation=null;
	String side=null;
	String Material=null;
	int size;
	java.sql.Date progressDate;
	String tumorart=null;
	String lage=null;


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
			}
			if (i+"Tumorklassifikation".length() < this.source.length() && source.substring(i, i+"Tumorklassifikation:".length()).equals("Tumorklassifikation:")) {
				tumorclassStart = i;
				textSub[2] = source.substring(diagStart, tumorclassStart);
				textSub[3] = source.substring(tumorclassStart, this.source.length());
				break;
			}
		}

		this.source = null;

		for (int i = 0; i < textSub[3].length(); i++) {
			FindKlassifikation(textSub[3], i);
		}

		int a=3;
		while (a>-1) {
			for (int i = 0; i < textSub[a].length(); i++) {
				if (ER==null || PR==null) {
					FindER_PR(textSub[a], i);
				}
				if (her2_Neu==null) {
					FindHer2_neu(textSub[a], i);
				}
				if (lage==null || tumorart==null ) {
					FindTumorart(textSub[a], i);
				}
				a--;
			}
		}
	}

	private void FindTumorart (String textSub, int i) {
		if (Pattern.matches(".*M[ ]?8[\\d]{3}/3.*", textSub)){
			lage="invasiv";
		}
		if (Pattern.matches(".*M[ ]?8[\\d]{3}/2.*", textSub)){
			lage="in situ";
		}
		if (Pattern.matches(".*M[ ]?8500.*", textSub)) {
			tumorart="duktal";
		}
		if (Pattern.matches(".*M[ ]?8520.*", textSub)) {
			tumorart="lobulär";
		}
		if (Pattern.matches(".*M[ ]?8211.*", textSub)) {
			tumorart="tubulär";
		}
		if (Pattern.matches(".*M[ ]?8480.*", textSub)) {
			tumorart="muzinös";
		}
		if (Pattern.matches(".*M[ ]?8503.*", textSub)) {
			tumorart="papillär";
		}
		if (Pattern.matches(".*M[ ]?8201.*", textSub)) {
			tumorart="kribriform";
		}
		if (Pattern.matches(".*M[ ]?8510.*", textSub)) {
			tumorart="medullär";
		}
		if (Pattern.matches(".*M[ ]?8575.*", textSub)) {
			tumorart="metaplastisch";
		}

	}

	private void FindHer2_neu (String textSub, int i) {
		if ((i < textSub.length() - 8) && (textSub.substring(i, i + 5)).toUpperCase().equals("SCORE")) {
			if (textSub.substring(i + 5, i + 7).equals("2+") || textSub.substring(i + 5, i + 7).equals("3+")) {
				her2_Neu="+";
			}
			if (textSub.substring(i + 5, i + 7).equals("1+") || textSub.substring(i + 5, i + 6).equals("0")) {
				her2_Neu="-";
			}
			if (textSub.substring(i + 6, i + 8).equals("2+") || textSub.substring(i + 6, i + 8).equals("3+")) {
				her2_Neu="+";
			}
			if (textSub.substring(i + 6, i + 8).equals("1+") || textSub.substring(i + 6, i + 7).equals("0")) {
				her2_Neu="-";
			}
		}
		if ((i < textSub.length() - 5) && (textSub.substring(i, i + 5)).toUpperCase().equals("NEU -")) {
			her2_Neu="-";
		}
		if ((i < textSub.length() - 5) && (textSub.substring(i, i + 5)).toUpperCase().equals("NEU +")) {
			her2_Neu="+";
		}
	}

	private void FindER_PR (String textSub, int i) {

		String[] receptor = {" ER", "ÖSTROGEN", " PR", "PROGESTERON"};
		String[] value = {"+", "POSITIV", "-", "NEGATIV"};

		for (int l = 0; l < receptor.length; l++) {
			if ((i < textSub.length() - receptor[l].length()) && textSub.substring(i, i + receptor[l].length()).toUpperCase().equals(receptor[l])) {
				for (int m = 0; m < value.length; m++) {
					for (int j = i + receptor[l].length(); j <= textSub.length() - value[m].length(); j++) {
						if (textSub.substring(j, j+value[m].length()).toUpperCase().equals(value[m])) {
							if ((l==0 || l==1) && (m==0 || m==1)) {
								ER="+";
							}
							if ((l==2 || l==3) && (m==0 || m==1)){
								PR="+";
							}
							if ((l==0 || l==1) && (m==2 || m==3)) {
								ER="-";
							} 
							if ((l==2 || l==3) && (m==2 || m==3)){
								PR="-";
							}
							break;
						}
						if ((j - i > 35 && (l==1 || l==3)) || (j - i > 4 && (l==0 || l==2))) {
							break;
						}
					}
				}
			}
		}
	}

	private void FindKlassifikation (String textSub,int i) {

		if (Character.toUpperCase(textSub.charAt(i)) == 'G'){
			if (textSub.charAt(i+1) == '1' || textSub.charAt(i+1) == '2' || 
					textSub.charAt(i+1) == '3' || textSub.charAt(i+1) == '0'){
				G=textSub.charAt(i+1)-'0';
			} else if (textSub.charAt(i+1) == ' ' && (textSub.charAt(i+2) == '1' || textSub.charAt(i+2) == '2' || 
					textSub.charAt(i+2) == '3'|| textSub.charAt(i+2) == '0')) {
				G=textSub.charAt(i+2)-'0';
			}
			if (textSub.substring(i+1, i+4).equals("III") || textSub.substring(i+1, i+5).equals(" III")) {
				G=3;
			} else if (textSub.substring(i+1, i+3).equals("II") || textSub.substring(i+1, i+4).equals(" II")){
				G=2;
			} else if (textSub.substring(i+1, i+2).equals("I") || textSub.substring(i+1, i+3).equals(" I")){
				G=1;
			} else if (textSub.substring(i+1, i+2).toUpperCase().equals("X") || 
					textSub.substring(i+1, i+3).toUpperCase().equals(" X")){
				G=5;
			}
		}

		if (Character.toUpperCase(textSub.charAt(i)) == 'T') {
			if ((textSub.charAt(i+1) == '1' || textSub.charAt(i+1) == '2' || 
					textSub.charAt(i+1) == '3' || textSub.charAt(i+1) == '4'|| 
					Character.toUpperCase(textSub.charAt(i+1)) == 'X')){
				T=""+ textSub.charAt(i+1);

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

		if (Character.toUpperCase(textSub.charAt(i)) == 'N') {
			if ((textSub.charAt(i+1) == '1' || textSub.charAt(i+1) == '2' || 
					textSub.charAt(i+1) == '3' || Character.toUpperCase(textSub.charAt(i+1)) == 'X')){
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

		if (Character.toUpperCase(textSub.charAt(i)) == 'M') {
			if (textSub.charAt(i+1) == '1' || textSub.charAt(i+1) == '0'){
				M=""+ textSub.charAt(i+1);

				//TODO: localisation code (extrem low priority)
			}
		}

		if (Character.toUpperCase(textSub.charAt(i)) == 'L'){
			if (textSub.charAt(i+1) == '/'){
				if (textSub.charAt(i+3) == '0') {
					L=V=0;
				} else {
					L=1;
					V=textSub.charAt(i+3)-'0';
				}
				if (Character.toUpperCase(textSub.charAt(i+2)) == 'V' && textSub.charAt(i+1) != '/'){
					L=textSub.charAt(i+1)-'0';
					V=textSub.charAt(i+3)-'0';
				}
			}
		}

		if (Character.toUpperCase(textSub.charAt(i)) == 'R') {
			if (textSub.charAt(i+1) == '0' || textSub.charAt(i+1) == '1' ||
					textSub.charAt(i+1) == '2'){
				R=textSub.charAt(i+1)-'0';
			}
			if (Character.toUpperCase(textSub.charAt(i+1)) == 'X') {
				R=5;
			}
		}






	}

	private void readyForDB() {

		if (this.progressDate != null && this.progressDate.toString().equals("")) {};

	}

}

