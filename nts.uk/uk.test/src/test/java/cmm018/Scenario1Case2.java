package cmm018;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario1Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm018/Scenario1Case2";
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
        // driver.findElement(By.id("employee-code-inp")).sendKeys("025445");
        // driver.findElement(By.id("password-input")).clear();
        // driver.findElement(By.id("password-input")).sendKeys("Jinjikoi5");
        // File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image1.png"));
        // driver.findElement(By.id("login-btn")).click();
        // WaitPageLoad();
        login("025445", "Jinjikoi5");
        driver.get(domain+"nts.uk.com.web/view/cmm/018/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//a[contains(.,'社員')]")).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));
        screenShot();
        driver.findElements(By.xpath("//button")).get(232).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElements(By.xpath("//span[contains(@class,'ui-icon-check')]")).get(3).click();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));
        screenShot();
        driver.findElement(By.xpath("//button[contains(@class,'move-back')]")).click();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image4.png"));
        screenShot();
        driver.findElement(By.id("execution")).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image5.png"));
        screenShot();
        this.uploadTestLink(71, 11);
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