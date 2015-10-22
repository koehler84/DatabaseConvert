package fx;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import tableMasks.Patientendaten;

public class controller implements Initializable {
	
	@FXML private AnchorPane centerPanel;
	@FXML public ProgressBar progressBar;
	@FXML public Label lblConnected;
	@FXML public TableView<Patientendaten> tabelle_Patientendaten;
	@FXML public AnchorPane tablePane;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		lblConnected.setVisible(false);
		System.out.println("init");
		
		Task<Boolean> task_connect = FX_Main.connect(lblConnected);
		new Thread(task_connect).start();
		buildTable_Patientendaten();
		try {
			System.out.println(task_connect.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void buildTable_Patientendaten() {
		
		TableColumn<Patientendaten, String> column_Gebdatum = new TableColumn<>("Geburtsdatum");
		column_Gebdatum.setCellValueFactory(new PropertyValueFactory<Patientendaten, String>("geburtsdatum"));
		
		TableColumn<Patientendaten, String> column_Vorname = new TableColumn<>("Vorname");
		column_Vorname.setCellValueFactory(new PropertyValueFactory<Patientendaten, String>("vorname"));
		
		TableColumn<Patientendaten, String> column_Name = new TableColumn<>("Nachname");
		column_Name.setCellValueFactory(new PropertyValueFactory<Patientendaten, String>("name"));
				
		tabelle_Patientendaten.getColumns().addAll(column_Gebdatum, column_Vorname, column_Name);
		
	}

	public void abbrechen_Button(ActionEvent e) {
		try {
			if (FX_Main.cn != null && !FX_Main.cn.isClosed() && FX_Main.methodsCompleted) {
				FX_Main.cn.close();
				System.out.println("Datenbankverbindung beednet! WINDOW");
			}
		} catch (SQLException e1) {
			System.out.println("Fehler beim Beenden der Datenbankverbindung!");
		}
		
		System.exit(0);
	}
	
	public void ok_Button(ActionEvent e) {				
		try {
			if (FX_Main.cn != null && !FX_Main.cn.isClosed() && FX_Main.methodsCompleted) {
				FX_Main.cn.close();
				System.out.println("Datenbankverbindung beednet! WINDOW");
			}
		} catch (SQLException e1) {
			System.out.println("Fehler beim Beenden der Datenbankverbindung!");
		}
		
		FX_Window.window.close();
	}
	
	public void setProgress() {
		progressBar.progressProperty().unbind();
		
		final Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				
				for (long i = 0; i < 300000; i++) {
					System.out.println("Erg: " + i);
					updateProgress(i, 300000);
				}
				
				return null;
			}
		};
		
//		service.restart();
		
		progressBar.progressProperty().bind(task.progressProperty());
		Thread thread = new Thread(task);
		thread.start();
				
	}
	
	public void datenAnalyse() {
		
		FileChooser fc = new FileChooser();
		File file = fc.showOpenDialog(FX_Window.window);
		
		if (file != null && file.exists()) {			
			new Thread(FX_Main.loadExcel(file)).start();
			Task<Void> startTask = FX_Main.start(file.getPath());
			progressBar.progressProperty().bind(startTask.progressProperty());
			new Thread(startTask).start();
		}
		
	}
	
	public void DBtoTable_Patientendaten() {
		
		ObservableList<Patientendaten> old_data = tabelle_Patientendaten.getItems();
		ObservableList<Patientendaten> new_data = FXCollections.observableArrayList();
		boolean success = false;
		
		try {
			Statement st = FX_Main.cn.createStatement();
			ResultSet res = st.executeQuery("select * from mydb.vPatientendaten_Hauptparameter where `Fehler` != 0");
			
			while (res.next()) {				
				Patientendaten pat = new Patientendaten(res.getDate("Geburtsdatum").toString(), 
						res.getString("Vorname"), res.getString("Name"));
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
	
}
