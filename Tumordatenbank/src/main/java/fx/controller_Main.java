package fx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import tableMasks.Patientendaten;

public class controller_Main implements Initializable {
	
	@FXML public AnchorPane centerPanel;
	@FXML public ProgressBar progressBar;
	@FXML public Label lblConnected;
	@FXML public TableView<Patientendaten> tabelle_Patientendaten;
		
	//textFields Patientendaten
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		lblConnected.setVisible(false);
		System.out.println("init");
		
		AnchorPane panelPatientendaten = null;
		try {
			panelPatientendaten = FXMLLoader.load(getClass().getResource("/fx/layouts/panelPatientendaten.fxml"));
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println(e + " - loading panelPatientendaten in " + getClass().getName());
		}
		
		centerPanel.getChildren().setAll(panelPatientendaten);
		panelPatientendaten.prefWidthProperty().bind(centerPanel.widthProperty());
		panelPatientendaten.prefHeightProperty().bind(centerPanel.heightProperty());
		
		Task<Boolean> task_connect = FX_Main.connect(lblConnected);
		new Thread(task_connect).start();
		
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
		
		progressBar.progressProperty().bind(task.progressProperty());
		Thread thread = new Thread(task);
		thread.start();
				
	}
	
	public void datenAnalyse() {
		
		final File file = new FileChooser().showOpenDialog(FX_Window.window);
		
		if (file != null && file.exists()) {
			
			Task<XSSFSheet> loadSheet = FX_Main.loadExcel(file);
			progressBar.setProgress(-1);
			new Thread(loadSheet).start();
			
			Task<Void> startTask = FX_Main.excelToPatient(loadSheet);
			progressBar.progressProperty().bind(startTask.progressProperty());
			new Thread(startTask).start();
			
		} else {
			//user nerven
		}
		
	}
	
}
