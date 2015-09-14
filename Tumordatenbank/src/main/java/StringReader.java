
public class StringReader {

	String source;
	//--Klassifikation--
	String Quadrant;
	int G;
	String T;
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

		for (int i = 0; i < tumorclassSub.length(); i++) {

			if (tumorclassSub.charAt(i) == 'G' || tumorclassSub.charAt(i) == 'g') {
				if (tumorclassSub.charAt(i+1) == '1' || tumorclassSub.charAt(i+1) == '2' || 
						tumorclassSub.charAt(i+1) == '3' ){
					System.out.println("Gradient G" + tumorclassSub.charAt(i+1));
					G=tumorclassSub.charAt(i+1);
				} 
				if (tumorclassSub.charAt(i+1) == ' ' && (tumorclassSub.charAt(i+2) == '1' || tumorclassSub.charAt(i+2) == '3' || 
						tumorclassSub.charAt(i+2) == '3')) {
					System.out.println("Gradient G" + tumorclassSub.charAt(i+2));
					G=tumorclassSub.charAt(i+2);
				}
				if (tumorclassSub.substring(i+1, i+4).equals("III") || tumorclassSub.substring(i+2, i+5).equals("III")) {
					System.out.println("Gradient G3");
					G=3;
				} else if (tumorclassSub.substring(i+1, i+3).equals("II") || tumorclassSub.substring(i+2, i+4).equals("II")){
					System.out.println("Gradient G2");
					G=2;
				} else if (tumorclassSub.substring(i+1, i+2).equals("I") || tumorclassSub.substring(i+2, i+3).equals("I")){
					System.out.println("Gradient G1");
					G=1;
				}
			}

		}


	}

}
