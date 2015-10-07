import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.*;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class start {
	
	public static Connection cn;
	public static correctParameters UIFenster1;
	public static boolean methodsCompleted;
	public static int recordsToRead;
	
	static void showDbTable(String dbTbl) {
		
		if(dbTbl == null) {
			System.out.println( "Fehler: Parameter fehlt." );
			return;
		}
		
		try {
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery( "select * from " + dbTbl );
			// Get meta data:
			ResultSetMetaData rsmd = rs.getMetaData();
			int i, n = rsmd.getColumnCount();
			// Print table content:
			for( i=0; i<n; i++ )
				System.out.print( "+---------------" );
			System.out.println( "+" );
			for( i=1; i<=n; i++ )    // Attention: first column with 1 instead of 0
				System.out.print( "| " + extendStringTo14( rsmd.getColumnName( i ) ) );
			System.out.println( "|" );
			for( i=0; i<n; i++ )
				System.out.print( "+---------------" );
			System.out.println( "+" );
			while( rs.next() ) {
				for( i=1; i<=n; i++ )  // Attention: first column with 1 instead of 0
					System.out.print( "| " + extendStringTo14( rs.getString( i ) ) );
				System.out.println( "|" );
			}
			for( i=0; i<n; i++ )
				System.out.print( "+---------------" );
			System.out.println( "+" );
		} catch( Exception ex ) {
			System.out.println( ex );
		}
		
	}

	static final String extendStringTo14( String s ) {
		// Extend String to length of 14 characters
		if( s == null ) { s = ""; }
		final String sFillStrWithWantLen = "              ";
		final int iWantLen = sFillStrWithWantLen.length();
		final int iActLen  = s.length();
		if( iActLen < iWantLen )
			return (s + sFillStrWithWantLen).substring( 0, iWantLen );
		if( iActLen > 2 * iWantLen )
			return s.substring( 0, 2 * iWantLen );
		return s;
	}

	static void excelToPatient(String excelPath, XSSFSheet sheet) {
		//TODO
		try {

			PreparedStatement Pst = cn.prepareStatement("insert into patientendaten (`Geburtsdatum`, `Vorname`, `Name`, `Strasse`, `Hausnummer`, `Land`, `PLZ`, `Ort`, `Fehler`)"
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? );");

			Iterator<Row> itr = sheet.iterator();
			if (itr.hasNext()) {
				itr.next();		//skipping the header row			
			}
			// Iterating over Excel file in Java

			int i = 0;	//iterator
			
			int[][] positions = {{3,4,5,6,7,8,9,10},{1,2,3,4,5,6,7,8}};

			while (itr.hasNext() && i<recordsToRead) {

				i++;
				
				UIFenster1.progressBar.setValue(UIFenster1.progressBar.getValue()+1);
				Row row = itr.next();
				// Iterating over each column of Excel file
				
				Pst.clearParameters();		//clear parameters in Pst for next insert
				Cell cell = null;
				Pst.setInt(9, 0);
				
				for (int j=0; j<positions[0].length;j++) {
					cell = row.getCell(positions[0][j]);

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						if (positions[0][j] != 9) {
							Pst.setString(positions[1][j], cell.getStringCellValue());
						} else {
							//PLZ is no int
							Pst.setInt(9, 1);
							Pst.setNull(7, java.sql.Types.NULL);
						}
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (positions[0][j]==3){
							Pst.setString(1, new java.sql.Date(cell.getDateCellValue().getTime()) + "");
						} else {
							Pst.setInt(positions[1][j], (int)cell.getNumericCellValue());
						}
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						if (cell.getBooleanCellValue()) {
							Pst.setString(positions[1][j], "True");
						} else {
							Pst.setString(positions[1][j], "False");
						}
						break;
					case Cell.CELL_TYPE_BLANK:
						
						if (positions[0][j] == 3) {
							Pst.setString(positions[1][j], "0001-01-01");
							System.out.println("Fehler: Geburtsdatum fehlt!");
							Pst.setInt(9, 1);
							break;
						} else if (positions[0][j] == 4 || positions[0][j] == 5) {
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

	static void excelToFall(String excelPath, XSSFSheet sheet) {
				
		try {
			
			PreparedStatement Pst_Fall = cn.prepareStatement("insert into mydb.fall (`Patientendaten_PatientenID`, `Eingangsdatum`, "
					+ "`E.-Nummer`, `Arzt`, `Befundtyp`, `Fehler`) values "
					+ "((select PatientenID from mydb.patientendaten where Name = ? and Vorname = ? and Geburtsdatum = ? ),"
					+ " ? , ? , ? , ? , ? );");
			PreparedStatement Pst_Klassifikation = cn.prepareStatement("insert into mydb.klassifikation (`Fall_E.-Nummer`, `Fall_Befundtyp`, "
					+ "G, T, N, M, L, V, R, ER, PR, `Her2/neu`, Lage, Tumorart) "
					+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? );");

			Iterator<Row> itr = sheet.iterator();
			if (itr.hasNext()) {
				itr.next();
			}
			// Iterating over Excel file in Java
			
			int[][] positions = {{0,1,2,3,4,5,26},{4,5,6,3,2,1,7}};
			
			//i:	0,1,2,3,4,5,26		oben: Spalte in excel datei
			//		4,5,6,3,2,1,7		unten: Position in Pst
			
			int k = 0;	//iterator
			
			while (itr.hasNext() && k<recordsToRead) {

				k++;

				UIFenster1.progressBar.setValue(UIFenster1.progressBar.getValue()+1);
				Row row = itr.next();
				// Iterating over each column of Excel file
				Pst_Fall.clearParameters();
				Pst_Klassifikation.clearParameters();
				Cell cell = null;
				Pst_Fall.setInt(8, 0);
				
				String E_NR = null;
				Befundtyp befundtyp = null;
				
				for (int j=0; j<positions[0].length;j++) {
					
					cell = row.getCell(positions[0][j]);
					
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						if (positions[0][j] == 26) {
							switch (cell.getStringCellValue())	{
								case "Hauptbefund":
									Pst_Fall.setInt(7, Befundtyp.Hauptbefund.getValue());
									befundtyp = Befundtyp.Hauptbefund;
									break;
								case "Nachbericht 1":
									Pst_Fall.setInt(7, Befundtyp.Nachbericht_1.getValue());
									befundtyp = Befundtyp.Nachbericht_1;
									break;
								case "Nachbericht 2":
									Pst_Fall.setInt(7, Befundtyp.Nachbericht_2.getValue());
									befundtyp = Befundtyp.Nachbericht_2;
									break;
								case "Korrekturbefund 1":
									Pst_Fall.setInt(7, Befundtyp.Korrekturbefund_1.getValue());
									befundtyp = Befundtyp.Korrekturbefund_1;
									break;
								case "Korrekturbefund 2":
									Pst_Fall.setInt(7, Befundtyp.Korrekturbefund_2.getValue());
									befundtyp = Befundtyp.Korrekturbefund_2;
									break;
								case "Korrekturbefund 3":
									Pst_Fall.setInt(7, Befundtyp.Korrekturbefund_3.getValue());
									befundtyp = Befundtyp.Korrekturbefund_3;
									break;
								case "Konsiliarbericht 1":
									Pst_Fall.setInt(7, Befundtyp.Konsiliarbericht_1.getValue());
									befundtyp = Befundtyp.Konsiliarbericht_1;
									break;
								default:
									Pst_Fall.setInt(8, 1);
									Pst_Fall.setInt(7, Befundtyp.Fehler.getValue());
									befundtyp = Befundtyp.Fehler;
									break;
							}
						} else {
							if (positions[0][j] == 1) E_NR = cell.getStringCellValue();
							Pst_Fall.setString(positions[1][j], cell.getStringCellValue());
						}
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (positions[0][j] == 0 || positions[0][j] == 3){
							//Eingangsdatum bzw Geburtsdatum
							Pst_Fall.setString(positions[1][j], new java.sql.Date(cell.getDateCellValue().getTime())+"");
						} else {
							//Befundtyp - nope der steht als Text in excel
							Pst_Fall.setInt(positions[1][j], (int)cell.getNumericCellValue());
						}
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						//TODO Ask for User input
						break;
					case Cell.CELL_TYPE_BLANK:
												
						if (positions[0][j] == 1) {		//E.-Nummer fehlt
							Pst_Fall.setString(positions[1][j], "INVALID");
							Pst_Fall.setInt(8, 1);
							break;
						} else if (positions[0][j] == 26) {	//Befundtyp fehlt
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
				
				cell = row.getCell(28);
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
		
		UIFenster1 = new correctParameters();
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
		
		boolean readExcelToPatientendaten = true;
		boolean readExcelToFall = true;
		
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
			recordsToRead = sheet.getPhysicalNumberOfRows();
			
			if (readExcelToPatientendaten && readExcelToFall) {
				UIFenster1.progressBar.setIndeterminate(false);
				UIFenster1.progressBar.setMaximum(recordsToRead*2);
				excelToPatient(excelPath, sheet);
				excelToFall(excelPath, sheet);
				book.close();
				fis.close();
			} else if (readExcelToPatientendaten && !readExcelToFall) {
				UIFenster1.progressBar.setIndeterminate(false);
				UIFenster1.progressBar.setMaximum(recordsToRead);
				excelToPatient(excelPath, sheet);
				book.close();
				fis.close();
			} else if (readExcelToFall && !readExcelToPatientendaten) {
				UIFenster1.progressBar.setIndeterminate(false);
				UIFenster1.progressBar.setMaximum(recordsToRead);
				excelToFall(excelPath, sheet);
				book.close();
				fis.close();
			}
			
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Fehler: Irgendetwas stimmt mit der Datei nicht!");
		}
		
		
//		showDbTable( dbPatTbl );
//		showDbTable( dbFallTbl );
		//----------------------------------------------------

//		new StringReader("Makroskopie: 7 x 7 x 4 cm großes Mammaexzidat (links oben außen) mit zwei Fadenmarkierungen. 1,5 cm oberhalb der langen "
//						+ "Fadenmarkierung ein 2,2 x 2 x 1,8 cm großer lobulierter unscharf begrenzter 0,1 cm vom ventralen Resektionsrand entfernter Tumor. "
//						+ "Das übrige Gewebe fettreich mit diskreten streifenförmigen Fibrosierungen. Zusätzlich ein Telepathologieschnellschnittpräparat. "
//						+ "Mikroskopie: (HE, Schnellschnitt, Paraffineinbettung, HE, PAS, Östrogen- und Progesteronrzeptor, übriges Mammagewebe HE) Im "
//						+ "Bereich des makroskopisch beschriebenen Tumors eine vollständige Destruktion des ortsständigen Brustdrüsengewebes durch nahezu "
//						+ "ausschließlich solide, nur ganz diskret primitiv-tubuläre atypische Epithelverbände mit erheblicher Kernpleomorphie, Hyperchromasie "
//						+ "und deutlich erhöhter Mitoserate (mehr als 10 Mitosen auf 10 Gesichtsfelder bei 40facher Objektivvergrößerung). Tumorzellverbände "
//						+ "teilweise von einem dichten vorwiegend lymphozytären Entzündungszellinfiltrat umgeben. Im Randbereich einzelne Gangformationen mit "
//						+ "intraduktal gelegenen atypischen Epithelverbänden. Keine überzeugende Angioinvasion. Tumorkerne negativ für das Östrogen- und das "
//						+ "Progesteronrezeptor-Protein. Das übrige Mammagewebe parenchymarm mit geringen interstitiellen Fibrosierungen. Diagnose: Niedrig "
//						+ "differenziertes invasives duktales Karzinom (Tumordurchmesser 2,2 cm). Vorläufige Tumorklassifikation: C 57, M 8441/3, G 3, pT3c pN1(15/34) "
//						+ "L/V1. Der Tumor ist Östrogen- und Progesteronrezeptor ER -");

		//Tumorklassifikation: C 57, M 8441/3, G 3, pT3c pN1(15/34) L/V1. Der Tumor ist Östrogen- und Progesteronrezeptor-negativ. Sonstiges: ip

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
