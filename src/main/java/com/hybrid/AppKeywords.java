package com.hybrid;

import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.hybrid.util.Constants;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class AppKeywords extends GenericKeywords {
	//Application specific Keywords
	public AppKeywords(ExtentTest test) {
		super(test);
	}

	public String login(String username, String password) {
		getElement("loginLink_xpath").click();
		getElement("userName_xpath").sendKeys(username);
		getElement("password_xpath").sendKeys(password);
		getElement("loginButton_xpath").click();

		return Constants.PASS;
		// Validate login
	}

	public String flipkartLogin(Hashtable<String, String> testData) {
		test.log(LogStatus.INFO, "Loggin In with " + testData.get("Username") + " / " + testData.get("Password"));
		return login(testData.get("Username"), testData.get("Password"));
	}

	public String defaultLogin() {
		return login(prop.getProperty("username"), prop.getProperty("password"));
	}

	public String verifyFlipkartLogin(String expectedResult) throws InterruptedException {
		test.log(LogStatus.INFO, "Validating Login");
		Thread.sleep(5000);
		boolean result = isElementPresent("myAccount_xpath");
		String actualResult = "";
		if (result)
			actualResult = "Success";
		else
			actualResult = "Failure";
		if (!actualResult.equals(expectedResult)) {
			return "Failed - Could not login, Got Actual Result as" + actualResult;
			//test.log(LogStatus.FAIL, "Could not login, Got Actual Result as" + actualResult);
			//Assert.fail("Could not login, Got Actual Result as" + actualResult);
		}
		return Constants.PASS;
	}

	public String filterMobileAndValidate(Hashtable<String, String> testData) {
		String brandName_xpath = testData.get("MobileCompany");
		//driver.findElement(By.linkText(brandName)).click();			// taking brandName from xls file
		getElement(brandName_xpath).click(); // taking xpath object from xls file - properties file

		List<WebElement> mobiles = driver.findElements(By.xpath(prop.getProperty("samsung_xpath")));
		for (int i = 0; i < mobiles.size(); i++) {
			System.out.println(mobiles.get(i).getText());
			if (!mobiles.get(i).getText().contains("SAMSUNG")) {
				return Constants.FAIL + " - Found Mobile Name with - " + mobiles.get(i).getText();
			}
		}
		return Constants.PASS;
	}
}
