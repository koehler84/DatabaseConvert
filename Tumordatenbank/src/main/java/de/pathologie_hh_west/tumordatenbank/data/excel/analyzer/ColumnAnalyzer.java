package de.pathologie_hh_west.tumordatenbank.data.excel.analyzer;

import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import de.pathologie_hh_west.tumordatenbank.logic.exceptions.MissingColumnsException;

public interface ColumnAnalyzer {

	/**
	 * 
	 * @param excelSheet
	 * @return
	 * @throws NoSuchElementException Excel file does not contain any rows.
	 * @throws MissingColumnsException One or more of the required Columns are missing.
	 */
	Map<? extends Enum<?>, Integer> analyze(XSSFSheet excelSheet) throws NoSuchElementException, MissingColumnsException;
	
	boolean containsRequiredColumns(Map<? extends Enum<?>, Integer> map);
	
}
