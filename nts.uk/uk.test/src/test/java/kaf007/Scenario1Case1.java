package kaf007;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kaf007/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {   
        // login 025445
        // File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // login("025445", "Jinjikoi5", "image1", screenshotFile);
        login("025445", "Jinjikoi5");
        driver.get(domain + "nts.uk.at.web/view/kaf/011/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//span[contains(@class,'box')]")).get(0).click();
        driver.findElement(By.id("recDatePicker")).sendKeys("2019/11/30");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("absDatePicker"));
        driver.findElement(By.id("absDatePicker")).sendKeys("2019/11/29");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//button[contains(@class,'proceed')]"));
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));
        screenShot();
        driver.findElements(By.xpath("//button[contains(@class,'proceed')]")).get(0).click();
        WaitPageLoad();
        // login 010392 approval
        // login("010392", "Jinjikoi5", "image3", screenshotFile);
        login("010392", "Jinjikoi5");
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        driver.findElements(By.xpath("//span[contains(@class,'box')]")).get(6).click();
        WaitElementLoad(By.xpath("//button[contains(.,'承認')]"));
        driver.findElements(By.xpath("//button[contains(.,'承認')]")).get(0).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image4.png"));
        screenShot();
        // login 025445
        // login("025445", "Jinjikoi5", "image5", screenshotFile);
        login("025445", "Jinjikoi5");
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        Thread.sleep(5000);
        driver.findElements(By.xpath("//button[contains(@class,'ntsDatePrevButton')]")).get(1).click();
        WaitElementLoad(By.id("btnExtraction"));
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image6.png"));
        screenShot();
        this.uploadTestLink(97, 20);
    }

    // public void login(String id,String pass,String nameImage,File screenshotFile) throws Exception{
    //     driver.get(domain+"nts.uk.com.web/view/ccg/007/d/index.xhtml");
    //     WaitPageLoad();
    //     driver.findElement(By.id("company-code-select")).click();
    //     WaitElementLoad(By.xpath("//li[@data-value='0001']"));
    //     driver.findElement(By.xpath("//li[@data-value='0001']")).click();
    //     driver.findElement(By.id("employee-code-inp")).clear();
    //     driver.findElement(By.id("employee-code-inp")).sendKeys(id);
    //     driver.findElement(By.id("password-input")).clear();
    //     driver.findElement(By.id("password-input")).sendKeys(pass);
    //     screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    //     FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/"+nameImage+".png"));
    //     driver.findElement(By.id("login-btn")).click();
    //     WaitPageLoad();
    // }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}