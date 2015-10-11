
public class columnData extends columnIndex {

	Object data;
	
	public columnData() {
		this.columnName = null;
		this.columnIndex = -1;
		this.next = null;
		this.data = null;
	}
	
	public columnData(String name, int index) {
		this.columnName = name;
		this.columnIndex = index;
		this.PstIndex = -1;
		this.next = null;
		this.data = null;
	}
	
	public columnData(String name, int index, Object data) {
		this.columnName = name;
		this.columnIndex = index;
		this.PstIndex = -1;
		this.next = null;
		this.data = data;
	}
	
	public columnData(columnIndex colIndex) {
		this.columnName = colIndex.columnName;
		this.columnIndex = colIndex.columnIndex;
		this.PstIndex = -1;
		this.next = null;
		this.data = null;
	}
	
	public columnData(columnIndex colIndex, Object data) {
		this.columnName = colIndex.columnName;
		this.columnIndex = colIndex.columnIndex;
		this.PstIndex = 0;
		this.next = null;
		this.data = data;
	}
	
}
