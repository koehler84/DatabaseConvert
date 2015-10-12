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
	
	static columnStructure<columnIndex> getColumnIndizes(XSSFSheet sheet, String method) {
		//TODO
		if (spaltenFehler) return null;
		
		Iterator<Row> itr = sheet.iterator();
		Row row = itr.next();
		columnStructure<columnIndex> structure = new columnStructure<>();
		
		if (method.equals("patientendaten")) {
			
			for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
				
				Cell cell = row.getCell(i);
				String columnName = cell.getStringCellValue().toLowerCase();
				columnIndex index = null;
				
				switch (columnName) {
				case "geburtsdatum": index = new columnIndex(columnName, i, 1); break;
				case "vorname": index = new columnIndex(columnName, i, 2); break;
				case "name": index = new columnIndex(columnName, i, 3); break;
				case "strasse": index = new columnIndex(columnName, i, 4); break;
				case "hausnummer": index = new columnIndex(columnName, i, 5); break;
				case "land": index = new columnIndex(columnName, i, 6); break;
				case "plz": index = new columnIndex(columnName, i, 7); break;
				case "ort": index = new columnIndex(columnName, i, 8); break;
				case "eingangsdatum": index = new columnIndex(columnName, i); break;
				case "verstorben (quelle)": index = new columnIndex(columnName, i, 10); break;
				case "verstorben (datum)": index = new columnIndex(columnName, i, 11); break;
				case "bemerkung tod": index = new columnIndex(columnName, i, 12); break;
				case "follow-up": index = new columnIndex(columnName, i, 13); break;
				case "follow-up status": index = new columnIndex(columnName, i, 14); break;
				case "ee-status": index = new columnIndex(columnName, i, 15); break;
				}
				if (index != null) structure.add(index);
			}
						
			if (structure.check("patientendaten")) {
				return structure;
			}
		} else if (method.equals("fall")) {
			
			for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
				
				Cell cell = row.getCell(i);
				String columnName = cell.getStringCellValue().toLowerCase();
				columnIndex index = null;
				
				switch (columnName) {
				case "geburtsdatum": index = new columnIndex(columnName, i, 1); break;
				case "vorname": index = new columnIndex(columnName, i, 2); break;
				case "name": index = new columnIndex(columnName, i, 3); break;
				case "e.-nummer": index = new columnIndex(columnName, i, 4); break;
				case "eingangsdatum": index = new columnIndex(columnName, i, 5); break;
				case "einsender": index = new columnIndex(columnName, i, 6); break;
				case "befundtyp": index = new columnIndex(columnName, i, 7); break;
				case "arzt": index = new columnIndex(columnName, i, 9); break;
				case "kryo": index = new columnIndex(columnName, i, 10); break;
				case "op-datum": index = new columnIndex(columnName, i, 11); break;
				case "mikroskopie": index = new columnIndex(columnName, i, 12); break;
				case "befundtext": index = new columnIndex(columnName, i); break;
				}
				if (index != null) structure.add(index);
			}
						
			if (structure.check("fall")) {
				return structure;
			}			
		} else if (method.equals("klassifikation")) {
			
		}
		
		System.out.println("Spaltennamen wurden nicht gefunden!");
		JOptionPane.showMessageDialog(start.UIFenster1, "Einige Datenfelder konnten nicht gefunden werden.\n"
				+ "Bitte �berpr�fen sie die Spaltennamen der Excel-Datei!", "Fehler in Excel Datei", JOptionPane.ERROR_MESSAGE);
		spaltenFehler = true;
		return null;
	}
	
	static void excelToPatient(XSSFSheet sheet) {
		//TODO
		if (spaltenFehler) return;
		
		Iterator<Row> itr = sheet.iterator();
		Row row = itr.next();
		
		columnStructure<columnIndex> structure = getColumnIndizes(sheet, "patientendaten");
		if (spaltenFehler) return;
		
		try {
			
			PreparedStatement Pst = cn.prepareStatement("insert into patientendaten (`Geburtsdatum`, `Vorname`, `Name`,"
					+ " `Strasse`, `Hausnummer`, `Land`, `PLZ`, `Ort`, `Fehler`, `Verstorben (Quelle)`, `Verstorben (Datum)`, `Bemerkung Tod`,"
					+ " `Follow-up`, `Follow-up Status`, `EE-Status`) "
					+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? );");

			int i = 0;	//iterator
			
			while (itr.hasNext() && i < recordsToRead) {

				i++;
				
				UIFenster1.progressBar.setValue(UIFenster1.progressBar.getValue()+1);
				row = itr.next();
				// Iterating over each column of Excel file
				
				Pst.clearParameters();		//clear parameters in Pst for next insert
				Cell cell = null;
				Pst.setNull(4, java.sql.Types.NULL);	//setNull so not every values has to be set in do-while loop
				Pst.setNull(5, java.sql.Types.NULL);
				Pst.setNull(6, java.sql.Types.NULL);
				Pst.setNull(7, java.sql.Types.NULL);
				Pst.setNull(8, java.sql.Types.NULL);
				Pst.setInt(9, 0);
				Pst.setNull(10, java.sql.Types.NULL);
				Pst.setNull(11, java.sql.Types.NULL);
				Pst.setNull(12, java.sql.Types.NULL);
				Pst.setNull(13, java.sql.Types.NULL);
				Pst.setNull(14, java.sql.Types.NULL);
				Pst.setNull(15, java.sql.Types.NULL);
				columnIndex columnObject = structure.head;
				boolean first = true;
				
				do {
					if (first) {
						first = false;
					} else {
						columnObject = columnObject.next;
					}
					
					if (columnObject.PstIndex != -1) {
						//this is only executed if the parameter can be inserted into the PreparedStatement
						cell = row.getCell(columnObject.columnIndex);
						
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							Pst.setString(columnObject.PstIndex, cell.getStringCellValue());						
							break;
						case Cell.CELL_TYPE_NUMERIC:
							if (columnObject.PstIndex == 1){
								
								columnIndex object2 = structure.head;
								int eingangsdatumColumnIndex = -1;
								boolean first2 = true;
								
								do {
									if (first2) {
										first2 = false;
									} else {
										object2 = object2.next;									
									}
									
									if (object2.columnName.equals("eingangsdatum")) {
										eingangsdatumColumnIndex = object2.columnIndex;
										break;
									}
								} while (object2.hasNext());
								
								Date geburtsdatum = new java.sql.Date(cell.getDateCellValue().getTime());
								@SuppressWarnings("deprecation")
								Date datum1 = new Date(0, 0, 1);
								@SuppressWarnings("deprecation")
								Date datum2 = new Date(100, 0, 1);
								
								if (eingangsdatumColumnIndex != -1) {
									cell = row.getCell(eingangsdatumColumnIndex);
									Date eingangsdatum = new java.sql.Date(cell.getDateCellValue().getTime());
									
									if (!geburtsdatum.equals(eingangsdatum) && !geburtsdatum.equals(datum1) && !geburtsdatum.equals(datum2)) {
										Pst.setString(1, geburtsdatum + "");
									} else {
										Pst.setString(columnObject.PstIndex, "0001-01-01");
										System.out.println("Fehler: Geburtsdatum ist fehlerhaft!");
										Pst.setInt(9, 1);
									}
								} else {
									if (!geburtsdatum.equals(datum1) && !geburtsdatum.equals(datum2)) {
										Pst.setString(1, geburtsdatum + "");
									} else {
										Pst.setString(columnObject.PstIndex, "0001-01-01");
										System.out.println("Fehler: Geburtsdatum ist fehlerhaft!");
										Pst.setInt(9, 1);
									}
								}
								
								cell = row.getCell(columnObject.columnIndex);
							} else if (columnObject.PstIndex == 7) {
								//PLZ als String speichern
								Pst.setString(7, ((int)cell.getNumericCellValue()) + "");
							} else if (columnObject.PstIndex == 11 || columnObject.PstIndex == 13) {
								Pst.setString(columnObject.PstIndex, new java.sql.Date(cell.getDateCellValue().getTime()) + "");
							} else {
								Pst.setInt(columnObject.PstIndex, (int)cell.getNumericCellValue());
							}
							break;
						case Cell.CELL_TYPE_BLANK:
							if (columnObject.PstIndex == 1) {
								Pst.setString(columnObject.PstIndex, "0001-01-01");
								System.out.println("Fehler: Geburtsdatum fehlt!");
								Pst.setInt(9, 1);
								break;
							} else if (columnObject.PstIndex == 2 || columnObject.PstIndex == 3) {
								Pst.setString(columnObject.PstIndex, "INVALID_NAME");
								System.out.println("Fehler: Vorname oder Nachmame fehlt!");
								Pst.setInt(9, 1);
								break;
							} else {
								Pst.setInt(9, 1);
							}
							
							//Abfrage in der Datenbank: "select * from mydb.patientendaten where PLZ is null;"
							Pst.setNull(columnObject.PstIndex, java.sql.Types.NULL);
							break;
						}
						//end of switch
					}
					
				} while (columnObject.hasNext());
				
				try {
					//Execution of PreparedStatement, SQL Exeption if person is already in database
					System.out.println("Updated rows in mydb.patientendaten: " + Pst.executeUpdate());
				} catch (SQLException se){
					System.out.println("Fehler beim Ausf�hren von \"insert into patientendaten\": Person ggf. schon erfasst!");
				}
				
			}
			//end of while
			
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
		
		columnStructure<columnIndex> structure = getColumnIndizes(sheet, "fall");
		if (spaltenFehler) return;
		
		try {
			
			PreparedStatement Pst_Fall = cn.prepareStatement("insert into mydb.fall (`Patientendaten_PatientenID`, `E.-Nummer`, "
					+ "`Eingangsdatum`, `Einsender`, `Befundtyp`, `Fehler`, `Arzt`, `Kryo`, `OP-Datum`, `Mikroskopie`) values "
					+ "((select PatientenID from mydb.patientendaten where Geburtsdatum = ? and Vorname = ? and Name = ? ),"
					+ " ? , ? , ? , ? , ? , ? , ? , ? , ? );");
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
				Pst_Fall.setNull(5, java.sql.Types.NULL);
				Pst_Fall.setNull(6, java.sql.Types.NULL);
				Pst_Fall.setInt(8, 0);
				Pst_Fall.setNull(9, java.sql.Types.NULL);
				Pst_Fall.setNull(10, java.sql.Types.NULL);
				Pst_Fall.setNull(11, java.sql.Types.NULL);
				Pst_Fall.setNull(12, java.sql.Types.NULL);
				Cell cell = null;
				Pst_Klassifikation.clearParameters();
				
				String E_NR = null;
				Befundtyp befundtyp = null;				
				columnIndex columnObject = structure.head;
				boolean first = true;
				
				do {
					if (first) {
						first = false;
					} else {
						columnObject = columnObject.next;
					}
					
					if (columnObject.PstIndex != -1) {
						//this is only executed if the parameter can be inserted into the PreparedStatement
						cell = row.getCell(columnObject.columnIndex);
						
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							if (columnObject.PstIndex == 7) {
								befundtyp = Befundtyp.getBefundtyp(cell.getStringCellValue());
								if (befundtyp == null) {
									Pst_Fall.setInt(8, 1);
									befundtyp = Befundtyp.Fehler;
								}
								Pst_Fall.setInt(7, befundtyp.getValue());
							} else {
								if (columnObject.PstIndex == 4) E_NR = cell.getStringCellValue();
								Pst_Fall.setString(columnObject.PstIndex, cell.getStringCellValue());
							}
							break;
						case Cell.CELL_TYPE_NUMERIC:
							if (columnObject.PstIndex == 1 || columnObject.PstIndex == 5 || columnObject.PstIndex == 11){
								//Eingangsdatum, Geburtsdatum oder OP-Datum
								Pst_Fall.setString(columnObject.PstIndex, new java.sql.Date(cell.getDateCellValue().getTime())+"");
							} else {
								//Befundtyp - nope der steht als Text in excel
								Pst_Fall.setInt(columnObject.PstIndex, (int)cell.getNumericCellValue());
							}
							break;
						case Cell.CELL_TYPE_BLANK:
							if (columnObject.PstIndex == 5) {		//E.-Nummer fehlt
								Pst_Fall.setString(columnObject.PstIndex, "INVALID");
								Pst_Fall.setInt(8, 1);
								break;
							} else if (columnObject.PstIndex == 7) {	//Befundtyp fehlt
								Pst_Fall.setInt(columnObject.PstIndex, Befundtyp.Fehler.getValue());
								befundtyp = Befundtyp.Fehler;
								Pst_Fall.setInt(8, 1);
								break;
							} else {
								Pst_Fall.setInt(8, 1);
							}
							
							Pst_Fall.setNull(columnObject.PstIndex, java.sql.Types.NULL);
							break;
						}
						//end of switch
					}
					
				} while (columnObject.hasNext());
				
				try {
					System.out.print("Updated rows in mydb.fall: " + Pst_Fall.executeUpdate() + " - ");
				} catch (SQLException e) {
					//e.printStackTrace();
					System.out.print("Fehler beim Ausf�hren von \"insert into fall\": Fall ggf. doppelt!" + " ");
				}
				
				columnObject = structure.head;
				int befundtextColumnIndex = -1;
				first = true;
				
				do {
					if (first) {
						first = false;
					} else {
						columnObject = columnObject.next;									
					}
					
					if (columnObject.columnName.equals("befundtext")) {
						befundtextColumnIndex = columnObject.columnIndex;
						break;
					}
				} while (columnObject.hasNext());
				
				if (befundtextColumnIndex != -1) {
					cell = row.getCell(befundtextColumnIndex);
					String befundtext = cell.getStringCellValue();
					
					excelToKlassifikation(Pst_Klassifikation, befundtext, E_NR, befundtyp);					
				} else {
					System.out.println();
				}
				
			}
			//end of while
			
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
			System.out.println("Einf�gen in Klassifikation, ge�nderte Zeilen: " + Pst.executeUpdate());
		} catch (Exception e) {
			System.out.println("Fehler beim Einf�gen der Falldaten.");
		}
		
	}
	
	static void restart(String path) {
		
		String excelPath = path;
		
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
		//TODO
		String dbDrv="", dbUrl="", dbUsr="", dbPwd="", excelPath="";
		
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
		//ist daf�r da, das es auf allen meinen rechnern parallel mit einer datenbank funktioniert
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

	//http://download.eclipse.org/egit/github/updates-nightly/ <- GITHUB Task manager (�ber help -> install new software)

}
