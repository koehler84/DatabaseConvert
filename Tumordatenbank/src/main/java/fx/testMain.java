package fx;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

public class testMain {

	static FX_Window window;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//FX_Window.launch(FX_Window.class, args);
		
		Thread t1 = new Thread(new thread());
		
		t1.start();
		
		System.out.println("bla");
		
		FX_Window.window.toBack();
		
	}
	
	public static void otherTask() {
		
		for (long i = 0; i < 1000000; i++) {
			System.out.println("Erg: " + i);
		}
		
	}
	
}
