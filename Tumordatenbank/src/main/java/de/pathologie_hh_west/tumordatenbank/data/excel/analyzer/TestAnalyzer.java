package de.pathologie_hh_west.tumordatenbank.data.excel.analyzer;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import de.pathologie_hh_west.tumordatenbank.data.excel.columnenums.TestFileColumns;

public class TestAnalyzer implements ColumnAnalyzer {
	
	@Override
	public Map<TestFileColumns, Integer> analyze(XSSFSheet excelSheet) throws NoSuchElementException {
		Iterator<Row> itr = excelSheet.iterator();
		Row row = itr.next();
		Map<TestFileColumns, Integer> columnStructure = new TreeMap<>();
		
		for (Cell cell : row) {
			String columnName = cell.getStringCellValue();
			
			TestFileColumns columnType = null;
			try {
				columnType = TestFileColumns.fromString(columnName);
				columnStructure.put(columnType, cell.getColumnIndex());
			} catch (IllegalArgumentException e) {
				
			}
		}
		return columnStructure;
	}

	@Override
	public boolean containsRequiredColumns(Map<? extends Enum<?>, Integer> map) {
		// TODO Auto-generated method stub
		return false;
	}

}
