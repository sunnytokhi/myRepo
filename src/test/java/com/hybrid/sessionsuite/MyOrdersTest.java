package com.hybrid.sessionsuite;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hybrid.Keywords;
import com.hybrid.base.BaseTest;
import com.hybrid.util.Constants;
import com.hybrid.util.DataUtil;
import com.hybrid.util.ExtentManager;
import com.hybrid.util.Xls_Reader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MyOrdersTest extends BaseTest {

	@BeforeTest
	public void init() {
		xls = new Xls_Reader(Constants.SESSION_SUITE_XLS);
		testName = "MyOrdersTest";
	}

	@Test(dataProvider = "getData")
	public void myOrdersTest(Hashtable<String, String> data) throws InterruptedException {
		test = rep.startTest(testName);
		test.log(LogStatus.INFO, data.toString());

		if (DataUtil.isSkip(xls, testName) || data.get("Runmode").equals("N")) {
			test.log(LogStatus.SKIP, "Skipping test as runmode is N");
			throw new SkipException("Skipping test as runmode is N");
		}

		test.log(LogStatus.INFO, "Stating " + testName);

		System.out.println("Location : " + System.getProperty("user.dir") + "\\data\\SessionSuite.xlsx");
		app = new Keywords(test);
		test.log(LogStatus.INFO, "Executing Keyword");
		app.executeKeywords(testName, xls, data);
		System.out.println("Read frm Excel");
		//app.getGenericKeywords().reportFailure("XXXX");

		// Add Screenshot 
		app.getGenericKeywords().takeScreenshot();
		test.log(LogStatus.PASS, "Test Passed");
	}
}
