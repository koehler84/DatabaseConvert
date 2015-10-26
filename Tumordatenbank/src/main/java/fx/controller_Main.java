package fx;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class controller_Main implements Initializable {
	
	@FXML public AnchorPane centerPanel;
	@FXML public ProgressBar progressBar;
	@FXML public Label lblConnected;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		lblConnected.setVisible(false);
		System.out.println("init");
		
		centerPanel.getChildren().setAll(controller_Logo.mainPanel);
		controller_Logo.mainPanel.prefWidthProperty().bind(centerPanel.widthProperty());
		controller_Logo.mainPanel.prefHeightProperty().bind(centerPanel.heightProperty());
		
//		centerPanel.getChildren().setAll(controller_Patientendaten.mainPanel);
//		controller_Patientendaten.mainPanel.prefWidthProperty().bind(centerPanel.widthProperty());
//		controller_Patientendaten.mainPanel.prefHeightProperty().bind(centerPanel.heightProperty());
		
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
	
	public void setCenterPanel_Patientendaten() {
		centerPanel.getChildren().setAll(controller_Patientendaten.mainPanel);
		controller_Patientendaten.mainPanel.prefWidthProperty().bind(centerPanel.widthProperty());
		controller_Patientendaten.mainPanel.prefHeightProperty().bind(centerPanel.heightProperty());
	}
	
	public void setCenterPanel_Faelle() {
		centerPanel.getChildren().setAll(controller_Fall.mainPanel);
		controller_Fall.mainPanel.prefWidthProperty().bind(centerPanel.widthProperty());
		controller_Fall.mainPanel.prefHeightProperty().bind(centerPanel.heightProperty());
	}
	
	public void setCenterPanel_SQLManager() {
		centerPanel.getChildren().setAll(controller_SQLManager.mainPanel);
		controller_SQLManager.mainPanel.prefWidthProperty().bind(centerPanel.widthProperty());
		controller_SQLManager.mainPanel.prefHeightProperty().bind(centerPanel.heightProperty());
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
