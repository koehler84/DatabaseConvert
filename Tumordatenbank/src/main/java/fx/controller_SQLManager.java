package fx;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class controller_SQLManager implements Initializable {

	@FXML private static AnchorPane mainPanel;
	@FXML public ComboBox<String> cmbBox_Statement;
	@FXML public TableView<String[]> table;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("controller SQLManager");
		
		cmbBox_Statement.getItems().addAll("select * from Patientendaten;", "select * from Fall;");
	}
	
	
	public static AnchorPane getMainPanel() {
		return mainPanel;
	}
	
	public static void setMainPanel(Object obj) {
		mainPanel = (AnchorPane) obj;
	}
	
	@SuppressWarnings("unchecked")
	public void executeStatement() {

		try {
			if (cmbBox_Statement.getEditor().getText().length() == 0) return;
		} catch(NullPointerException ex) {
			
		}
		handleComboBoxText();
		
		Statement st = null;
		ResultSet rs = null;
		ResultSetMetaData rsMeta = null;
		ObservableList<String[]> tableData = FXCollections.observableArrayList();
		
		try {			
			st = FX_Main.cn.createStatement();						
			rs = st.executeQuery(cmbBox_Statement.getSelectionModel().getSelectedItem());
			rsMeta = rs.getMetaData();
			
			while (rs.next()) {
				List<Object> list = new LinkedList<>();
				//SQL zählt Spalten etc. ab 1, nicht ab 0 wir z.B. Java-Arrays
				for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
					list.add(rs.getObject(i));
				}
				tableData.add(dataToStringArray(list));
			}
			
			String[] colNames = new String[rsMeta.getColumnCount()];
			for (int i = 0; i < colNames.length; i++) {
				colNames[i] = rsMeta.getColumnName(i+1);
			}
			
			table.getColumns().clear();
						
			for (int i = 0; i < colNames.length; i++) {				
				@SuppressWarnings("rawtypes")
				TableColumn tc = new TableColumn<>(colNames[i]);
				final int colNo = i;
				tc.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<String[], String> param) {
						return new SimpleStringProperty(param.getValue()[colNo]);
					}
				});
				table.getColumns().add(tc);
			}
			
			//---------------
			
			table.getItems().setAll(tableData);
		} catch (CommunicationsException ex) {	
			System.err.println("Verbindung verloren");
			controller_Main.setConnectionIndicatorState(false);
		} catch (MySQLNonTransientConnectionException e) {
			controller_Main.setConnectionIndicatorState(false);
		} catch (SQLException ex) {
			System.err.println(ex + "\nFehler beim Statement/ResultSet in executeStatement() in " + getClass().getName());
		}
	}
	
	/**
	 * Method will be called when you hit "Enter" in the ComboBox on panelSQLManager 
	 * @param e - KeyEvent
	 */
	public void addStatementToComboBox(KeyEvent e) {
		try {			
			if (e.getCode() == KeyCode.ENTER && cmbBox_Statement.getEditor().getText().length() != 0) {			
				cmbBox_Statement.getItems().add(cmbBox_Statement.getEditor().getText());
				cmbBox_Statement.getSelectionModel().clearSelection();
			}
		} catch (NullPointerException ex) {
			System.err.println(ex + " - There is no String in the ComboBox Editor.");
		}
	}
	
	private String[] dataToStringArray(List<Object> list) {		
		String[] array = new String[list.size()];
		
		for (int i = 0; i < array.length; i++) {			
			try {				
				array[i] = list.get(i).toString();
			} catch (NullPointerException e) {
				array[i] = "";
			}
		}
		
		return array;
	}
	
	private void handleComboBoxText() {		
		if (!cmbBox_Statement.getItems().contains(cmbBox_Statement.getEditor().getText())) {
			cmbBox_Statement.getItems().add(cmbBox_Statement.getEditor().getText());
			int index = cmbBox_Statement.getItems().indexOf(cmbBox_Statement.getEditor().getText());
			cmbBox_Statement.getSelectionModel().select(index);
		}		
	}
	
}
