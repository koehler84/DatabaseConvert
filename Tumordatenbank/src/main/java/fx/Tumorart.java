package fx;

public class Tumorart {
	private String tumorArt1="";
	private String tumorArt2="";
	private String tumorArt3="";
	private String tumorArt4="";
	public Tumorart() {
		tumorArt1="";
		tumorArt2="";
		tumorArt3="";
		tumorArt4="";	}
	public void setTumorArt1(String tumorArt1) {
		this.tumorArt1 = tumorArt1;
	}
	public void setTumorArt2(String tumorArt2) {
		this.tumorArt2 = tumorArt2;
	}
	public void setTumorArt3(String tumorArt3) {
		this.tumorArt3 = tumorArt3;
	}
	public void setTumorArt4(String tumorArt4) {
		this.tumorArt4 = tumorArt4;
	}
	public String toString() {
		if (tumorArt2!="" || tumorArt3!="" || tumorArt4!=""){
			return "["+tumorArt2 + " " + tumorArt3 + " " + tumorArt4+"]";
		} else {
		return "["+tumorArt1+"]";
		}
	}

}
