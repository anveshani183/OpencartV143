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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseClass {

    public static WebDriver driver;
    public Logger logger; // Log4j
    public Properties p;

    @SuppressWarnings("deprecation")
    @BeforeClass(groups = { "Sanity", "Regression", "Master" })
    @Parameters({ "os", "browser" })
    public void setup(@Optional("windows") String os,
                      @Optional("chrome") String br) throws IOException {

        // Load config.properties
        FileReader file = new FileReader(".//src//test//resources//config.properties");
        p = new Properties();
        p.load(file);

        logger = LogManager.getLogger(this.getClass());

        // Remote execution
        if (p.getProperty("execution_env").equalsIgnoreCase("remote")) {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            // OS setup
            if (os.equalsIgnoreCase("windows")) {
                capabilities.setPlatform(Platform.WINDOWS);
            } else if (os.equalsIgnoreCase("mac")) {
                capabilities.setPlatform(Platform.MAC);
            } else if (os.equalsIgnoreCase("linux")) {
                capabilities.setPlatform(Platform.LINUX);
            } else {
                System.out.println("No matching OS");
                return;
            }

            // Browser setup
            switch (br.toLowerCase()) {
                case "chrome":
                    capabilities.setBrowserName("chrome");
                    break;
                case "edge":
                    capabilities.setBrowserName("MicrosoftEdge");
                    break;
                case "firefox":
                    capabilities.setBrowserName("firefox");
                    break;
                default:
                    System.out.println("No matching browser");
                    return;
            }

            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
        }

        // Local execution
        if (p.getProperty("execution_env").equalsIgnoreCase("local")) {
            switch (br.toLowerCase()) {
                case "chrome":
                    driver = new ChromeDriver();
                    break;
                case "edge":
                    driver = new EdgeDriver();
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                default:
                    System.out.println("Invalid browser name...");
                    return;
            }
        }

        System.out.println("[setup] driver = " + driver);

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(p.getProperty("appURL"));
        driver.manage().window().maximize();
    }

    @AfterClass(groups = { "Sanity", "Regression", "Master" })
    public void tearDown() {
        System.out.println("[tearDown] driver = " + driver);
        if (driver != null) {
            driver.quit();
        }
    }

    // ================= Utility Methods =================

    @SuppressWarnings("deprecation")
	public String randomestring() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    @SuppressWarnings("deprecation")
	public String randomeNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    @SuppressWarnings("deprecation")
	public String randomeAlphaNumberic() {
        return RandomStringUtils.randomAlphabetic(3) + "@*" + RandomStringUtils.randomNumeric(3);
    }

    public static String captureScreen(String tname) throws IOException {
        if (driver == null) return null;

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

        String screenshotsDir = System.getProperty("user.dir") + File.separator + "screenshots";
        File dir = new File(screenshotsDir);
        if (!dir.exists()) dir.mkdirs();

        String targetFilePath = screenshotsDir + File.separator + tname + "_" + timeStamp + ".png";
        File targetFile = new File(targetFilePath);

        sourceFile.renameTo(targetFile);

        return targetFilePath;
    }
}
