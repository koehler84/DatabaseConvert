package fx;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class controller_SQLManager implements Initializable {

	@FXML public static AnchorPane mainPanel;
	@FXML public TextField txtField_Statement;
	@SuppressWarnings("rawtypes")
	@FXML public TableView table;
	@SuppressWarnings("rawtypes")
	private ObservableList<ObservableList> data;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		System.out.println("controller SQLManager");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void executeStatement() {
		Statement st = null;
		ResultSet res = null;
		ResultSetMetaData resMeta = null;
		data = FXCollections.observableArrayList();
		
		/**********************************
		* TABLE COLUMN ADDED DYNAMICALLY *
		**********************************/
		try {
			st = FX_Main.cn.createStatement();
			res = st.executeQuery(txtField_Statement.getText());
			resMeta = res.getMetaData();
		} catch (SQLException e) {
			System.err.println(e + "\nFehler beim Statement/ResultSet in executeStatement() in " + getClass().getName());
			return;
		}
		
		table.getColumns().clear();
		
//		try {
//			
//			for (int i = 0; i < resMeta.getColumnCount(); i++) {
//				
//				final int j = i;
//				TableColumn column = new TableColumn(resMeta.getColumnName(i+1));
//				column.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
//					@Override
//					public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
//						return new SimpleStringProperty(param.getValue().get(j).toString());
//					}
//				});
//				
//				table.getColumns().addAll(column);
//			}
//			
//			/********************************
//			* Data added to ObservableList *
//			********************************/
//			while (res.next()) {
//				
//				ObservableList<String> rowData = FXCollections.observableArrayList();
//				for (int i = 1; i < resMeta.getColumnCount(); i++) {
//					rowData.add(res.getString(i));
//				}				
//				data.add(rowData);
//				
//			}
//			
//			//table.setItems(data);
//			table.getItems().add(data);
//		} catch (Exception e) {
//			System.err.println("Fehler beim Erstellen der dynamischen Tabelle");
//			System.out.println(e);
//			return;
//		}
		
	}

}
