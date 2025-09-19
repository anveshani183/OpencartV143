package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
	//Constructor
	
	public LoginPage(WebDriver driver) {
		super(driver);
	}
	
	//Locators
	
	@FindBy(xpath = "//input[@id='input-email']")
	private WebElement txtEmailAdress;
	
	@FindBy (xpath ="//input[@id='input-password']")
	private WebElement txtPassword;
	
	@FindBy (xpath="//input[@value='Login']")
	private WebElement btnLogin;
	
	//Action Method
	
	public void setEmail(String email)
	{
		txtEmailAdress.sendKeys(email);
	}
	
	public void setPassword(String pwd)
	{
		txtPassword.sendKeys(pwd);
	}
	
	public void ClickLogin()
	{
		btnLogin.click();
	}

}
