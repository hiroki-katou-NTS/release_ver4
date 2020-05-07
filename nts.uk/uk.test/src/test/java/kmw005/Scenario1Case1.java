package kmw005;


import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario1Case1 extends TestRoot {
    String inpMonth ="inpMonth";//id
    String btnsave = "btn_save";//id
    String locklist ="//*[@id='actualLock-list']/tbody/tr[1]";
    String lock1 ="//*[@id='dailyActualLock']/button[1]";
    String unlock1 ="//*[@id='dailyActualLock']/button[2]";
    String closeMsg15="/html/body/div[5]/div[3]/div/button";
    


    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kmw005/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {

        //login 018234/Jinjikoi5
        login("018234", "Jinjikoi5");

        //change closure 1
        driver.get(domain+ "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id(inpMonth)).click();
        driver.findElement(By.id(inpMonth)).clear();
        driver.findElement(By.id(inpMonth)).sendKeys("2019/11");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id(btnsave)).click();

        //go kmw005
        driver.get(domain+ "nts.uk.at.web/view/kmw/005/a/index.xhtml");
        WaitPageLoad();
        //lock 1
        driver.findElement(By.xpath(locklist)).click();
        driver.findElement(By.xpath(lock1)).click();
        driver.findElement(By.id(btnsave)).click();
        driver.findElement(By.xpath(closeMsg15)).click();
        WaitPageLoad();
        screenShotFull();

        // go kdw003

        driver.get(domain+"nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        // 
        driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsDatePrevButton")).click();
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//*[@id='dpGrid']/div[3]/table/tbody/tr[2]/td[2]/span")).click();
        WaitPageLoad();
        screenShotFull();
        //
        driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsDateNextButton")).click();
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        screenShotFull();// done case1-1

         //unlock1
        
         driver.get(domain+ "nts.uk.at.web/view/kmw/005/a/index.xhtml");
         WaitPageLoad();
         driver.findElement(By.xpath(locklist)).click();
         driver.findElement(By.xpath(unlock1)).click();
         driver.findElement(By.id(btnsave)).click();
         WaitPageLoad();
        
         this.uploadTestLink(789, 177);
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