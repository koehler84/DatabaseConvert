import java.io.*;
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
			//			rs = st.executeQuery( "insert into  );
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

	static void readExcelPatienten(String excelPath, Statement st, String dbTbl) {
		try {
			File excel = new File(excelPath);
			FileInputStream fis = new FileInputStream(excel);
			XSSFWorkbook book = new XSSFWorkbook(fis);
			XSSFSheet sheet = book.getSheetAt(0);
			book.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
			Iterator<Row> itr = sheet.iterator();
			// Iterating over Excel file in Java
			int i =0;
			String oldDbValues="";
			while (itr.hasNext() && i<30) {
				i++;
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

				String subString = "default";

				byte zaehlerVar = 0;

				for (int k = 0; (k < oldDbValues.length()); k++) {
					if (oldDbValues.charAt(k) == ',') {
						zaehlerVar++;
					}
					if (zaehlerVar >= 3) {
						subString = oldDbValues.substring(0, k);
						break;
					}
				}

				if (!dbValues.substring(0, subString.length()).equals(subString)&&!dbValues.substring(0, subString.length()).equals("\"Geburt")) {
					try{
						st.executeUpdate( "insert into "+dbTbl+" (Geburtsdatum, Vorname, Name, Strasse, Hausnummer, Land, PLZ, Ort)"
								+ " values ( "+dbValues+" );");	
						System.out.println(dbValues);
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
		
	}

	public static void main(String[] args) {
		String dbPatTbl=null, dbFallTbl=null, dbDrv=null, dbUrl=null, dbUsr="", dbPwd="", excelPath="";
		//-----------------------------------
		dbPatTbl = "patientendaten";
		dbFallTbl = "fall";
		excelPath = "C://Project Pathologie/test.xlsx";
		//-----------------------------------
		dbDrv = "com.mysql.jdbc.Driver";
		dbUrl = "jdbc:mysql://localhost:3306/mydb";
		dbUsr = "java";
		dbPwd = "geheim";



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


			readExcelPatienten(excelPath, st, dbPatTbl);

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
