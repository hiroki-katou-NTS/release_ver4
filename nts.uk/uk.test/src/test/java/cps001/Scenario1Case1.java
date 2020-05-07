package cps001;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cps001/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        //login申請者
        // login("020905", "Jinjikoi5", "image1", screenshotFile);
        login("020905", "Jinjikoi5");
        driver.get(domain+"nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-6"));
        driver.findElement(By.id("ui-id-6")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("020909");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(1).click();   
        WaitPageLoad();
        WaitElementLoad(By.id("ui-id-3"));
        driver.findElement(By.id("ui-id-3")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00003']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00003']")).click();
        WaitElementLoad(By.xpath("//div[@id ='COM1000000000000000CS00003IS00021']/.//input"));
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00003IS00021']/.//input")).clear();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00003IS00021']/.//input")).sendKeys("2019/12/16");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));
        driver.findElements(By.xpath("//button[contains(@class,'proceed')]")).get(0).click();
        WaitPageLoad();
        screenShot();
        //login
        // driver.get(domain+"nts.uk.com.web/view/ccg/007/d/index.xhtml");
        // WaitPageLoad();
        // driver.findElement(By.id("company-code-select")).click();
        // WaitElementLoad(By.xpath("//li[@data-value='0001']"));
        // driver.findElement(By.xpath("//li[@data-value='0001']")).click();
        // driver.findElement(By.id("employee-code-inp")).clear();
        // driver.findElement(By.id("employee-code-inp")).sendKeys("020909");
        // driver.findElement(By.id("password-input")).clear();
        // driver.findElement(By.id("password-input")).sendKeys("Jinjikoi5");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));
        // driver.findElement(By.id("login-btn")).click();
        // WaitPageLoad();
        login("020909", "Jinjikoi5");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image4.png"));
        screenShot();


        //
        // login("020905", "Jinjikoi5", "image5", screenshotFile);
        login("020905", "Jinjikoi5");
        driver.get(domain+"nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-6"));
        driver.findElement(By.id("ui-id-6")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("020909");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(1).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ui-id-3"));
        driver.findElement(By.id("ui-id-3")).click();
        By.xpath("//div[@id ='COM1000000000000000CS00003IS00021']/../input");
        WaitPageLoad();
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00003']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00003']")).click();
        WaitElementLoad(By.xpath("//div[@id ='COM1000000000000000CS00003IS00021']/.//input"));
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00003IS00021']/.//input")).clear();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image6.png"));
        screenShot();
        driver.findElements(By.xpath("//button[contains(@class,'proceed')]")).get(0).click();
        WaitPageLoad();
        //login
        // login("020909", "Jinjikoi5", "image7", screenshotFile);
        login("020909", "Jinjikoi5");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image8.png"));
        screenShot();
        this.uploadTestLink(292, 62);
     
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