package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyAccountPage extends BasePage {
     // Constructor
	
	public MyAccountPage(WebDriver driver) {
		super(driver);
		
	}
	
	//Locators
	@FindBy(xpath="//h2[text()='My Account']")  //My Account page heading
	private WebElement msgHeading;
	
	@FindBy(xpath="//div[@class='list-group']//a[text()='Logout']")  //added in step no 6
	private WebElement InkLogout;
	
	//Action Method
	
	public boolean isMyAccountPageExists()
	{
		try
		{
		return(msgHeading.isDisplayed());	
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public void clickLogout()
	{
	InkLogout.click();
	}
	
	

}
