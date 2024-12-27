package testCases;

import java.time.Duration;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;

public class TC001_AccountRegistrationTest extends BaseClass {

	@Test(groups={"Regression","Master"})
	public void verify_account_registration() 
	{
		logger.info("*******STARTING - TC001_AccountRegistrationTest ********");
		try {
			HomePage hp=new HomePage(driver);
			hp.clickMyAccount();
			logger.info("*******Clicked on clickMyAccount ********");

			hp.clickRegister();
			logger.info("*******Clicked on  clickRegister ********");

			AccountRegistrationPage ap=new AccountRegistrationPage(driver);
			logger.info("*******Providing Customer Details ********");
			ap.setFirstName(randomString().toUpperCase());
			ap.setLastName(randomString().toUpperCase());
			ap.setEmail(randomString()+"@gmail.com");
			ap.setTelephone(randomNum());

			String password=randomAlphaNumeric();

			ap.setPassword(password);
			ap.setConfirmPassword(password);
			ap.setPrivacyPolicy();
			ap.clickContinue();

			logger.info("*******Validated Expected Message ********");
			String cMsg=ap.getConfirmationMsg();
			if(cMsg.equals("Your Account Has Been Created!"))
			{
				Assert.assertTrue(true);
			}
			else 
			{
				logger.error("Test FAILED");
				logger.debug("Debug logs....");
				Assert.assertTrue(false);

			}
		//	Assert.assertEquals(cMsg, "Your Account Has Been Created!!");
		}
		catch(Exception e) 
		{
			Assert.fail();
		}
		logger.info("*******FINISHED - TC001_AccountRegistrationTest ********");
	}
}
