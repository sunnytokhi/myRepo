package com.hybrid.base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.testng.TestNG;
import org.testng.xml.Parser;
import org.testng.xml.XmlSuite;

public class Parallel_Execution {
	public static void main(String[] args)
			throws FileNotFoundException, ParserConfigurationException, IOException, org.xml.sax.SAXException {
		TestNG testng = new TestNG();
		testng.setXmlSuites(
				(List<XmlSuite>) (new Parser(System.getProperty("user.dir") + "//src//test//resources//testng.xml")
						.parse()));
		testng.setSuiteThreadPoolSize(3);
		testng.run();
	}
}
