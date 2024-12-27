package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
//import org.apache.poi.hpsf.Date;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentReportManager implements ITestListener{

	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest test;
	String repName;
	
	public void onStart(ITestContext testContext) {
		
		/*
		 * SimpleDateFormat df=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss"); 
		 * Date dt=new  Date();
		 * String currentdatetimestamp=df.format(dt);
		 */
		
		String timeStamp=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		repName="Test-Report-"+ timeStamp +".html";
		sparkReporter=new ExtentSparkReporter(".\\reports\\"+repName);
		
		sparkReporter.config().setDocumentTitle("opencart Automation Report");
		sparkReporter.config().setReportName("opencart Functional Testing");
		sparkReporter.config().setTheme(Theme.STANDARD);//DARK
		
		extent=new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Application", "opencart");
		extent.setSystemInfo("Module", "Admin");
		extent.setSystemInfo("Sub Module", "Customers");
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.setSystemInfo("Environment", "QA");
		
		String os=testContext.getCurrentXmlTest().getParameter("os");
		extent.setSystemInfo("Oparating System", os);
		
		String browser=testContext.getCurrentXmlTest().getParameter("browser");
		extent.setSystemInfo("Browser", browser);
		
		List<String> includedGroups=testContext.getCurrentXmlTest().getIncludedGroups();
		if(!includedGroups.isEmpty()) {
			extent.setSystemInfo("Groups", includedGroups.toString());
		}
	}
	
	public void onTestSucess(ITestResult result) {
		test=extent.createTest(result.getTestClass().getName());	//create new entry
		test.assignCategory(result.getMethod().getGroups());		//to display groups in report
		test.log(Status.PASS, "Test case PASSED is:" + result.getName());
	}
	
	public void onTestFailure(ITestResult result) {
		test=extent.createTest(result.getTestClass().getName());	//create new entry
		test.assignCategory(result.getMethod().getGroups());		//to display groups in report
		
		test.log(Status.FAIL, "Test case FAILED is:" + result.getName());
		test.log(Status.INFO, "Test case FAILED coz:" + result.getThrowable().getMessage());
		
		try {
			String imgpath=new BaseClass().captureScreen(result.getName());
			test.addScreenCaptureFromPath(imgpath);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onTestSkipped(ITestResult result) {
		test=extent.createTest(result.getTestClass().getName());	//create new entry
		test.assignCategory(result.getMethod().getGroups());		//to display groups in report
		
		test.log(Status.SKIP, "Test case SKIPPED is:" + result.getName());
		test.log(Status.INFO, "Test case SKIPPED coz:" + result.getThrowable().getMessage());
	}
	
	public void onFinish(ITestContext testContext) {
		extent.flush();
		//automatically open the report code
		String pathOfExtentReport =System.getProperty("user.dir")+"\\reports\\"+repName;
		File extentReport =new File(pathOfExtentReport);
		
		try {
			Desktop.getDesktop().browse(extentReport.toURI());
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		//Include the code if you the report to be mailed 
		/*
		 * try { URL url=new URL("file://"+
		 * System.getProperty("user.dir")+"\\reports\\"+repName); //create the email
		 * message
		 * 
		 * ImageHtmlEmail email=new ImageHtmlEmail(); //java email dependency added
		 * email.setDataSourceResolver(new DataSourceUrlResolver(url));
		 * email.setHostName("smtp.googlemail.com"); email.setSmtpPort(465);
		 * email.setAuthenticator(new
		 * DefaultAuthenticator("pavanoltraining@gmail.com","password"));
		 * email.setSSLOnConnect(true);
		 * email.setFrom("pavanoltraining@gmail.com");//sender
		 * email.setSubject("Test Results"); email.setMsg("Plz find Attached results");
		 * email.addTo("pavankumar.busyqa@gmail.com"); //receiver
		 * email.attach(url,"extent report","Plz check report"); email.send(); //send
		 * email }catch(Exception e) { e.printStackTrace(); }
		 */
	}
}
