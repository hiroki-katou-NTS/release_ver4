package kaf010;

import java.io.File;
import java.util.Calendar;
import org.junit.jupiter.api.*;
import org.apache.commons.io.FileUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario1Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kaf010/Scenario1Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        int i = 1;
        //login
        login(screenshotFile, "025497", "Jinjikoi5", i);
        WaitPageLoad();
        
        //load kdw/003
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image"+ i+ ".png"));
        i++;
        //input kaf/010
        Calendar inputdate = Calendar.getInstance();
        login(screenshotFile, "025497", "Jinjikoi5", i);
        inputdate.clear();
        inputdate.set(2019, 11, 8);
        i = setDateKaf010(inputdate, screenshotFile, "004", i);
        TestRoot.folder =   new File(screenshotPath);
        this.uploadTestLink(145, 32);
    }
    
    public int setDateKaf010(Calendar inputdate, File screenshotFile, String wType, int count) throws Exception{
        driver.get(domain + "nts.uk.at.web/view/kaf/010/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("inputdate")).sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.className("workSelect")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElement(By.xpath("//*[@data-id="+ wType + "]")).click();
        driver.findElement(By.xpath("//*[@data-id='040']")).click();
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

        //load cmm/045/a
        login(screenshotFile, "015310", "Jinjikoi5", count);
        WaitPageLoad();
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        WebElement wEleStart = driver.findElements(By.xpath("//input[contains(@class,'ntsDateRangeComponent')]")).get(0);
        wEleStart.clear();
        wEleStart.sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        WebElement wEleEnd = driver.findElements(By.xpath("//input[contains(@class,'ntsEndDatePicker')]")).get(0);
        wEleEnd.clear();
        wEleEnd.sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        WaitPageLoad();
        driver.findElements(By.cssSelector("#grid1_container span.box")).get(0).click();
        driver.findElement(By.xpath("//body")).click();
        driver.switchTo().defaultContent();
        driver.findElement(By.xpath("//button[@tabindex='1']")).click();
        WaitPageLoad();
        
        //count++;
        login(screenshotFile, "025497", "Jinjikoi5", count);

        //load kdw/003/a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        count++;
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