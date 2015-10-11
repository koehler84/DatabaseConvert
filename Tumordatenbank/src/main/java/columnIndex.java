
public class columnIndex {
	
	String columnName;
	int columnIndex;
	int PstIndex;
	columnIndex next;
	
	public columnIndex() {
		this.columnName = null;
		this.columnIndex = -1;
		this.PstIndex = -1;
		this.next = null;
	}
	
	public columnIndex(String name, int index) {
		this.columnName = name;
		this.columnIndex = index;
		this.PstIndex = -1;
		this.next = null;
	}
	
	public columnIndex(String name, int columnIndex, int PstIndex) {
		this.columnName = name;
		this.columnIndex = columnIndex;
		this.PstIndex = PstIndex;
		this.next = null;
	}
	
	public boolean hasNext() {
		if (this.next == null) return false;
		return true;
	}
	
}
