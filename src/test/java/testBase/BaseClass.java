package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseClass {

	public static WebDriver driver;
	public Logger logger;
	public Properties prop;

	@BeforeClass(groups= {"Sanity","Regression","Master"})
	@Parameters({"os","browser"})
	public void setUp(String os, String browse) throws IOException 
	{
		//Loading Config.properties file
		FileReader file=new FileReader(".//src/test/resources/config.properties");
		prop=new Properties();
		prop.load(file);

		logger=LogManager.getLogger(this.getClass());
		
		if(prop.getProperty("execution_env").equalsIgnoreCase("remote")) {
			DesiredCapabilities capabilities=new DesiredCapabilities();
			
			//OS
			if(os.equalsIgnoreCase("windows"))
			{
				capabilities.setPlatform(Platform.WIN10);
			}else if(os.equalsIgnoreCase("mac")){
				capabilities.setPlatform(Platform.MAC);
			}else {
				System.out.println("No Matching OS");
				return;
			}
			
			//browser
			switch(browse.toLowerCase()) 
			{
			case "chrome": capabilities.setBrowserName("chrome"); break;
			case "edge": capabilities.setBrowserName("MicrosoftEdge"); break;
			default: System.out.println("No Matching browser"); return;
			}
			driver=new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),capabilities);
		}

		if(prop.getProperty("execution_env").equalsIgnoreCase("local")) {
			switch(browse.toLowerCase()) 
			{
			case "chrome": 		driver=new ChromeDriver(); break;
			case "edge": 		driver=new EdgeDriver(); break;
			case "firefox": 	driver=new FirefoxDriver(); break;
			case "ie": 			driver=new InternetExplorerDriver(); break;
			case "safari": 		driver=new SafariDriver(); break;

			default: System.out.println("Invalid Browser..."); return;

			}
		}

		driver=new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		//		driver.get("https://tutorialsninja.com/demo/");
		driver.get(prop.getProperty("appURL"));  //Reading URL from Properties file
		driver.manage().window().maximize();
	}

	@AfterClass(groups= {"Sanity","Regression","Master"})
	public void tearDown() {
		driver.quit();
	}

	public String randomString() {
		String generatedString=RandomStringUtils.randomAlphabetic(5);
		return generatedString;
	}

	public String randomNum() {
		String generatedNum=RandomStringUtils.randomNumeric(5);
		return generatedNum;
	}

	public String randomAlphaNumeric() {
		String generatedString=RandomStringUtils.randomAlphabetic(5);
		String generatedNum=RandomStringUtils.randomNumeric(5);
		return (generatedString+ "@"+ generatedNum);
	}

	public String captureScreen(String tname) throws IOException {
		// TODO Auto-generated method stub
		String timeStamp=new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		
		TakesScreenshot takeScreenshot=(TakesScreenshot) driver;
		File srcFile=takeScreenshot.getScreenshotAs(OutputType.FILE);
		
		String targetFilePath=System.getProperty("user.dir")+"\\screenshots\\"+ tname + "_"+ timeStamp + ".png" ;
		File targetFile=new File(targetFilePath);
		
		srcFile.renameTo(targetFile);
		return targetFilePath;
	}
}
