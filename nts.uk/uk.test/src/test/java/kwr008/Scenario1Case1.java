package kwr008;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

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

public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kwr008/Scenario1Case1";
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
        driver.get(domain + "nts.uk.at.web/view/kwr/008/a/index.xhtml");
        WaitPageLoad();

        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        Thread.sleep(500);
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));
        driver.findElement(By.id("ccg001-btn-search-all")).click();
        WaitPageLoad();
        
        // Click switch button
        driver.findElements(By.xpath("//button[contains(@class, 'nts-switch-button')]")).get(9).click();
        Thread.sleep(200);

        // Click combobox
        WaitElementLoad(By.id("outputItem"));
        driver.findElement(By.id("outputItem")).click();

        // Set combobox
        WaitElementLoad(By.xpath("//li[@data-value='08']"));
        driver.findElements(By.xpath("//li[@data-value='08']")).get(0).click();

        // Click A6_2
        WaitElementLoad(By.id("A6_2"));
        driver.findElement(By.id("A6_2")).click();

        WaitElementLoad(By.xpath("//li[@data-value='1']"));
        driver.findElements(By.xpath("//li[@data-value='1']")).get(0).click();

        WaitElementLoad(By.xpath("//div[@tabindex='3']"));
        driver.findElements(By.xpath("//div[@tabindex='3']")).get(1).click();

        driver.findElements(By.xpath("//button")).get(25).click();
        Thread.sleep(200);
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));
        WaitPageLoad();

        driver.findElement(By.id("A1_1")).click();
        WaitPageLoad();

        // Print full screen
        Robot r = new Robot();
        Rectangle capture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage Image = r.createScreenCapture(capture);
        ImageIO.write(Image, "png", new File(screenshotPath + "/image4.png"));

        this.uploadTestLink(124, 27);
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