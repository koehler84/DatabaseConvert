package fx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import tableMasks.Patientendaten;

public class controller_Patientendaten implements Initializable {

	@FXML public TableView<Patientendaten> tabelle_Patientendaten;
	@FXML public static AnchorPane tablePane;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		System.out.println("controller Pat");
		
		tabelle_Patientendaten.getColumns().addAll(Patientendaten.getColumns());
		
	}

}
