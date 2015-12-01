package fx;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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

public class controller_Fall implements Initializable, PanelController {

	private static boolean doubleCheck = false;
	@FXML public TableView<Fall> table;
	@FXML public static AnchorPane mainPanel;
	@FXML private DatePicker datePicker_Geburtsdatum;
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
	
	public void loadDataIntoTable() {
		
		ObservableList<Fall> old_data = table.getItems();
		ObservableList<Fall> new_data = FXCollections.observableArrayList();
		boolean success = false;
		
		try {
			Statement st = FX_Main.cn.createStatement();
			ResultSet res = st.executeQuery("select * from vFehlerFall;");
			
			while (res.next()) {
				LocalDate geburtsdatum = null;
				LocalDate eingangsdatum = null;
				if (res.getDate("Geburtsdatum") != null) geburtsdatum = res.getDate("Geburtsdatum").toLocalDate();
				if (res.getDate("Eingangsdatum") != null) eingangsdatum = res.getDate("Eingangsdatum").toLocalDate();
				
				Fall fall = new Fall(res.getString("E.-Nummer"), Befundtyp.getBefundtyp(res.getInt("Befundtyp")), res.getString("Arzt"),
						eingangsdatum, res.getString("Einsender"), geburtsdatum, res.getString("Vorname"), res.getString("Name"));
				new_data.add(fall);
			}
			success = true;			
		} catch (SQLException e) {
			System.err.println(e + " - fx.controller_Fall / DBtoTable");
		}
		
		if (success) {
			table.setItems(new_data);
		} else {
			table.setItems(old_data);
		}
		
	}
	
	public void tableRowToInputMask(MouseEvent e) {
		
		datePicker_Geburtsdatum.setStyle("-fx-border-color: null");
		txtField_Vorname.setStyle("-fx-border-color: null");
		txtField_Name.setStyle("-fx-border-color: null");
		txtField_eNummer.setStyle("-fx-border-color: null");
		choiceBox_Befundtyp.setStyle("-fx-border-color: null");
		txtField_Arzt.setStyle("-fx-border-color: null");
		datePicker_Eingangsdatum.setStyle("-fx-border-color: null");
		txtField_Einsender.setStyle("-fx-border-color: null");
		
		if (e.getClickCount() == 2) {
			
			Fall selectedFall = table.getSelectionModel().getSelectedItem();
			doubleCheck = false;
			
			if (selectedFall != null) {
				datePicker_Geburtsdatum.setValue(selectedFall.getGeburtsdatum());
				txtField_Vorname.setText(selectedFall.getVorname());
				txtField_Name.setText(selectedFall.getName());
				txtField_eNummer.setText(selectedFall.getENummer());
				choiceBox_Befundtyp.setValue(selectedFall.getBefundtyp());
				txtField_Arzt.setText(selectedFall.getArzt());
				datePicker_Eingangsdatum.setValue(selectedFall.getEingangsdatum());
				txtField_Einsender.setText(selectedFall.getEinsender());				
			}
			
		}
		
	}

	public void submitToDB() {
		
		datePicker_Geburtsdatum.setStyle("-fx-border-color: null");
		txtField_Vorname.setStyle("-fx-border-color: null");
		txtField_Name.setStyle("-fx-border-color: null");
		txtField_eNummer.setStyle("-fx-border-color: null");
		choiceBox_Befundtyp.setStyle("-fx-border-color: null");
		txtField_Arzt.setStyle("-fx-border-color: null");
		datePicker_Eingangsdatum.setStyle("-fx-border-color: null");
		txtField_Einsender.setStyle("-fx-border-color: null");
		
		if (table.getSelectionModel().getSelectedItem() != null) {
			
			//If E-Nummer and Befundtyp are empty, skip update method
			if (datePicker_Geburtsdatum.getValue() != null && datePicker_Geburtsdatum.getEditor().getText().length() == 10
					&& txtField_Vorname.getText().length() != 0 && txtField_Name.getText().length() != 0
					&& txtField_eNummer.getText().length() != 0 && choiceBox_Befundtyp.getValue() != null
					&& datePicker_Eingangsdatum.getValue() != null && datePicker_Eingangsdatum.getEditor().getText().length() == 10
					&& txtField_Einsender.getText().length() != 0 && txtField_Arzt.getText().length() != 0) {
				inputsToDatabase();
			} else if (doubleCheck && txtField_eNummer.getText().length() != 0 && choiceBox_Befundtyp.getValue() != null) {
				inputsToDatabase();
				doubleCheck = false;
			} else {
				if (datePicker_Geburtsdatum.getValue() == null || datePicker_Geburtsdatum.getEditor().getText().length() != 10) {
					datePicker_Geburtsdatum.setStyle("-fx-border-color: FF0000");
					doubleCheck = true;
				}
				
				if (txtField_Vorname.getText().length() == 0) {
					txtField_Vorname.setStyle("-fx-border-color: FF0000");
					doubleCheck = true;
				}
				
				if (txtField_Name.getText().length() == 0) {
					txtField_Name.setStyle("-fx-border-color: FF0000");
					doubleCheck = true;
				}
				
				if (datePicker_Eingangsdatum.getValue() == null || datePicker_Eingangsdatum.getEditor().getText().length() != 10) {
					datePicker_Eingangsdatum.setStyle("-fx-border-color: FF0000");
					doubleCheck = true;
				}
				
				if (txtField_Einsender.getText().length() == 0) {
					txtField_Einsender.setStyle("-fx-border-color: FF0000");
					doubleCheck = true;
				}
				
				if (txtField_Arzt.getText().length() == 0) {
					txtField_Arzt.setStyle("-fx-border-color: FF0000");
					doubleCheck = true;
				}
				
				if (txtField_eNummer.getText().length() == 0) {
					txtField_eNummer.setStyle("-fx-border-color: FF0000");
					doubleCheck = false;
				}
				
				if (choiceBox_Befundtyp.getValue() == null) {
					choiceBox_Befundtyp.setStyle("-fx-border-color: FF0000");
					doubleCheck = false;
				}
			}
			
		} else {
			System.out.println("Fehler: Keine Eingabe!");
		}
		
	}
	
	private void inputsToDatabase() {
		
		try {
			
			PreparedStatement Pst = FX_Main.cn.prepareStatement("update mydb.fall set `E.-Nummer` = ? , Befundtyp = ? ,  Arzt = ? , "
					+ "Eingangsdatum = ? , Einsender = ? , Patientendaten_PatientenID = (select PatientenID from mydb.Patientendaten "
					+ "where Geburtsdatum = ? and Vorname = ? and Name = ? ), Fehler = ? where `E.-Nummer` = ? and Befundtyp = ? ;");
			
			Fall fall = table.getSelectionModel().getSelectedItem();
			
			Pst.setString(10, fall.getENummer());	//E.-Nummer
			Pst.setInt(11, fall.getBefundtyp().getValue());	//Befundtyp
			
			Pst.setString(1, txtField_eNummer.getText());
			Pst.setInt(2, choiceBox_Befundtyp.getValue().getValue());
			
			if (datePicker_Geburtsdatum.getValue() != null && datePicker_Geburtsdatum.getEditor().getText().length() == 10) {
				Pst.setString(6, datePicker_Geburtsdatum.getEditor().getText());
			} else {
				Pst.setNull(6, java.sql.Types.NULL);
			}
			if (!txtField_Vorname.getText().equals("")) {
				Pst.setString(7, txtField_Vorname.getText());
			} else {
				Pst.setNull(7, java.sql.Types.NULL);
			}
			if (!txtField_Name.getText().equals("")) {
				Pst.setString(8, txtField_Name.getText());
			} else {
				Pst.setNull(8, java.sql.Types.NULL);
			}
			if (datePicker_Eingangsdatum.getValue() != null && datePicker_Eingangsdatum.getEditor().getText().length() == 10) {
				Pst.setString(4, datePicker_Eingangsdatum.getEditor().getText());
			} else {
				Pst.setNull(4, java.sql.Types.NULL);
			}
			if (!txtField_Einsender.getText().equals("")) {
				Pst.setString(5, txtField_Einsender.getText());
			} else {
				Pst.setNull(5, java.sql.Types.NULL);
			}
			if (!txtField_Arzt.getText().equals("")) {
				Pst.setString(3, txtField_Arzt.getText());
			} else {
				Pst.setNull(3, java.sql.Types.NULL);
			}
			
			if (checkBox.isSelected()) {
				Pst.setInt(9, 0);
			} else {
				Pst.setInt(9, 1);
			}
			
			System.out.println("Zeilen manuell geändert: " + Pst.executeUpdate());
			
			Pst.close();
			
			//delete line from table
			table.getItems().remove(fall);
			
			datePicker_Geburtsdatum.setValue(null);
			txtField_Vorname.setText("");
			txtField_Name.setText("");
			txtField_eNummer.setText("");
			choiceBox_Befundtyp.setSelectionModel(null);
			datePicker_Eingangsdatum.setValue(null);
			txtField_Einsender.setText("");
			txtField_Arzt.setText("");
			checkBox.setSelected(false);
			
			datePicker_Geburtsdatum.setStyle("-fx-border-color: null");
			txtField_Vorname.setStyle("-fx-border-color: null");
			txtField_Name.setStyle("-fx-border-color: null");
			txtField_eNummer.setStyle("-fx-border-color: null");
			choiceBox_Befundtyp.setStyle("-fx-border-color: null");
			txtField_Arzt.setStyle("-fx-border-color: null");
			datePicker_Eingangsdatum.setStyle("-fx-border-color: null");
			txtField_Einsender.setStyle("-fx-border-color: null");
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
	}
	
}
