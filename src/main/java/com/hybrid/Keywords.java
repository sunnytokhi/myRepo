package com.hybrid;

import java.util.Hashtable;

import org.testng.Assert;

import com.hybrid.util.Constants;
import com.hybrid.util.Xls_Reader;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Keywords {

	ExtentTest test;
	AppKeywords app;

	public Keywords(ExtentTest test) {
		this.test = test;
	}

	public void executeKeywords(String testUnderExecution, Xls_Reader xls, Hashtable<String, String> testData)
			throws InterruptedException {

		app = new AppKeywords(test);

		int rows = xls.getRowCount(Constants.KEYWORD_SHEET); // sheetname 

		for (int rNum = 2; rNum <= rows; rNum++) { // Iterate from 2nd row, coz data starts from 2nd Row to total number of Rows
			String tcid = xls.getCellData(Constants.KEYWORD_SHEET, Constants.TCID_COL, rNum);
			if (tcid.equals(testUnderExecution)) {
				String keyword = xls.getCellData(Constants.KEYWORD_SHEET, Constants.KEYWORD_COL, rNum);
				String object = xls.getCellData(Constants.KEYWORD_SHEET, Constants.OBJECT_COL, rNum);
				String key = xls.getCellData(Constants.KEYWORD_SHEET, Constants.DATA_COL, rNum);
				String data = testData.get(key);

				test.log(LogStatus.INFO, tcid + " | " + keyword + " | " + object + " | " + data);
				System.out.println(tcid + " | " + keyword + " | " + object + " | " + data);

				String result = "";
				if (keyword.equals("openBrowser")) {
					result = app.openBrowser(data);
					Thread.sleep(3000);
				} else if (keyword.equals("navigate")) {
					result = app.navigate(object);
					Thread.sleep(3000);
				} else if (keyword.equals("click")) {
					result = app.click(object);
					Thread.sleep(3000);
				} else if (keyword.equals("input")) {
					result = app.input(object, data);
					Thread.sleep(3000);
				} else if (keyword.equals("closeBrowser")) {
					result = app.closeBrowser();
				} else if (keyword.equals("verifyElementPresent")) {
					result = app.verifyElementPresent(object);
				} else if (keyword.equals("flipkartLogin")) {
					result = app.flipkartLogin(testData);
				} else if (keyword.equals("verifyFlipkartLogin")) {
					result = app.verifyFlipkartLogin(testData.get("ExpectedResult"));
				} else if (keyword.equals("defaultLogin")) {
					result = app.defaultLogin();
				} else if (keyword.equals("getText")) {
					result = app.getText(object);
				} else if (keyword.equals("sleep")) {
					result = app.wait(key);
				} else if (keyword.equals("filterMobileAndValidate")) {
					result = app.filterMobileAndValidate(testData);
				}
				
				

				/*
				if (!result.equals(Constants.PASS)) {
					app.reportFailure(result);
					Assert.fail(result);
				}
				*/
			}
		}
	}

	public AppKeywords getGenericKeywords() {
		return app;
	}

}
