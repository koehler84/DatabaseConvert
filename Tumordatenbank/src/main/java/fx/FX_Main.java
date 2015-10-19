package fx;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class FX_Main {
	
	public static Connection cn;
	public static boolean methodsCompleted;
	public static int recordsToRead;
	public static boolean readExcelToPatientendaten;
	public static boolean readExcelToFall;
	public static boolean spaltenFehler;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		methodsCompleted = true;
		
		FX_Window.launch(FX_Window.class, args);
		
		try {
			if (FX_Main.cn != null && !FX_Main.cn.isClosed() && FX_Main.methodsCompleted) {
				FX_Main.cn.close();
				System.out.println("Datenbankverbindung beednet! WINDOW");
			}
		} catch (SQLException e) {
			System.out.println("Fehler beim Beenden der Datenbankverbindung!");
		}
		
		System.out.println();
		System.out.println("Window closed!");
	}
	
	public static Task<Boolean> connect(final Label label) {
		
		Task<Boolean> task = new Task<Boolean>() {

			@Override
			protected Boolean call() throws Exception {
				// TODO Auto-generated method stub
				
				String dbDrv="", dbUrl="", dbUsr="", dbPwd="";
				
				//-----------------------------------
				//DB connection data
				//-----------------------------------
				dbDrv = "com.mysql.jdbc.Driver";
				dbUrl = "jdbc:mysql://localhost:3306/mydb";
				dbUsr = "java";
				dbPwd = "geheim";
				
				//-----------------------------------
				//Um das zu connection mit localhost zu beschleunigen  kannst das auskommentieren,
				//ist dafür da, das es auf allen meinen rechnern parallel mit einer datenbank funktioniert
				//-----------------------------------
				try {
					Socket socket = new Socket ();
					socket.connect(new InetSocketAddress("192.168.178.22", 3306), 200 );
					socket.close();
					dbUrl = "jdbc:mysql://192.168.178.22:3306/mydb";
				} catch (Exception e) {
					//System.out.println(e);
				}
				
				try {
					// Select fitting database driver and connect:
					/*???	*/Class.forName( dbDrv );
					cn = DriverManager.getConnection( dbUrl, dbUsr, dbPwd );
					label.setVisible(true);
					//UIFenster1.DBtoTable_Patientendaten();
					return true;
				} catch ( Exception ex ) {
					System.out.println( ex );
					return false;
				}
				
			}
		}; 
		
		return task;		
	}
	
	public static Task<Void> start(final String path) {
		
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				
				//excelToPatient
				System.out.println("excelToPatient - Path: " + path);
				
				FX_Main.methodsCompleted = true;
				
				return null;
			}
			
		};
		
		return task;
	}
	
}
