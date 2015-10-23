package fx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import tableMasks.Patientendaten;

public class controller_Main implements Initializable {
	
	@FXML public AnchorPane centerPanel;
	@FXML public ProgressBar progressBar;
	@FXML public Label lblConnected;
	@FXML public TableView<Patientendaten> tabelle_Patientendaten;
	@FXML public AnchorPane tablePane;
	@FXML public AnchorPane insertPane;
	@FXML public AnchorPane random;
	
	//textFields Patientendaten
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		lblConnected.setVisible(false);
		System.out.println("init");
		
		AnchorPane panel = null;
		try {
			panel = FXMLLoader.load(getClass().getResource("/fx/layouts/panelPatientendaten.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			//System.out.println(e);
		}
		
		System.out.println(random.getParent() == centerPanel);
		
		//centerPanel.getChildren().addAll(panel);
		centerPanel.getChildren().setAll(panel);
		panel.prefWidthProperty().bind(centerPanel.widthProperty());
		panel.prefHeightProperty().bind(centerPanel.heightProperty());
		
		Task<Boolean> task_connect = FX_Main.connect(lblConnected);
		new Thread(task_connect).start();
		
		//tabelle_Patientendaten.getColumns().addAll(Patientendaten.getColumns());
		
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
