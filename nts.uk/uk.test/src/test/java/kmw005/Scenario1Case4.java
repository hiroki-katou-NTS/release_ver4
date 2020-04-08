package kmw005;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;



public class Scenario1Case4 extends TestRoot {

    String inpMonth ="inpMonth";//id
    String btnsave = "btn_save";//id
    String locklist ="//*[@id='actualLock-list']/tbody/tr[1]";
    String lock1 ="//*[@id='monthlyActualLock']/button[1]";
    String unlock1 ="//*[@id='monthlyActualLock']/button[2]";
    String closeMsg15="/html/body/div[5]/div[3]/div/button";
    


    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kmw005/Scenario1Case4";
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

        // click checkbox KDW006C
        driver.get(domain + "nts.uk.at.web/view/kdw/006/c/index.xhtml");
        WaitPageLoad();
        WebElement a = driver.findElement(By.xpath("//*[@id='checkBox161']"));
        WaitElementLoad(By.xpath("//*[@id='checkBox161']/label/span[1]"));
        if (a.getAttribute("class").indexOf("checked") == -1) {
            driver.findElement(By.id("register-button")).click();
        } else {
            driver.findElement(By.xpath("//*[@id='checkBox161']/label/span[1]")).click();
            driver.findElement(By.id("register-button")).click();
        }
        WaitPageLoad();
        driver.findElement(By.xpath("//body")).click();
        // go kdw003

        driver.get(domain+"nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        // tháng 11
        driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsDatePrevButton")).click();
        driver.findElement(By.id("btnExtraction")).click();
        //WaitPageLoad();
        //driver.findElement(By.xpath("//*[@id='dpGrid']/div[3]/table/tbody/tr[2]/td[2]/span")).click();
        screenShotFull();;// done case1-4

        // //tháng 
        // driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsDateNextButton")).click();
        // driver.findElement(By.id("btnExtraction")).click();
        // WaitPageLoad();
        // screenShot_Full("/image5.png");

         //unlock1
        
         driver.get(domain+ "nts.uk.at.web/view/kmw/005/a/index.xhtml");
         WaitPageLoad();
         driver.findElement(By.xpath(locklist)).click();
         driver.findElement(By.xpath(unlock1)).click();
         driver.findElement(By.id(btnsave)).click();
         WaitPageLoad();
        
        this.uploadTestLink(795, 180);
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