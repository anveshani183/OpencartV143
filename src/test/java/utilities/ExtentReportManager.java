package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentReportManager implements ITestListener {

    private ExtentSparkReporter sparkReporter;
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private String repName;

    @Override
    public void onStart(ITestContext testContext) {
        try {
            String reportsDirPath = System.getProperty("user.dir") + File.separator + "reports";
            File reportsDir = new File(reportsDirPath);
            if (!reportsDir.exists()) reportsDir.mkdirs();

            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            repName = "Test-Report-" + timeStamp + ".html";

            sparkReporter = new ExtentSparkReporter(reportsDirPath + File.separator + repName);
            sparkReporter.config().setDocumentTitle("Opencart Automation Report");
            sparkReporter.config().setReportName("Opencart Functional Testing");
            sparkReporter.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            extent.setSystemInfo("Application", "Opencart");
            extent.setSystemInfo("Module", "Admin");
            extent.setSystemInfo("Sub Module", "Customers");
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
            extent.setSystemInfo("Environment", "QA");

            String os = testContext.getCurrentXmlTest().getParameter("os");
            if (os != null) extent.setSystemInfo("Operating System", os);

            String browser = testContext.getCurrentXmlTest().getParameter("browser");
            if (browser != null) extent.setSystemInfo("Browser", browser);

            List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
            if (!includedGroups.isEmpty()) {
                extent.setSystemInfo("Groups", includedGroups.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        test.assignCategory(result.getMethod().getGroups());
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, result.getName() + " got successfully executed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().log(Status.FAIL, result.getName() + " got failed");
        if (result.getThrowable() != null) {
            extentTest.get().log(Status.INFO, result.getThrowable().getMessage());
        }

        try {
            if (BaseClass.driver != null) {
                String imgPath = BaseClass.captureScreen(result.getMethod().getMethodName());
                if (imgPath != null) {
                    extentTest.get().addScreenCaptureFromPath(imgPath);
                } else {
                    extentTest.get().log(Status.WARNING, "Screenshot method returned null.");
                }
            } else {
                extentTest.get().log(Status.WARNING, "WebDriver was null; screenshot not captured.");
            }
        } catch (IOException e) {
            extentTest.get().log(Status.WARNING, "Failed to capture/attach screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, result.getName() + " got skipped");
        if (result.getThrowable() != null) {
            extentTest.get().log(Status.INFO, result.getThrowable().getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }

        String pathOfExtentReport = System.getProperty("user.dir") + File.separator + "reports" + File.separator + repName;
        File extentReport = new File(pathOfExtentReport);
        try {
            if (extentReport.exists()) {
                Desktop.getDesktop().browse(extentReport.toURI());
            } else {
                System.out.println("Extent report not found at: " + pathOfExtentReport);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
