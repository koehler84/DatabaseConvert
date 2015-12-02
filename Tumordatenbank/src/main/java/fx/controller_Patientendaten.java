package fx;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.mysql.jdbc.CommunicationsException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import tableMasks.Patient;

public class controller_Patientendaten implements Initializable, PanelController {

	private boolean doubleCheck = false;
	private static ArrayList<TableColumn<Patient, ?>> tableData;
	@FXML public TableView<Patient> table;
	@FXML private static AnchorPane mainPanel;
	@FXML private DatePicker datePicker_Geburtsdatum;
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
		System.out.println("controller Pat");
		
		tableData = Patient.getColumns();
		table.getColumns().addAll(tableData);
		
	}
	
	public static Pane getMainPanel() {
		return mainPanel;
	}
	
	public static void setMainPanel(Object obj) {
		mainPanel = (AnchorPane) obj;
	}
	
	public void loadDataIntoTable() {
		
		ObservableList<Patient> old_data = table.getItems();
		ObservableList<Patient> new_data = FXCollections.observableArrayList();
		boolean success = false;
		
		try {
			Statement st = FX_Main.cn.createStatement();
			ResultSet res = st.executeQuery("select * from mydb.vPatientendaten_Hauptparameter where `Fehler` != 0");
			
			while (res.next()) {
				LocalDate geburtsdatum = null;
				if (res.getDate("Geburtsdatum") != null) geburtsdatum = res.getDate("Geburtsdatum").toLocalDate();
				
				Patient pat = new Patient(geburtsdatum, res.getString("Vorname"),
						res.getString("Name"), res.getString("Strasse"), res.getString("Hausnummer"), res.getString("Land"),
						res.getString("PLZ"), res.getString("Ort"));
				new_data.add(pat);
			}
			success = true;			
		} catch (CommunicationsException ex) {
			controller_Main.getStatic_lblConnected().setVisible(false);
		} catch (SQLException e) {
			System.err.println(e + " - fx.controller_Patientendaten / DBtoTable");
		} catch (NullPointerException e) {
			System.err.println(e + " - Datenverbindung fehlt!");
		}
		
		if (success) {
			table.setItems(new_data);
		} else {
			table.setItems(old_data);
		}
		
	}
	
	/**
	 * Method will fill input fields on current panel (<i>textField</i> etc.) with data from the <i>TableView</i> on said panel. 
	 * Double clicking a <i>TableRow</i> with start the method and use the data from the selected row.
	 * Red borders will be reset.
	 * @param e MouseEvent
	 */
	public void tableRowToInputMask(MouseEvent e) {
		
		datePicker_Geburtsdatum.setStyle("-fx-border-color: null");
		txtField_Vorname.setStyle("-fx-border-color: null");
		txtField_Name.setStyle("-fx-border-color: null");
		txtField_Strasse.setStyle("-fx-border-color: null");
		txtField_Hausnummer.setStyle("-fx-border-color: null");
		txtField_Land.setStyle("-fx-border-color: null");
		txtField_PLZ.setStyle("-fx-border-color: null");
		txtField_Ort.setStyle("-fx-border-color: null");
		
		if (e.getClickCount() == 2) {
			
			Patient selectedPat = table.getSelectionModel().getSelectedItem();
			doubleCheck = false;
			
			if (selectedPat != null) {
				datePicker_Geburtsdatum.setValue(selectedPat.getGeburtsdatum());
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
	
	/**
	 * Checks <i>textFields</i> etc. if all relevant data is accessible, gives user feedback if data is missing, 
	 * starts <i>inputsToDatabase()</i> if data is complete.
	 */
	public void submitToDB() {
		
		datePicker_Geburtsdatum.setStyle("-fx-border-color: null");
		txtField_Vorname.setStyle("-fx-border-color: null");
		txtField_Name.setStyle("-fx-border-color: null");
		txtField_Strasse.setStyle("-fx-border-color: null");
		txtField_Hausnummer.setStyle("-fx-border-color: null");
		txtField_Land.setStyle("-fx-border-color: null");
		txtField_PLZ.setStyle("-fx-border-color: null");
		txtField_Ort.setStyle("-fx-border-color: null");
		
		if (table.getSelectionModel().getSelectedItem() != null) {
			
			//If textFields Geburtsdatum, Name, Vorname are empty, skip update method
			if (datePicker_Geburtsdatum.getValue() != null && datePicker_Geburtsdatum.getEditor().getText().length() == 10
					&& txtField_Name.getText().length() != 0 && txtField_Vorname.getText().length() != 0
					&& txtField_Strasse.getText().length() != 0 && txtField_Hausnummer.getText().length() != 0
					&& txtField_Land.getText().length() != 0 && txtField_PLZ.getText().length() != 0
					&& txtField_Ort.getText().length() != 0) {
				inputsToDatabase();
			} else if (doubleCheck && datePicker_Geburtsdatum.getValue() != null && datePicker_Geburtsdatum.getEditor().getText().length() == 10
					&& txtField_Name.getText() != null  && txtField_Vorname.getText() != null) {
				inputsToDatabase();
				doubleCheck = false;
			} else {
				if (txtField_Strasse.getText().length() == 0) {
					txtField_Strasse.setStyle("-fx-border-color: FF0000");
					doubleCheck = true;
				}
				
				if (txtField_Hausnummer.getText().length() == 0) {
					txtField_Hausnummer.setStyle("-fx-border-color: FF0000");
					doubleCheck = true;
				}
				
				if (txtField_Land.getText().length() == 0) {
					txtField_Land.setStyle("-fx-border-color: FF0000");
					doubleCheck = true;
				}
				
				if (txtField_PLZ.getText().length() == 0) {
					txtField_PLZ.setStyle("-fx-border-color: FF0000");
					doubleCheck = true;
				}
				
				if (txtField_Ort.getText().length() == 0) {
					txtField_Ort.setStyle("-fx-border-color: FF0000");
					doubleCheck = true;
				}
				
				if (datePicker_Geburtsdatum.getValue() == null) {
					datePicker_Geburtsdatum.setStyle("-fx-border-color: FF0000");
					doubleCheck = false;
				}
				
				if (datePicker_Geburtsdatum.getEditor().getText().length() != 10) {
					datePicker_Geburtsdatum.setStyle("-fx-border-color: FF0000");
					doubleCheck = false;
				}
				
				if (txtField_Vorname.getText().length() == 0) {
					txtField_Vorname.setStyle("-fx-border-color: FF0000");
					doubleCheck = false;
				}
				
				if (txtField_Name.getText().length() == 0) {
					txtField_Name.setStyle("-fx-border-color: FF0000");
					doubleCheck = false;
				}
			}
			
		} else {
			System.out.println("Fehler: Keine Eingabe!");
		}
		
	}
	
	/**
	 * Inserts (in table-) selected Patient into database and removes the related row from the table.
	 */
	private void inputsToDatabase() {
		
		try {
			
			PreparedStatement Pst = FX_Main.cn.prepareStatement("update mydb.patientendaten set `Geburtsdatum` = ? , `Vorname` = ? , "
					+ "`Name` = ? , `Strasse` = ? , `Hausnummer` = ? , `Land` = ? , `PLZ` = ? , `Ort` = ?, `Fehler` = ? "
					+ " where `Geburtsdatum` = ? and `Vorname` = ? and `Name` = ? ;");
			
			Patient pat = table.getSelectionModel().getSelectedItem();
			
			Pst.setString(10, pat.getGeburtsdatum().toString());		//where Geb
			Pst.setString(11, pat.getVorname());		//where Vorname
			Pst.setString(12, pat.getName());		//where Name
			
			Pst.setString(1, datePicker_Geburtsdatum.getValue().toString());	//set Geb
			Pst.setString(2, txtField_Vorname.getText());	//set Vorname
			Pst.setString(3, txtField_Name.getText());	//set Name
			
			if (!txtField_Strasse.getText().equals("")) {
				Pst.setString(4, txtField_Strasse.getText());	//set Strasse
			} else {
				Pst.setNull(4, java.sql.Types.NULL);
			}
			if (!txtField_Hausnummer.getText().equals("")) {
				Pst.setString(5, txtField_Hausnummer.getText());	//set Hausnummer
			} else {
				Pst.setNull(5, java.sql.Types.NULL);
			}
			if (!txtField_Land.getText().equals("")) {
				Pst.setString(6, txtField_Land.getText());	//set Land
			} else {
				Pst.setNull(6, java.sql.Types.NULL);
			}
			if (!txtField_PLZ.getText().equals("")) {
				Pst.setInt(7, Integer.parseInt(txtField_PLZ.getText()));	//set PLZ
			} else {
				Pst.setNull(7, java.sql.Types.NULL);
			}
			if (!txtField_Ort.getText().equals("")) {
				Pst.setString(8, txtField_Ort.getText());	//set Ort
			} else {
				Pst.setNull(8, java.sql.Types.NULL);
			}
			
			if (checkBox.isSelected()) {
				Pst.setInt(9, 0);
			} else {
				Pst.setInt(9, 1);
			}
			
			System.out.println("Zeilen manuell geändert: " + Pst.executeUpdate());
			
			Pst.close();
			
			//delete line from table
			table.getItems().remove(pat);
			
			datePicker_Geburtsdatum.setValue(null);
			txtField_Vorname.setText("");
			txtField_Name.setText("");
			txtField_Strasse.setText("");
			txtField_Hausnummer.setText("");
			txtField_Land.setText("");
			txtField_PLZ.setText("");
			txtField_Ort.setText("");
			checkBox.setSelected(false);
			
			datePicker_Geburtsdatum.setStyle("-fx-border-color: null");
			txtField_Vorname.setStyle("-fx-border-color: null");
			txtField_Name.setStyle("-fx-border-color: null");
			txtField_Strasse.setStyle("-fx-border-color: null");
			txtField_Hausnummer.setStyle("-fx-border-color: null");
			txtField_Land.setStyle("-fx-border-color: null");
			txtField_PLZ.setStyle("-fx-border-color: null");
			txtField_Ort.setStyle("-fx-border-color: null");
			
		} catch (CommunicationsException ex) {
			controller_Main.getStatic_lblConnected().setVisible(false);
		} catch (SQLException e) {
			System.out.println(e);
			//TODO Fenster "Diese Person existiert bereits in der Datenbank!"
		}
		
	}

}
