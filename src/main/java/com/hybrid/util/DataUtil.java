package com.hybrid.util;

import java.util.Hashtable;

public class DataUtil {
	public static Object[][] getData(Xls_Reader xls, String testName) {
		//String testCaseName = "HomePage"; // read data for Test Specified
		String sheetName = "Data";

		int testStartRowNum = 1;
		while (!xls.getCellData(sheetName, 0, testStartRowNum).equals(testName)) {
			testStartRowNum++;
		}
		System.out.println("Test Start from Row No : " + testStartRowNum);

		int colStartRowNum = testStartRowNum + 1; // RunMode column
		int dataStartRowNum = testStartRowNum + 2; // Y and N of Runmode

		// Calculate rows of data
		int rows = 0;
		while (!xls.getCellData(sheetName, 0, dataStartRowNum + rows).equals("")) {
			rows++;
		}
		System.out.println("Total Rows are : " + rows);

		// Calculate cols of data
		int cols = 0;
		while (!xls.getCellData(sheetName, cols, colStartRowNum).equals("")) { // Row will be constant
			cols++;
		}
		System.out.println("Total Cols are : " + cols);

		Object[][] data = new Object[rows][1];
		int dataRow = 0;

		Hashtable<String, String> table = null;
		//Read Data
		for (int rNum = dataStartRowNum; rNum < dataStartRowNum + rows; rNum++) {
			table = new Hashtable<String, String>(); //Every Row will hv its own hash Table
			for (int cNum = 0; cNum < cols; cNum++) {
				String key = xls.getCellData(sheetName, cNum, colStartRowNum);
				String value = xls.getCellData(sheetName, cNum, rNum);
				table.put(key, value);
			}
			data[dataRow][0] = table; // 0 coz it has only 1 col
			dataRow++;
		}
		return data;
	}

	public static boolean isSkip(Xls_Reader xls, String testName) {
		int rows = xls.getRowCount(Constants.TESTCASES_SHEET);

		for (int rNum = 2; rNum <= rows; rNum++) {
			String tcid = xls.getCellData(Constants.TESTCASES_SHEET, Constants.TCID_COL, rNum);
			if (tcid.equals(testName)) {
				String runmode = xls.getCellData(Constants.TESTCASES_SHEET, Constants.RUNMODE_COL, rNum);
				if (runmode.equals("Y")) //true - N; false - Y
					return false;
				else
					return true;
			}
		}
		return true; //Default
	}
}
