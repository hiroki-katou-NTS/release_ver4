package kwr001;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import common.TestRoot;

public class Scenario1Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kwr001/Scenario1Case2";
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

        // 残業申請
        driver.get(domain + "nts.uk.at.web/view/kwr/001/a/index.xhtml");
        WaitPageLoad();

        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        driver.findElement(By.id("cbb-closure")).click();
        Thread.sleep(200);
        driver.findElements(By.xpath("//li[@data-value='0']")).get(1).click();
        WaitPageLoad();

        // Setting data Calendar
        Calendar inputdate = Calendar.getInstance();
        inputdate.add(Calendar.DATE, 1);
        driver.findElement(By.xpath("//input[contains(@class,'ntsStartDatePicker')]")).clear();
        driver.findElement(By.xpath("//input[contains(@class,'ntsStartDatePicker')]")).sendKeys("2019/07/01");
        Thread.sleep(200);
        driver.findElement(By.xpath("//input[contains(@class,'ntsEndDatePicker')]")).clear();
        driver.findElement(By.xpath("//input[contains(@class,'ntsEndDatePicker')]")).sendKeys("2019/07/31");
        WaitPageLoadLess();

        // Setting tab 2
        driver.findElement(By.xpath("//li[@aria-controls='tab-2']")).click();
        WaitPageLoadLess();
        WaitElementLoad(By.id("WorkplaceList"));
        driver.findElement(By.id("WorkplaceList")).click();

        driver.findElements(By.xpath("//input[contains(@class,'ntsSearchBox nts-editor ntsSearchBox_Component')]"))
                .get(1).click();
        driver.findElements(By.xpath("//input[contains(@class,'ntsSearchBox nts-editor ntsSearchBox_Component')]"))
                .get(1).sendKeys("TIS");
        Thread.sleep(200);

        // Take a photo
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));
        WaitPageLoadLess();

        // Click button
        driver.findElements(By.xpath("//button")).get(16).click();
        Thread.sleep(200);
        driver.findElements(By.xpath("//button")).get(17).click();
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();
        WaitPageLoadLess();

        // Setting combobox
        driver.findElement(By.id("combo-box")).click();
        Thread.sleep(200);
        driver.findElements(By.xpath("//li[@data-value='20']")).get(0).click();
        driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox-label')]")).get(0).click();
        driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox-label')]")).get(1).click();
        driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox-label')]")).get(2).click();
        driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox-label')]")).get(3).click();
        driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox-label')]")).get(4).click();
        driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox-label')]")).get(5).click();
        driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox-label')]")).get(6).click();
        driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox-label')]")).get(7).click();
        driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox-label')]")).get(8).click();
        driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox-label')]")).get(9).click();
        WaitPageLoadLess();

        // Take a photo
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));
        WaitPageLoad();

        // Click button
        driver.findElements(By.xpath("//button")).get(1).click();
        WaitPageLoad();

        // Print full screen
        Robot r = new Robot();
        Rectangle capture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage Image = r.createScreenCapture(capture);
        ImageIO.write(Image, "png", new File(screenshotPath + "/image4.png"));

        this.uploadTestLink(121, 26);
    }

    public void WaitPageLoadLess() {
        try {
            Thread.sleep(500);
            _wait.until(d -> {
                try {
                    d.findElement(By.xpath("//div[contains(@class,'blockOverlay')]"));
                } catch (NoSuchElementException e) {
                    return true;
                }
                return false;
            });
        } catch (Exception e) {
        }
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