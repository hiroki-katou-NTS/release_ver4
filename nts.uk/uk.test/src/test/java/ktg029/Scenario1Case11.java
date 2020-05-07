package ktg029;

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


public class Scenario1Case11 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg029/Scenario1Case11";
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
        driver.findElement(By.id("employee-code-inp")).sendKeys("005517");
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image1.png"));
        driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();


// 1.11 日別実績のエラー有無 - エラー無しの場合
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
        WaitPageLoad();

        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();

        // Go to screen kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        // Clear Error
        WaitElementLoad(By.xpath("//*[@id='dpGrid']/div[4]/table/tbody/tr[3]/td[5]"));
        driver.findElement(By.xpath("//*[@id='dpGrid']/div[4]/table/tbody/tr[3]/td[5]")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id='dpGrid']/div[4]/table/tbody/tr[3]/td[5]")).sendKeys("830");
        Thread.sleep(2000);
        WaitElementLoad(By.id("function-content"));
        driver.findElement(By.id("function-content")).click();
        WaitPageLoad();

        // Click save
        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        // Go to screen cgg008
        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();

        // Tacke a photo
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));

        // Go to screen kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        // Tacke a photo
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));

        WaitPageLoad();
        this.uploadTestLink(554, 133);
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