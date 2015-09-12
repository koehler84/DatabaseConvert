import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//Stephan Test 20:07

public class start {
	static void showDbTable( String dbTbl, String dbDrv, String dbUrl, String dbUsr, String dbPwd )
	{
		if( dbTbl == null || dbTbl.length() == 0 ||
				dbDrv == null || dbDrv.length() == 0 ||
				dbUrl == null || dbUrl.length() == 0 ) {
			System.out.println( "Fehler: Parameter fehlt." );
			return;
		}
		Connection cn = null;
		Statement  st = null;
		ResultSet  rs = null;
		try {
			// Select fitting database driver and connect:
			Class.forName( dbDrv );
			cn = DriverManager.getConnection( dbUrl, dbUsr, dbPwd );
			st = cn.createStatement();
			rs = st.executeQuery( "select * from " + dbTbl );
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
		} finally {
			try { if( rs != null ) rs.close(); } catch( Exception ex ) {/* nothing to do*/}
			try { if( st != null ) st.close(); } catch( Exception ex ) {/* nothing to do*/}
			try { if( cn != null ) cn.close(); } catch( Exception ex ) {/* nothing to do*/}
		}
	}

	static final String extendStringTo14( String s )
	{
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

	static void readExcelPatient(String excelPath, Statement st, String dbTbl) {
		try {
			File excel = new File(excelPath);
			FileInputStream fis = new FileInputStream(excel);
			XSSFWorkbook book = new XSSFWorkbook(fis);
			XSSFSheet sheet = book.getSheetAt(0);
			book.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
			Iterator<Row> itr = sheet.iterator();
			// Iterating over Excel file in Java

			//------------------------------------------
			int i =0;	//stop after 30 rows for testing
			//------------------------------------------

			String oldDbValues="";
			//------------------------------
			while (itr.hasNext() && i<30) {	//stop after 30 rows for testing
				//------------------------------

				//--------------------------------------
				i++;	//stop after 30 rows for testing
				//--------------------------------------

				Row row = itr.next();
				String dbValues="";
				// Iterating over each column of Excel file
				Cell cell = null;
				for (int j=3; j<11;j++){
					cell=row.getCell(j);

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						dbValues = dbValues+"\""+cell.getStringCellValue()+"\"";
						if (j!=10){
							dbValues+=",";
						}
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (j==3){
							dbValues = dbValues+"\"" + new java.sql.Date(cell.getDateCellValue().getTime())+"\",";
						} else {
							if(j==7){
								dbValues = dbValues+"\""+(int)cell.getNumericCellValue() + "\",";
							} else {
								dbValues = dbValues+(int) cell.getNumericCellValue()+",";
							}
						}
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						dbValues = dbValues+"\""+cell.getBooleanCellValue() + "\",";
						break;
					case Cell.CELL_TYPE_BLANK:
						if (j==9){
							dbValues+="99999";
						}else {
							dbValues+= "\"\"";
						}
						if (j!=10){
							dbValues+=",";
						}
					default:

					}
				}
				//create substring, value is changed in for-loop, "default" to prevent the code from thinking 
				//the first line from the excel document is already in the database
				String subString = "default";

				//zaehlerVar to count the commas, just the first 3 attributed are compared to prevent doubling 
				//of persons
				byte zaehlerVar = 0;

				//for-loop extracts the substring with the 3 attributes from dbValues
				for (int k = 0; (k < oldDbValues.length()); k++) {
					//after each attribute zaehlerVar counts up (because of the , in the dbValues String)
					if (oldDbValues.charAt(k) == ',') {
						zaehlerVar++;
					}
					//if the substring contains the 3 required attributes, for-loop ends
					if (zaehlerVar >= 3) {
						subString = oldDbValues.substring(0, k);
						break;
					}
				}

				//checking with the previous line from the excel file if the persons are the same, 
				//the substrings with the 3 attributes from oldDbValues and dbValues are compared
				//to eliminate the first row test if the first 8 (length "default") chars equals "\"Geburt"
				if (!dbValues.substring(0, subString.length()).equals(subString)&&!dbValues.substring(0, subString.length()).equals("\"Geburt")) {
					try{
						//write to database if person is not the same
						st.executeUpdate( "insert into "+dbTbl+" (Geburtsdatum, Vorname, Name, Strasse, Hausnummer, Land, PLZ, Ort)"
								+ " values ( "+dbValues+" );");	
						//------------------------------------
						//for testing proposes
						System.out.println(dbValues);
						//------------------------------------
					}
					catch (SQLException se){
						se.printStackTrace();
					}

				}
				oldDbValues=dbValues;



			}

			book.close();
			fis.close();
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

	}

	static void readExcelFall(String excelPath, Statement st, String dbTbl) {
		ResultSet rs = null;
		try{
			String name, firstname, birthdate = null;
			name = "Abdi";
			firstname = "Pari";
			birthdate ="1958-04-30";
			
			rs = st.executeQuery( "select * from " + dbTbl + " where name= \"" + name + "\" AND vorname= \"" +
			firstname +"\" AND geburtsdatum= \""+birthdate+"\"");
			// Get meta data:
			ResultSetMetaData rsmd = rs.getMetaData();
			
						
			while( rs.next() ) {
				
				
				System.out.print(rs.getInt(1));
			}
		} catch( Exception ex ) {
			System.out.println( ex );
		} finally {
			try { if( rs != null ) rs.close(); } catch( Exception ex ) {/* nothing to do*/}
			try { if( st != null ) st.close(); } catch( Exception ex ) {/* nothing to do*/}
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
		//ist daf�r da, das es auf allen meinen rechnern parallel mit einer datenbank funktioniert
		//-----------------------------------
		try {
			Socket s = new Socket("192.168.178.22", 3306);
			s.close();
			dbUrl = "jdbc:mysql://192.168.178.22:3306/mydb";
		}
		catch (Exception e){
		}
		//-----------------------------------
		
		//Validate connection data
		if( dbPatTbl == null || dbPatTbl.length() == 0 ||
				dbFallTbl == null || dbFallTbl.length() == 0 ||
				dbDrv == null || dbDrv.length() == 0 ||
				dbUrl == null || dbUrl.length() == 0 ) {
			System.out.println( "Fehler: Parameter fehlt." );
			return;
		}
		Connection cn = null;
		Statement  st = null;
		ResultSet  rs = null;
		try {
			// Select fitting database driver and connect:
			Class.forName( dbDrv );
			cn = DriverManager.getConnection( dbUrl, dbUsr, dbPwd );
			st = cn.createStatement();


//			readExcelPatient(excelPath, st, dbPatTbl);
			readExcelFall(excelPath, st, dbPatTbl);
			
		} catch( Exception ex ) {
			System.out.println( ex );
		} finally {
			try { if( rs != null ) rs.close(); } catch( Exception ex ) {/* nothing to do*/}
			try { if( st != null ) st.close(); } catch( Exception ex ) {/* nothing to do*/}
			try { if( cn != null ) cn.close(); } catch( Exception ex ) {/* nothing to do*/}
		}

		//		showDbTable( dbTbl, dbDrv, dbUrl, dbUsr, dbPwd );
	}



}
