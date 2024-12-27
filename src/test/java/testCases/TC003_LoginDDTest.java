package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProviders;

/*
 * Data is Valid -> Login Success -> Test PASS -> Logout
 * Data is Valid -> Login Unsuccess -> Test FAIL 
 * 
 * Data is InValid -> Login Success -> Test FAIL
 * Data is InValid -> Login Unsuccess -> Test PASS -> Logout 
 *  * 
 */
public class TC003_LoginDDTest extends BaseClass {

	@Test(dataProvider="LoginData",dataProviderClass=DataProviders.class,groups="DataDriven") //getting dataprovider from diff class
	public void verify_LoginDDT(String email, String pwd, String expected) throws InterruptedException {

		logger.info("***********Starting TC003_LoginDDTest*********************");

		//HomePage
		try {
			HomePage hp=new HomePage(driver);
			hp.clickMyAccount();
			hp.clickLogin();


			//LoginPage
			LoginPage lp=new LoginPage(driver);
			lp.setEmail(prop.getProperty(email));
			lp.setPassword(prop.getProperty(pwd));
			lp.clickLogin();

			//MyAccountPage
			MyAccountPage macc=new MyAccountPage(driver);
			boolean targetPage=macc.isMyAccountPageExists();


			/*
			 * Data is Valid -> Login Success -> Test PASS -> Logout
			 * 				 -> Login Unsuccess -> Test FAIL 
			 * 

			 */
			if(expected.equalsIgnoreCase("Valid"))
			{
				if(targetPage==true) 
				{
					macc.clickLogout();
					Assert.assertTrue(true);
				}
				else 
				{
					Assert.assertTrue(false);
				}
			}

			/* Data is InValid -> Login Success -> Test FAIL  -> Logout
			 * 				   -> Login Unsuccess -> Test PASS
			 *  
			 */

			if(expected.equalsIgnoreCase("Invalid")) 
			{
				if(targetPage==true) 
				{
					macc.clickLogout();
					Assert.assertTrue(false);
				}
				else 
				{

					Assert.assertTrue(true);
				}
			}
		}catch(Exception e) {
			Assert.fail();
		}
		Thread.sleep(3000);
		logger.info("**************Completed TC003_LoginDDTest********");

	}
}
