package kaf007;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario4Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kaf007/Scenario4Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login 025445
        // File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // login("025445", "Jinjikoi5", "image1", screenshotFile);
        login("025445", "Jinjikoi5");
        //kdw003
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//span[contains(@class,'box')]")).get(6).click();
        WaitElementLoad(By.xpath("//button[contains(@class,'proceed')]"));
        driver.findElements(By.xpath("//button[contains(@class,'proceed')]")).get(0).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));
        screenShot();
        //kaf007 run 1
        driver.get(domain + "nts.uk.at.web/view/kaf/007/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//span[contains(@class,'box')]")).get(0).click();
        driver.findElements(By.xpath("//button[contains(@class,'nts-switch-button')]")).get(0).click();
        driver.findElement(By.id("singleDate")).sendKeys("2019/12/04");
        driver.findElement(By.xpath("//body")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[contains(@class,'proceed')]"));
        driver.findElements(By.xpath("//button[contains(@class,'proceed')]")).get(0).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));
        screenShot();
        
        //kaf007 run 2
        driver.get(domain + "nts.uk.at.web/view/kaf/007/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//span[contains(@class,'box')]")).get(0).click();
        driver.findElements(By.xpath("//button[contains(@class,'nts-switch-button')]")).get(1).click();
        driver.findElement(By.id("singleDate")).sendKeys("2019/12/03");
        driver.findElement(By.xpath("//body")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[contains(@class,'proceed')]"));
        driver.findElements(By.xpath("//button[contains(@class,'proceed')]")).get(0).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image4.png"));
        screenShot();
        this.uploadTestLink(189, 45);//case 1
        this.uploadTestLink(245, 52);//case 2
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