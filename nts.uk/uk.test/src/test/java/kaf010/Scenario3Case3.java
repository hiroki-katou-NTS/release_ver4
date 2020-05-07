package kaf010;

import java.io.File;
import java.util.Calendar;
import org.junit.jupiter.api.*;
import org.apache.commons.io.FileUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;

public class Scenario3Case3 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kaf010/Scenario3Case3";
        this.init();
    }

    @Test
    public void test() throws Exception {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        int i = 1;
        //login
        login(screenshotFile, "025445", "Jinjikoi5", i);
        WaitPageLoad();
        
        //load kdw/003
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image"+ i+ ".png"));     
        TestRoot.folder =   new File(screenshotPath);
        this.uploadTestLink(155, 36);
    }

    
    public int setDateKaf010(Calendar inputdate, File screenshotFile, int count) throws Exception{
        driver.get(domain + "nts.uk.at.web/view/kaf/010/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("inputdate")).sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.className("workSelect")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElement(By.xpath("//*[@data-id='006']")).click();
        driver.findElement(By.xpath("//*[@data-id='010']")).click();
        driver.findElement(By.xpath("//button[@tabindex='11']")).click();
        driver.switchTo().defaultContent();
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("inpStartTime1")).click();
        driver.findElement(By.id("inpStartTime1")).clear();
        driver.findElement(By.id("inpStartTime1")).sendKeys("当日8:00");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("inpEndTime1")).click();
        driver.findElement(By.id("inpEndTime1")).clear();
        driver.findElement(By.id("inpEndTime1")).sendKeys("当日18:00");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.xpath("//button[@class='caret-bottom']")).click();
        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image" + count+ ".png"));
        return count;
    }

    public void login(File screenshotFile, String empCode, String passWord, int count)throws Exception{
        driver.get(domain + "nts.uk.com.web/view/ccg/007/d/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("company-code-select")).click();
        WaitElementLoad(By.xpath("//li[@data-value='0001']"));
        driver.findElement(By.xpath("//li[@data-value='0001']")).click();
        driver.findElement(By.id("employee-code-inp")).click();
        driver.findElement(By.id("employee-code-inp")).clear();
        driver.findElement(By.id("employee-code-inp")).sendKeys(empCode);

        driver.findElement(By.id("password-input")).click();
        driver.findElement(By.id("password-input")).clear();
        driver.findElement(By.id("password-input")).sendKeys(passWord);
        driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();
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