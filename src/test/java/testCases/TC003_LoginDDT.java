package testCases;


import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProviders;

public class TC003_LoginDDT extends BaseClass  {
	
	@Test(dataProvider="LoginData",dataProviderClass=DataProviders.class,groups="datadriven") //getting data provider from differnt class
	public void verify_loginDDT(String email,String pwd, String exp) throws InterruptedException
	{
		logger.info("*****Starting TC003_LoginDDT*****");
		
		try
		{
		//HomePage
				HomePage hp=new HomePage(driver);
				hp.clickMyAccount();
				hp.clickLogin();
				
				//Login
				LoginPage lp=new LoginPage(driver);
				lp.setEmail(p.getProperty(email));
				lp.setPassword(p.getProperty(pwd));
				lp.ClickLogin();
				
				//MyAccount
				MyAccountPage myac =new MyAccountPage(driver);
				boolean targetPage=myac.isMyAccountPageExists();
				
				/*Data is valid - login success - testpass - logout
				                  login failed -- test fail

				Data is invalid -- login succcess- test fail - logout
				                   login failed -- test pass
				*/
				
				if(exp.equalsIgnoreCase("Valid"))
				{
					if(targetPage==true)
					{
						myac.clickLogout();
						Assert.assertTrue(true);
						
					}
					else
					{
						Assert.assertTrue(false);
					}
				}
				if(exp.equalsIgnoreCase("Invalid"))
				{
					if(targetPage==true)
					{
						myac.clickLogout();
						Assert.assertTrue(false);
					}
					else
					{
						Assert.assertTrue(true);
					}
				}
				
		       }catch(Exception e)
	        	{
			     Assert.fail();
		         }
		Thread.sleep(3000);
				logger.info("*****Finished TC003_LoginDDT*****");
		
	}

}
