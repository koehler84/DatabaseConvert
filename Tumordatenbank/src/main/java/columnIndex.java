
public class columnIndex {
	
	String columnName;
	int columnIndex;
	columnIndex next;
	
	public columnIndex() {
		this.columnName = null;
		this.columnIndex = -1;
		this.next = null;
	}
	
	public columnIndex(String name, int index) {
		this.columnName = name;
		this.columnIndex = index;
		this.next = null;
	}
	
	public boolean hasNext() {
		if (this.next == null) return false;
		return true;
	}
	
}
