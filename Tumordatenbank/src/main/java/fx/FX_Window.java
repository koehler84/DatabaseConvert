package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FX_Window extends Application {

	public static Stage window;
	private AnchorPane rootScene;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		long start = System.currentTimeMillis();
		
		window = primaryStage;
		
		//TODO load subscenes, maybe put into thread/task
		controller_Patientendaten.setMainPanel(FXMLLoader.load(getClass().getResource("/fx/layouts/panelPatientendaten.fxml")));
		controller_Fall.setMainPanel(FXMLLoader.load(getClass().getResource("/fx/layouts/panelFall.fxml")));
		controller_SQLManager.setMainPanel(FXMLLoader.load(getClass().getResource("/fx/layouts/panelSQLManager.fxml")));
		controller_Logo.setMainPanel(FXMLLoader.load(getClass().getResource("/fx/layouts/panelLogo.fxml")));
		
		//load child scenes before root
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FX_Window.class.getResource("/fx/layouts/MainPanel.fxml"));
		rootScene = loader.load();
		
		Scene scene = new Scene(rootScene);	
		
		window.setScene(scene);
		window.setTitle("Tumordatenbank");
		window.show();
		
		window.setMinWidth(600);
		window.setMinHeight(550);
		window.getIcons().add(new Image("fx/media/Icon.png"));
		
		long zeit = System.currentTimeMillis() - start;
		System.out.println("Ben�tigte Zeit zum �ffnen: " + zeit);		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
}
