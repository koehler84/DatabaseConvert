
public class columnStructure {
	
	columnIndex first;
	
	public columnStructure() {
		first = null;
	}
	
	public void add(columnIndex object) {
		if (first == null) {
			first = object;
		} else {
			object.next = first;
			first = object;
		}
	}
	
	public int length() {
		if (first == null) {
			return 0;
		} else {
			int length = 1;
			columnIndex object = first;
			while (object.hasNext()) {
				length++;
				object = object.next;
			}
			return length;
		}
	}
	
}
