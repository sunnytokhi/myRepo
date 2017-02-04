package com.hybrid.base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;

import com.hybrid.Keywords;
import com.hybrid.util.DataUtil;
import com.hybrid.util.ExtentManager;
import com.hybrid.util.Xls_Reader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class BaseTest {
	public ExtentReports rep = ExtentManager.getInstance();
	public ExtentTest test;
	public Keywords app;
	public Xls_Reader xls; // Every test will have different xls file path and testName, so keep it in beforeTest
	public String testName;

	@DataProvider
	public Object[][] getData() { // put data in 2d Object Array and supply to testA(), No of Rows will be No of Rows in data (xls) and Cols will be no of param in data
		return DataUtil.getData(xls, testName);
	}

	@AfterMethod
	public void quit() {
		if (rep != null) {
			rep.endTest(test);
			rep.flush();
		}
		//quit
		if (app != null)
			app.getGenericKeywords().closeBrowser();
	}
}
