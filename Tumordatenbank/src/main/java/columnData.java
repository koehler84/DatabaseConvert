
public class columnData extends columnIndex {

	Object data;
	
	public columnData() {
		this.columnName = null;
		this.columnIndex = -1;
		this.next = null;
		this.data = null;
	}
	
	public columnData(String name, int index, Object data) {
		this.columnName = name;
		this.columnIndex = index;
		this.next = null;
		this.data = data;
	}
	
}
