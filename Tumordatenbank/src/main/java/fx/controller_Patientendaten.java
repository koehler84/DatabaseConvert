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
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
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
	
	public void DBtoTable_Patientendaten() {
		
		ObservableList<Patientendaten> old_data = tabelle_Patientendaten.getItems();
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
			tabelle_Patientendaten.setItems(new_data);
		} else {
			tabelle_Patientendaten.setItems(old_data);
		}
		
	}
	
	public void rowToTextField_Patientendaten(MouseEvent e) {
		
		if (e.getClickCount() == 2) {
			
			//int selectedRow = tabelle_Patientendaten.getSelectionModel().getSelectedIndex();
			
			
			
		}
		
	}

}
