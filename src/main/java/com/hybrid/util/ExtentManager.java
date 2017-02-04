package com.hybrid.util;

//http://relevantcodes.com/Tools/ExtentReports2/javadoc/index.html?com/relevantcodes/extentreports/ExtentReports.html

import java.io.File;
import java.util.Date;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
	private static ExtentReports extent;

	public static ExtentReports getInstance() {
		if (extent == null) {
			Date d = new Date();
			String fileName = d.toString().replace(":", "_").replace(" ", "_") + ".html";
			//System.out.println(d.toString().replace(":", "_").replace(" ", "_"));

			extent = new ExtentReports(Constants.REPORT_PATH+fileName, true, DisplayOrder.NEWEST_FIRST);

			//extent = new ExtentReports("F:\\reports\\report.html", true, DisplayOrder.NEWEST_FIRST);

			extent.loadConfig(new File(System.getProperty("user.dir") + "//ReportsConfig.xml"));
			// optional
			extent.addSystemInfo("Selenium Version", "2.53.0").addSystemInfo("Environment", "QA");
		}
		return extent;
	}
}