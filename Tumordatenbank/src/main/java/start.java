import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.*;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class start {
	
	public static Connection cn;
	public static MainWindow UIFenster1;
	public static boolean methodsCompleted;
	public static int recordsToRead;
	private static boolean readExcelToPatientendaten;
	private static boolean readExcelToFall;
	private static boolean spaltenFehler;
	
	static void excelToPatient(XSSFSheet sheet) {
		//TODO
		if (spaltenFehler) return;
		
		Iterator<Row> itr = sheet.iterator();
		Row row = itr.next();
		int[][] positions = {{-1,-1,-1,-1,-1,-1,-1,-1,-1},{1,2,3,4,5,6,7,8}};
		//int[][] positions = {{3,4,5,6,7,8,9,10},{1,2,3,4,5,6,7,8}};
		
		for (int i = row.getFirstCellNum(); i <= row.getLastCellNum() && positions[0][7] == -1; i++) {
			
			Cell cell = row.getCell(i);
			
			switch (cell.getStringCellValue().toLowerCase()) {
			case "geburtsdatum": positions[0][0] = i; break;
			case "vorname": positions[0][1] = i; break;
			case "name": positions[0][2] = i; break;
			case "strasse": positions[0][3] = i; break;
			case "hausnummer": positions[0][4] = i; break;
			case "land": positions[0][5] = i; break;
			case "plz": positions[0][6] = i; break;
			case "ort": positions[0][7] = i; break;
			case "eingangsdatum": positions[0][8] = i;break;
			}
			
		}
		
		for (int is : positions[0]) {
			if (is == -1) {
				System.out.println("Spaltennamen wurden nicht gefunden!");
				JOptionPane.showMessageDialog(start.UIFenster1, "Einige Datenfelder konnten nicht gefunden werden.\n"
						+ "Bitte überprüfen sie die Spaltennamen der Excel-Datei!", "Fehler in Excel Datei", JOptionPane.ERROR_MESSAGE);
				spaltenFehler = true;
				return;
			}
		}
		
		try {

			PreparedStatement Pst = cn.prepareStatement("insert into patientendaten (`Geburtsdatum`, `Vorname`, `Name`,"
					+ " `Strasse`, `Hausnummer`, `Land`, `PLZ`, `Ort`, `Fehler`) values ( ? , ? , ? , ? , ? , ? , ? , ? , ? );");

			int i = 0;	//iterator
			
			//int[][] positions = {{3,4,5,6,7,8,9,10},{1,2,3,4,5,6,7,8}};

			while (itr.hasNext() && i < recordsToRead) {

				i++;
				
				UIFenster1.progressBar.setValue(UIFenster1.progressBar.getValue()+1);
				row = itr.next();
				// Iterating over each column of Excel file
				
				Pst.clearParameters();		//clear parameters in Pst for next insert
				Cell cell = null;
				Pst.setInt(9, 0);
				
				for (int j=0; j < positions[1].length;j++) {
					cell = row.getCell(positions[0][j]);

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						Pst.setString(positions[1][j], cell.getStringCellValue());						
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (positions[1][j] == 1){
							Date geburtsdatum = new java.sql.Date(cell.getDateCellValue().getTime());
							cell = row.getCell(positions[0][8]);
							Date eingangsdatum = new java.sql.Date(cell.getDateCellValue().getTime());
							@SuppressWarnings("deprecation")
							Date datum1 = new Date(0, 0, 1);
							@SuppressWarnings("deprecation")
							Date datum2 = new Date(100, 0, 1);
							
							if (!(geburtsdatum.equals(eingangsdatum) || geburtsdatum.equals(datum1) || geburtsdatum.equals(datum2))) {
								Pst.setString(1, geburtsdatum + "");
							} else {
								Pst.setString(positions[1][j], "0001-01-01");
								System.out.println("Fehler: Geburtsdatum ist fehlerhaft!");
								Pst.setInt(9, 1);
							}
							cell = row.getCell(positions[0][j]);
						} else if (positions[1][j] == 7) {
							//PLZ als String speichern
							Pst.setString(7, ((int)cell.getNumericCellValue()) + "");
						} else {
							Pst.setInt(positions[1][j], (int)cell.getNumericCellValue());
						}
						break;
					case Cell.CELL_TYPE_BLANK:
						if (positions[1][j] == 1) {
							Pst.setString(positions[1][j], "0001-01-01");
							System.out.println("Fehler: Geburtsdatum fehlt!");
							Pst.setInt(9, 1);
							break;
						} else if (positions[1][j] == 2 || positions[1][j] == 3) {
							Pst.setString(positions[1][j], "INVALID_NAME");
							System.out.println("Fehler: Vorname oder Nachmame fehlt!");
							Pst.setInt(9, 1);
							break;
						} else {
							Pst.setInt(9, 1);
						}
						
						//Abfrage in der Datenbank: "select * from mydb.patientendaten where PLZ is null;"
						Pst.setNull(positions[1][j], java.sql.Types.NULL);
						break;
					}
					
				}
				
				try {
					//Execution of PreparedStatement, SQL Exeption if person is already in database
					System.out.println("Updated rows in mydb.patientendaten: " + Pst.executeUpdate());
				} catch (SQLException se){
					System.out.println("Fehler beim Ausführen von \"insert into patientendaten\": Person ggf. schon erfasst!");
				}
				
			}
			
			Pst.close();
			System.out.println("Write patientendaten success");
			System.out.println();
		} catch (SQLException e) {
			System.out.println("Fehler beim Erstellen des PreparedStatement \"insert into patientendaten\"!");
		}

	}

	static void excelToFall(XSSFSheet sheet) {
		//TODO
		if (spaltenFehler) return;
		
		Iterator<Row> itr = sheet.iterator();
		Row row = itr.next();
		int[][] positions = {{-1,-1,-1,-1,-1,-1,-1,-1},{1,2,3,4,5,6,7}};
		//int[][] positions = {{0,1,2,3,4,5,26},{1,2,3,4,5,6,7}};
		
		//i:	0,1,2,3,4,5,26		oben: Spalte in excel datei
		//		4,5,6,3,2,1,7		unten: Position in Pst
		
		for (int i = row.getFirstCellNum(); i <= row.getLastCellNum() && positions[0][7] == -1; i++) {
			
			Cell cell = row.getCell(i);
			
			switch (cell.getStringCellValue().toLowerCase()) {
			case "geburtsdatum": positions[0][0] = i; break;
			case "vorname": positions[0][1] = i; break;
			case "name": positions[0][2] = i; break;
			case "eingangsdatum": positions[0][3] = i; break;
			case "e.-nummer": positions[0][4] = i; break;
			case "einsender": positions[0][5] = i; break;
			case "befundtyp": positions[0][6] = i; break;
			case "befundtext": positions[0][7] = i; break;
			}
			
		}
		
		for (int is : positions[0]) {
			if (is == -1) {
				System.out.println("Spaltennamen wurden nicht gefunden!");
				JOptionPane.showMessageDialog(start.UIFenster1, "Einige Datenfelder konnten nicht gefunden werden.\n"
						+ "Bitte überprüfen sie die Spaltennamen der Excel-Datei!", "Fehler in Excel Datei", JOptionPane.ERROR_MESSAGE);
				spaltenFehler = true;
				return;
			}
		}
		
		try {
			
			PreparedStatement Pst_Fall = cn.prepareStatement("insert into mydb.fall (`Patientendaten_PatientenID`, `Eingangsdatum`, "
					+ "`E.-Nummer`, `Einsender`, `Befundtyp`, `Fehler`) values "
					+ "((select PatientenID from mydb.patientendaten where Geburtsdatum = ? and Vorname = ? and Name = ? ),"
					+ " ? , ? , ? , ? , ? );");
			PreparedStatement Pst_Klassifikation = cn.prepareStatement("insert into mydb.klassifikation (`Fall_E.-Nummer`, `Fall_Befundtyp`, "
					+ "G, T, N, M, L, V, R, ER, PR, `Her2/neu`, Lage, Tumorart) "
					+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? );");
			
			int k = 0;	//iterator
			
			while (itr.hasNext() && k < recordsToRead) {

				k++;

				UIFenster1.progressBar.setValue(UIFenster1.progressBar.getValue()+1);
				row = itr.next();
				// Iterating over each column of Excel file
				Pst_Fall.clearParameters();
				Pst_Klassifikation.clearParameters();
				Cell cell = null;
				Pst_Fall.setInt(8, 0);
				
				String E_NR = null;
				Befundtyp befundtyp = null;
				
				for (int j=0; j < positions[1].length;j++) {
					cell = row.getCell(positions[0][j]);
					
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						if (positions[1][j] == 7) {
							befundtyp = Befundtyp.getBefundtyp(cell.getStringCellValue());
							if (befundtyp == null) {
								Pst_Fall.setInt(8, 1);
								befundtyp = Befundtyp.Fehler;
							}
							Pst_Fall.setInt(7, befundtyp.getValue());
						} else {
							if (positions[1][j] == 5) E_NR = cell.getStringCellValue();
							Pst_Fall.setString(positions[1][j], cell.getStringCellValue());
						}
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (positions[1][j] == 1 || positions[1][j] == 4){
							//Eingangsdatum bzw Geburtsdatum
							Pst_Fall.setString(positions[1][j], new java.sql.Date(cell.getDateCellValue().getTime())+"");
						} else {
							//Befundtyp - nope der steht als Text in excel
							Pst_Fall.setInt(positions[1][j], (int)cell.getNumericCellValue());
						}
						break;
					case Cell.CELL_TYPE_BLANK:
						if (positions[1][j] == 5) {		//E.-Nummer fehlt
							Pst_Fall.setString(positions[1][j], "INVALID");
							Pst_Fall.setInt(8, 1);
							break;
						} else if (positions[1][j] == 7) {	//Befundtyp fehlt
							Pst_Fall.setInt(positions[1][j], Befundtyp.Fehler.getValue());
							befundtyp = Befundtyp.Fehler;
							Pst_Fall.setInt(8, 1);
							break;
						} else {
							Pst_Fall.setInt(8, 1);
						}
						
						Pst_Fall.setNull(positions[1][j], java.sql.Types.NULL);
						break;
					}
				}
				
				try {
					System.out.print("Updated rows in mydb.fall: " + Pst_Fall.executeUpdate() + " - ");
				} catch (SQLException e) {
					//e.printStackTrace();
					System.out.print("Fehler beim Ausführen von \"insert into fall\": Fall ggf. doppelt!" + " ");
				}
				
				cell = row.getCell(positions[0][7]);
				String befundtext = cell.getStringCellValue();
				
				excelToKlassifikation(Pst_Klassifikation, befundtext, E_NR, befundtyp);
				
			}
			
			Pst_Fall.close();
			Pst_Klassifikation.close();
			System.out.println("Write fall success");
			System.out.println();
		} catch (SQLException SQLex) {
			System.out.println("Fehler beim Erstellen des PreparedStatement \"insert into fall\"!");
		}

	}
	
	private static void excelToKlassifikation(PreparedStatement Pst, String befundtext, String E_Nr, Befundtyp befundtyp) throws SQLException {
		
		StringReader srObject = new StringReader();
		try {
			srObject = new StringReader(befundtext);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Objektfehler!");
			return;
			//e.printStackTrace();
		}
		
		Pst.setString(1, E_Nr);
		Pst.setInt(2, befundtyp.getValue());
		Pst.setString(4, srObject.T);
		Pst.setString(5, srObject.N);
		Pst.setString(6, srObject.M);
		Pst.setString(10, srObject.ER);
		Pst.setString(11, srObject.PR);
		Pst.setString(12, srObject.her2_Neu);
		Pst.setString(13, srObject.lage);
		Pst.setString(14, srObject.tumorart);
		
		if (srObject.G != 9) {
			Pst.setInt(3, srObject.G);
		} else {
			Pst.setNull(3, java.sql.Types.NULL);
		}
		
		if (srObject.L != 9) {
			Pst.setInt(7, srObject.L);
		} else {
			Pst.setNull(7, java.sql.Types.NULL);
		}
		
		if (srObject.V != 9) {
			Pst.setInt(8, srObject.V);
		} else {
			Pst.setNull(8, java.sql.Types.NULL);
		}
		
		if (srObject.R != 9) {
			Pst.setInt(9, srObject.R);
		} else {
			Pst.setNull(9, java.sql.Types.NULL);
		}
		
		try {
			System.out.println("Einfügen in Klassifikation, geänderte Zeilen: " + Pst.executeUpdate());
		} catch (Exception e) {
			System.out.println("Fehler beim Einfügen der Falldaten.");
		}
		
	}
	
	static void restart() {
		
		String excelPath="";
		excelPath = "C://Project Pathologie/test.xlsx";
		
		UIFenster1.progressBar.setValue(0);
		methodsCompleted = false;
		
		try {
			
			File excel = null;
			FileInputStream fis = null;
			XSSFSheet sheet = null;
			XSSFWorkbook book = null;
			
			if (readExcelToPatientendaten || readExcelToFall) {
				excel = new File(excelPath);
				fis = new FileInputStream(excel);
				book = new XSSFWorkbook(fis);
				sheet = book.getSheetAt(0);
				book.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
			}
			
			//Alle Reihen lesen: sheet.getPhysicalNumberOfRows()
			recordsToRead = 15;
			
			if (readExcelToPatientendaten && readExcelToFall) {
				UIFenster1.progressBar.setIndeterminate(false);
				UIFenster1.progressBar.setMaximum(recordsToRead*2);
				excelToPatient(sheet);
				excelToFall(sheet);
				book.close();
				fis.close();
			} else if (readExcelToPatientendaten && !readExcelToFall) {
				UIFenster1.progressBar.setIndeterminate(false);
				UIFenster1.progressBar.setMaximum(recordsToRead);
				excelToPatient(sheet);
				book.close();
				fis.close();
			} else if (readExcelToFall && !readExcelToPatientendaten) {
				UIFenster1.progressBar.setIndeterminate(false);
				UIFenster1.progressBar.setMaximum(recordsToRead);
				excelToFall(sheet);
				book.close();
				fis.close();
			}
			
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Fehler: Irgendetwas stimmt mit der Datei nicht!");
		}
		
		methodsCompleted = true;
		UIFenster1.DBtoTable_Patientendaten();
		UIFenster1.DBtoTable_Fall();
		
		try {
			if (cn != null && !cn.isClosed() && !UIFenster1.isShowing()) {
				cn.close();
				System.out.println("Datenbankverbindung beednet! STARTCLASS");
			}
		} catch (SQLException e) {
			System.out.println("Fehler beim Beenden der Datenbankverbindung!");
		}
		
		if (!readExcelToPatientendaten && !readExcelToFall) {
			UIFenster1.progressBar.setIndeterminate(false);
			UIFenster1.progressBar.setValue(UIFenster1.progressBar.getMaximum());
		}
		
	}
	
	public static void main(String[] args) {
		
		String dbPatTbl=null, dbFallTbl=null, dbDrv=null, dbUrl=null, dbUsr="", dbPwd="", excelPath="";
		//-----------------------------------
		//Workpath
		//-----------------------------------
		dbPatTbl = "patientendaten";
		dbFallTbl = "fall";
		excelPath = "C://Project Pathologie/test.xlsx";
		//-----------------------------------
		//DB connection data
		//-----------------------------------
		dbDrv = "com.mysql.jdbc.Driver";
		dbUrl = "jdbc:mysql://localhost:3306/mydb";
		dbUsr = "java";
		dbPwd = "geheim";
		
		UIFenster1 = new MainWindow();
		UIFenster1.progressBar.setIndeterminate(true);
		
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

		//Validate connection data
		if( dbPatTbl == null || dbPatTbl.length() == 0 ||
				dbFallTbl == null || dbFallTbl.length() == 0 ||
				dbDrv == null || dbDrv.length() == 0 ||
				dbUrl == null || dbUrl.length() == 0 ) {
			System.out.println( "Fehler: Parameter fehlt." );
			return;
		}
		
		try {
			// Select fitting database driver and connect:
	/*???	*/Class.forName( dbDrv );
			cn = DriverManager.getConnection( dbUrl, dbUsr, dbPwd );
			UIFenster1.lblConnected.setVisible(true);
			UIFenster1.DBtoTable_Patientendaten();

		} catch ( Exception ex ) {
			System.out.println( ex );
		}

		//----------------------------------------------------
		
		readExcelToPatientendaten = true;
		readExcelToFall = true;
		
		try {
			
			File excel = null;
			FileInputStream fis = null;
			XSSFSheet sheet = null;
			XSSFWorkbook book = null;
			
			if (readExcelToPatientendaten || readExcelToFall) {
				excel = new File(excelPath);
				fis = new FileInputStream(excel);
				book = new XSSFWorkbook(fis);
				sheet = book.getSheetAt(0);
				book.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
			}
			
			//Alle Reihen lesen: sheet.getPhysicalNumberOfRows()
			recordsToRead = 15;
			
			if (readExcelToPatientendaten && readExcelToFall) {
				UIFenster1.progressBar.setIndeterminate(false);
				UIFenster1.progressBar.setMaximum(recordsToRead*2);
				excelToPatient(sheet);
				excelToFall(sheet);
				book.close();
				fis.close();
			} else if (readExcelToPatientendaten && !readExcelToFall) {
				UIFenster1.progressBar.setIndeterminate(false);
				UIFenster1.progressBar.setMaximum(recordsToRead);
				excelToPatient(sheet);
				book.close();
				fis.close();
			} else if (readExcelToFall && !readExcelToPatientendaten) {
				UIFenster1.progressBar.setIndeterminate(false);
				UIFenster1.progressBar.setMaximum(recordsToRead);
				excelToFall(sheet);
				book.close();
				fis.close();
			}
			
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			System.out.println("Fehler: Irgendetwas stimmt mit der Datei nicht!");
		}
		
		methodsCompleted = true;
		UIFenster1.DBtoTable_Patientendaten();
		UIFenster1.DBtoTable_Fall();
		
		try {
			if (cn != null && !cn.isClosed() && !UIFenster1.isShowing()) {
				cn.close();
				System.out.println("Datenbankverbindung beednet! STARTCLASS");
			}
		} catch (SQLException e) {
			System.out.println("Fehler beim Beenden der Datenbankverbindung!");
		}
		
		if (!readExcelToPatientendaten && !readExcelToFall) {
			UIFenster1.progressBar.setIndeterminate(false);
			UIFenster1.progressBar.setValue(UIFenster1.progressBar.getMaximum());
		}
		
	}

	//http://download.eclipse.org/egit/github/updates-nightly/ <- GITHUB Task manager (über help -> install new software)

}
