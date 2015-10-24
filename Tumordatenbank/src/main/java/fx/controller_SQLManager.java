package fx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class controller_SQLManager implements Initializable {

	@FXML public static AnchorPane mainPanel;
	@FXML public TextField txtField_Statement;
	@SuppressWarnings("rawtypes")
	@FXML public TableView table;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		System.out.println("controller SQLManager");
	}
	
	public void executeStatement() {
		
	}

}
