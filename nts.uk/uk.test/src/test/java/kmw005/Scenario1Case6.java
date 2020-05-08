package kmw005;


import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;


public class Scenario1Case6 extends TestRoot {

    String inpMonth ="inpMonth";//id
    String btnsave = "btn_save";//id
    String locklist ="//*[@id='actualLock-list']/tbody/tr[1]";
    String lock1 ="//*[@id='monthlyActualLock']/button[1]";
    String unlock1 ="//*[@id='monthlyActualLock']/button[2]";
    String closeMsg15="/html/body/div[5]/div[3]/div/button";
    String closeMsg15_ ="/html/body/div[8]/div[3]/div/button";

    


    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kmw005/Scenario1Case6";
        this.init();
    }

    @Test
    public void test() throws Exception {

        //login 024800/Jinjikoi5
        login("024800", "Jinjikoi5");

        //change closure 1
        driver.get(domain+ "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id(inpMonth)).click();
        driver.findElement(By.id(inpMonth)).clear();
        driver.findElement(By.id(inpMonth)).sendKeys("2019/11");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id(btnsave)).click();

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
        // go kdw003 checkbox all

        driver.get(domain+"nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        // tháng 11
        driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsDatePrevButton")).click();
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        driver.findElement(By.id("btn-signAll")).click();
        driver.findElement(By.xpath("//*[@id='function-content']/button[1]")).click();
        WaitPageLoad();
        driver.findElement(By.xpath(closeMsg15_)).click();
        //WaitPageLoad();
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
    
        //login 018234/Jinjikoi5
        login("018234", "Jinjikoi5");

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
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        //WaitPageLoad();

        //login 024800 / Jinjikoi5
        login("024800", "Jinjikoi5");

        // go kmw003 

        driver.get(domain+"nts.uk.at.web/view/kmw/003/a/index.xhtml");
        WaitPageLoad();
        // tháng 11
        // driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsDatePrevButton")).click();
        // driver.findElement(By.id("btnExtraction")).click();
        // WaitPageLoad();
        screenShotFull();
        // done case1-6

        //log out để unlock và đổi closure
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();

         //unlock1
        login("018234", "Jinjikoi5");
        driver.get(domain+ "nts.uk.at.web/view/kmw/005/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath(locklist)).click();
        driver.findElement(By.xpath(unlock1)).click();
        driver.findElement(By.id(btnsave)).click();
        driver.findElement(By.xpath(closeMsg15)).click();
        WaitPageLoad();

         //log out
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();

        //login 024800/Jinjikoi5
        login("024800", "Jinjikoi5");
        //tháng 11 uncheckbox
        driver.get(domain+ "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsDatePrevButton")).click();
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        driver.findElement(By.id("btn-releaseAll")).click();
        driver.findElement(By.xpath("//*[@id='function-content']/button[1]")).click();
        // //comeback closure 
        // driver.get(domain+ "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        // WaitPageLoad();
        // driver.findElement(By.id(inpMonth)).click();
        // driver.findElement(By.id(inpMonth)).clear();
        // driver.findElement(By.id(inpMonth)).sendKeys("2019/11");
        // driver.findElement(By.xpath("//body")).click();
        // driver.findElement(By.id(btnsave)).click();
        // WaitPageLoad();
     
         this.uploadTestLink(799, 182);
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