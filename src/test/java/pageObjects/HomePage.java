package pageObjects;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {
	
	
     //Constructor
    public HomePage(WebDriver driver) { super(driver); }
    
    
     // Locators
    
    @FindBy(xpath = "//span[normalize-space()='My Account']")
    private WebElement myAccount;

    @FindBy(linkText = "Register") // often simpler & stable here
    private WebElement registerLink;

    @FindBy(linkText = "Login")  //Log in link added in steps
    private WebElement linkLogin;
    //Acton
    
    public void clickMyAccount() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.elementToBeClickable(myAccount))
            .click();
    }

    public void clickRegister() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.elementToBeClickable(registerLink))
            .click();
    }
    
    public void clickLogin() {
    	new WebDriverWait(driver, Duration.ofSeconds(10))
        .until(ExpectedConditions.elementToBeClickable(linkLogin))
        .click();
    	
    }
}

