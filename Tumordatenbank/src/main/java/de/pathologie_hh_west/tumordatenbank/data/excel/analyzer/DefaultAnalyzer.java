package de.pathologie_hh_west.tumordatenbank.data.excel.analyzer;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import de.pathologie_hh_west.tumordatenbank.data.excel.columnenums.DefaultFileColumns;
import de.pathologie_hh_west.tumordatenbank.logic.exceptions.MissingColumnsException;

public class DefaultAnalyzer implements ColumnAnalyzer {

	@Override
	public Map<? extends Enum<?>, Integer> analyze(XSSFSheet excelSheet) throws NoSuchElementException, MissingColumnsException {
		Iterator<Row> itr = excelSheet.iterator();
		Row row = itr.next();
		Map<DefaultFileColumns, Integer> columnStructure = new TreeMap<>();
		
		for (Cell cell : row) {
			String columnName = cell.getStringCellValue();
			
			DefaultFileColumns columnType = null;
			try {
				columnType = DefaultFileColumns.fromString(columnName);
				columnStructure.put(columnType, cell.getColumnIndex());
			} catch (IllegalArgumentException e) {
				
			}
		}
		
		if (containsRequiredColumns(columnStructure)) {
			return columnStructure;
		}
		throw new MissingColumnsException();
	}
	
	@Override
	public boolean containsRequiredColumns(Map<? extends Enum<?>, Integer> map) {
		if (map.containsKey(DefaultFileColumns.E_NUMMER) && map.containsKey(DefaultFileColumns.NAME)) {
			return true;
		}
		return false;
	}

}
