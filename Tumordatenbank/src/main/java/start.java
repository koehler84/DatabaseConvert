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

	static void excelToPatient(String excelPath, String dbPatTbl) {
		
		try {

			PreparedStatement Pst = cn.prepareStatement("insert into patientendaten (`Geburtsdatum`, `Vorname`, `Name`, `Strasse`, `Hausnummer`, `Land`, `PLZ`, `Ort`)"
					+ " values ( ? , ? , ? , ? , ?  , ? , ? , ? );");

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

			//------------------------------------------
			int i = 0;	//stop after 30 rows for testing
			//------------------------------------------

			//------------------------------
			while (itr.hasNext() && i<30) {	//stop after 30 rows for testing
				//------------------------------

				//--------------------------------------
				i++;	//stop after 30 rows for testing
				//--------------------------------------
				Pst.clearParameters();		//clear parameters in Pst for next insert
				Row row = itr.next();
				
				// Iterating over each column of Excel file
				Cell cell = null;
				for (int j=3; j<11;j++){
					cell=row.getCell(j);

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						Pst.setString(j-2, cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (j==3){
							Pst.setString(1, new java.sql.Date(cell.getDateCellValue().getTime()) + "");
						} else {
							if(j==7){
								Pst.setString(5, (int)cell.getNumericCellValue() + "");
							} else {
								Pst.setInt(j-2, (int)cell.getNumericCellValue());
							}
						}
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						//Do we even have boolean columns to read?
						break;
					case Cell.CELL_TYPE_BLANK:
						if (j==9){
							//dbValues+="99999";
							//insert into PLZ database as null or 99999?
							Pst.setNull(j-2, java.sql.Types.NULL);
						}else {
							Pst.setString(j-2, null);
						}
					default:

					}
				}

				try {

					//Execution of PreparedStatement, SQL Exeption if person is already in database
					//--------------------------------------------------------------------
					System.out.println("Updated rows in mydb.patientendaten: " + Pst.executeUpdate());
					//--------------------------------------------------------------------

				}
				catch (SQLException se){
					System.out.println("Fehler beim Ausführen von \"insert into patientendaten\": Person ggf. schon erfasst!");
				}

			}

			book.close();
			fis.close();
			System.out.println("Write patientendaten success");
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Fehler beim Erstellen des PreparedStatement \"insert into patientendaten\"!");
		}

	}

	static void excelToFall(String excelPath, Statement st, String dbPatTbl, String dbFallTbl) {
		ResultSet rs = null;
		try{

			File excel = new File(excelPath);
			FileInputStream fis = new FileInputStream(excel);
			XSSFWorkbook book = new XSSFWorkbook(fis);
			XSSFSheet sheet = book.getSheetAt(0);
			book.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
			Iterator<Row> itr = sheet.iterator();
			// Iterating over Excel file in Java

			//------------------------------------------
			int k =0;	//stop after 30 rows for testing
			//------------------------------------------
			while (itr.hasNext() && k<29) {	//stop after 30 rows for testing
				//------------------------------

				//--------------------------------------
				k++;	//stop after 30 rows for testing
				//--------------------------------------

				Row row = itr.next();
				if (row.getCell(1).getStringCellValue().equals("Eingangsnummer")){
					row =itr.next();
				}
				String dbValues="";
				// Iterating over each column of Excel file
				Cell cell = null;
				int[] i={0,1,2,3,4,5,26};
				String name = "", firstname = "", birthdate = "";
				for (int j=0; j<i.length;j++){
					cell=row.getCell(i[j]);
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						if (i[j] == 26){
							switch (cell.getStringCellValue())	{
							case "Hauptbefund":
								dbValues = dbValues+0;
								break;
							case "Nachbericht 1":
								dbValues = dbValues+1;
								break;
							case "Nachbericht 2":
								dbValues = dbValues+2;
								break;
							case "Korrekturbefund 1":
								dbValues = dbValues+3;
								break;
							case "Korrekturbefund 2":
								dbValues = dbValues+4;
								break;
							case "Korrekturbefund 3":
								dbValues = dbValues+5;
								break;
							case "Konsiliarbericht 1":
								dbValues = dbValues+6;
							default:
							}
						} else {
							switch (i[j]) {
							case 4:
								firstname = cell.getStringCellValue();
								break;
							case 5:
								name =cell.getStringCellValue();
								break;
							default:
								dbValues = dbValues+"\""+cell.getStringCellValue()+"\",";
							}
						}
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if ((i[j]==0)){
							dbValues = dbValues+"\"" + new java.sql.Date(cell.getDateCellValue().getTime())+"\",";
						} else if (i[j]==3){
							birthdate =new java.sql.Date(cell.getDateCellValue().getTime())+"";
						} else {
							dbValues = dbValues+(int) cell.getNumericCellValue()+",";
						}

						break;
					case Cell.CELL_TYPE_BOOLEAN:
						dbValues = dbValues+"\""+cell.getBooleanCellValue() + "\",";
						break;
					case Cell.CELL_TYPE_BLANK:
						dbValues+= "\"\",";

					default:

					}
				}
				rs = st.executeQuery( "select * from " + dbPatTbl + " where name= \"" + name + "\" AND vorname= \"" +
						firstname +"\" AND geburtsdatum= \""+birthdate+"\"");

				rs.first();
				dbValues+=","+rs.getInt(1);
				while (rs.next()){
					System.out.println("Fehler");
				}
				st.executeUpdate( "insert into "+dbFallTbl+" (`Eingangsdatum`, `E.-Nummer`, `Arzt`, `Befundtyp`, `Patientendaten_PatientenID`)"
						+ " values ( "+dbValues+" );");

			}
			book.close();
			fis.close();
			System.out.println("Write fall success");
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
		//ist dafür da, das es auf allen meinen rechnern parallel mit einer datenbank funktioniert
		//-----------------------------------
		try {
			Socket sock = new Socket ();
			sock.connect(new InetSocketAddress("192.168.178.22", 3306), 200 );
			sock.close();
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
		//Connection cn = null; 	changed cn to static variable
		Statement  st = null;
		ResultSet  rs = null;
		try {
			// Select fitting database driver and connect:
			Class.forName( dbDrv );
			cn = DriverManager.getConnection( dbUrl, dbUsr, dbPwd );
			st = cn.createStatement();

			//----------------------------------------------------
			excelToPatient(excelPath, dbPatTbl);
			//----------------------------------------------------
			//			excelToFall(excelPath, st, dbPatTbl, dbFallTbl);

		} catch( Exception ex ) {
			System.out.println( ex );
		} finally {
			try { if( rs != null ) rs.close(); } catch( Exception ex ) {/* nothing to do*/}
			try { if( st != null ) st.close(); } catch( Exception ex ) {/* nothing to do*/}
			try { if( cn != null ) cn.close(); } catch( Exception ex ) {/* nothing to do*/}
		}

		//		showDbTable( dbPatTbl, dbDrv, dbUrl, dbUsr, dbPwd );
		//		showDbTable( dbFallTbl, dbDrv, dbUrl, dbUsr, dbPwd );

		//		new StringReader("Makroskopie: 7 x 7 x 4 cm großes Mammaexzidat (links oben außen) mit zwei Fadenmarkierungen. 1,5 cm oberhalb der langen "
		//				+ "Fadenmarkierung ein 2,2 x 2 x 1,8 cm großer lobulierter unscharf begrenzter 0,1 cm vom ventralen Resektionsrand entfernter Tumor. "
		//				+ "Das übrige Gewebe fettreich mit diskreten streifenförmigen Fibrosierungen. Zusätzlich ein Telepathologieschnellschnittpräparat. "
		//				+ "Mikroskopie: (HE, Schnellschnitt, Paraffineinbettung, HE, PAS, Östrogen- und Progesteronrzeptor, übriges Mammagewebe HE) Im "
		//				+ "Bereich des makroskopisch beschriebenen Tumors eine vollständige Destruktion des ortsständigen Brustdrüsengewebes durch nahezu "
		//				+ "ausschließlich solide, nur ganz diskret primitiv-tubuläre atypische Epithelverbände mit erheblicher Kernpleomorphie, Hyperchromasie "
		//				+ "und deutlich erhöhter Mitoserate (mehr als 10 Mitosen auf 10 Gesichtsfelder bei 40facher Objektivvergrößerung). Tumorzellverbände "
		//				+ "teilweise von einem dichten vorwiegend lymphozytären Entzündungszellinfiltrat umgeben. Im Randbereich einzelne Gangformationen mit "
		//				+ "intraduktal gelegenen atypischen Epithelverbänden. Keine überzeugende Angioinvasion. Tumorkerne negativ für das Östrogen- und das "
		//				+ "Progesteronrezeptor-Protein. Das übrige Mammagewebe parenchymarm mit geringen interstitiellen Fibrosierungen. Diagnose: Niedrig "
		//				+ "differenziertes invasives duktales Karzinom (Tumordurchmesser 2,2 cm). Vorläufige Tumorklassifikation: C 57, M 8441/3, G 3, pT3c pN1(15/34) "
		//				+ "L/V1. Der Tumor ist Östrogen- und Progesteronrezeptor-negativ. Sonstiges: ip");

		//Tumorklassifikation: C 57, M 8441/3, G 3, pT3c pN1(15/34) L/V1. Der Tumor ist Östrogen- und Progesteronrezeptor-negativ. Sonstiges: ip

	}

	//http://download.eclipse.org/egit/github/updates-nightly/ <- GITHUB Task manager (über help -> install new software)

}
