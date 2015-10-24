package fx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import main.Befundtyp;
import tableMasks.Fall;

public class controller_Fall implements Initializable {

	@FXML public TableView<Fall> table;
	@FXML public static AnchorPane mainPanel;
	@FXML private TextField txtField_Geburtsdatum;
	@FXML private TextField txtField_Vorname;
	@FXML private TextField txtField_Name;
	@FXML private TextField txtField_eNummer;
	@FXML private ChoiceBox<Befundtyp> choiceBox_Befundtyp;
	@FXML private TextField txtField_Arzt;
	@FXML private DatePicker datePicker_Eingangsdatum;
	@FXML private TextField txtField_Einsender;
	@FXML private CheckBox checkBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub		
		System.out.println("controller Fall");
		
		choiceBox_Befundtyp.getItems().setAll(Befundtyp.values());
		table.getColumns().setAll(Fall.getColumns());
		
	}
	
	public void fertig_Button() {
		
		System.out.println("Date: " + datePicker_Eingangsdatum.getValue());
		
	}

}
