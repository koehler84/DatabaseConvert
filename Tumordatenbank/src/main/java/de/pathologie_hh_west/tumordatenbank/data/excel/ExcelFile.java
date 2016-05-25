package de.pathologie_hh_west.tumordatenbank.data.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.pathologie_hh_west.tumordatenbank.data.excel.analyzer.ColumnAnalyzer;
import de.pathologie_hh_west.tumordatenbank.logic.exceptions.DataAccessException;
import de.pathologie_hh_west.tumordatenbank.logic.exceptions.MissingColumnsException;

public class ExcelFile {

	private File file;
	private int numberOfRows;
	private XSSFSheet excelSheet;
	private ColumnAnalyzer columnAnalyzer;
	private Map<? extends Enum<?>, Integer> columnStructure;
	
	ExcelFile(File file, ColumnAnalyzer analyzer) throws DataAccessException, NoSuchElementException {
		this.file = file;
		
		try {
			FileInputStream fis = new FileInputStream(this.file);
			XSSFWorkbook book = new XSSFWorkbook(fis);
			book.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
			this.excelSheet = book.getSheetAt(0);
			this.numberOfRows = this.excelSheet.getPhysicalNumberOfRows();
			
			fis.close();
			book.close();
		} catch (IOException e) {
			throw new DataAccessException(e.getMessage());
		}
		
		this.columnAnalyzer = analyzer;
		analyze();
	}
	
	public XSSFSheet getExcelSheet() {
		return excelSheet;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}
	
	private void analyze() throws NoSuchElementException, MissingColumnsException {
		this.columnStructure = this.columnAnalyzer.analyze(this.excelSheet);
	}
	
	public Map<? extends Enum<?>, Integer> getColumnStructure() {
		return columnStructure;
	}
	
}
