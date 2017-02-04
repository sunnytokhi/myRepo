package com.hybrid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.hybrid.util.Constants;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class GenericKeywords {

	public WebDriver driver;
	public Properties prop;
	ExtentTest test;

	public GenericKeywords(ExtentTest test) {
		this.test = test;

		System.out.println("GenericKeywords Constructor Called, when object is created");
		prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(Constants.PROP_FILE);
			prop.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("GenericKeywords constructor completed ... ");
	}

	public String openBrowser(String browserType) {
		test.log(LogStatus.INFO, "Opening Browser " + browserType);

		if (prop.getProperty("grid").equals("Y")) {
			DesiredCapabilities cap = null;
			if (browserType.equals("Mozilla")) {
				cap = DesiredCapabilities.firefox();
				cap.setBrowserName("firefox");
				cap.setJavascriptEnabled(true);
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);

			} else if (browserType.equals("Chrome")) {
				cap = DesiredCapabilities.chrome();
				cap.setBrowserName("chrome");
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			}

			try {
				driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (browserType.equals("Mozilla")) {
				System.setProperty("webdriver.gecko.driver", "C:\\geckodriver.exe");
				driver = new FirefoxDriver();
			} else if (browserType.equals("Chrome")) {
				System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
				driver = new ChromeDriver();
			} else if (browserType.equals("IE")) {
				System.setProperty("webdriver.ie.driver", "C:\\IEDriverServer.exe");
				driver = new InternetExplorerDriver();
			}
		}

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//driver.manage().window().maximize();
		return Constants.PASS;
	}

	public String navigate(String urlKey) {
		// driver.get(urlKey);
		test.log(LogStatus.INFO, "Navigating to " + prop.getProperty(urlKey));
		driver.get(prop.getProperty(urlKey));
		return Constants.PASS;
	}

	public String click(String locatorKey) {
		// click should be smart, it could be xpath, name, id, etc.
		test.log(LogStatus.INFO, "Clicking on " + prop.getProperty(locatorKey));

		WebElement e = getElement(locatorKey);
		e.click();
		System.out.println("Clicked ... ");
		return Constants.PASS;
	}

	public String input(String locatorKey, String data) {
		test.log(LogStatus.INFO, "Typing in " + prop.getProperty(locatorKey));

		WebElement e = getElement(locatorKey);
		e.sendKeys(data);
		return Constants.PASS;
	}

	public String getText(String locatorKey) {
		test.log(LogStatus.INFO, "Fetching Text " + prop.getProperty(locatorKey));
		WebElement e = getElement(locatorKey);
		e.getText();
		return Constants.PASS;
	}

	public String closeBrowser() {
		test.log(LogStatus.INFO, "Closing Broser ... ");
		driver.quit();
		return Constants.PASS;
	}

	public String wait(String timeout) {
		try {
			Thread.sleep(Integer.parseInt(timeout));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Constants.PASS;
	}

	// Validations Functions ********************************************************************
	public String verifyText(String locatorKey, String expectedText) {
		WebElement e = getElement(locatorKey);
		String actualText = e.getText();

		if (actualText.equals(expectedText))
			return Constants.PASS;
		else
			return Constants.FAIL;
	}

	public String verifyElementPresent(String locatorKey) {
		boolean result = isElementPresent(locatorKey);
		if (result)
			return Constants.PASS;
		else
			return Constants.FAIL + " - Could not find Element " + locatorKey;
	}

	public String verifyElementNotPresent(String locatorKey) {
		boolean result = isElementPresent(locatorKey);
		if (!result)
			return Constants.PASS;
		else
			return Constants.FAIL + " - Could find Element " + locatorKey;
	}

	// Utility_Functions**************************************************************************

	public WebElement getElement(String locatorKey) {
		// Centralized function which is responsible to extract any object from
		// webPage
		WebElement e = null;

		try {
			if (locatorKey.endsWith("_id"))
				e = driver.findElement(By.id(prop.getProperty(locatorKey)));
			else if (locatorKey.endsWith("_xpath"))
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			else if (locatorKey.endsWith("_name"))
				e = driver.findElement(By.name(prop.getProperty(locatorKey)));
			else if (locatorKey.endsWith("_linktext"))
				e = driver.findElement(By.linkText(prop.getProperty(locatorKey)));

			System.out.println("webElement : " + e);
			return e;
		} catch (Exception ex) {
			reportFailure("Failure in Element Extraction - " + locatorKey);
			test.log(LogStatus.FAIL, "Failure in Element Extraction - " + locatorKey);
			//Assert.fail("Failure in Element Extraction - " + locatorKey);
		}
		return e;
	}

	public boolean isElementPresent(String locatorKey) {
		List<WebElement> e = null;

		if (locatorKey.endsWith("_id"))
			e = driver.findElements(By.id(prop.getProperty(locatorKey)));
		else if (locatorKey.endsWith("_xpath"))
			e = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		else if (locatorKey.endsWith("_name"))
			e = driver.findElements(By.name(prop.getProperty(locatorKey)));
		else if (locatorKey.endsWith("_linktext"))
			e = driver.findElements(By.linkText(prop.getProperty(locatorKey)));

		if (e.size() == 0)
			return false;
		else
			return true;
	}

	// Reporting Function *****************************************************************************

	public void reportFailure(String failureMessage) {
		takeScreenshot();
		test.log(LogStatus.FAIL, failureMessage);
	}

	public void takeScreenshot() {
		// decide name - time stamp
		Date d = new Date();
		String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
		String path = Constants.SCREENSHOT_PATH + screenshotFile;
		// take screenshot
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// embed
		test.log(LogStatus.INFO, test.addScreenCapture(path));
	}
}
