package fx;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class controller implements Initializable {
	
	@FXML
	private AnchorPane centerPanel;
	@FXML
	public ProgressBar progressBar;
	@FXML
	public Label lblConnected;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		lblConnected.setVisible(false);
		System.out.println("init");
		
		new Thread(FX_Main.connect(lblConnected)).start();
		
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				//start methods
				return null;
			}
		};
		
		Thread thread = new Thread(task);
		thread.start();
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
		//progressBar.setProgress(-1);
		
		progressBar.progressProperty().unbind();
		
		final Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
//				testMain.otherTask();
				
				for (long i = 0; i < 1000000; i++) {
					System.out.println("Erg: " + i);
					updateProgress(i, 1000000);
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
		
		new Thread(FX_Main.start(file.getPath())).start();
	}

}
