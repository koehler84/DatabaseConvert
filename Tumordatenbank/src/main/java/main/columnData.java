package main;

public class columnData extends columnIndex {

	public Object data;
	
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
	
	public columnData(String name, int index, int Pst_index) {
		this.columnName = name;
		this.columnIndex = index;
		this.PstIndex = Pst_index;
		this.Pst_updateIndex = -1;
		this.next = null;
		this.data = null;
	}
	
	public columnData(String name, int index, int Pst_index, int Pst_updateIndex) {
		this.columnName = name;
		this.columnIndex = index;
		this.PstIndex = Pst_index;
		this.Pst_updateIndex = Pst_updateIndex;
		this.next = null;
		this.data = null;
	}
	
	public columnData(columnIndex colIndex) {
		this.columnName = colIndex.columnName;
		this.columnIndex = colIndex.columnIndex;
		this.PstIndex = colIndex.PstIndex;
		this.Pst_updateIndex = colIndex.Pst_updateIndex;
		if (colIndex.hasNext()) {			
			this.next = new columnData(colIndex.next);
		} else {
			this.next = null;
		}
		this.data = null;
	}
	
}
