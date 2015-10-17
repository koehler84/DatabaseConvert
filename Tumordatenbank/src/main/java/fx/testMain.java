package fx;

public class testMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		new Thread() {
			public void run() {
				FX_Window.launch(FX_Window.class);
			};
		}.start();
		
		for (long i = 0; i < 100000; i++) {
			System.out.println("Erg: " + Math.sqrt(9));
		}
		
	}
	
}
