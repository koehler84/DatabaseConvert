package de.pathologie_hh_west.tumordatenbank.data.excel.analyzer;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class TestAnalyzer implements ColumnAnalyzer {

	@Override
	public void analyze(XSSFSheet excelSheet) {
		Iterator<Row> itr = excelSheet.iterator();
		try {
			Row row = itr.next();
			
			for (Cell cell : row) {
				String columnName = cell.getStringCellValue();
				
				
				
				
				
				
				System.out.println(columnName);
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
	}

}
