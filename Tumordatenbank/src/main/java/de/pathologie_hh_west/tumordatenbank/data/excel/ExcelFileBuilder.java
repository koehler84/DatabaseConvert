package de.pathologie_hh_west.tumordatenbank.data.excel;

import java.io.File;

import de.pathologie_hh_west.tumordatenbank.data.excel.analyzer.ColumnAnalyzer;
import de.pathologie_hh_west.tumordatenbank.data.excel.analyzer.TestAnalyzer;
import de.pathologie_hh_west.tumordatenbank.data.sql.DataAccessException;

public class ExcelFileBuilder {
	
	private ExcelFileBuilder() {
		
	}
	
	public static ExcelFile build(File file, ExcelType type) throws DataAccessException {
		if (file == null || type == null) {
			throw new NullPointerException("File or Type equals null.");
		}
		
		ColumnAnalyzer analyzer = null;
		
		switch (type) {
		case ANALYSE:
			analyzer = null;
			break;
		case EINV_2011:
			analyzer = null;
			break;
		case EXPRIMAGE:
			analyzer = null;
			break;
		case KREBSREGISTER:
			analyzer = null;
			break;
		case QUESTOR:
			analyzer = null;
			break;
		case TEST:
			analyzer = new TestAnalyzer();
			break;
		}
		
		ExcelFile excelFile = new ExcelFile(file, analyzer);
		
		return excelFile;
	}

}
