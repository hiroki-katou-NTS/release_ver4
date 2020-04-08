package ktg030;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import common.TestRoot;


public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg030/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
        driver.get(domain + "nts.uk.com.web/view/ccg/007/d/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("company-code-select")).click();
        WaitElementLoad(By.xpath("//li[@data-value='0001']"));
        driver.findElement(By.xpath("//li[@data-value='0001']")).click();
        driver.findElement(By.id("password-input")).clear();
        driver.findElement(By.id("password-input")).sendKeys("Jinjikoi5");
        driver.findElement(By.id("employee-code-inp")).clear();
        driver.findElement(By.id("employee-code-inp")).sendKeys("004515");
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image1.png"));
        driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();

        // Setting screen kmk012
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        
        // Clear Input Month
        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).clear();

        // Input into Month
        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).sendKeys("2019/11");

        driver.findElement(By.id("contents-right")).click();

        // Click button Save
        WaitElementLoad(By.id("btn_save"));
        driver.findElement(By.id("btn_save")).click();

        // Setting screen kmw003
        driver.get(domain + "nts.uk.at.web/view/kmw/003/a/index.xhtml?initmode=2");
        WaitPageLoad();
        // Check checkbox of data 2019/11
        if (driver.findElements(By.xpath("//input[(@checked='checked')]")).size() <= 1) {
            driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox')]")).get(11).click();
            Thread.sleep(100);
            driver.findElements(By.xpath("//button")).get(0).click();
            WaitPageLoad();
            driver.findElements(By.xpath("//button[contains(@class,'large')]")).get(1).click();
        }

        // Go to 2019/12
        driver.findElement(By.xpath("//button[contains(@class,'ntsDatePrevButton')]")).click();
        WaitPageLoad();
        
        // Check checkbox of data 2019/12
        if (driver.findElements(By.xpath("//input[(@checked='checked')]")).size() <= 1) {
            driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox')]")).get(3).click();
            Thread.sleep(100);
            driver.findElements(By.xpath("//button")).get(0).click();
            WaitPageLoad();
            driver.findElements(By.xpath("//button[contains(@class,'large')]")).get(1).click();
        }

        // Go to screen cgg008
        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.xpath("//li[@aria-controls='tab-1']"));
        driver.findElement(By.xpath("//li[@aria-controls='tab-1']")).click();

        // Take a photo
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));
        Thread.sleep(500);

        // Go to screen kmw003
        driver.get(domain + "nts.uk.at.web/view/kmw/003/a/index.xhtml?initmode=2");
        WaitPageLoad();

        // Take a photo
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));

        WaitPageLoad();
        this.uploadTestLink(127, 28);
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