package fx;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class controller_SQLManager implements Initializable {

	@FXML public static AnchorPane mainPanel;
	@FXML public TextField txtField_Statement;
	@FXML public ComboBox<String> cmbBox_Statement;
	//@FXML public TableView<Prototyp_universell> table;
	@FXML public TableView<String[]> table;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("controller SQLManager");
		
		//*table.getColumns().setAll(Prototyp_universell.getColumns());
	}
	
	@SuppressWarnings("unchecked")
	public void executeStatement() {

		handleComboBoxText();
		
		Statement st = null;
		ResultSet rs = null;
		ResultSetMetaData rsMeta = null;
		ObservableList<String[]> tableData = FXCollections.observableArrayList();
		
		try {
			st = FX_Main.cn.createStatement();
			//rs = st.executeQuery(cmbBox_Statement.getSelectionModel().getSelectedItem());
//			rs = st.executeQuery("select patdaten.`Name`, patdaten.Vorname, patdaten.Geburtsdatum ,mydb.fall.`E.-Nummer`, mydb.klassifikation.ER, mydb.klassifikation.PR,  mydb.klassifikation.`Her2/neu`, mydb.klassifikation.lage, mydb.klassifikation.Tumorart,\r\n" + 
//					" klassifikation.G, klassifikation.T, klassifikation.N, klassifikation.N, klassifikation.M, klassifikation.L, klassifikation.V, klassifikation.R\r\n" + 
//					"from mydb.klassifikation as klassifikation\r\n" + 
//					"join mydb.fall as fall \r\n" + 
//					"join mydb.patientendaten as patdaten on klassifikation.`Fall_E.-Nummer` = fall.`E.-Nummer` and klassifikation.Fall_Befundtyp = fall.Befundtyp and patdaten.PatientenID = fall.Patientendaten_PatientenID;");
//			
//			while (rs.next()) {
//				LocalDate geburtsdatum = null;
//				if (rs.getDate("Geburtsdatum") != null) geburtsdatum = rs.getDate("Geburtsdatum").toLocalDate();
//				
//				Prototyp_universell data = new Prototyp_universell(geburtsdatum, rs.getString("Vorname"), rs.getString("Name"),
//						rs.getString("E.-Nummer"), rs.getString("ER"), rs.getString("PR"), rs.getString("Her2/neu"), 
//						rs.getString("Tumorart"), rs.getString("T"), rs.getString("N"), rs.getString("M"), 
//						rs.getInt("G"), rs.getInt("L"), rs.getInt("R"), rs.getInt("V"));
//				
//				table.getItems().add(data);
//			}
			
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
			
		} catch (SQLException e) {
			System.err.println(e + "\nFehler beim Statement/ResultSet in executeStatement() in " + getClass().getName());
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
			cmbBox_Statement.getSelectionModel().selectFirst();
		}
		
	}
	
}
