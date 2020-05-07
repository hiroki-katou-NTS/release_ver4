package kdw004;

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

import kdw003.Kdw003Common;


public class Scenario1Case1 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw004/Scenario1Case1";
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
        driver.findElement(By.id("employee-code-inp")).sendKeys("007102");
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image1.png"));
        driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();

        // Go to screen Kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        Calendar inputStartDate = Calendar.getInstance();
        inputStartDate.set(2019, 10, 1);
        Calendar inputEndDate = Calendar.getInstance();
        inputEndDate.set(2019, 10, 30);

        extractData(inputStartDate, inputEndDate);
        WaitPageLoad();

        if(selectItemKdw003_1("本人", "11/12(火)").isSelected()){
            selectItemKdw003_1("本人", "11/12(火)").click();
        }

        if(selectItemKdw003_1("本人", "11/13(水)").isSelected()){
            selectItemKdw003_1("本人", "11/13(水)").click();
        }

        if(selectItemKdw003_1("本人", "11/14(木)").isSelected()){
            selectItemKdw003_1("本人", "11/14(木)").click();
        }

        // tacke a photo
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();

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
         screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
         FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));
         driver.findElement(By.id("login-btn")).click();
         WaitPageLoad();

        kmk012("2019/10");

        // Go to screen Kdw004a
        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        WaitPageLoad();
       
        // tacke a photo
        Thread.sleep(2000);
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image4.png"));

        WaitPageLoad();
        this.uploadTestLink(847, 201);
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