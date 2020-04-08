package kdw003;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Scenario5Case1 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario5Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
        driver.get(domain + "nts.uk.com.web/view/ccg/007/d/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("company-code-select")).click();
        driver.findElement(By.id("password-input")).sendKeys("Jinjikoi5");
        driver.findElement(By.id("employee-code-inp")).clear();
        driver.findElement(By.id("employee-code-inp")).sendKeys("018234");
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image1.png"));
        driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();
        kmk012("2019/08");
        // Go to screen Kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        if(driver.findElements(By.xpath("//iframe[@name='window_1']")).size() !=0){
            driver.switchTo().frame("window_1");
            driver.findElement(By.id("dialogClose")).click();
        }
        js.executeScript("$('#contents-area').scrollTop(150)");
        
        // tacke a photo
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));
        Thread.sleep(2000);

        //5.2
        // login申請者
        driver.get(domain + "nts.uk.com.web/view/ccg/007/d/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("company-code-select")).click();
        driver.findElement(By.id("password-input")).sendKeys("Jinjikoi5");
        driver.findElement(By.id("employee-code-inp")).clear();
        driver.findElement(By.id("employee-code-inp")).sendKeys("016209");
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));
        driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();

        kmk012("2019/08");
        // Go to screen Kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        js.executeScript("$('#contents-area').scrollTop(150)");

        // tacke a photo
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image4.png"));
        Thread.sleep(2000);

        //5.3
        kmk012("2019/06");
        // Go to screen Kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        js.executeScript("$('#contents-area').scrollTop(150)");
        
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image5.png"));

        // 5.4
        kmk012("2019/08");
        // Go to screen Kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        js.executeScript("$('#contents-area').scrollTop(150)");

        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image6.png"));

        // 5.5
        kmk012("2019/07");
        // Go to screen Kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        WaitElementLoad(By.id("btn-releaseAll"));
        driver.findElement(By.id("btn-releaseAll")).click();

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();

        js.executeScript("$('#contents-area').scrollTop(150)");

        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image7.png"));
        Thread.sleep(2000);

        // 5.6
        kmk012("2019/07");
        // Go to screen Kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        WaitElementLoad(By.id("btn-signAll"));
        driver.findElement(By.id("btn-signAll")).click();

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        kmk012("2019/08");
        // Go to screen Kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        Calendar inputStartDate = Calendar.getInstance();
        inputStartDate.set(2019, 7, 1);
        Calendar inputEndDate = Calendar.getInstance();
        inputEndDate.set(2019, 7, 30);

        extractData(inputStartDate, inputEndDate);

        js.executeScript("$('#contents-area').scrollTop(150)");

        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image8.png"));
        Thread.sleep(2000);

        // 5.7
        kmk012("2019/07");

        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        scrollToRowColumn(21, 2);
        selectItemKdw003_1("本人", "08/20(火)").click();

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        
        selectItemKdw003_1("退勤時刻1", "08/20(火)").click();
        selectItemKdw003_1("退勤時刻1", "08/20(火)").sendKeys("2000");

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        selectItemKdw003_1("本人", "08/20(火)").click();

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();

        js.executeScript("$('#contents-area').scrollTop(150)");
        Thread.sleep(2000);

        kmk012("2019/08");

        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        Calendar inputStartDate2 = Calendar.getInstance();
        inputStartDate2.set(2019, 7, 1);
        Calendar inputEndDate2 = Calendar.getInstance();
        inputEndDate2.set(2019, 7, 30);

        extractData(inputStartDate2, inputEndDate2);
        WaitPageLoad();

        driver.findElement(By.xpath("//button[contains(.,'本人締め処理')]")).click();
        WaitPageLoad();

        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image9.png"));

        //5.8
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        Calendar inputStartDate3 = Calendar.getInstance();
        inputStartDate3.set(2019, 7, 1);
        Calendar inputEndDate3 = Calendar.getInstance();
        inputEndDate3.set(2019, 7, 30);

        extractData(inputStartDate3, inputEndDate3);
        WaitPageLoad();

        scrollToRowColumn(21, 2);
        Thread.sleep(2000);

        selectItemKdw003_1("本人", "08/20(火)").click();

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        
        selectItemKdw003_1("退勤時刻1", "08/20(火)").click();
        selectItemKdw003_1("退勤時刻1", "08/20(火)").sendKeys("1000");

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        selectItemKdw003_1("本人", "08/20(火)").click();

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        js.executeScript("$('#contents-area').scrollTop(150)");

        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image10.png"));
        Thread.sleep(2000);

        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();

        WaitPageLoad();
        this.uploadTestLink(1023, 257);

    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    public void kmk012(String date) {
        // Setting screen kmk012
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();

        // Clear Input Month
        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).clear();

        // Input into Month
        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).sendKeys(date);
        driver.findElement(By.id("contents-right")).click();

        // Click button Save
        WaitElementLoad(By.id("btn_save"));
        driver.findElement(By.id("btn_save")).click();
        WaitPageLoad();
    }
}