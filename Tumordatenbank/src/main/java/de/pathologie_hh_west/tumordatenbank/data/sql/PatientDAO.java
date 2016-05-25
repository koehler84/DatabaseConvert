package de.pathologie_hh_west.tumordatenbank.data.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.pathologie_hh_west.tumordatenbank.logic.Patient;
import de.pathologie_hh_west.tumordatenbank.logic.PatientKey;
import de.pathologie_hh_west.tumordatenbank.logic.exceptions.DataAccessException;

public class PatientDAO implements DataAccessObject<PatientKey, Patient> {

	public PatientDAO() {
		super();
	}

	@Override
	public Patient get(PatientKey key) throws DataAccessException {
		// TODO Auto-generated catch block
		return null;
	}

	@Override
	public void insert(Patient obj) throws DataAccessException {
		if (obj == null) {
			return;
		}
		
		PreparedStatement pst = null;
		try {
			pst = DBManager.getInstance().getConnection().prepareStatement("insert into patientendaten (`Geburtsdatum`, `Vorname`, `Name`,"
					+ " `Strasse`, `Hausnummer`, `Land`, `PLZ`, `Ort`, `Fehler`, `Verstorben (Quelle)`, `Verstorben (Datum)`, `Bemerkung Tod`,"
					+ " `Follow-up`, `Follow-up Status`, `EE-Status`) "
					+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? );");
		} catch (SQLException e) {
			try {
				DBManager.getInstance().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DataAccessException(e.getMessage());
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
			}
		}
	}

	@Override
	public void update(Patient obj) throws DataAccessException {
		if (obj == null) {
			return;
		}
		
		PreparedStatement pst = null;
		try {
			pst = DBManager.getInstance().getConnection().prepareStatement("UPDATE mydb.patientendaten\r\n" + 
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
		} catch (SQLException e) {
			try {
				DBManager.getInstance().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DataAccessException(e.getMessage());
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
			}
		}
	}

	@Override
	public void upsert(Patient obj) throws DataAccessException {
		if (obj == null) {
			return;
		}
		
		ResultSet resCheckId = null;
		try {
			resCheckId = DBManager.getInstance().getStatement().executeQuery("select COUNT(*) as 'amount' from mydb.patientendaten where " /*TODO*/);
			
			if (resCheckId.next()) {
				int numberOfRowsFound = resCheckId.getInt("amount");
				
				if (numberOfRowsFound == 0) {
					insert(obj);
				} else {
					update(obj);
				}
			} else {
				//TODO some kind of exception 
			}
			DBManager.getInstance().commit();
		} catch (SQLException e) {
			try {
				DBManager.getInstance().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DataAccessException(e.getMessage());
		} finally {
			try {
				resCheckId.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
			}
		}
	}
	
	@Override
	public void delete(Patient obj) throws DataAccessException {
		if (obj == null) {
			return;
		}
		
		PreparedStatement pst = null;
		try {
			pst = DBManager.getInstance().prepareStatement("delete from mydb.patientendaten where `Geburtsdatum` = ? and `Vorname` = ? and `Name` = ? ;");
			
			pst.setDate(1, Date.valueOf(obj.getGeburtsdatum()));
			pst.setString(2, obj.getVorname());
			pst.setString(3, obj.getName());
			
			pst.executeUpdate();
			
			DBManager.getInstance().commit();
		} catch (SQLException e) {
			try {
				DBManager.getInstance().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DataAccessException(e.getMessage());
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
			}
		}
	}
	
}
