
public class columnIndex {
	
	String columnName;
	int columnIndex;
	int PstIndex;
	int Pst_updateIndex;
	columnIndex next;
	columnIndex prev;
	
	public columnIndex() {
		this.columnName = null;
		this.columnIndex = -1;
		this.PstIndex = -1;
		this.Pst_updateIndex = -1;
		this.next = null;
		this.prev = null;
	}
	
	public columnIndex(String name, int index) {
		this.columnName = name;
		this.columnIndex = index;
		this.PstIndex = -1;
		this.Pst_updateIndex = -1;
		this.next = null;
		this.prev = null;
	}
	
	public columnIndex(String name, int columnIndex, int PstIndex) {
		this.columnName = name;
		this.columnIndex = columnIndex;
		this.PstIndex = PstIndex;
		this.Pst_updateIndex = -1;
		this.next = null;
		this.prev = null;
	}
	
	public columnIndex(String name, int columnIndex, int PstIndex, int Pst_updateIndex) {
		this.columnName = name;
		this.columnIndex = columnIndex;
		this.PstIndex = PstIndex;
		this.Pst_updateIndex = Pst_updateIndex;
		this.next = null;
		this.prev = null;
	}
	
	public boolean hasNext() {
		if (this.next == null) return false;
		return true;
	}
	
	public boolean hasPrev() {
		if (this.prev == null) return false;
		return true;
	}
	
}
