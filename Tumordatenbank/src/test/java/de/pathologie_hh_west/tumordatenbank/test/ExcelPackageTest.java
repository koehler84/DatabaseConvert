package de.pathologie_hh_west.tumordatenbank.test;

import java.io.File;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.pathologie_hh_west.tumordatenbank.data.excel.ExcelFile;
import de.pathologie_hh_west.tumordatenbank.data.excel.ExcelFileBuilder;
import de.pathologie_hh_west.tumordatenbank.data.excel.ExcelType;
import de.pathologie_hh_west.tumordatenbank.logic.exceptions.DataAccessException;
import de.pathologie_hh_west.tumordatenbank.logic.exceptions.MissingColumnsException;

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
	public void columnAnalysisTest() throws InterruptedException, DataAccessException, NoSuchElementException, MissingColumnsException {
		File file = new File("H:/Windows - Dokumente/GitHub/DatabaseConvert/Tumordatenbank/src/test/java/de/pathologie_hh_west/tumordatenbank/test/TestFile.xlsx");
		ExcelType type = ExcelType.TEST;
		
		ExcelFile excelFile = ExcelFileBuilder.build(file, type);
		excelFile.getColumnStructure().forEach( (e, b) -> {
			System.out.println(e.toString() + " " + b);
		});
	}
	
	@Test
	public void defaultExcelTest() throws InterruptedException, DataAccessException, NoSuchElementException, MissingColumnsException {
		File file = new File("C:/Project Pathologie/test.xlsx");
		ExcelType type = ExcelType.DEFAULT;
		
		ExcelFile excelFile = ExcelFileBuilder.build(file, type);
		excelFile.getColumnStructure().forEach( (e, b) -> {
			System.out.println(e.toString() + " " + b);
		});
	}

}
