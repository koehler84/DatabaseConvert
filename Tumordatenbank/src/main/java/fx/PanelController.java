package fx;

import javafx.scene.input.MouseEvent;

interface PanelController {

	
	public void loadDataIntoTable();	
	public void tableRowToInputMask(MouseEvent e);
	public void submitToDB();
	
}
