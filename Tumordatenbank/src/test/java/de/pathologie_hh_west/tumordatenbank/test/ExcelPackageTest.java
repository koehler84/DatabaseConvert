package de.pathologie_hh_west.tumordatenbank.test;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.pathologie_hh_west.tumordatenbank.data.excel.ExcelFile;
import de.pathologie_hh_west.tumordatenbank.data.excel.ExcelFileBuilder;
import de.pathologie_hh_west.tumordatenbank.data.excel.ExcelType;
import de.pathologie_hh_west.tumordatenbank.data.sql.DataAccessException;

public class ExcelPackageTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void columnAnalysisTest() throws InterruptedException, DataAccessException {
		File file = new File("H:/Windows - Dokumente/GitHub/DatabaseConvert/Tumordatenbank/src/test/java/de/pathologie_hh_west/tumordatenbank/test/TestFile.xlsx");
		ExcelType type = ExcelType.TEST;
		
		ExcelFile excelFile = ExcelFileBuilder.build(file, type);
		excelFile.analyze();
	}

}
