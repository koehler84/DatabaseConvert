package fx;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.property.Property;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import main.start;

public class controller implements Initializable {
	
	@FXML
	private AnchorPane centerPanel;
	@FXML
	public ProgressBar progressBar;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void abbrechen_Button(ActionEvent e) {
//		try {
//			if (start.cn != null && !start.cn.isClosed() && start.methodsCompleted) {
//				start.cn.close();
//				System.out.println("Datenbankverbindung beednet! WINDOW");
//			}
//		} catch (SQLException e1) {
//			System.out.println("Fehler beim Beenden der Datenbankverbindung!");
//		}
		
		System.exit(0);
	}
	
	public void ok_Button(ActionEvent e) {
		FX_Window.window.close();
				
//		try {
//			if (start.cn != null && !start.cn.isClosed() && start.methodsCompleted) {
//				start.cn.close();
//				System.out.println("Datenbankverbindung beednet! WINDOW");
//			}
//		} catch (SQLException e1) {
//			System.out.println("Fehler beim Beenden der Datenbankverbindung!");
//		}
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

}
