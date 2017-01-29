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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
							+ " `Strasse`=VALUES(`Strasse`), `Hausnummer`= VALUES(`Hausnummer`), `Land`= VALUES(`Land`), `PLZ`= VALUES(`PLZ`),`Ort`= VALUES(`Ort`),`Fehler`= VALUES(`Fehler`) ");

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
				int tColumnIndex = -1;
				int nColumnIndex = -1;
				boolean first = true;
				columnIndex columnObject2 = structure.head;

				do {
					if (first) {
						first = false;
					} else {
						columnObject2 = columnObject2.next;									
					}
					/*	*/
					//TODO
					switch (columnObject2.columnName){
					case "befundtext":befundtextColumnIndex = columnObject2.columnIndex;
					break;
					case "t":tColumnIndex = columnObject2.columnIndex;
					break;
					case "n":nColumnIndex = columnObject2.columnIndex;
					break;
					}



					/*
					if (columnObject2.columnName.equals("befundtext")) {
						befundtextColumnIndex = columnObject2.columnIndex;
						break;
					}*/
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
							+ " ? , ? , ? , ? , ? , ? , ? , ? , ? )"
							+ "ON DUPLICATE KEY UPDATE `Patientendaten_PatientenID` = VALUES(`Patientendaten_PatientenID`)"
							+ ", `E.-Nummer` = VALUES(`E.-Nummer`), `Eingangsdatum` = VALUES(`Eingangsdatum`), `Einsender` = VALUES(`Einsender`)"
							+ ", `Befundtyp` = VALUES(`Befundtyp`), `Fehler` = VALUES(`Fehler`), `Arzt` = VALUES(`Arzt`), `Kryo` = VALUES(`Kryo`)"
							+ ", `OP-Datum` = VALUES(`OP-Datum`), `Mikroskopie` = VALUES(`Mikroskopie`);");
					PreparedStatement Pst_Klassifikation = cn.prepareStatement("insert into mydb.klassifikation (`Fall_E.-Nummer`, `Fall_Befundtyp`, "
							+ "G, T, N, M, L, V, R, ER, PR, `Her2/neu`, Lage, Tumorart, `Her2/neu-Score`) "
							+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ? )"
							+ "ON DUPLICATE KEY UPDATE `Fall_E.-Nummer` = VALUES(`Fall_E.-Nummer`), `Fall_Befundtyp` = VALUES(`Fall_Befundtyp`), "
							+ "G = VALUES(G), T = VALUES(T), N = VALUES(N), M = VALUES(M), L = VALUES(L), V = VALUES(V), R = VALUES(R), "
							+ "ER = VALUES(ER), PR = VALUES(PR), `Her2/neu` = VALUES(`Her2/neu`), Lage = VALUES(Lage), Tumorart = VALUES(Tumorart);");


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

								if (cell.getCellType()==Cell.CELL_TYPE_STRING && cell.getStringCellValue().equals("A/1996/302669")){
									System.out.println("");
								}
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
							cell =row.getCell(tColumnIndex);
							String t = cell.getStringCellValue();
							cell =row.getCell(nColumnIndex);
							String n = cell.getStringCellValue();

							start.excelToKlassifikation_text(Pst_Klassifikation, befundtext, E_NR, befundtyp, t, n);					
						} else {
							start.excelToKlassifikation_spalten(Pst_Klassifikation, E_NR, befundtyp, structureKlassifikation, cell);										
							System.out.println("ERROR");
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
				String error = null;

				try {

					pWriter = new PrintWriter(new BufferedWriter(new FileWriter("einv.log"))); 

					// INSERT DATA into "patientendaten"
					PreparedStatement Pst_UpPat = cn.prepareStatement("INSERT INTO mydb.patientendaten (Name, altName, Vorname, "
							+ "Geburtsdatum, Strasse, Hausnummer, PLZ, Ort) "
							+ "VALUES (?,?,?,?,?,?,?,?) "
							+ "ON DUPLICATE KEY UPDATE Name=VALUES(Name), altName=VALUES(altName), Vorname=VALUES(Vorname),"
							+ "Geburtsdatum=VALUES(Geburtsdatum), Strasse=VALUES(Strasse), Hausnummer=VALUES(Hausnummer) ,PLZ=VALUES(PLZ) , "
							+ "Ort=VALUES(Ort)");

					// FILL "patientenzusatz" with additional DATA
					PreparedStatement Pst_Einv = cn.prepareStatement("INSERT INTO mydb.patientenzusatz (fragebogen_pseudonym, ee2015Pseudonym2, "
							+ "EE2015Status, EE2015Datum, EE2015Notizen, EE2015TodQuelle, EE2015TodDatum, patientendaten_PatientenID) "
							+" VALUES ( ?, ?, ?, ?, ?, ?, ?, (select PatientenID from mydb.patientendaten "
							+ "where Geburtsdatum = ? and Vorname = ? and Name = ? ))"
							+" ON DUPLICATE KEY UPDATE fragebogen_pseudonym=VALUES(fragebogen_pseudonym), ee2015Pseudonym2=VALUES(ee2015Pseudonym2),"
							+ "ee2015Status=VALUES(ee2015Status), ee2015Datum=VALUES(ee2015Datum), ee2015Notizen=VALUES(ee2015Datum), "
							+ "EE2015TodQuelle=VALUES(EE2015TodQuelle), ee2015TodDatum=VALUES(ee2015TodDatum), "
							+ "patientendaten_PatientenID=values(patientendaten_PatientenID);");

					PreparedStatement Pst_Frag = cn.prepareStatement("insert into fragebogen (pseudonym) VALUES (?) on DUPLICATE KEY UPDATE pseudonym = VALUES(pseudonym);");

					int k = 0;	//iterator

					while (itr.hasNext() && k < recordsToRead) {

						k++;

						updateProgress(recordsToRead + k, recordsToRead*2);
						row = itr.next();
						// Iterating over each column of Excel file

						Pst_Einv.clearParameters();
						Pst_UpPat.clearParameters();
						Pst_Frag.clearParameters();

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
									if (columnObject.columnName.equalsIgnoreCase("nachname")){
										Pst_UpPat.setString(1, cell.getStringCellValue());
									}
									if (columnObject.columnName.equalsIgnoreCase("vorname")){
										Pst_UpPat.setString(3, cell.getStringCellValue());
									}
									if (columnObject.columnName.equalsIgnoreCase("altern. name")){
										//TODO Austauschen von NAME\VORNAME gegen ALT -- PST Verändern?
										String alt = cell.getStringCellValue();
										if (alt.startsWith("?")){
											Pst_UpPat.setString(2, alt);
											writeableVorname = false;
										}else if (alt.startsWith("!") || alt==""){
											Pst_UpPat.setString(2, alt);
										} else {
											Pst_UpPat.setString(2, alt);
											writeableName = false;
										}
									} 
									if (columnObject.columnName.equalsIgnoreCase("straße") || columnObject.columnName.equalsIgnoreCase("hausnr.")
											|| columnObject.columnName.equalsIgnoreCase("plz") || columnObject.columnName.equalsIgnoreCase("stadt")){
										Pst_UpPat.setString(columnObject.Pst_updateIndex, cell.getStringCellValue());
									}
									if (columnObject.columnName.equalsIgnoreCase("geb.datum")){
										error+= "[kein Datum, sondern String (" + cell.getStringCellValue()+ ")]";
									}
									break;
								case Cell.CELL_TYPE_NUMERIC:
									if (columnObject.columnName.equalsIgnoreCase("straße") || columnObject.columnName.equalsIgnoreCase("hausnr.")
											|| columnObject.columnName.equalsIgnoreCase("plz") || columnObject.columnName.equalsIgnoreCase("stadt")){
										Pst_UpPat.setInt(columnObject.Pst_updateIndex,(int) cell.getNumericCellValue());
									} else if (columnObject.columnName.equalsIgnoreCase("geb.datum")) {
										Pst_UpPat.setDate(columnObject.Pst_updateIndex, new java.sql.Date(cell.getDateCellValue().getTime()));
									}

									break;
								case Cell.CELL_TYPE_BLANK:
									if (columnObject.columnName.equalsIgnoreCase("straße") || columnObject.columnName.equalsIgnoreCase("hausnr.")
											|| columnObject.columnName.equalsIgnoreCase("plz") || columnObject.columnName.equalsIgnoreCase("stadt") || 
											columnObject.columnName.equalsIgnoreCase("altern. name")){
										Pst_UpPat.setNull(columnObject.Pst_updateIndex, java.sql.Types.NULL);
									}
									if (columnObject.columnName.equalsIgnoreCase("geb.datum") || columnObject.columnName.equalsIgnoreCase("nachname") 
											|| columnObject.columnName.equalsIgnoreCase("vorname")) {
										error+= "[kein eindeutiges Tupel (geb.datum, vorname oder nachname ist leer)]";
									}
									break;
								}
								//end of switch
							}

						} while (columnObject.hasNext());
						try {
							int upsertrow = Pst_UpPat.executeUpdate();
							if (upsertrow == 0) {
								error+= "[keine Änderung]";
							}
						} catch (SQLException e) {
							error+= "["+e.toString()+"]";
						}



						//________________________________________________________________________________
						cell = null;
						columnObject = structure.head;
						first = true;
						int eeStatus=0;

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
									if (columnObject.columnName.equals("pseudonym")){
										Pst_Einv.setString(columnObject.PstIndex, cell.getStringCellValue());
										Pst_Frag.setString(1, cell.getStringCellValue());
									} else {
										Pst_Einv.setString(columnObject.PstIndex, cell.getStringCellValue());
									}
									break;
								case Cell.CELL_TYPE_NUMERIC:
									if (columnObject.columnName.equalsIgnoreCase("tod_dat")|| columnObject.columnName.equalsIgnoreCase("datum ee 2015") || columnObject.columnName.equalsIgnoreCase("geb.datum")){
										//Eingangsdatum, Geburtsdatum oder OP-Datum
										Pst_Einv.setDate(columnObject.PstIndex, new java.sql.Date(cell.getDateCellValue().getTime()));
									}else if (columnObject.columnName.equalsIgnoreCase("pseudonym2") || columnObject.columnName.equalsIgnoreCase("pseudonym")){
										Pst_Einv.setString(columnObject.PstIndex, String.format("%05d", cell.getNumericCellValue()));
										break;
									}else if (columnObject.columnName.equalsIgnoreCase("ee 2015")){
										eeStatus=(int)cell.getNumericCellValue();
										Pst_Einv.setInt(columnObject.PstIndex, (int)cell.getNumericCellValue());
										break;
									} else {
										//Befundtyp - nope der steht als Text in excel
										Pst_Einv.setInt(columnObject.PstIndex, (int)cell.getNumericCellValue());
									} 
									break;
								case Cell.CELL_TYPE_BLANK:
									if (columnObject.columnName.equalsIgnoreCase("geb.datum") || columnObject.columnName.equalsIgnoreCase("nachname") 
											|| columnObject.columnName.equalsIgnoreCase("vorname")) {
										error+= "[EE2015 kein eindeutiges Tupel (geb.datum, vorname oder nachname ist leer)]";
									} else {
										Pst_Einv.setNull(columnObject.PstIndex, java.sql.Types.NULL);
									}
									break;
								}
								//end of switch
							}

						} while (columnObject.hasNext());

						try {
							int upsertrow; 
							if (k==31) {
								System.out.println();
							}
							upsertrow = Pst_Frag.executeUpdate();
							if (upsertrow == 0) {
								error+= "[EE2015 keine Änderung in Fragebogen]";
							}
							upsertrow = Pst_Einv.executeUpdate();
							if (upsertrow == 0) {
								error+= "[EE2015 keine Änderung in Patientenzusatz]";
							}
						} catch (SQLException e) {
							error+= "[EE2015 "+e.toString()+"]";
						}
						System.out.println(Pst_UpPat);
						columnObject = structure.head;

					}
					//end of while

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information");
					alert.setHeaderText(null);
					alert.setContentText("Einlesen der Einverständnis2015-Tabelle abgeschlossen: " + error);
					alert.show();

					Pst_Einv.close();
					Pst_UpPat.close();
				} catch (SQLException SQLex) {
					error+= "["+SQLex+"]";
				} catch (IOException ioe) { 
					error+= "["+ioe+"]";
				} finally { 

					if (error != null){
						pWriter.println(error);
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





					PreparedStatement Pst_Qest = cn.prepareStatement("update fragebogen set welle=?, rawid=?, source=?, zeit=?, chemo=?, chemo_zeitpunkt=?, medikamente=?, "
							+ "bestrahlung=?, med_anithormon=?, med_antihormon_unbekannt=?, med_antihormon_tamoxifen=?, med_antihormon_arimidex=?, med_antihormon_aromasin=?, "
							+ "med_antihormon_fe03a=?, herceptin=?, biophosphonaten=?, biophosphaten_text=?, weitere_erkrankung=?, rezidiv=?, metastasen=?, metastasen_abrust=?, "
							+ "metastasen_lymphknoten=?, metastasen_knochen=?, metastasen_lunge=?, metastasen_gehirn=?, metastasen_leber=?, metastasen_andere=?, "
							+ "metastasen_andere_text=?, rezidiv_zeitpunkt=?, hausarzt=?, frauenarzt=?, anmerkungen=?, information=? where pseudonym=?;");


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
									if (columnObject.columnName.equals("zeit") || columnObject.columnName.equals("6.3 wann ist die krebserkrankung erneut aufgetreten?")){
										//Eingangsdatum, Geburtsdatum oder OP-Datum
										if ((int) cell.getNumericCellValue()==-42){
											Pst_Qest.setNull(columnObject.PstIndex, java.sql.Types.NULL);
										} else {
											Pst_Qest.setDate(columnObject.PstIndex, new java.sql.Date(cell.getDateCellValue().getTime()));
										} 

									} else if (columnObject.PstIndex == 10 ||columnObject.PstIndex == 11 ||columnObject.PstIndex == 12 ||columnObject.PstIndex == 13 ||columnObject.PstIndex == 14 ||columnObject.PstIndex == 9 || columnObject.PstIndex == 20 || columnObject.PstIndex == 21 ||columnObject.PstIndex == 22 ||columnObject.PstIndex == 23 ||columnObject.PstIndex == 24 ||columnObject.PstIndex == 25 ||columnObject.PstIndex == 26 ||columnObject.PstIndex == 27 ||columnObject.PstIndex == 19) {
										if ((int) cell.getNumericCellValue()==1){
											Pst_Qest.setBoolean(columnObject.PstIndex, true);
										} else if ((int) cell.getNumericCellValue()==-42){
											Pst_Qest.setBoolean(columnObject.PstIndex, false);
										}
									} else if (columnObject.columnName.equals("pseudonym")){
										Pst_Qest.setString(columnObject.PstIndex, String.format("%05d",(int) cell.getNumericCellValue()));

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
				String error=null;

				try {




					pWriter = new PrintWriter(new BufferedWriter(new FileWriter("einv2011.log"))); 




					PreparedStatement Pst_Einv11 = cn.prepareStatement("INSERT INTO mydb.patientenzusatz (EE2011status, EE2011Datum, "
							+ "EE2011Rezidiv_Metastase, EE2011RDatum, EE2011RDatum2, EE2011Notizen3, EE2011HA, EE2011FA ,EE2011Chemo, "
							+ "EE2011Radiatio, EE2011aH ,EE2011R, patientendaten_PatientenID) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
							+ "(select PatientenID from mydb.patientendaten where Geburtsdatum = ? and Vorname = ? and Name = ? )) "
							+ "ON DUPLICATE KEY UPDATE EE2011status=values(EE2011status), EE2011datum=values(EE2011datum), "
							+ "EE2011Rezidiv_Metastase=values(EE2011Rezidiv_Metastase), EE2011RDatum=values(EE2011RDatum), "
							+ "EE2011RDatum2=values(EE2011RDatum2), EE2011Notizen3=values(EE2011Notizen3), EE2011HA=values(EE2011HA), "
							+ "EE2011FA=values(EE2011FA), EE2011Chemo=values(EE2011Chemo), EE2011Radiatio=values(EE2011Radiatio), "
							+ "EE2011aH=values(EE2011aH), EE2011R=values(EE2011R) ,  patientendaten_PatientenID=values(patientendaten_PatientenID);");
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
									if (columnObject.columnName.equals("datum ee 2011") || columnObject.columnName.equals("geb.datum") || 
											columnObject.columnName.equals("r_datum1") || columnObject.columnName.equals("r_datum2")){
										//Eingangsdatum, Geburtsdatum oder OP-Datum
										Pst_Einv11.setDate(columnObject.PstIndex, new java.sql.Date(cell.getDateCellValue().getTime()));
									} else if (columnObject.columnName.equals("ah")){
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
							int upsertrow = Pst_Einv11.executeUpdate();
							if (upsertrow == 0) {
								error+= "[EE2015 keine Änderung]";
							}
						} catch (SQLException e) {
							error+= "[EE2015 "+e.toString()+"]";
						}

						columnObject = structure.head;

					}
					//end of while

					Pst_Einv11.close();
					//					Pst_UpPat.close();
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information");
					alert.setHeaderText(null);
					alert.setContentText("Einlesen der Einverständnis2011-Tabelle abgeschlossen"+ error);
					alert.show();

				} catch (SQLException SQLex) {
					error+= "["+SQLex+"]";
				} catch (IOException ioe) { 
					error+= "["+ioe+"]";
				}catch (Exception e){ 
					System.out.println(e.toString());
				}finally { 

					if (error != null){
						pWriter.println(error);
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
		//used for Plausibilitätstest_Ronja

		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				
				XSSFSheet sheet = loadSheetTask.get();

				
				System.out.println("excelToPlausi");
				updateProgress(-1, recordsToRead);

				Iterator<Row> itr = sheet.iterator();
				Row row = itr.next();


				PrintWriter pWriter = null; 

				try {




					pWriter = new PrintWriter(new BufferedWriter(new FileWriter("plausi.log"))); 





					PreparedStatement Pst_plausi = cn.prepareStatement("INSERT INTO mydb.klassifikation "
							+ "(klassifikation.`Fall_E.-Nummer`, g, t, n, m, v, l, r, ER, PR, `Her2/neu`, lage, Tumorart) "
							+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE "
							+ "klassifikation.`Fall_E.-Nummer`=values(klassifikation.`Fall_E.-Nummer`), g=values(g), t=values(t), n=values(n), m=values(m),"
							+ " v=values(v), l=values(l), r=values(r), er=values(er), pr=values(pr), `Her2/neu`=values(`Her2/neu`), lage=values(lage), "
							+ "Tumorart=values(Tumorart) ;");
					Tumorart tumorart = new Tumorart();
					int k = 0;	//iterator

					int[] iter = {10,14,16,19,21,23,25,27,29,31,33,36,37,38,39,40};
					while (itr.hasNext() && k < recordsToRead) {
						k++;
						updateProgress(recordsToRead + k, recordsToRead*2);
						row = itr.next();
						// Iterating over each column of Excel file

						Cell cell = null;

						for (int i: iter){
							cell = row.getCell(i);

							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_STRING:
								switch (cell.getColumnIndex()) {
								case 10:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(1, cell.getStringCellValue());
									break;
								case 14:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(2, cell.getStringCellValue());
									break;
								case 16:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(3, cell.getStringCellValue());
									break;
								case 19:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(4, cell.getStringCellValue());
									break;
								case 21:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(5, cell.getStringCellValue());
									break;
								case 23:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(6, cell.getStringCellValue());
									break;
								case 25:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(7, cell.getStringCellValue());
									break;
								case 27:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(8, cell.getStringCellValue());
									break;
								case 29:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(9, cell.getStringCellValue());
									break;
								case 31:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(10, cell.getStringCellValue());
									break;
								case 33:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(11, cell.getStringCellValue());
									break;
								case 36:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(12, cell.getStringCellValue());
									break;
								case 37:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										tumorart.setTumorArt1(cell.getStringCellValue());
									break;
								case 38:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										tumorart.setTumorArt2(cell.getStringCellValue());
									break;
								case 39:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										tumorart.setTumorArt3(cell.getStringCellValue());
									break;
								case 40:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										tumorart.setTumorArt4(cell.getStringCellValue());
									break;
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								switch (cell.getColumnIndex()) {
								case 10:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(1, cell.getNumericCellValue()+"");
									break;
								case 14:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(2, cell.getNumericCellValue()+"");
									break;
								case 16:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(3, cell.getNumericCellValue()+"");
									break;
								case 19:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(4, cell.getNumericCellValue()+"");
									break;
								case 21:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(5, cell.getNumericCellValue()+"");
									break;
								case 23:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(6, cell.getNumericCellValue()+"");
									break;
								case 25:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(7, cell.getNumericCellValue()+"");
									break;
								case 27:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(8, cell.getNumericCellValue()+"");
									break;
								case 29:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(9, cell.getNumericCellValue()+"");
									break;
								case 31:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(10, cell.getNumericCellValue()+"");
									break;
								case 33:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(11, cell.getNumericCellValue()+"");
									break;
								case 36:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Pst_plausi.setString(12, cell.getNumericCellValue()+"");
									break;
								case 37:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										tumorart.setTumorArt1(cell.getNumericCellValue()+"");
									break;
								case 38:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										tumorart.setTumorArt2(cell.getNumericCellValue()+"");
									break;
								case 39:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										tumorart.setTumorArt3(cell.getNumericCellValue()+"");
									break;
								case 40:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										tumorart.setTumorArt4(cell.getNumericCellValue()+"");
									break;
								}
								break;

							}
						}
						Pst_plausi.setString(13, tumorart.toString());
						int upsertrow = Pst_plausi.executeUpdate();
						System.out.println(upsertrow);

					}
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
				try {
					pWriter = new PrintWriter(new BufferedWriter(new FileWriter("exprimage.log"))); 

					int[] iter = {0,1,3,4,5,11,12,13,14,15,16,17,25,26,27,28,56,57,58,59,60,61,62,63,64,65,66,67,72,73};
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
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setEinsenderExcel(cell.getStringCellValue());
									break;
								case 1:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.seteNR(cell.getStringCellValue());
									break;
								case 3:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setName(cell.getStringCellValue());

									break;
								case 4:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setVorname(cell.getStringCellValue());
									break;
								case 11:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setGExcel(cell.getStringCellValue());
									break;
								case 12:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setTExcel(cell.getStringCellValue());
									break;
								case 13:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setNExcel(cell.getStringCellValue());
									break;
								case 14:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setMExcel(cell.getStringCellValue());
									break;
								case 15:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setLExcel(cell.getStringCellValue());
									break;
								case 16:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setVExcel(cell.getStringCellValue());
									break;
								case 17:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setRExcel(cell.getStringCellValue());
									break;
								case 25:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setErExcel(cell.getStringCellValue());
									break;
								case 26:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setPrExcel(cell.getStringCellValue());
									break;
								case 27:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setHer2NeuScoreExcel(cell.getStringCellValue());
									break;
								case 28:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setTumorArtExcel(cell.getStringCellValue());
								case 56:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setNotizenExcel(cell.getStringCellValue());
									break;
								case 59:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setMedAntihormonAromataseHExcel(cell.getStringCellValue());
									break;
								case 60:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setBestrahlung(cell.getStringCellValue());
									break;
								case 63:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setEE2015StatusExcel(cell.getStringCellValue());
									break;
								case 65:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setExpFU(cell.getStringCellValue());
									break;
								case 66:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setQuelleTodExcel(cell.getStringCellValue());
									break;
								case 72:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setNotizen2Excel(cell.getStringCellValue());
									break;
								case 73:
									if (cell.getStringCellValue()!="" && cell.getStringCellValue()!=" ")
										Daten.setARZT_EXCEL(cell.getStringCellValue());
									break;

								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								switch (cell.getColumnIndex()){
								case 5:
									Daten.setGebDatum(cell.getDateCellValue());
									break;
								case 11:
									Daten.setGExcel((int) cell.getNumericCellValue()+"");
									break;
								case 12:
									Daten.setTExcel((int) cell.getNumericCellValue()+"");
									break;
								case 13:
									Daten.setNExcel((int) cell.getNumericCellValue()+"");
									break;
								case 14:
									Daten.setMExcel((int) cell.getNumericCellValue()+"");
									break;
								case 15:
									Daten.setLExcel((int) cell.getNumericCellValue()+"");
									break;
								case 16:
									Daten.setVExcel((int) cell.getNumericCellValue()+"");
									break;
								case 17:
									Daten.setRExcel((int) cell.getNumericCellValue()+"");
									break;
								case 28:
									Daten.setTumorArtExcel((int) cell.getNumericCellValue()+"");
									break;
								case 57:
									Daten.setChemoExcel((int) cell.getNumericCellValue());
									break;
								case 58:
									Daten.setMedAntihormonTamoxifenExcel((int) cell.getNumericCellValue());
								case 59:
									Daten.setMedAntihormonAromataseHExcel((int) cell.getNumericCellValue()+"");
									break;
								case 60:
									Daten.setBestrahlung((int) cell.getNumericCellValue()+"");
									break;
								case 61:
									Daten.setrDatumExcel(cell.getDateCellValue());
									break;
								case 62:
									Daten.setrDatum2Excel(cell.getDateCellValue());
									break;
								case 63:
									Daten.setEE2015StatusExcel((int) cell.getNumericCellValue()+"");
									break;
								case 64:
									Daten.setEE2015DatumExcel(cell.getDateCellValue());
									break;
								case 65:
									Daten.setExpFU((int) cell.getNumericCellValue()+"");
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
						String statement = "select klass.er, klass.pr, klass.`Her2/neu-Score`, klass.Lage, klass.Tumorart,"
								+ " fall.Einsender, patd.PatientenID, klass.G, klass.T, klass.N, klass.M, klass.L, klass.V,"
								+ " klass.R, patd.Geburtsdatum, patd.`Name`, patd.Vorname from "
								+ "mydb.klassifikation as klass join mydb.fall as fall  join mydb.patientendaten as patd "
								+ "on klass.`Fall_E.-Nummer` = fall.`E.-Nummer` and klass.Fall_Befundtyp = fall.Befundtyp and patd.PatientenID = fall.Patientendaten_PatientenID "
								+ "where fall.`E.-Nummer` = '"+Daten.geteNR()+"';";
						ResultSet rs = cn.createStatement().executeQuery(statement);


						if (!rs.isBeforeFirst() ) {
							int rowReturnByStatement;
							statement = "INSERT INTO mydb.patientendaten (Name, Vorname, Geburtsdatum) "
									+" VALUES ( " + Daten.getName() + ", " + Daten.getVorname() + ", " + Daten.getGebDatum() + ")"
									+" ON DUPLICATE KEY UPDATE Name=VALUES(Name), Vorname=VALUES(Vorname), Geburtsdatum=VALUES(Geburtsdatum);";
							rowReturnByStatement = cn.createStatement().executeUpdate(statement);
							statement = "INSERT INTO mydb.fall (`E.-Nummer`, Befundtyp, Patientendaten_PatientenID) "
									+" VALUES ( '" + Daten.geteNR() + "', Befundtyp = 0, (select PatientenID from Patientendaten where Name =" + Daten.getName() + 
									" AND Vorname = " + Daten.getVorname() + " AND Geburtsdatum = " + Daten.getGebDatum() + "))"
									+" ON DUPLICATE KEY UPDATE `E.-Nummer`=VALUES(`E.-Nummer`), Befundtyp=VALUES(Befundtyp), Patientendaten_PatientenID=VALUES(Patientendaten_PatientenID);";
							rowReturnByStatement = cn.createStatement().executeUpdate(statement);
							statement = "INSERT INTO mydb.klassifikation (`Fall_E.-Nummer`, Fall_Befundtyp) "
									+" VALUES ( '" + Daten.geteNR() + "', Fall_Befundtyp = 0)"
									+" ON DUPLICATE KEY UPDATE `Fall_E.-Nummer`=VALUES(`Fall_E.-Nummer`), Fall_Befundtyp=VALUES(Fall_Befundtyp);";
							rowReturnByStatement = cn.createStatement().executeUpdate(statement);
							System.out.println(rowReturnByStatement);
						} 

						while (rs.next() ){
							Daten.setErDB(rs.getString(1));
							Daten.setPrDB(rs.getString(2));
							Daten.setHer2NeuScoreDB(rs.getString(3));
							//TODO Daten.setTumorDB(rs.getString(4),rs.getString(5)); 
							Daten.setEinsenderDB(rs.getString(6));
							String save = rs.getString(7);
							if (save != null) Daten.setPatIDDB(rs.getInt(7));
							Daten.setGDB(rs.getString(8));
							Daten.setTDB(rs.getString(9));
							Daten.setNDB(rs.getString(10));
							Daten.setMDB(rs.getString(11));
							Daten.setLDB(rs.getString(12));
							Daten.setVDB(rs.getString(13));
							Daten.setRDB(rs.getString(14));
							if (rs.getDate(15)!=null) Daten.setGebDatum(rs.getDate(15));
							if (rs.getString(16)!=null) Daten.setName(rs.getString(16));
							if (rs.getString(17)!=null) Daten.setVorname(rs.getString(17));
						}

						//						statement = "select ee2015.Notizen, frag.Chemo, frag.med_antihormon_tamoxifen, ee2015.`2015EEStatus`, ee2015.`2015EEDatum`, ee2015.QuelleTod, "
						//								+ "ee2015.TodDatum from mydb.fragebogen as frag join mydb.einverständnis as ee2015 join mydb.patientendaten as patd "
						//								+ "on frag.Pseudonym = ee2015.Pseudonym and ee2015.patientendaten_PatientenID = patd.PatientenID "
						//								+ "where patd.PatientenID= '"+Daten.getPatIDDB()+"' ;";
						//						rs = cn.createStatement().executeQuery(statement);
						//						if (rs.getMetaData().getColumnCount()==0 && patient){
						//							pWriter.println("Patient vorhanden: "+statement);
						//						}
						//
						//						while (rs.next() ){
						//							Daten.setNotizenDB(rs.getString(1));
						//							Daten.setChemoDB(rs.getString(2));
						//							Daten.setMedAntihormonTamoxifenDB(rs.getInt(3));
						//							Daten.setEE2015StatusDB(rs.getString(4));
						//							Daten.setEE2015DatumDB(rs.getDate(5));
						//							Daten.setQuelleTodDB(rs.getString(6));
						//							Daten.setTodDatumDB(rs.getDate(7));
						//						}
						//
						//						statement = "select ee2011.RDatum, ee2011.RDatum2, ee2011.HA, ee2011.FA from mydb.patientendaten as patd "
						//								+ "join mydb.einverstaendnis2011 as ee2011 on patd.PatientenID = ee2011.patientendaten_PatientenID "
						//								+ "where patd.PatientenID= '"+Daten.getPatIDDB()+"' ;";
						//						rs = cn.createStatement().executeQuery(statement);
						//						if (rs.getMetaData().getColumnCount()==0 && patient){
						//							pWriter.println("Patient vorhanden: "+statement);
						//						}
						//						while (rs.next() ){
						//							Daten.setrDatumDB(rs.getDate(1));
						//							Daten.setrDatum2DB(rs.getDate(2));
						//							Daten.setHA_DB(rs.getString(3));
						//							Daten.setFA_DB(rs.getString(4));
						//						}
						List<String> list = new ArrayList<String>();
						list = Daten.buildStatment();

						if (Daten.getPatIDDB()!=0){
							for (String updStatement : list) { 
								cn.createStatement().executeUpdate(updStatement);

							}
							//end of while
						}
					}
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
					cn.close();
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
