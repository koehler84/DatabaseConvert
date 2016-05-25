package de.pathologie_hh_west.tumordatenbank.data.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.pathologie_hh_west.tumordatenbank.data.excel.analyzer.ColumnAnalyzer;
import de.pathologie_hh_west.tumordatenbank.data.sql.DataAccessException;

public class ExcelFile {

	private File file;
	private int numberOfRows;
	private XSSFSheet excelSheet;
	private ColumnAnalyzer columnAnalyzer;
	
	ExcelFile(File file, ColumnAnalyzer analyzer) throws DataAccessException {
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
	}
	
	public XSSFSheet getExcelSheet() {
		return excelSheet;
	}
	
	public void analyze() {
		this.columnAnalyzer.analyze(this.excelSheet);
	}
	
}
