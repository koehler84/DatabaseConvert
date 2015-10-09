
public class columnStructure<Type extends columnIndex> {
	
	Type first;
	
	public columnStructure() {
		first = null;
	}
	
	public void add(Type object) {
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
	
	public columnStructure<Type> copy() {
		
		columnStructure<Type> newStructure = new columnStructure<Type>();
		Type columnObject = this.first;
		
		while (columnObject.hasNext()) {
			
			Type a = columnObject;
			
			newStructure.add(a);
			columnObject = (Type) columnObject.next;
			
		}
		
		return newStructure;
	}
	
}
