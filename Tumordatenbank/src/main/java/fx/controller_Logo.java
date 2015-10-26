package fx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class controller_Logo implements Initializable {

	@FXML public static BorderPane mainPanel;
	@FXML private ImageView image;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void easterEgg(MouseEvent e) {		
		if (e.getClickCount() == 2) {
			//Do something funny maybe?
		}		
	}

}
