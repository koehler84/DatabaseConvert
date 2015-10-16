package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FX_Window extends Application {

	private AnchorPane rootScene;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FX_Window.class.getResource("/fx/layouts/rootScene.fxml"));
		rootScene = loader.load();
		
		Scene scene = new Scene(rootScene);	
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Tumordatenbank");
		primaryStage.show();
		
		primaryStage.setMinWidth(565);
		primaryStage.setMinHeight(550);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
