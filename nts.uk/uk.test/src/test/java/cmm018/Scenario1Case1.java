package cmm018;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm018/Scenario1Case1";
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
        // driver.findElement(By.id("employee-code-inp")).sendKeys("010392");
        // driver.findElement(By.id("password-input")).clear();
        // driver.findElement(By.id("password-input")).sendKeys("Jinjikoi5");
        // File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image1.png"));
        // driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();
        login("010392", "Jinjikoi5");
        driver.get(domain+"nts.uk.com.web/view/cmm/053/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-3"));
        driver.findElement(By.id("ui-id-3")).click();
        WaitElementLoad(By.id("ui-id-7"));
        driver.findElement(By.id("ui-id-7")).click();
        WaitElementLoad(By.id("ccg001-btn-advanced-search"));
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();
        //WaitPageLoad();
        WaitElementLoad(By.xpath("//tr[@data-id='025445']"));
        driver.findElement(By.xpath("//tr[@data-id='025445']")).click();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));
        screenShot();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        //Thread.sleep(1000);
        WaitElementLoad(By.id("A2_10"));
        driver.findElement(By.id("A2_10")).clear();
        driver.findElement(By.id("A2_10")).sendKeys("016976");
        driver.findElement(By.id("A1_3")).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));
        screenShot();
        driver.findElements(By.xpath("//button")).get(28).click();
        //cmm018
        driver.get(domain+"nts.uk.com.web/view/cmm/018/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//a[contains(.,'社員')]")).click();
        //WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-search-drawer"));
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-3"));
        driver.findElement(By.id("ui-id-3")).click();
        WaitElementLoad(By.id("ui-id-7"));
        driver.findElement(By.id("ui-id-7")).click();
        //Thread.sleep(1000);
        WaitElementLoad(By.id("ccg001-btn-advanced-search"));
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image4.png"));
        screenShot();
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();
        //WaitPageLoad();
        WaitElementLoad(By.id("btn_show_list-emp-component"));
        driver.findElement(By.id("btn_show_list-emp-component")).click();
        //WaitPageLoad();
        WaitElementLoad(By.xpath("//td[contains(@class,'nts-column')]"));
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image5.png"));
        screenShot();
        driver.findElements(By.xpath("//td[contains(@class,'nts-column')]")).get(5).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image6.png"));
        this.uploadTestLink(64, 9);
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