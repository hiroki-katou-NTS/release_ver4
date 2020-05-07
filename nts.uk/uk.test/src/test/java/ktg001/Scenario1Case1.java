package ktg001;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg001/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login
        // driver.get(domain+"nts.uk.com.web/view/ccg/007/d/index.xhtml");
        // WaitPageLoad();
        // driver.findElement(By.id("company-code-select")).click();
        // WaitElementLoad(By.xpath("//li[@data-value='0001']"));
        // driver.findElement(By.xpath("//li[@data-value='0001']")).click();
        // driver.findElement(By.id("employee-code-inp")).clear();
        // driver.findElement(By.id("employee-code-inp")).sendKeys("004515");
        // driver.findElement(By.id("password-input")).clear();
        // driver.findElement(By.id("password-input")).sendKeys("Jinjikoi5");
        // File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image1.png"));
        // driver.findElement(By.id("login-btn")).click();
        // WaitPageLoad();
        login("004515", "Jinjikoi5");
        driver.get(domain+"nts.uk.at.web/view/kdw/004/a/index.xhtml");
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));
        screenShot();
        driver.findElement(By.xpath("//a[contains(.,'017267')]")).click();
        WaitPageLoad();
        WaitElementLoad(By.id("btn-releaseAll"));
        driver.findElement(By.id("btn-releaseAll")).click();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));
        screenShot();
        driver.findElement(By.xpath("//button[contains(.,'確定')]")).click();
        WaitPageLoad();
        driver.get(domain+"nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image4.png"));
        screenShot();
        driver.switchTo().frame(2); //chuyển frame
        driver.findElement(By.xpath("//button")).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image5.png"));
        screenShot();
        this.uploadTestLink(91, 18);
    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}