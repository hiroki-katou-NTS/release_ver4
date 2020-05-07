package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.*;
import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario12Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario12Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login
        login("016209", "Jinjikoi5");

        //kmk012 change closure 1
        driver.get(domain+ "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("inpMonth")).click();
        driver.findElement(By.id("inpMonth")).clear();
        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).sendKeys("2019/11");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("btn_save"));
        driver.findElement(By.id("btn_save")).click();

        // //KAF005A 事前申請
        /**
         *  @param1 = basedate
         *  @parame2 = reson 
         */
        driver.get(domain + "nts.uk.at.web/view/kaf/005/a/index.xhtml?overworkatr=2");
        WaitPageLoad();
        
        createApplicationKAF005("2019/11/11", "事前申請 11/11");
        Thread.sleep(2000);
        createApplicationKAF005("2019/11/12", "事前申請 11/12");
        Thread.sleep(2000);
        createApplicationKAF005("2019/11/13", "事前申請 11/13");
        Thread.sleep(2000);
        createApplicationKAF005("2019/11/14", "事前申請 11/14");
        Thread.sleep(2000);
        createApplicationKAF005("2019/11/15", "事前申請 11/15");
       
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        //chon ngay    
        final WebElement startDate = driver.findElement(By.id("daterangepicker"))
            .findElement(By.className("ntsStartDatePicker"));
            startDate.clear();
            startDate.sendKeys("2019/11/01");
            driver.findElement(By.xpath("//body")).click();
            Thread.sleep(1000);
        final WebElement endDate = driver.findElement(By.id("daterangepicker"))
            .findElement(By.className("ntsEndDatePicker"));
        endDate.clear();
        endDate.sendKeys("2019/11/30");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//button[@id='btnExtraction']"));
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();        
        js.executeScript("$('.mgrid-free').scrollLeft(2000)"); 
        screenShotFull();

        this.uploadTestLink(1134, 272);

    }
    public void createApplicationKAF005(String inputdate, String appReason ){
        driver.findElement(By.id("inputdate")).click();
        WaitElementLoad(By.id("inputdate"));
        driver.findElement(By.id("inputdate")).sendKeys(inputdate);
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("inpStartTime1"));
        driver.findElement(By.id("inpStartTime1")).click();
        driver.findElement(By.id("inpStartTime1")).clear();
        WaitElementLoad(By.id("inpStartTime1"));
        driver.findElement(By.id("inpStartTime1")).sendKeys("当日8:00");
        WaitElementLoad(By.xpath("//body"));
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("inpEndTime1"));
        driver.findElement(By.id("inpEndTime1")).click();
        driver.findElement(By.id("inpEndTime1")).clear();
        WaitElementLoad(By.id("inpEndTime1"));
        driver.findElement(By.id("inpEndTime1")).sendKeys("当日19:00");
        driver.findElement(By.xpath("//button[@tabindex='14']")).click();
        WaitPageLoad();
        driver.findElement(By.id("appReason")).click();
        driver.findElement(By.id("appReason")).clear();
        driver.findElement(By.id("appReason")).sendKeys(appReason);
        screenShot();
        driver.findElement(By.xpath("//button[@tabindex='1']")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        screenShot();
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElements(By.xpath("//button")).get(1).click();
        WaitPageLoad();

    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        final String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}