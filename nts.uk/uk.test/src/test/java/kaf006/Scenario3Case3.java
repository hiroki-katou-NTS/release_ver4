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


public class Scenario3Case3 extends Kaf006Common{
    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kaf006/Scenario3Case3";
        this.init();
        employeeCode = "025445";
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
        driver.get(kaf006);
        WaitPageLoad();
        driver.findElement(By.xpath("//table[@id ='tbl']/.//span[@class ='box']")).click();
        Calendar inputdate = Calendar.getInstance();
        inputdate.clear();
        inputdate.set(2019, 11, 19);
        WebElement wEleStart = driver.findElements(By.xpath("//input[contains(@class,'ntsStartDatePicker ')]")).get(0);
        wEleStart.clear();
        wEleStart.sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        inputdate.clear();
        inputdate.set(2019, 11, 20);
        WebElement wEleEnd = driver.findElements(By.xpath("//input[contains(@class,'ntsEndDatePicker')]")).get(0);
        wEleEnd.clear();
        wEleEnd.sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();
        
        WaitElementLoad(By.xpath("//div[@id ='hdType']/button[2]"));
        driver.findElement(By.xpath("//div[@id ='hdType']/button[2]")).click();
        WaitElementLoad(By.xpath("//*[@id = 'workTypes']/.//*[@class ='ui-igcombo-buttonicon']"));
        driver.findElement(By.xpath("//*[@id = 'workTypes']/.//*[@class ='ui-igcombo-buttonicon']")).click();
        WaitElementLoad(By.xpath("//ul[@class ='ui-igcombo-listitemholder']/li[@data-value='120']"));
        driver.findElement(By.xpath("//ul[@class ='ui-igcombo-listitemholder']/li[@data-value='120']")).click();
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image"+ i+ ".png"));
        this.uploadTestLink(516, 120);
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