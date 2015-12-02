package fx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class controller_Logo implements Initializable {

	@FXML private static BorderPane mainPanel;
	@FXML private ImageView image;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public static Pane getMainPanel() {
		return mainPanel;
	}
	
	public static void setMainPanel(Object obj) {
		mainPanel = (BorderPane) obj;
	}
	
	public void easterEgg(MouseEvent e) {		
		if (e.getClickCount() == 2) {
			//Do something funny maybe?
		}		
	}

}
