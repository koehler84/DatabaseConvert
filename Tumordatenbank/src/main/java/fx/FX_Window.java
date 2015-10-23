package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FX_Window extends Application {

	public static Stage window;
	private AnchorPane rootScene;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		window = primaryStage;
		
//		FXMLLoader loader2 = new FXMLLoader();
//		loader2.setLocation(FX_Window.class.getResource("/fx/layouts/panelPatientendaten.fxml"));
//		loader2.load();
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FX_Window.class.getResource("/fx/layouts/rootScene.fxml"));
		rootScene = loader.load();
		
		Scene scene = new Scene(rootScene);	
		
		window.setScene(scene);
		window.setTitle("Tumordatenbank");
		window.show();
		
		window.setMinWidth(600);
		window.setMinHeight(550);
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
}
