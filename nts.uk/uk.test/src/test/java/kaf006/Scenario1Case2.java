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


public class Scenario1Case2 extends Kaf006Common{
    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kaf006/Scenario1Case2";
        this.init();
        employeeCode = "018937";
        password = "Jinjikoi5";
        kaf006 = linkFullScreen("nts.uk.at.web/view/kaf/006/a/index.xhtml");
        cmm045 = linkFullScreen("nts.uk.at.web/view/cmm/045/a/index.xhtml");
        kdw003 = linkFullScreen("nts.uk.at.web/view/kdw/013/a/index.xhtml");
        kmk015 = linkFullScreen("nts.uk.at.web/view/kmk/015/a/index.xhtml");
    }

    @Test
    public void test() throws Exception {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        int i = 1;
        //login
        login(screenshotFile, employeeCode, password);
        WaitPageLoad();
        
        //set kmk015
        driver.get(kmk015);
        WaitPageLoad();
        WaitElementLoad(By.xpath("//*[@data-id='106']"));
        driver.findElement(By.xpath("//*[@data-id='106']")).click();
        WaitElementLoad(By.id("number-1"));
        driver.findElement(By.id("number-1")).click();
        driver.findElement(By.id("number-1")).clear();
        driver.findElement(By.id("number-1")).sendKeys("5");
        //driver.findElement(By.xpath("//@id ='tbl-master-list'/.//*[@data-row-idx='0']")).click();
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("submitHist")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@class = 'large']")).click();

        employeeCode = "025497";
        login(screenshotFile, employeeCode, password);
        driver.get(kaf006);
        WaitPageLoad();
        driver.findElement(By.xpath("//table[@id ='tbl']/.//span[@class ='box']")).click();
        Calendar inputdate = Calendar.getInstance();
        inputdate.clear();
        inputdate.set(2019, 11, 9);
        WebElement wEleStart = driver.findElements(By.xpath("//input[contains(@class,'ntsStartDatePicker ')]")).get(0);
        wEleStart.clear();
        wEleStart.sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        inputdate.clear();
        inputdate.set(2019, 11, 13);
        WebElement wEleEnd = driver.findElements(By.xpath("//input[contains(@class,'ntsEndDatePicker')]")).get(0);
        wEleEnd.clear();
        wEleEnd.sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();
        
        WaitElementLoad(By.xpath("//div[@id ='hdType']/button"));
        driver.findElement(By.xpath("//div[@id ='hdType']/button")).click();
        WaitElementLoad(By.xpath("//*[@id = 'workTypes']/.//*[@class ='ui-igcombo-buttonicon']"));
        driver.findElement(By.xpath("//*[@id = 'workTypes']/.//*[@class ='ui-igcombo-buttonicon']")).click();
        WaitElementLoad(By.xpath("//ul[@class ='ui-igcombo-listitemholder']/li[@data-value='106']"));
        driver.findElement(By.xpath("//ul[@class ='ui-igcombo-listitemholder']/li[@data-value='106']")).click();
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image"+ i+ ".png"));
        this.uploadTestLink(500, 112);
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