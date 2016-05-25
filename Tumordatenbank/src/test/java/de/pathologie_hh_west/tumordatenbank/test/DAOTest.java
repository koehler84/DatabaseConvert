package de.pathologie_hh_west.tumordatenbank.test;

import java.time.LocalDate;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.pathologie_hh_west.tumordatenbank.data.sql.DBManager;
import de.pathologie_hh_west.tumordatenbank.data.sql.PatientDAO;
import de.pathologie_hh_west.tumordatenbank.logic.Patient;
import de.pathologie_hh_west.tumordatenbank.logic.exceptions.DataAccessException;

public class DAOTest {
	
	private Patient patient;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DBManager.openConnection();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DBManager.closeConnection();
	}

	@Before
	public void setUp() throws Exception {
		patient = new Patient(LocalDate.of(2000, 01, 31), "hänschen", "klein", "apfelweg", "24a", "usbekistan", "32465", "reinbek");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void writeAndDeletePatient() throws DataAccessException {
		PatientDAO dao = new PatientDAO();
		dao.upsert(patient);
		dao.delete(patient);
	}

}
