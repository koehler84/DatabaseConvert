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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import tableMasks.Patientendaten;

public class controller_Patientendaten implements Initializable {

	@FXML public TableView<Patientendaten> table;
	@FXML public static AnchorPane tablePane;
	@FXML private TextField txtField_Geburtsdatum;
	@FXML private TextField txtField_Vorname;
	@FXML private TextField txtField_Name;
	@FXML private TextField txtField_Strasse;
	@FXML private TextField txtField_Hausnummer;
	@FXML private TextField txtField_Land;
	@FXML private TextField txtField_PLZ;
	@FXML private TextField txtField_Ort;
	@FXML private CheckBox checkBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		System.out.println("controller Pat");
		
		table.getColumns().addAll(Patientendaten.getColumns());
		
	}
	
	public void DBtoTable_Patientendaten() {
		
		ObservableList<Patientendaten> old_data = table.getItems();
		ObservableList<Patientendaten> new_data = FXCollections.observableArrayList();
		boolean success = false;
		
		try {
			Statement st = FX_Main.cn.createStatement();
			ResultSet res = st.executeQuery("select * from mydb.vPatientendaten_Hauptparameter where `Fehler` != 0");
			
			while (res.next()) {				
				Patientendaten pat = new Patientendaten(res.getDate("Geburtsdatum").toString(), res.getString("Vorname"),
						res.getString("Name"), res.getString("Strasse"), res.getString("Hausnummer"), res.getString("Land"),
						res.getString("PLZ"), res.getString("Ort"));
				new_data.add(pat);
			}
			success = true;			
		} catch (SQLException e) {
			System.out.println(e + " - fx.controller / DBtoTable_Patientendaten");
		}
		
		if (success) {
			table.setItems(new_data);
		} else {
			table.setItems(old_data);
		}
		
	}
	
	public void rowToTextField(MouseEvent e) {
		
		if (e.getClickCount() == 2) {
			
			Patientendaten selectedPat = table.getSelectionModel().getSelectedItem();
			
			if (selectedPat != null) {
				txtField_Geburtsdatum.setText(selectedPat.getGeburtsdatum());
				txtField_Vorname.setText(selectedPat.getVorname());
				txtField_Name.setText(selectedPat.getName());
				txtField_Strasse.setText(selectedPat.getStrasse());
				txtField_Hausnummer.setText(selectedPat.getHausnummer());
				txtField_Land.setText(selectedPat.getLand());
				txtField_PLZ.setText(selectedPat.getPlz());
				txtField_Ort.setText(selectedPat.getOrt());
			}
			
		}
		
	}
	
	public void submitToDB() {
		
	}

}
