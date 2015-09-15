
public class StringReader {

	String source;
	//--Klassifikation--
	String Quadrant;
	int G;								//Klassifikation =0,1,2,3,5=X,9 Nicht aus Datensatz 
										//erkennbar (siehe Task 2)
	String T;							//Tumor [c,p,][1,2,3,4,X][a,b,c] oder "missing" (siehe Task 2)
	String N;
	String M;
	int L;
	int V;
	int R;
	String nSum;
	String nMeta;
	boolean ER;
	String erIrs;
	boolean PR;
	String prIrs;
	String her2_Neu;
	int her2_NeuScore;
	String K67;
	//--Tumor--
	String Localisation;
	String Side;
	String Material;
	int size;
	java.sql.Date progressDate;


	public StringReader (String source) {
		this.source = source;
		this.StringAnalyse();
	}

	private void StringAnalyse() {

		int makroskopStart=0,mikroskopStart=0,diagStart=0,tumorclassStart=0;
		String makroSub="",mikroSub="", diagSub="",tumorclassSub="";


		for (int i = 0; i < this.source.length(); i++) {
			if (source.substring(i, i+"Makroskopie:".length()).equals("Makroskopie:")) {
				makroskopStart = i;
			}
			if (source.substring(i, i+"Mikroskopie:".length()).equals("Mikroskopie:")) {
				mikroskopStart = i;
				makroSub = source.substring(makroskopStart, mikroskopStart);				
			}
			if (source.substring(i, i+"Diagnose:".length()).equals("Diagnose:")) {
				diagStart = i;
				mikroSub = source.substring(mikroskopStart, diagStart);
			}
			if (i+"Tumorklassifikation".length() < this.source.length() && source.substring(i, i+"Tumorklassifikation:".length()).equals("Tumorklassifikation:")) {
				tumorclassStart = i;
				diagSub = source.substring(diagStart, tumorclassStart);
				tumorclassSub = source.substring(tumorclassStart, this.source.length()-1);
				break;
			}
			//			if (i == this.source.length()-1) {
			//				tumorclassSub = source.substring(tumorclassStart, this.source.length()-1);
			//			}
		}

		this.source = null;
		
		//Init Failsafe
		G=9;
		T="missing";
		
		for (int i = 0; i < tumorclassSub.length(); i++) {
			//-------------
			
			if (Character.toUpperCase(tumorclassSub.charAt(i)) == 'G'){
				if (tumorclassSub.charAt(i+1) == '1' || tumorclassSub.charAt(i+1) == '2' || 
						tumorclassSub.charAt(i+1) == '3' || tumorclassSub.charAt(i+1) == '0'){
					G=tumorclassSub.charAt(i+1)-'0';
				} 
				if (tumorclassSub.charAt(i+1) == ' ' && (tumorclassSub.charAt(i+2) == '1' || tumorclassSub.charAt(i+2) == '2' || 
						tumorclassSub.charAt(i+2) == '3'|| tumorclassSub.charAt(i+1) == '0')) {
					G=tumorclassSub.charAt(i+2)-'0';
				}
				if (tumorclassSub.substring(i+1, i+4).equals("III") || tumorclassSub.substring(i+2, i+5).equals("III")) {
					G=3;
				} else if (tumorclassSub.substring(i+1, i+3).equals("II") || tumorclassSub.substring(i+2, i+4).equals("II")){
					G=2;
				} else if (tumorclassSub.substring(i+1, i+2).equals("I") || tumorclassSub.substring(i+2, i+3).equals("I")){
					G=1;
				} else if (tumorclassSub.substring(i+1, i+2).toUpperCase().equals("X") || 
						tumorclassSub.substring(i+1, i+2).toUpperCase().equals("X")){
					G=5;
				}
			}
			
			if (tumorclassSub.charAt(i) == 'T') {
				if ((tumorclassSub.charAt(i+1) == '1' || tumorclassSub.charAt(i+1) == '2' || 
						tumorclassSub.charAt(i+1) == '3' || tumorclassSub.charAt(i+1) == '4')|| 
						Character.toUpperCase(tumorclassSub.charAt(i+1)) == 'X'){
					T=""+ tumorclassSub.charAt(i+1);

					switch (tumorclassSub.charAt(i-1)){
					case 'p': T="p"+T;
					break;
					case 'c': T="c"+T;
					break;
					default:
					}

					switch (tumorclassSub.charAt(i+2)){
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

		}
		System.out.println("G: " + G + ", T: "+T);	
		/*
		 *  pT3L1V0R1
		 *  pT3N1aL1V0R0
		 *  pT2
		 *  pT2N1bivR0
		 *  pT1b1 pN0(0/11) L/V0 MX R0  --- Sonderfall ist gar kein Brustkrebs
		 *  C 50, M 8500/3, G2, pT2(3,8 cm) pN1a(3/3) L/V0 R1, ER+, PR+, Her-2/neu-, Ki67-Index niedrig
		 *  C 57, M 8441/3, G 3, pT3c pN1(15/34) L/V1
		 *  pT2L0V0R0
		 *  -> 	G möglicherweise auf 4 erweitern
		 *  	p - pathologisch
		 *     	c - klinisch 
		 *     	T 0-4; 2a, 2b, 2c, 3a, 3b
		 *     	N 0-3; a-... (keine Begrenzung recherchiert) in Klammern dahinter nSum und nMeta
		 *		M - 1 oder 0 möglich boolean, aber manchmal Kürzel wo
		 *		L (= Lymphgefäßinvasion): Gibt an, ob sich auch in Lymphbahnen der Tumorregion Tumorzellen gefunden haben (L1) oder nicht (L0). Nicht zu verwechseln mit "N" für die Angabe zu den regionären Lymphknoten.
		 *		V (= Veneninvasion): Einbruch des Tumors in Venen (V0 = nicht nachweisbar, V1 = mikroskopisch, V2 = makroskopisch erkennbar), also Blutgefäße, die zum Herzen führen.
		 *		Deswegen mach es Sinn das zusammenzuziehen wenn V>0 dann L=1 wenn V=0 dann L=0
		 *		R 0,1,2
		 *		Überall X für nicht beurteilbar möglich
		 */ 







	}

}
