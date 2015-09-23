import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.*;
import java.util.Iterator;

import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class start {
	
	public static Connection cn;
	public static correctParameters UIFenster1;
	public static boolean methodsCompleted;
	
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

	static void excelToPatient(String excelPath) {
		
		try {

			PreparedStatement Pst = cn.prepareStatement("insert into patientendaten (`Geburtsdatum`, `Vorname`, `Name`, `Strasse`, `Hausnummer`, `Land`, `PLZ`, `Ort`, `Vollständig`)"
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? );");

			File excel = new File(excelPath);
			FileInputStream fis = new FileInputStream(excel);
			XSSFWorkbook book = new XSSFWorkbook(fis);
			XSSFSheet sheet = book.getSheetAt(0);
			book.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
			Iterator<Row> itr = sheet.iterator();
			if (itr.hasNext()) {
				itr.next();		//skipping the header row			
			}
			// Iterating over Excel file in Java

			int i = 0;	//stop after 30 rows for testing
			
			int[][] positions = {{3,4,5,6,7,8,9,10},{1,2,3,4,5,6,7,8}};

			//Max. Reihen: sheet.getPhysicalNumberOfRows()
			while (itr.hasNext() && i<30) {	//stop after 30 rows for testing

				i++;	//stop after 30 rows for testing

				Row row = itr.next();
				
				// Iterating over each column of Excel file
				Pst.clearParameters();		//clear parameters in Pst for next insert
				boolean writeToDB = true;
				Cell cell = null;
				boolean RowContinue = true;
				Pst.setInt(9, 0);
				
				for (int j=0; j<positions[0].length && RowContinue;j++) {
					cell = row.getCell(positions[0][j]);

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						if (positions[0][j] != 9) {
							Pst.setString(positions[1][j], cell.getStringCellValue());
						} else {
							//fehlerToWindow("excelToPatient", row, positions);
							Pst.setInt(9, 1);
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
						//Do we even have boolean columns to read?
						//TODO ASK THE USER FOR INPUT!
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
							//fehlerToWindow("excelToPatient", row, positions);
							Pst.setInt(9, 1);
							break;
						} else if (positions[0][j] == 4 || positions[0][j] == 5) {
							Pst.setString(positions[1][j], "INVALID_NAME");
							System.out.println("Fehler: Vorname oder Nachmame fehlt!");
							//fehlerToWindow("excelToPatient", row, positions);
							Pst.setInt(9, 1);
							break;
						} else if (positions[0][j] == 7 && row.getCell(positions[0][j-1]).getCellType() == Cell.CELL_TYPE_STRING) {
							//Es gibt keine Hausnummer zu vorhandener Straße
							System.out.println("Fehler: Hausnummer fehlt!");
							//fehlerToWindow("excelToPatient", row, positions);
							Pst.setInt(9, 1);
						} else {
							//fehlerToWindow("excelToPatient", row, positions);
							Pst.setInt(9, 1);
						}
						
						//Abfrage in der Datenbank: "select * from mydb.patientendaten where PLZ is null;"
						Pst.setNull(positions[1][j], java.sql.Types.NULL);
						break;
					}
					
				}
				
				if (writeToDB) {
					try {
						//Execution of PreparedStatement, SQL Exeption if person is already in database
						System.out.println("Updated rows in mydb.patientendaten: " + Pst.executeUpdate());
					} catch (SQLException se){
						System.out.println("Fehler beim Ausführen von \"insert into patientendaten\": Person ggf. schon erfasst!");
					}
				} else {
					System.out.println("Fehler beim Einlesen der Patientendaten! Abbruch des Schreibvorgangs.");
				}

			}
			
			Pst.close();
			book.close();
			fis.close();
			System.out.println("Write patientendaten success");
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Fehler beim Erstellen des PreparedStatement \"insert into patientendaten\"!");
		}

	}

	static void excelToFall(String excelPath) {
				
		try {
			
			PreparedStatement Pst = cn.prepareStatement("insert into mydb.fall (`Patientendaten_PatientenID`, `Eingangsdatum`, "
					+ "`E.-Nummer`, `Arzt`, `Befundtyp`) values "
					+ "((select PatientenID from mydb.patientendaten where Name = ? and Vorname = ? and Geburtsdatum = ? ),"
					+ " ? , ? , ? , ? );");

			File excel = new File(excelPath);
			FileInputStream fis = new FileInputStream(excel);
			XSSFWorkbook book = new XSSFWorkbook(fis);
			XSSFSheet sheet = book.getSheetAt(0);
			book.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
			Iterator<Row> itr = sheet.iterator();
			if (itr.hasNext()) {
				itr.next();
			}
			// Iterating over Excel file in Java
			
			int[][] positions = {{0,1,2,3,4,5,26},{4,5,6,3,2,1,7}};
			
			//i:	0,1,2,3,4,5,26		oben: Spalte in excel datei
			//		4,5,6,3,2,1,7		unten: Position in Pst
			
			int k = 0;	//stop after 30 rows for testing
			
			//Max. Reihen: sheet.getPhysicalNumberOfRows()
			while (itr.hasNext() && k<29) {	//stop after 30 rows for testing

				k++;	//stop after 30 rows for testing

				Row row = itr.next();
				// Iterating over each column of Excel file
				Pst.clearParameters();
				boolean writeToDB = true;
				Cell cell = null;
				
				cell = row.getCell(28);
				String abc = cell.getStringCellValue();
				
				String E_NR = null;
				Befundtyp befundtyp = null;
				StringReader srObject = null;				
				try {
					srObject = new StringReader(abc);
				} catch (Exception e) {
					System.out.println("Objektfehler!");
				}
				
				for (int j=0; j<positions[0].length;j++) {
					
					cell = row.getCell(positions[0][j]);
					
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						if (positions[0][j] == 26) {
							switch (cell.getStringCellValue())	{
								case "Hauptbefund":
									Pst.setInt(7, Befundtyp.Hauptbefund.getValue());
									befundtyp = Befundtyp.Hauptbefund;
									break;
								case "Nachbericht 1":
									Pst.setInt(7, Befundtyp.Nachbericht_1.getValue());
									befundtyp = Befundtyp.Nachbericht_1;
									break;
								case "Nachbericht 2":
									Pst.setInt(7, Befundtyp.Nachbericht_2.getValue());
									befundtyp = Befundtyp.Nachbericht_2;
									break;
								case "Korrekturbefund 1":
									Pst.setInt(7, Befundtyp.Korrekturbefund_1.getValue());
									befundtyp = Befundtyp.Korrekturbefund_1;
									break;
								case "Korrekturbefund 2":
									Pst.setInt(7, Befundtyp.Korrekturbefund_2.getValue());
									befundtyp = Befundtyp.Korrekturbefund_2;
									break;
								case "Korrekturbefund 3":
									Pst.setInt(7, Befundtyp.Korrekturbefund_3.getValue());
									befundtyp = Befundtyp.Korrekturbefund_3;
									break;
								case "Konsiliarbericht 1":
									Pst.setInt(7, Befundtyp.Konsiliarbericht_1.getValue());
									befundtyp = Befundtyp.Konsiliarbericht_1;
									break;
								default:
									//TODO Ask User for input
									//Es ist keiner der normalen Befundtypen, Rechtschreibfehler?
									writeToDB = false;
									System.out.println("FEHLER: Keiner der bekannten Befundtypen!");
									break;
							}
						} else {
							if (positions[0][j] == 1) E_NR = cell.getStringCellValue();
							Pst.setString(positions[1][j], cell.getStringCellValue());
						}
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (positions[0][j] == 0 || positions[0][j] == 3){
							//Eingangsdatum bzw Geburtsdatum
							Pst.setString(positions[1][j], new java.sql.Date(cell.getDateCellValue().getTime())+"");
						} else {
							//Befundtyp
							Pst.setInt(positions[1][j], (int)cell.getNumericCellValue());
						}
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						//TODO Ask for User input
						break;
					case Cell.CELL_TYPE_BLANK:
						
						if (positions[0][j] >= 0 && positions[0][j] <= 5 || positions[0][j] == 26) {		//TODO Wie wichtig ist der Einsender?
							writeToDB = false;
							System.out.println("Fehler: Wichtiger Parameter fehlt!");
							break;
						}
						
						Pst.setNull(positions[1][j], java.sql.Types.NULL);
						break;
					}
				}
				
				if (writeToDB) {
					try {
						System.out.println("Updated rows in mydb.patientendaten: " + Pst.executeUpdate());
					} catch (SQLException e) {
						//e.printStackTrace();
						System.out.println("Fehler beim Ausführen von \"insert into fall\": Fall ggf. doppelt!");
					}
				} else {
					System.out.println("Fehler beim Einlesen des Falls! Abbruch des Schreibvorgangs.");
				}
				
				PreparedStatement st = cn.prepareStatement("insert into mydb.klassifikation (`Fall_E.-Nummer`, `Fall_Befundtyp`, "
						+ "G, T, N, M, L, V, R, ER, PR, `Her2/neu`) values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? );");
				
				st.setString(1, E_NR);
				st.setInt(2, befundtyp.getValue());
				st.setInt(3, srObject.G);
				st.setString(4, srObject.T);
				st.setString(5, srObject.N);
				st.setString(6, srObject.M);
				st.setInt(7, srObject.L);
				st.setInt(8, srObject.V);
				st.setInt(9, srObject.R);
				st.setString(10, srObject.ER);
				st.setString(11, srObject.PR);
				st.setString(12, srObject.her2_Neu);
				
				try {
					System.out.println("Einfügen in Klassifikation, geänderte Zeilen: " + st.executeUpdate());
					st.close();
				} catch (Exception e) {
					System.out.println("Fehler beim Einfügen der Falldaten.");
				}
				
			}
			
			Pst.close();
			book.close();
			fis.close();
			System.out.println("Write fall success");
		} catch (SQLException SQLex) {
			System.out.println("Fehler beim Erstellen des PreparedStatement \"insert into fall\"!");
		} catch (IOException e) {
			System.out.println("IO Exeption");
		}

	}
	
	public static void fehlerToWindow(String method, Row row, int[][] positions) {
		
		UIFenster1.scrollPane.setVisible(true);
		UIFenster1.getContentPane().revalidate();		//essential for the scrollPane to be visible
		UIFenster1.getContentPane().repaint();
		UIFenster1.table.setVisible(true);
		
		if (method.equals("excelToPatient")) {
			
			DefaultTableModel tableModel = new DefaultTableModel();
			if (UIFenster1.table.getModel().getColumnCount() == 0) {
				System.out.println("TableModel null");
				tableModel = new DefaultTableModel(
						new String[]{"Geburtsdatum", "Vorname", "Name", "Straße", "Hausnummer", "Land", "PLZ", "Ort"}, 0);
				UIFenster1.table.setModel(tableModel);
			}
			
			Object[] parameterArray = new Object[8];
			
			for (int j=0; j < positions[0].length;j++) {
				Cell cell = row.getCell(positions[0][j]);

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					parameterArray[j] = cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (positions[0][j]==3){
						parameterArray[j] = new java.sql.Date(cell.getDateCellValue().getTime()) + "";
					} else {
						parameterArray[j] = (int)cell.getNumericCellValue();
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					if (cell.getBooleanCellValue()) {
						parameterArray[j] = "True";
					} else {
						parameterArray[j] = "False";
					}
					break;
				case Cell.CELL_TYPE_BLANK:
					parameterArray[j] = null;
					break;
				}
				
			}
			
			((DefaultTableModel) UIFenster1.table.getModel()).addRow(parameterArray);
			
		} else if (method.equals("excelToFall")) {
			
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

		//-----------------------------------
		//Um das zu connection mit localhost zu beschleunigen  kannst das auskommentieren,
		//ist dafür da, das es auf allen meinen rechnern parallel mit einer datenbank funktioniert
		//-----------------------------------
		try {
			Socket sock = new Socket ();
			sock.connect(new InetSocketAddress("192.168.178.22", 3306), 200 );
			sock.close();
			dbUrl = "jdbc:mysql://192.168.178.22:3306/mydb";
		} catch (Exception e) {
			
		}

		//Validate connection data
		if( dbPatTbl == null || dbPatTbl.length() == 0 ||
				dbFallTbl == null || dbFallTbl.length() == 0 ||
				dbDrv == null || dbDrv.length() == 0 ||
				dbUrl == null || dbUrl.length() == 0 ) {
			System.out.println( "Fehler: Parameter fehlt." );
			return;
		}
		
		UIFenster1 = new correctParameters();
		UIFenster1.progressBar.setIndeterminate(true);
		
		try {
			// Select fitting database driver and connect:
	/*???	*/Class.forName( dbDrv );
			cn = DriverManager.getConnection( dbUrl, dbUsr, dbPwd );
			UIFenster1.lblConnected.setVisible(true);
			UIFenster1.insertModel();

		} catch ( Exception ex ) {
			System.out.println( ex );
		}

		//----------------------------------------------------
//		excelToPatient(excelPath);
		excelToFall(excelPath);
		
//		showDbTable( dbPatTbl );
//		showDbTable( dbFallTbl );
		//----------------------------------------------------

		new StringReader("Makroskopie: 7 x 7 x 4 cm großes Mammaexzidat (links oben außen) mit zwei Fadenmarkierungen. 1,5 cm oberhalb der langen "
						+ "Fadenmarkierung ein 2,2 x 2 x 1,8 cm großer lobulierter unscharf begrenzter 0,1 cm vom ventralen Resektionsrand entfernter Tumor. "
						+ "Das übrige Gewebe fettreich mit diskreten streifenförmigen Fibrosierungen. Zusätzlich ein Telepathologieschnellschnittpräparat. "
						+ "Mikroskopie: (HE, Schnellschnitt, Paraffineinbettung, HE, PAS, Östrogen- und Progesteronrzeptor, übriges Mammagewebe HE) Im "
						+ "Bereich des makroskopisch beschriebenen Tumors eine vollständige Destruktion des ortsständigen Brustdrüsengewebes durch nahezu "
						+ "ausschließlich solide, nur ganz diskret primitiv-tubuläre atypische Epithelverbände mit erheblicher Kernpleomorphie, Hyperchromasie "
						+ "und deutlich erhöhter Mitoserate (mehr als 10 Mitosen auf 10 Gesichtsfelder bei 40facher Objektivvergrößerung). Tumorzellverbände "
						+ "teilweise von einem dichten vorwiegend lymphozytären Entzündungszellinfiltrat umgeben. Im Randbereich einzelne Gangformationen mit "
						+ "intraduktal gelegenen atypischen Epithelverbänden. Keine überzeugende Angioinvasion. Tumorkerne negativ für das Östrogen- und das "
						+ "Progesteronrezeptor-Protein. Das übrige Mammagewebe parenchymarm mit geringen interstitiellen Fibrosierungen. Diagnose: Niedrig "
						+ "differenziertes invasives duktales Karzinom (Tumordurchmesser 2,2 cm). Vorläufige Tumorklassifikation: C 57, M 8441/3, G 3, pT3c pN1(15/34) "
						+ "L/V1. Der Tumor ist Östrogen- und Progesteronrezeptor ER -");

		//Tumorklassifikation: C 57, M 8441/3, G 3, pT3c pN1(15/34) L/V1. Der Tumor ist Östrogen- und Progesteronrezeptor-negativ. Sonstiges: ip

		methodsCompleted = true;
		UIFenster1.insertModel();
		
		try {
			if (cn != null && !cn.isClosed() && !UIFenster1.isShowing()) {
				cn.close();
				System.out.println("Datenbankverbindung beednet! STARTCLASS");
			}
		} catch (SQLException e) {
			System.out.println("Fehler beim Beenden der Datenbankverbindung!");
		}
		
		UIFenster1.progressBar.setIndeterminate(false);
		UIFenster1.progressBar.setValue(1000);
		
		
	}

	//http://download.eclipse.org/egit/github/updates-nightly/ <- GITHUB Task manager (über help -> install new software)

}
