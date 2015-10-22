package fx;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.concurrent.Task;
import javafx.scene.control.Label;
import main.columnData;
import main.columnIndex;
import main.columnStructure;

public class FX_Main {
	
	public static Connection cn;
	public static boolean methodsCompleted;
	public static int recordsToRead;
	public static boolean readExcelToPatientendaten;
	public static boolean readExcelToFall;
	public static boolean spaltenFehler;
	public static XSSFSheet sheet;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		methodsCompleted = true;
		recordsToRead = 30;
		
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
					Class.forName( dbDrv );
					cn = DriverManager.getConnection( dbUrl, dbUsr, dbPwd );
					label.setVisible(true);
					return true;
				} catch ( Exception ex ) {
					System.out.println( ex );
					return false;
				}
				
			}
		}; 
		
		return task;		
	}
	
	public static Task<Void> loadExcel(final File excel) {
		
		Task<Void> task = new Task<Void>() {

			@SuppressWarnings("resource")
			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				
				//String excelPath = "C://Project Pathologie/test.xlsx";
				
				FileInputStream fis = null;
				sheet = null;
				XSSFWorkbook book = null;
				
				fis = new FileInputStream(excel);
				book = new XSSFWorkbook(fis);
				sheet = book.getSheetAt(0);
				book.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
				
				return null;
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
				updateProgress(-1, recordsToRead);
				
				//if (spaltenFehler) return;
				
				File excel = null;
				FileInputStream fis = null;
				XSSFSheet sheet = null;
				XSSFWorkbook book = null;
				
				excel = new File(path);
				fis = new FileInputStream(excel);
				book = new XSSFWorkbook(fis);
				sheet = book.getSheetAt(0);
				book.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
				
								
				Iterator<Row> itr = sheet.iterator();
				Row row = itr.next();
				
				columnStructure<columnIndex> structure = main.start.getColumnIndizes(sheet, "patientendaten");
				//if (spaltenFehler) return;
				
				try {
					
					PreparedStatement Pst = cn.prepareStatement("insert into patientendaten (`Geburtsdatum`, `Vorname`, `Name`,"
							+ " `Strasse`, `Hausnummer`, `Land`, `PLZ`, `Ort`, `Fehler`, `Verstorben (Quelle)`, `Verstorben (Datum)`, `Bemerkung Tod`,"
							+ " `Follow-up`, `Follow-up Status`, `EE-Status`) "
							+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? );");
					PreparedStatement Pst_update = cn.prepareStatement("UPDATE mydb.patientendaten\r\n" + 
							"SET\r\n" + 
							"`Strasse` = IFNULL(`Strasse`, ? ),\r\n" + 
							"`Hausnummer` = IFNULL(`Hausnummer`, ? ),\r\n" + 
							"`Land` = IFNULL(`Land`, ? ),\r\n" + 
							"`PLZ` = IFNULL(`PLZ`, ? ),\r\n" + 
							"`Ort` = IFNULL(`Ort`, ? ),\r\n" + 
							"`Verstorben (Quelle)` = IFNULL(`Verstorben (Quelle)`, ? ),\r\n" + 
							"`Verstorben (Datum)` = IFNULL(`Verstorben (Datum)`, ? ),\r\n" + 
							"`Bemerkung Tod` = IFNULL(`Bemerkung Tod`, ? ),\r\n" + 
							"`Follow-up` = IFNULL(`Follow-up`, ? ),\r\n" + 
							"`Follow-up Status` = IFNULL(`Follow-up Status`, ? ),\r\n" + 
							"`EE-Status` = IFNULL(`EE-Status`, ? )\r\n" + 
							"where `Geburtsdatum` = ? and `Vorname` = ? and `Name` = ? ;");

					int i = 0;	//iterator
					
					while (itr.hasNext() && i < recordsToRead) {

						i++;
						
						updateProgress(i, recordsToRead);
						row = itr.next();
						// Iterating over each column of Excel file
						
						Cell cell = null;
						Pst.clearParameters();		//clear parameters in Pst for next insert
						Pst_update.clearParameters();
						Pst.setInt(9, 0);		//Fehler column
						
						//setNull so not every values has to be set in do-while loop
						//Pst.setNull
						for (int j = 4; j < 16; j++) {
							if (j == 9) j++;
							Pst.setNull(j, java.sql.Types.NULL);
						}
						
						//Pst_update.setNull
						for (int j = 1; j < 12; j++) {
							Pst_update.setNull(j, java.sql.Types.NULL);					
						}
						
						columnData firstObject = new columnData(structure.head);
						columnData columnObject = firstObject;
						boolean first = true;
						
						do {
							if (first) {
								first = false;
							} else {
								columnObject = (columnData) columnObject.next;
							}
							
							if (columnObject.PstIndex != -1) {
								//this is only executed if the parameter can be inserted into the PreparedStatement
								cell = row.getCell(columnObject.columnIndex);
								
								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_STRING:
									Pst.setString(columnObject.PstIndex, cell.getStringCellValue());
									columnObject.data = cell.getStringCellValue();
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
												columnObject.data = geburtsdatum + "";
											} else {
												Pst.setString(columnObject.PstIndex, "0001-01-01");
												columnObject.data = "0001-01-01";
												System.out.println("Fehler: Geburtsdatum ist fehlerhaft!");
												Pst.setInt(9, 1);
											}
										} else {
											if (!geburtsdatum.equals(datum1) && !geburtsdatum.equals(datum2)) {
												Pst.setString(1, geburtsdatum + "");
												columnObject.data = geburtsdatum + "";
											} else {
												Pst.setString(columnObject.PstIndex, "0001-01-01");
												columnObject.data = "0001-01-01";
												System.out.println("Fehler: Geburtsdatum ist fehlerhaft!");
												Pst.setInt(9, 1);
											}
										}
										
										cell = row.getCell(columnObject.columnIndex);
									} else if (columnObject.PstIndex == 7) {
										//PLZ als String speichern
										Pst.setString(columnObject.PstIndex, ((int)cell.getNumericCellValue()) + "");
										columnObject.data = ((int)cell.getNumericCellValue()) + "";
									} else if (columnObject.PstIndex == 11 || columnObject.PstIndex == 13) {
										//Versorben (Datum) oder Follow-up
										Pst.setString(columnObject.PstIndex, new java.sql.Date(cell.getDateCellValue().getTime()) + "");
										columnObject.data = new java.sql.Date(cell.getDateCellValue().getTime()) + "";
									} else {
										Pst.setInt(columnObject.PstIndex, (int)cell.getNumericCellValue());
										columnObject.data = ((int)cell.getNumericCellValue()) + "";
									}
									break;
								case Cell.CELL_TYPE_BLANK:
									if (columnObject.PstIndex == 1) {
										Pst.setString(columnObject.PstIndex, "0001-01-01");
										columnObject.data = "0001-01-01";
										System.out.println("Fehler: Geburtsdatum fehlt!");
										Pst.setInt(9, 1);
										break;
									} else if (columnObject.PstIndex == 2 || columnObject.PstIndex == 3) {
										Pst.setString(columnObject.PstIndex, "INVALID_NAME");
										columnObject.data = "INVALID_NAME";
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
							System.out.print("Updated rows in mydb.patientendaten: " + Pst.executeUpdate());
						} catch (SQLException se){					
							System.out.print("Fehler beim Ausführen von \"insert into patientendaten\": Person ggf. schon erfasst!");
							
							try {
								first = true;
								
								do {
									if (first) {
										first = false;
									} else {
										firstObject = (columnData) firstObject.next;
									}
									
									switch (firstObject.Pst_updateIndex) {
									case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 12: case 13: case 14:
										Pst_update.setString(firstObject.Pst_updateIndex, (String) firstObject.data);
										break;
									case 10: case 11:
										Pst_update.setInt(firstObject.Pst_updateIndex, (int) firstObject.data);
										break;
									}
									
								} while (firstObject.hasNext());
								
								Pst_update.executeUpdate();
								System.out.print(" - Datensatz vervollständigt.");
							} catch (SQLException e) {
								//System.out.print(e);
								System.out.print(" - Fehler beim Vervollständigen.");
							}
							
						} finally {
							System.out.println();
						}
						
					}
					//end of while
					
					Pst.close();
					Pst_update.close();
					System.out.println("Write patientendaten success");
					System.out.println();
				} catch (SQLException e) {
					System.out.println("Fehler beim Erstellen des PreparedStatement \"insert into patientendaten\"!");
				}
				
				FX_Main.methodsCompleted = true;
				
				return null;
			}
			
		};
		
		return task;
	}
	
}
