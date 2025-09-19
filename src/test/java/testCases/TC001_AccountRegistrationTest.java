 package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;

public class TC001_AccountRegistrationTest extends BaseClass {
	
	@Test(groups={"Regression","Master"})
	public void verify_account_registration() {
		
		logger.info("*****Starting TC001_AccountRegistrationTest*****");
        
	    System.out.println("[test] driver at start = " + driver); // must be non-null
        try {
	    HomePage hp = new HomePage(driver);
	    hp.clickMyAccount();
	    
	    logger.info("Clicked on Register Link");
	    hp.clickRegister();
	    
        logger.info("Providing customer Detials.....");
	    AccountRegistrationPage reg = new AccountRegistrationPage(driver);
	    reg.setFirstName(randomestring().toUpperCase());
	    reg.setLastName(randomestring().toUpperCase());
	    reg.setEmail(randomestring() + "@gmail.com");
	    reg.setTelephone(randomeNumber());

	    String password = randomeAlphaNumberic();
	    reg.setPassword(password);
	    reg.setConfirmPassword(password);

	    reg.setPrivacyPolicy();
	    reg.setClickContinue();
	    
        logger.info("Validating expected messege..");
	    String conf = reg.getConfirmationMsg();
	    if(conf.equals("Your Account Has Been Created!"))
	    {
	    Assert.assertTrue(true);
        }
	    else
	    {
	    	Assert.assertTrue(false);
	    }
	     // Assert.assertEquals(conf, "Your Account Has Been Created!");
        }
        
        catch(Exception e)
        {
        	
        	
        	Assert.fail();
        }
        
        logger.info("*****Finished TC001_AccountREgistractionTest*****");
	}
}
