package fx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sun.javafx.css.PseudoClassState;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.Befundtyp;
import main.columnData;
import main.columnIndex;
import main.columnStructure;
import main.start;

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
		recordsToRead = -1;

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

	/**
	 * Method connects program to database.
	 * @param label <i>connected</i>-label from rootScene to be set visible after successful connecting. 
	 * @return Task that can be started, to connect to the database.
	 */
	public static Task<Boolean> connect() {

		Task<Boolean> task = new Task<Boolean>() {

			@Override
			protected Boolean call() throws Exception {
				// TODO Auto-generated method stub

				String dbUrl="", dbUsr="", dbPwd="";

				//-----------------------------------
				//DB connection data
				//-----------------------------------
				dbUrl = "jdbc:mysql://localhost:3306/mydb";
				dbUsr = "java";
				dbPwd = "geheim";

				//-----------------------------------
				//Um das zu connection mit localhost zu beschleunigen  kannst das auskommentieren,
				//ist dafür da, das es auf allen meinen rechnern parallel mit einer datenbank funktioniert
				//-----------------------------------
				try {
					Socket socket = new Socket ();
					socket.connect(new InetSocketAddress("192.168.178.25", 3306), 200 );
					socket.close();
					dbUrl = "jdbc:mysql://192.168.178.25:3306/mydb";
				} catch (Exception e) {
					//System.out.println(e);
				}

				try {
					cn = DriverManager.getConnection( dbUrl, dbUsr, dbPwd );
					controller_Main.setConnectionIndicatorState(true);
					return true;
				} catch (Exception ex) {
					//ex.printStackTrace();
					System.err.println(ex);
					return false;
				}

			}
		}; 

		return task;		
	}

	public static Task<XSSFSheet> loadExcel(final File excel) {
		//load excel is there to load the file for all of the reading methods, the sheet will be returned
		Task<XSSFSheet> task = new Task<XSSFSheet>() {

			@SuppressWarnings("resource")
			@Override
			protected XSSFSheet call() throws Exception {
				// TODO Auto-generated method stub
				updateProgress(-1, 5);


				FileInputStream fis = new FileInputStream(excel);

				XSSFWorkbook book = new XSSFWorkbook(fis);
				XSSFSheet sheet = book.getSheetAt(0);

				book.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);




				if (recordsToRead == -1) {
					recordsToRead=sheet.getPhysicalNumberOfRows();
				}
				return sheet;
			}

		};

		return task;
	}

	public static Task<Void> excelToPatient(final Task<XSSFSheet> loadSheetTask) {


		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub

				XSSFSheet sheet = loadSheetTask.get();

				//excelToPatient
				System.out.println("excelToPatient");
				updateProgress(-1, recordsToRead);

				//if (spaltenFehler) return;

				Iterator<Row> itr = sheet.iterator();
				Row row = itr.next();

				columnStructure<columnIndex> structure = main.start.getColumnIndizes(sheet, "patientendaten");
				//if (spaltenFehler) return;

				try {

					PreparedStatement Pst = cn.prepareStatement("insert into patientendaten (`Geburtsdatum`, `Vorname`, `Name`,"
							+ " `Strasse`, `Hausnummer`, `Land`, `PLZ`, `Ort`, `Fehler`) "
							+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ON DUPLICATE KEY UPDATE `Geburtsdatum`=VALUES(`Geburtsdatum`), `Vorname`= VALUES(`Vorname`), `Name`= VALUES(`Name`),"
							+ " `Strasse`=VALUES(`Strasse`), `Hausnummer`= VALUES(`Hausnummer`), `Land`= VALUES(`Land`), `PLZ`= VALUES(`PLZ`),`Ort`= `Ort`,`Fehler`= `Fehler`) ");

					PreparedStatement Pst_update = cn.prepareStatement("UPDATE mydb.patientendaten\r\n" + 
							"SET\r\n" + 
							"`Strasse` = IFNULL(`Strasse`, ? ),\r\n" + 
							"`Hausnummer` = IFNULL(`Hausnummer`, ? ),\r\n" + 
							"`Land` = IFNULL(`Land`, ? ),\r\n" + 
							"`PLZ` = IFNULL(`PLZ`, ? ),\r\n" + 
							"`Ort` = IFNULL(`Ort`, ? ),\r\n" + 
							"where `Geburtsdatum` = ? and `Vorname` = ? and `Name` = ? ;");

					int i = 0;	//iterator

					while (itr.hasNext() && i < recordsToRead) {

						i++;

						updateProgress(i, recordsToRead*2);
						row = itr.next();
						// Iterating over each column of Excel file

						Cell cell = null;
						Pst.clearParameters();		//clear parameters in Pst for next insert
						Pst_update.clearParameters();
						Pst.setInt(9, 0);		//Fehler column

						//setNull so not every values has to be set in do-while loop
						//Pst.setNull
						for (int j = 4; j < 9; j++) {
							Pst.setNull(j, java.sql.Types.NULL);
						}

						//Pst_update.setNull
						for (int j = 1; j < 9; j++) {
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

				updateProgress(recordsToRead, recordsToRead);
				FX_Main.methodsCompleted = true;

				return null;
			}

		};

		return task;
	}

	public static Task<Void> excelToFall(final Task<XSSFSheet> loadSheetTask) {

		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub

				XSSFSheet sheet = loadSheetTask.get();

				//excelToPatient
				System.out.println("excelToFall");
				updateProgress(-1, recordsToRead);

				//if (spaltenFehler) return;

				Iterator<Row> itr = sheet.iterator();
				Row row = itr.next();

				columnStructure<columnIndex> structure = start.getColumnIndizes(sheet, "fall");
				//if (spaltenFehler) return;

				int befundtextColumnIndex = -1;
				boolean first = true;
				columnIndex columnObject2 = structure.head;

				do {
					if (first) {
						first = false;
					} else {
						columnObject2 = columnObject2.next;									
					}

					if (columnObject2.columnName.equals("befundtext")) {
						befundtextColumnIndex = columnObject2.columnIndex;
						break;
					}
				} while (columnObject2.hasNext());
				columnObject2 = null;

				columnStructure<columnIndex> structureKlassifikation = null;
				if (befundtextColumnIndex == -1) {
					structureKlassifikation = start.getColumnIndizes(sheet, "klassifikation");
				}

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

						updateProgress(recordsToRead + k, recordsToRead*2);
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

						for (int i = 2; i <= 14; i++) {
							Pst_Klassifikation.setNull(i, java.sql.Types.NULL);
						}

						String E_NR = null;
						Befundtyp befundtyp = null;				
						columnIndex columnObject = structure.head;
						boolean first2 = true;

						do {
							if (first2) {
								first2 = false;
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
							System.out.print("Fehler beim Ausführen von \"insert into fall\": Fall ggf. doppelt!" + " ");
						}

						columnObject = structure.head;

						if (befundtextColumnIndex != -1) {
							cell = row.getCell(befundtextColumnIndex);
							String befundtext = cell.getStringCellValue();

							start.excelToKlassifikation_text(Pst_Klassifikation, befundtext, E_NR, befundtyp);					
						} else {
							start.excelToKlassifikation_spalten(Pst_Klassifikation, E_NR, befundtyp, structureKlassifikation, cell);										
							//System.out.println();
						}

					}
					//end of while

					Pst_Fall.close();
					Pst_Klassifikation.close();
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information");
					alert.setHeaderText(null);
					alert.setContentText("Einlesen der Patienten- und Fall-Tabelle abgeschlossen");
					alert.show();

				} catch (SQLException SQLex) {
					System.out.println("Fehler beim Erstellen des PreparedStatement \"insert into fall\"!");
				}

				return null;
			}
		};

		return task;
	}

	public static Task<Void> excelToEinv(final Task<XSSFSheet> loadSheetTask) {

		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub

				XSSFSheet sheet = loadSheetTask.get();

				//excelToPatient
				System.out.println("excelToEinv");
				updateProgress(-1, recordsToRead);

				Iterator<Row> itr = sheet.iterator();
				Row row = itr.next();

				columnStructure<columnIndex> structure = start.getColumnIndizes(sheet, "exprimage");

				PrintWriter pWriter = null; 

				try {




					pWriter = new PrintWriter(new BufferedWriter(new FileWriter("einv.log"))); 


					//					PreparedStatement Pst_UpPat = cn.prepareStatement("UPDATE mydb.patientendaten SET Name=?, altName=?, Vorname=?,Geburtsdatum=?, Strasse=?, Hausnummer=? ,PLZ=? , Ort=? WHERE Geburtsdatum=? AND Vorname=? AND Name=?");
					PreparedStatement Pst_UpPat = cn.prepareStatement("INSERT INTO mydb.patientendaten (Name, altName, Vorname, Geburtsdatum, Strasse, Hausnummer, PLZ, Ort) "
							+ "VALUES (?,?,?,?,?,?,?,?) "
							+ "ON DUPLICATE KEY UPDATE Name=?, altName=?, Vorname=?,Geburtsdatum=?, Strasse=?, Hausnummer=? ,PLZ=? , Ort=?");





					PreparedStatement Pst_Einv = cn.prepareStatement("INSERT INTO mydb.`einverständnis` ( Pseudonym, Pseudonym2, 2015EEStatus, 2015EEDatum, Notizen, QuelleTod, TodDatum, patientendaten_PatientenID) "
							+" VALUES ( ?, ?, ?, ?, ?, ?, ?, (select PatientenID from mydb.patientendaten where Geburtsdatum = ? and Vorname = ? and Name = ? ))"
							+" ON DUPLICATE KEY UPDATE Pseudonym=?, Pseudonym2=?, 2015EEStatus=?, 2015EEDatum=?, Notizen=?, "
							+" QuelleTod=?, TodDatum=?, patientendaten_PatientenID=(select PatientenID from mydb.patientendaten where Geburtsdatum = ? and Vorname = ? and Name = ?);");
					int k = 0;	//iterator

					while (itr.hasNext() && k < recordsToRead) {

						k++;

						updateProgress(recordsToRead + k, recordsToRead*2);
						row = itr.next();
						// Iterating over each column of Excel file

						Pst_Einv.clearParameters();
						Pst_UpPat.clearParameters();

						Cell cell = null;

						columnIndex columnObject = structure.head;
						boolean first = true;
						boolean writeableName = true;
						boolean writeableVorname = true;

						do {
							if (first) {
								first = false;
							} else {
								columnObject = columnObject.next;									
							}

							cell = row.getCell(columnObject.columnIndex);

							if (columnObject.Pst_updateIndex != -1) {
								//this is only executed if the parameter can be inserted into the PreparedStatement

								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_STRING:
									if (columnObject.columnIndex==4){
										Pst_UpPat.setString(9, cell.getStringCellValue());
										Pst_UpPat.setString(1, cell.getStringCellValue());
										//if (writeableName) Pst_UpPat.setString(19, cell.getStringCellValue());
									}
									if (columnObject.columnIndex==6){
										Pst_UpPat.setString(11, cell.getStringCellValue());
										Pst_UpPat.setString(3, cell.getStringCellValue());
										//if (writeableVorname) Pst_UpPat.setString(18, cell.getStringCellValue());
									}
									if (columnObject.columnIndex==5){
										String alt = cell.getStringCellValue();
										if (alt.startsWith("?")){
											Pst_UpPat.setString(10, alt);
											Pst_UpPat.setString(2, alt);
											//Pst_UpPat.setString(18, alt.substring(1));
											writeableVorname = false;
										}else if (alt.startsWith("!") || alt==""){
											Pst_UpPat.setString(10, alt);
											Pst_UpPat.setString(2, alt);
										} else {
											Pst_UpPat.setString(10, alt);
											Pst_UpPat.setString(2, alt);
											//Pst_UpPat.setString(19, alt);
											writeableName = false;
										}
									} 
									if (columnObject.columnIndex==9 || columnObject.columnIndex==10 || columnObject.columnIndex==11 || columnObject.columnIndex==8){
										Pst_UpPat.setString(columnObject.Pst_updateIndex, cell.getStringCellValue());
										Pst_UpPat.setString(columnObject.Pst_updateIndex+8, cell.getStringCellValue());
									}
									break;
								case Cell.CELL_TYPE_NUMERIC:
									if (columnObject.columnIndex==9 || columnObject.columnIndex==10 || columnObject.columnIndex==11 || columnObject.columnIndex==8){
										Pst_UpPat.setInt(columnObject.Pst_updateIndex+8,(int) cell.getNumericCellValue());
										Pst_UpPat.setInt(columnObject.Pst_updateIndex,(int) cell.getNumericCellValue());
									} else if (columnObject.columnIndex==7) {
										Pst_UpPat.setDate(columnObject.Pst_updateIndex+8, new java.sql.Date(cell.getDateCellValue().getTime()));
										Pst_UpPat.setDate(columnObject.Pst_updateIndex, new java.sql.Date(cell.getDateCellValue().getTime()));
										//Pst_UpPat.setDate(17, new java.sql.Date(cell.getDateCellValue().getTime()));
									}
									break;
								case Cell.CELL_TYPE_BLANK:
									if (columnObject.columnIndex==9 || columnObject.columnIndex==5 || columnObject.columnIndex==10 || columnObject.columnIndex==11 || columnObject.columnIndex==12 || columnObject.columnIndex==8){
										Pst_UpPat.setNull(columnObject.Pst_updateIndex, java.sql.Types.NULL);
										Pst_UpPat.setNull(columnObject.Pst_updateIndex+8, java.sql.Types.NULL);
									}
									break;
								}
								//end of switch
							}

						} while (columnObject.hasNext());
						try {
							System.out.println("wait");
							System.out.print("Updated rows in mydb.PatUp: " + Pst_UpPat.executeUpdate() + " - ");
						} catch (SQLException e) {
							//e.printStackTrace();
							System.out.print("PatUp: Fehler beim Ausführen von \"insert into fall\": Fall ggf. doppelt!" + " ");
							pWriter.println(Pst_UpPat.toString());
						}



						//________________________________________________________________________________
						cell = null;
						columnObject = structure.head;
						first = true;

						do {
							if (first) {
								first = false;
							} else {
								columnObject = columnObject.next;									
							}

							cell = row.getCell(columnObject.columnIndex);

							if (columnObject.PstIndex != -1) {
								//this is only executed if the parameter can be inserted into the PreparedStatement

								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_STRING:
									Pst_Einv.setString(columnObject.PstIndex, cell.getStringCellValue());
									break;
								case Cell.CELL_TYPE_NUMERIC:
									if (columnObject.columnIndex == 7 || columnObject.columnIndex == 13 || columnObject.columnIndex == 15 || columnObject.columnIndex == 18 || columnObject.columnIndex == 19){
										//Eingangsdatum, Geburtsdatum oder OP-Datum
										Pst_Einv.setDate(columnObject.PstIndex, new java.sql.Date(cell.getDateCellValue().getTime()));
									}else if (columnObject.columnIndex==3){
										Pst_Einv.setString(columnObject.PstIndex, String.format("%05d", cell.getNumericCellValue()));


										break;
									} else {
										//Befundtyp - nope der steht als Text in excel
										Pst_Einv.setInt(columnObject.PstIndex, (int)cell.getNumericCellValue());
									} 
									break;
								case Cell.CELL_TYPE_BLANK:
									Pst_Einv.setNull(columnObject.PstIndex, java.sql.Types.NULL);
									break;
								}
								//end of switch
							}

						} while (columnObject.hasNext());

						try {
							System.out.print("Updated rows in mydb.fall: " + Pst_Einv.executeUpdate() + " - ");
						} catch (SQLException e) {
							e.printStackTrace();
							System.out.print("Fehler beim Ausführen von \"insert into fall\": Fall ggf. doppelt!" + " ");
							pWriter.println(Pst_Einv.toString());
							pWriter.println(e.getMessage());
						}

						columnObject = structure.head;

					}
					//end of while

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information");
					alert.setHeaderText(null);
					alert.setContentText("Einlesen der Einverständnis2015-Tabelle abgeschlossen");
					alert.show();

					Pst_Einv.close();
					Pst_UpPat.close();
				} catch (SQLException SQLex) {
					System.out.println("Fehler beim Erstellen des PreparedStatement \"insert into fall\"!");
				} catch (IOException ioe) { 
					ioe.printStackTrace(); 
				} finally { 
					
					if (pWriter != null){ 
						pWriter.flush(); 
						pWriter.close(); 
					} 
				} 

				return null;
			}
		};

		return task;
	}

	//17.04 Questor und rumpf für vitaldaten ===================================
	public static Task<Void> excelToQuestor(final Task<XSSFSheet> loadSheetTask) {

		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub

				XSSFSheet sheet = loadSheetTask.get();

				//excelToPatient
				System.out.println("excelToQuestor");
				updateProgress(-1, recordsToRead);

				Iterator<Row> itr = sheet.iterator();
				Row row = itr.next();

				columnStructure<columnIndex> structure = start.getColumnIndizes(sheet, "questor");

				PrintWriter pWriter = null; 

				try {




					pWriter = new PrintWriter(new BufferedWriter(new FileWriter("questor.log"))); 


					//					PreparedStatement Pst_UpPat = cn.prepareStatement("UPDATE mydb.patientendaten SET Name=?, altName=?, Vorname=?,Geburtsdatum=?, Strasse=?, Hausnummer=? ,PLZ=? , Ort=? WHERE Geburtsdatum=? AND Vorname=? AND Name=?");





					PreparedStatement Pst_Qest = cn.prepareStatement("INSERT INTO mydb.`fragebogen`  "
							+" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,?);");
					int k = 0;	//iterator

					while (itr.hasNext() && k < recordsToRead) {

						k++;

						updateProgress(recordsToRead + k, recordsToRead*2);
						row = itr.next();
						// Iterating over each column of Excel file

						Pst_Qest.clearParameters();
						//						Pst_UpPat.clearParameters();

						Cell cell = null;

						columnIndex columnObject = structure.head;
						boolean first = true;

						cell = null;
						columnObject = structure.head;
						first = true;

						do {
							if (first) {
								first = false;
							} else {
								columnObject = columnObject.next;									
							}

							cell = row.getCell(columnObject.columnIndex);

							if (columnObject.PstIndex != -1) {
								//this is only executed if the parameter can be inserted into the PreparedStatement

								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_STRING:
									if (cell.getStringCellValue().equals("00/0000")){
										Pst_Qest.setNull(columnObject.PstIndex, java.sql.Types.NULL);
									} else {
										Pst_Qest.setString(columnObject.PstIndex, cell.getStringCellValue());
									}
									break;
								case Cell.CELL_TYPE_NUMERIC:
									if (columnObject.PstIndex == 5 || columnObject.PstIndex == 30){
										//Eingangsdatum, Geburtsdatum oder OP-Datum
										if ((int) cell.getNumericCellValue()==-42){
											Pst_Qest.setNull(columnObject.PstIndex, java.sql.Types.NULL);
										} else {
											Pst_Qest.setDate(columnObject.PstIndex, new java.sql.Date(cell.getDateCellValue().getTime()));
										} 

									} else if (columnObject.PstIndex == 10 ||columnObject.PstIndex == 11 ||columnObject.PstIndex == 12 ||columnObject.PstIndex == 13 ||columnObject.PstIndex == 14 ||columnObject.PstIndex == 15 || columnObject.PstIndex == 20 || columnObject.PstIndex == 21 ||columnObject.PstIndex == 22 ||columnObject.PstIndex == 23 ||columnObject.PstIndex == 24 ||columnObject.PstIndex == 25 ||columnObject.PstIndex == 26 ||columnObject.PstIndex == 27 ||columnObject.PstIndex == 28) {
										if ((int) cell.getNumericCellValue()==1){
											Pst_Qest.setBoolean(columnObject.PstIndex, true);
										} else if ((int) cell.getNumericCellValue()==-42){
											Pst_Qest.setBoolean(columnObject.PstIndex, false);
										}
									} else if (columnObject.Pst_updateIndex==1){
										Pst_Qest.setString(columnObject.PstIndex, String.format("%05d", cell.getNumericCellValue()));

									}else {
										//Befundtyp - nope der steht als Text in excel
										if ((int) cell.getNumericCellValue()==-42){
											Pst_Qest.setNull(columnObject.PstIndex, java.sql.Types.NULL);
										} else {
											Pst_Qest.setInt(columnObject.PstIndex, (int)cell.getNumericCellValue());;
										} 

									}
									break;
								case Cell.CELL_TYPE_BLANK:
									Pst_Qest.setNull(columnObject.PstIndex, java.sql.Types.NULL);
									break;
								}
								//end of switch
							}

						} while (columnObject.hasNext());

						try {
							System.out.print("Updated rows in mydb.fall: " + Pst_Qest.executeUpdate() + " - ");
						} catch (SQLException e) {
							//e.printStackTrace();
							System.out.print("Fehler beim Ausführen von \"insert into fall\": Fall ggf. doppelt!" + " ");
							pWriter.println(Pst_Qest.toString());
						}

						columnObject = structure.head;

					}
					//end of while

					Pst_Qest.close();
					//					Pst_UpPat.close();
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information");
					alert.setHeaderText(null);
					alert.setContentText("Einlesen der QuestorPro-Tabelle abgeschlossen");
					alert.show();

				} catch (SQLException SQLex) {
					System.out.println("Fehler beim Erstellen des PreparedStatement \"insert into fall\"!");
				} catch (IOException ioe) { 
					ioe.printStackTrace(); 
				} finally { 
					if (pWriter != null){ 
						pWriter.flush(); 
						pWriter.close(); 
					} 
				} 

				return null;
			}
		};

		return task;
	}

	public static Task<Void> excelToEinv2011(final Task<XSSFSheet> loadSheetTask) {

		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub

				XSSFSheet sheet = loadSheetTask.get();

				//excelToPatient
				System.out.println("excelToEinv2011");
				updateProgress(-1, recordsToRead);

				Iterator<Row> itr = sheet.iterator();
				Row row = itr.next();

				columnStructure<columnIndex> structure = start.getColumnIndizes(sheet, "einv2011");

				PrintWriter pWriter = null; 

				try {




					pWriter = new PrintWriter(new BufferedWriter(new FileWriter("einv2011.log"))); 





					PreparedStatement Pst_Einv11 = cn.prepareStatement("INSERT INTO mydb.`einverstaendnis2011` (2011EEStatus, 2011EEDatum, `Rezidiv/Metastase`, RDatum, RDatum2, `Notizen3`, HA, FA ,Chemo, Radiatio, aH ,R, patientendaten_PatientenID) "
							+" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (select PatientenID from mydb.patientendaten where Geburtsdatum = ? and Vorname = ? and Name = ? ))"
							+" ON DUPLICATE KEY UPDATE 2011EEStatus=?, 2011EEDatum=?, `Rezidiv/Metastase`=?, RDatum=?, RDatum2=?, `Notizen3`=?, HA=?, FA=?, Chemo=?, Radiatio=?, aH=?, R=? , "
							+" patientendaten_PatientenID=(select PatientenID from mydb.patientendaten where Geburtsdatum = ? and Vorname = ? and Name = ? );");
					int k = 0;	//iterator

					while (itr.hasNext() && k < recordsToRead) {

						k++;

						updateProgress(recordsToRead + k, recordsToRead*2);
						row = itr.next();
						// Iterating over each column of Excel file

						Pst_Einv11.clearParameters();
						//						Pst_UpPat.clearParameters();

						Cell cell = null;

						columnIndex columnObject = structure.head;
						boolean first = true;
						cell = null;
						columnObject = structure.head;
						first = true;

						do {
							if (first) {
								first = false;
							} else {
								columnObject = columnObject.next;									
							}

							cell = row.getCell(columnObject.columnIndex);

							if (columnObject.PstIndex != -1) {
								//this is only executed if the parameter can be inserted into the PreparedStatement

								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_STRING:
									Pst_Einv11.setString(columnObject.PstIndex, cell.getStringCellValue());
									break;
								case Cell.CELL_TYPE_NUMERIC:
									if (columnObject.PstIndex == 2 || columnObject.PstIndex == 4 || columnObject.PstIndex == 5 || columnObject.PstIndex == 13 || columnObject.PstIndex == 17 || columnObject.PstIndex == 19 || columnObject.PstIndex == 20 || columnObject.PstIndex == 28){
										//Eingangsdatum, Geburtsdatum oder OP-Datum
										Pst_Einv11.setDate(columnObject.PstIndex, new java.sql.Date(cell.getDateCellValue().getTime()));
									} else if (columnObject.PstIndex == 26 || columnObject.PstIndex == 11){
										Pst_Einv11.setString(columnObject.PstIndex,(int) cell.getNumericCellValue()+"");
									} else {
										Pst_Einv11.setInt(columnObject.PstIndex, (int)cell.getNumericCellValue());;
									}
									break;
								case Cell.CELL_TYPE_BLANK:
									Pst_Einv11.setNull(columnObject.PstIndex, java.sql.Types.NULL);
									break;
								}
								//end of switch
							}

						} while (columnObject.hasNext());

						try {
							System.out.print("Updated rows in mydb.fall: " + Pst_Einv11.executeUpdate() + " - ");
						} catch (SQLException e) {
							//e.printStackTrace();
							System.out.print("Fehler beim Ausführen von \"insert into fall\": Fall ggf. doppelt!" + " ");
							pWriter.println(Pst_Einv11.toString());
						}

						columnObject = structure.head;

					}
					//end of while

					Pst_Einv11.close();
					//					Pst_UpPat.close();
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information");
					alert.setHeaderText(null);
					alert.setContentText("Einlesen der Einverständnis2011-Tabelle abgeschlossen");
					alert.show();

				} catch (SQLException SQLex) {
					System.out.println("Fehler beim Erstellen des PreparedStatement \"insert into fall\"!");
				} catch (IOException ioe) { 
					ioe.printStackTrace(); 
				} finally { 
					if (pWriter != null){ 
						pWriter.flush(); 
						pWriter.close(); 
					} 
				} 

				return null;
			}
		};

		return task;
	}
	public static Task<Void> excelToKrebsregister(final Task<XSSFSheet> loadSheetTask) {

		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub

				XSSFSheet sheet = loadSheetTask.get();

				//excelToPatient
				System.out.println("excelToKrebsregister");
				updateProgress(-1, recordsToRead);

				Iterator<Row> itr = sheet.iterator();
				Row row = itr.next();

				columnStructure<columnIndex> structure = start.getColumnIndizes(sheet, "einv2011");

				PrintWriter pWriter = null; 

				try {




					pWriter = new PrintWriter(new BufferedWriter(new FileWriter("krebsr.log"))); 





					PreparedStatement Pst_Einv11 = cn.prepareStatement("INSERT INTO mydb.`einverstaendnis2011` (2011EEStatus, 2011EEDatum, `Rezidiv/Metastase`, RDatum, RDatum2, `Notizen3`, HA, FA ,Chemo, Radiatio, aH ,R, patientendaten_PatientenID) "
							+" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (select PatientenID from mydb.patientendaten where Geburtsdatum = ? and Vorname = ? and Name = ? ))"
							+" ON DUPLICATE KEY UPDATE 2011EEStatus=?, 2011EEDatum=?, `Rezidiv/Metastase`=?, RDatum=?, RDatum2=?, `Notizen3`=?, HA=?, FA=?, Chemo=?, Radiatio=?, aH=?, R=? , "
							+" patientendaten_PatientenID=(select PatientenID from mydb.patientendaten where Geburtsdatum = ? and Vorname = ? and Name = ? );");
					int k = 0;	//iterator

					while (itr.hasNext() && k < recordsToRead) {

						k++;

						updateProgress(recordsToRead + k, recordsToRead*2);
						row = itr.next();
						// Iterating over each column of Excel file

						Pst_Einv11.clearParameters();
						//						Pst_UpPat.clearParameters();

						Cell cell = null;

						columnIndex columnObject = structure.head;
						boolean first = true;
						cell = null;
						columnObject = structure.head;
						first = true;

						do {
							if (first) {
								first = false;
							} else {
								columnObject = columnObject.next;
							}

							cell = row.getCell(columnObject.columnIndex);

							if (columnObject.PstIndex != -1) {
								//this is only executed if the parameter can be inserted into the PreparedStatement

								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_STRING:
									Pst_Einv11.setString(columnObject.PstIndex, cell.getStringCellValue());
									break;
								case Cell.CELL_TYPE_NUMERIC:
									if (columnObject.PstIndex == 2 || columnObject.PstIndex == 4 || columnObject.PstIndex == 5 || columnObject.PstIndex == 13 || columnObject.PstIndex == 17 || columnObject.PstIndex == 19 || columnObject.PstIndex == 20 || columnObject.PstIndex == 28){
										//Eingangsdatum, Geburtsdatum oder OP-Datum
										Pst_Einv11.setDate(columnObject.PstIndex, new java.sql.Date(cell.getDateCellValue().getTime()));
									} else if (columnObject.PstIndex == 26 || columnObject.PstIndex == 11){
										Pst_Einv11.setString(columnObject.PstIndex,(int) cell.getNumericCellValue()+"");
									} else {
										Pst_Einv11.setInt(columnObject.PstIndex, (int)cell.getNumericCellValue());;
									}
									break;
								case Cell.CELL_TYPE_BLANK:
									Pst_Einv11.setNull(columnObject.PstIndex, java.sql.Types.NULL);
									break;
								}
								//end of switch
							}

						} while (columnObject.hasNext());

						try {
							System.out.print("Updated rows in mydb.fall: " + Pst_Einv11.executeUpdate() + " - ");
						} catch (SQLException e) {
							//e.printStackTrace();
							System.out.print("Fehler beim Ausführen von \"insert into fall\": Fall ggf. doppelt!" + " ");
							pWriter.println(Pst_Einv11.toString());
						}

						columnObject = structure.head;

					}
					//end of while

					Pst_Einv11.close();
					//					Pst_UpPat.close();
					System.out.println("Write fall success");
					System.out.println();
				} catch (SQLException SQLex) {
					System.out.println("Fehler beim Erstellen des PreparedStatement \"insert into fall\"!");
				} catch (IOException ioe) { 
					ioe.printStackTrace(); 
				} finally { 
					if (pWriter != null){ 
						pWriter.flush(); 
						pWriter.close(); 
					} 
				} 

				return null;
			}
		};

		return task;
	}
	public static Task<Void> excelToExpri(final Task<XSSFSheet> loadSheetTask) {

		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub

				XSSFSheet sheet = loadSheetTask.get();

				//excelToPatient
				System.out.println("excelToExpri");
				updateProgress(-1, recordsToRead);

				Iterator<Row> itr = sheet.iterator();
				Row row = itr.next();

				PrintWriter pWriter = null; 
				System.out.println("TEST");
				try {
					pWriter = new PrintWriter(new BufferedWriter(new FileWriter("exprimage.log"))); 


					int[] iter = {0,1,3,4,5,25,26,27,56,57,59,61,62,63,64,65,66,67,72,73};
					int k = 0;	//iterator
					exprimageDaten Daten = new exprimageDaten();

					while (itr.hasNext() && k < recordsToRead) {

						k++;
						Daten.clear();
						updateProgress(recordsToRead + k, recordsToRead*2);
						row = itr.next();
						// Iterating over each column of Excel file

						Cell cell = null;

						for (int i: iter){

							cell = row.getCell(i);

							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_STRING:
								switch (cell.getColumnIndex()) {
								case 0:
									Daten.setEinsenderExcel(cell.getStringCellValue());
									break;
								case 1:
									Daten.seteNR(cell.getStringCellValue());
									break;
								case 3:
									Daten.setName(cell.getStringCellValue());
									break;
								case 4:
									Daten.setVorname(cell.getStringCellValue());
									break;
								case 25:
									Daten.setErExcel(cell.getStringCellValue());
									break;
								case 26:
									Daten.setPrExcel(cell.getStringCellValue());
									break;
								case 27:
									Daten.setHer2NeuExcel(cell.getStringCellValue());
									break;
								case 56:
									Daten.setNotizenExcel(cell.getStringCellValue());
									break;
								case 63:
									Daten.setEE2015StatusExcel(cell.getStringCellValue());
									break;
								case 66:
									Daten.setQuelleTodExcel(cell.getStringCellValue());
									break;
								case 72:
									Daten.setNotizen2Excel(cell.getStringCellValue());
									break;
								case 73:
									Daten.setARZT_EXCEL(cell.getStringCellValue());
									break;

								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								switch (cell.getColumnIndex()){
								case 5:
									Daten.setGebDatum(cell.getDateCellValue());
									break;
								case 57:
									Daten.setChemoExcel((int) cell.getNumericCellValue());
									break;
								case 58:
									Daten.setMedAntihormonTamoxifenExcel((int) cell.getNumericCellValue());
									break;
								case 61:
									Daten.setrDatumExcel(cell.getDateCellValue());
									break;
								case 62:
									Daten.setrDatum2Excel(cell.getDateCellValue());
									break;
								case 64:
									Daten.setEE2015DatumExcel(cell.getDateCellValue());
									break;
								case 67:
									Daten.setTodDatumExcel(cell.getDateCellValue());
									break;
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								//TODO Exception handling? Not needed, oldest Source of Data 
								break;
							}
							//end of switch
						}


						//ResultSetMetaData rsMeta = rs.getMetaData();
						String statement = "select klass.er, klass.pr, klass.`Her2/neu`, fall.Einsender, patd.PatientenID from "
								+ "mydb.klassifikation as klass join mydb.fall as fall  join mydb.patientendaten as patd "
								+ "on klass.`Fall_E.-Nummer` = fall.`E.-Nummer` and klass.Fall_Befundtyp = fall.Befundtyp and patd.PatientenID = fall.Patientendaten_PatientenID "
								+ "where patd.`Name`= '"+Daten.getName()+"' and patd.Vorname = '"+Daten.getVorname()+"' and patd.Geburtsdatum = '"+Daten.getGebDatum()+"' "
								+ "and fall.`E.-Nummer` = '"+Daten.geteNR()+"';";
						ResultSet rs = cn.createStatement().executeQuery(statement);
						while (rs.next() ){
							Daten.setErDB(rs.getString(1));
							Daten.setPrDB(rs.getString(2));
							Daten.setHer2NeuDB(rs.getString(3));
							Daten.setEinsenderDB(rs.getString(4));
							Daten.setPatIDDB(rs.getInt(5));
						}

						statement = "select ee2015.Notizen, frag.Chemo, frag.med_antihormon_tamoxifen, ee2015.`2015EEStatus`, ee2015.`2015EEDatum`, ee2015.QuelleTod, "
								+ "ee2015.TodDatum from mydb.fragebogen as frag join mydb.einverständnis as ee2015 join mydb.patientendaten as patd "
								+ "on frag.Pseudonym = ee2015.Pseudonym and ee2015.patientendaten_PatientenID = patd.PatientenID "
								+ "where patd.`Name`= '"+Daten.getName()+"' and patd.Vorname = '"+Daten.getVorname()+"' and patd.Geburtsdatum = '"+Daten.getGebDatum()+"' ;";
						rs = cn.createStatement().executeQuery(statement);
						while (rs.next() ){
							Daten.setNotizenDB(rs.getString(1));
							Daten.setChemoDB(rs.getString(2));
							Daten.setMedAntihormonTamoxifenDB(rs.getInt(3));
							Daten.setEE2015StatusDB(rs.getString(4));
							Daten.setEE2015DatumDB(rs.getDate(5));
							Daten.setQuelleTodDB(rs.getString(6));
							Daten.setTodDatumDB(rs.getDate(7));
						}

						statement = "select ee2011.RDatum, ee2011.RDatum2, ee2011.HA, ee2011.FA from mydb.patientendaten as patd "
								+ "join mydb.einverstaendnis2011 as ee2011 on patd.PatientenID = ee2011.patientendaten_PatientenID "
								+ "where patd.`Name`= '"+Daten.getName()+"' and patd.Vorname = '"+Daten.getVorname()+"' and patd.Geburtsdatum = '"+Daten.getGebDatum()+"' ;";
						rs = cn.createStatement().executeQuery(statement);
						while (rs.next() ){
							Daten.setrDatumDB(rs.getDate(1));
							Daten.setrDatum2DB(rs.getDate(2));
							Daten.setHA_DB(rs.getString(3));
							Daten.setFA_DB(rs.getString(4));
						}
						List<String> list = new ArrayList<String>();
						list = Daten.buildStatment();

						cn.setAutoCommit(false);
						for (String updStatement : list) { 
							if (Daten.getPatIDDB()==563){
								System.out.println(updStatement);
							}
							if (cn.createStatement().executeUpdate(updStatement)==0){
								pWriter.println("0 Rows: "+ updStatement);
							};
							
						}
						cn.commit();
						//end of while

					}
					cn.setAutoCommit(true);
					//					Pst_UpPat.close();
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information");
					alert.setHeaderText(null);
					alert.setContentText("Einlesen der Exprimage-Tabelle abgeschlossen");
					alert.show();

				} catch (SQLException SQLex) {
					SQLex.printStackTrace();
					pWriter.println(SQLex.getMessage());
				} catch (IOException ioe) { 
					ioe.printStackTrace(); 
				} catch (Exception e){
					e.printStackTrace();
				} finally { 
					if (pWriter != null){ 
						pWriter.flush(); 
						pWriter.close(); 

					} 
				} 


				return null;
			} 
		};

		return task;
	}
	//==========================================================================

}
