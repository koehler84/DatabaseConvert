package fx;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
		
	}
	
	public void DBtoTable() {
		
		ObservableList<Fall> old_data = table.getItems();
		ObservableList<Fall> new_data = FXCollections.observableArrayList();
		boolean success = false;
		
		try {
			Statement st = FX_Main.cn.createStatement();
			ResultSet res = st.executeQuery("select * from vFehlerFall;");
			
			while (res.next()) {
				Fall fall = new Fall(res.getString("E.-Nummer"), Befundtyp.getBefundtyp(res.getInt("Befundtyp")), res.getString("Arzt"),
						res.getDate("Eingangsdatum"), res.getString("Einsender"), res.getDate("Geburtsdatum"),
						res.getString("Vorname"), res.getString("Name"));
				new_data.add(fall);
			}
			success = true;			
		} catch (SQLException e) {
			System.out.println(e + " - fx.controller_Fall / DBtoTable");
		}
		
		if (success) {
			table.setItems(new_data);
		} else {
			table.setItems(old_data);
		}
		
	}
	
	public void rowToTextField(MouseEvent e) {
		
		if (e.getClickCount() == 2) {
			
			Fall selectedFall = table.getSelectionModel().getSelectedItem();
			
			if (selectedFall != null) {
				if (selectedFall.getGeburtsdatum() != null) txtField_Geburtsdatum.setText(selectedFall.getGeburtsdatum().toString());
				txtField_Vorname.setText(selectedFall.getVorname());
				txtField_Name.setText(selectedFall.getName());
				txtField_eNummer.setText(selectedFall.getENummer());
				choiceBox_Befundtyp.setValue(selectedFall.getBefundtyp());
				txtField_Arzt.setText(selectedFall.getArzt());
				datePicker_Eingangsdatum.setValue(selectedFall.getEingangsdatum().toLocalDate());
				txtField_Einsender.setText(selectedFall.getEinsender());				
			}
			
		}
		
	}

}
