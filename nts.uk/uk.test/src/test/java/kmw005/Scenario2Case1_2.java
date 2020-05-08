package kmw005;



import org.junit.jupiter.api.*;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;



public class Scenario2Case1_2 extends TestRoot {
    public int randomInt;
    String inpMonth ="inpMonth";//id
    String btnsave = "btn_save";//id
    String locklist ="//*[@id='actualLock-list']/tbody/tr[1]";
    String lock1 ="//*[@id='monthlyActualLock']/button[1]";
    String unlock1 ="//*[@id='monthlyActualLock']/button[2]";
    String closeMsg15="/html/body/div[5]/div[3]/div/button";
   


    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kmw005/Scenario2Case1_2";
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
        WaitElementLoad(By.xpath(closeMsg15));
        screenShotFull();
        driver.findElement(By.xpath(closeMsg15)).click();
        WaitPageLoad();

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
        // tháng 11 change time value
        driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsDatePrevButton")).click();
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        Random rd = new Random();
        randomInt = rd.nextInt((1959 - 1900) + 1) + 1900;
        String value = String.valueOf(randomInt);
        selectItemKdw003_2("退勤時刻1", "11/05(火)").click();
        selectItemKdw003_2("退勤時刻1", "11/05(火)").sendKeys(value);
        driver.findElement(By.xpath("//body")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//*[@id='function-content']/button[1]")).click();
        WaitPageLoad();
        Thread.sleep(2000);
        screenShotFull();
        // done case2-1

         //unlock1
        driver.get(domain+ "nts.uk.at.web/view/kmw/005/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath(locklist)).click();
        driver.findElement(By.xpath(unlock1)).click();
        driver.findElement(By.id(btnsave)).click();
        driver.findElement(By.xpath(closeMsg15)).click();
        WaitPageLoad();

        //tháng 11 return value 17:30
        driver.get(domain+ "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsDatePrevButton")).click();
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        selectItemKdw003_2("退勤時刻1", "11/05(火)").click();
        selectItemKdw003_2("退勤時刻1", "11/05(火)").sendKeys("17:30");
        driver.findElement(By.xpath("//body")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//*[@id='function-content']/button[1]")).click();

        this.uploadTestLink(801, 183);
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