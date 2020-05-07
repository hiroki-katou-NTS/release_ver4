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


public class Scenario1Case6 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg029/Scenario1Case6";
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

 // 1.6
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

        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();       

        // tacke a photo
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));
        
        // Go to kaf010
        driver.get(domain + "nts.uk.at.web/view/kaf/010/a/index.xhtml");
        WaitPageLoad();

        // Click checkbox
        WaitElementLoad(By.xpath("//*[@id='functions-area']/div/label/span[1]"));
        driver.findElement(By.xpath("//*[@id='functions-area']/div/label/span[1]")).click();

        // Input Date
        WaitElementLoad(By.id("inputdate"));
        driver.findElement(By.id("inputdate")).sendKeys("2019/12/21");
        Thread.sleep(2000);
        // Input time 1
        WaitElementLoad(By.id("inpStartTime1"));
        driver.findElement(By.id("inpStartTime1")).sendKeys("900");

        // Input time 2
        WaitElementLoad(By.id("inpEndTime1"));
        driver.findElement(By.id("inpEndTime1")).sendKeys("1730");

        // Input time 3
        WaitElementLoad(By.id("restTimeStart_0_1"));
        driver.findElement(By.id("restTimeStart_0_1")).sendKeys("1200");

        // Input time 4
        WaitElementLoad(By.id("restTimeEnd_0_1"));
        driver.findElement(By.id("restTimeEnd_0_1")).sendKeys("1300");

        // Click save
        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        // login申請者
        driver.get(domain + "nts.uk.com.web/view/ccg/007/d/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("company-code-select")).click();
        WaitElementLoad(By.xpath("//li[@data-value='0001']"));
        driver.findElement(By.xpath("//li[@data-value='0001']")).click();
        driver.findElement(By.id("password-input")).clear();
        driver.findElement(By.id("password-input")).sendKeys("Jinjikoi5");
        driver.findElement(By.id("employee-code-inp")).clear();
        driver.findElement(By.id("employee-code-inp")).sendKeys("002363");
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));
        driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();
        
        // Go to cmm045
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();

        // Click item last
        int index2 = driver.findElements(By.className("ntsButton")).size();
        Thread.sleep(2000);
        driver.findElements(By.className("ntsButton")).get(index2 - 1).click();
        WaitPageLoad();

        // Go to kaf010b
        WaitElementLoad((By.className("proceed")));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();
        
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image4.png"));

        // Go to screen cgg008
        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();

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
         driver.findElement(By.id("login-btn")).click();
         WaitPageLoad();

        // Click go to cmm045
        driver.switchTo().frame(1);
        Thread.sleep(2000);
        WaitElementLoad(By.xpath("//*[@id='contents-area']/table[1]/tbody/tr[7]/td[2]/table/tbody/tr/td[2]/button"));
        driver.findElement(By.xpath("//*[@id='contents-area']/table[1]/tbody/tr[7]/td[2]/table/tbody/tr/td[2]/button")).click();
        WaitPageLoad();

        // tacke a photo
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image5.png"));

        WaitPageLoad();
        this.uploadTestLink(544, 128);
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