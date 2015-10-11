
public class columnStructure<Type extends columnIndex> {
	
	Type head;
	
	public columnStructure() {
		head = null;
	}
	
	public void add(Type object) {
		if (head == null) {
			head = object;
		} else {
			object.next = head;
			head = object;
		}
	}
	
	public int length() {
		if (head == null) {
			return 0;
		} else {
			int length = 1;
			columnIndex object = head;
			while (object.hasNext()) {
				length++;
				object = object.next;
			}
			return length;
		}
	}
	
	public boolean check(String tabelle) {
		
		if (tabelle.equals("patientendaten")) {			
			int requiredColumns = 0;
			columnIndex object = this.head;
			
			while (object.hasNext() && requiredColumns < 3) {
				if (object.columnName.equals("geburtsdatum") || object.columnName.equals("vorname") || object.columnName.equals("name")) {
					requiredColumns++;
				}
				object = object.next;
			}
			
			if (requiredColumns == 3) return true;
		} else if (tabelle.equals("fall")) {
			int requiredColumns = 0;
			columnIndex object = this.head;
			
			while (object.hasNext() && requiredColumns < 5) {
				if (object.columnName.equals("geburtsdatum") || object.columnName.equals("vorname") || object.columnName.equals("name") || 
						object.columnName.equals("e.-nummer") || object.columnName.equals("befundtyp")) {
					requiredColumns++;
				}
				object = object.next;
			}
			
			if (requiredColumns == 5) return true;
		}
		
		return false;
	}
	
//	public columnStructure<Type> copy() {
//		
//		columnStructure<Type> newStructure = new columnStructure<Type>();
//		Type columnObject = this.first;
//		
//		while (columnObject.hasNext()) {
//			
//			Type a = columnObject;
//			
//			newStructure.add(a);
//			columnObject = (Type) columnObject.next;
//			
//		}
//		
//		return newStructure;
//	}
	
}
