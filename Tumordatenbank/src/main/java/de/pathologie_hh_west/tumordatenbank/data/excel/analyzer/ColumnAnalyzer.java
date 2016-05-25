package de.pathologie_hh_west.tumordatenbank.data.excel.analyzer;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public interface ColumnAnalyzer {

	void analyze(XSSFSheet excelSheet);
	
}
