package fx;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FX_Window extends Application {

	public static Stage window;
	private AnchorPane rootScene;
	@FXML
	private AnchorPane centerPanel;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		window = primaryStage;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FX_Window.class.getResource("/fx/layouts/rootScene.fxml"));
		rootScene = loader.load();
		
		Scene scene = new Scene(rootScene);	
		
		window.setScene(scene);
		window.setTitle("Tumordatenbank");
		window.show();
		
		window.setMinWidth(565);
		window.setMinHeight(550);
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
}
