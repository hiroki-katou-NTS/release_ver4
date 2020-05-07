package kaf006;

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
import org.openqa.selenium.WebElement;


public class Scenario4Case1 extends Kaf006Common{
    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kaf006/Scenario4Case1";
        this.init();
        employeeCode = "025445";
        password = "Jinjikoi5";
        kaf006 = linkFullScreen("nts.uk.at.web/view/kaf/006/a/index.xhtml");
        cmm045 = linkFullScreen("nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        kdw003 = linkFullScreen("nts.uk.at.web/view/kdw/003/a/index.xhtml");
        kmk015 = linkFullScreen("nts.uk.at.web/view/kmk/015/a/index.xhtml");
    }

    @Test
    public void test() throws Exception {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        int i = 1;
        Calendar inputdate = Calendar.getInstance();
        inputdate.clear();
        inputdate.set(2019, 11, 27);
        //login
        login(screenshotFile, employeeCode, password);
        WaitPageLoad();
        driver.get(kaf006);
        WaitPageLoad();
        driver.findElement(By.xpath("//table[@id ='tbl']/.//span[@class ='box']")).click();
        WebElement wEleStart = driver.findElements(By.xpath("//input[contains(@class,'ntsStartDatePicker ')]")).get(0);
        wEleStart.clear();
        wEleStart.sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        inputdate.clear();
        inputdate.set(2019, 11, 31);
        WebElement wEleEnd = driver.findElements(By.xpath("//input[contains(@class,'ntsEndDatePicker')]")).get(0);
        wEleEnd.clear();
        wEleEnd.sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();
        
        WaitElementLoad(By.xpath("//div[@id ='hdType']/button[1]"));
        driver.findElement(By.xpath("//div[@id ='hdType']/button[1]")).click();
        WaitElementLoad(By.xpath("//*[@id = 'workTypes']/.//*[@class ='ui-igcombo-buttonicon']"));
        driver.findElement(By.xpath("//*[@id = 'workTypes']/.//*[@class ='ui-igcombo-buttonicon']")).click();
        WaitElementLoad(By.xpath("//ul[@class ='ui-igcombo-listitemholder']/li[@data-value='107']"));
        driver.findElement(By.xpath("//ul[@class ='ui-igcombo-listitemholder']/li[@data-value='107']")).click();
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image"+ i+ ".png"));

        //cmm045 approval
        //015310
        login(screenshotFile, "010392", password);
        driver.get(cmm045);
        WaitPageLoad();
        inputdate.clear();
        inputdate.set(2019, 11, 27);
        WebElement wEleStartApp = driver.findElements(By.xpath("//input[contains(@class,'ntsStartDatePicker')]")).get(0);
        wEleStartApp.clear();
        wEleStartApp.sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        inputdate.clear();
        inputdate.set(2019, 11, 31);
        WebElement wEleEndApp = driver.findElements(By.xpath("//input[contains(@class,'ntsEndDatePicker')]")).get(0);
        wEleEndApp.clear();
        wEleEndApp.sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        WaitPageLoad();
        driver.findElements(By.cssSelector("#grid1_container span.box")).get(0).click();
        driver.findElement(By.xpath("//body")).click();
        driver.switchTo().defaultContent();
        driver.findElement(By.xpath("//button[@tabindex='1']")).click();
        WaitPageLoad();
        
        login(screenshotFile, employeeCode, password);

        driver.get(linkFullScreen("nts.uk.at.web/view/cmm/045/a/index.xhtml?a=0"));
        WaitPageLoad();
        Thread.sleep(5000);
        inputdate.clear();
        inputdate.set(2019, 11, 27);
        WebElement wEleStartSelf = driver.findElement(By.xpath("//div[@id ='daterangepicker']/.//input[contains(@class,'ntsStartDatePicker')]"));
        wEleStartSelf.clear();
        wEleStartSelf.sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        inputdate.clear();
        inputdate.set(2019, 11, 31);
        WebElement wEleEndSelf = driver.findElement(By.xpath("//div[@id ='daterangepicker']/.//input[contains(@class,'ntsEndDatePicker')]"));
        wEleEndSelf.clear();
        wEleEndSelf.sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        WaitPageLoad();

        i++;
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image"+ i+ ".png"));

        driver.get(kdw003);
        WaitPageLoad();
        inputdate.clear();
        inputdate.set(2019, 11, 1);
        WebElement wEleStartKdw003 = driver.findElement(By.xpath("//div[@id ='daterangepicker']/.//input[contains(@class,'ntsStartDatePicker')]"));
        wEleStartKdw003.clear();
        wEleStartKdw003.sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        inputdate.clear();
        inputdate.set(2019, 11, 31);
        WebElement wEleEndKdw003 = driver.findElement(By.xpath("//div[@id ='daterangepicker']/.//input[contains(@class,'ntsEndDatePicker')]"));
        wEleEndKdw003.clear();
        wEleEndKdw003.sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        //WebElement element = driver.findElement(By.xpath("//td[text()='12/31(火)']"));
        WebElement element =  driver.findElement(By.xpath("//table[@class ='mgrid-free-table']/tbody/tr[30]/td[26]"));
        js.executeScript ("arguments[0].scrollIntoView();", element);
        i++;
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image"+ i+ ".png"));
        this.uploadTestLink(518, 121);
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