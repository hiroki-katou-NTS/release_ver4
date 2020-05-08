package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.*;
import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario12Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario12Case2";
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

        //KAF006A 休暇申請
         /**
         *  @param1 = basedate
         */
        driver.get(domain + "nts.uk.at.web/view/kaf/006/a/index.xhtml");
        WaitPageLoad();  
       
        createApplicationKAF006("2019/11/11");
        Thread.sleep(2000);
        createApplicationKAF006("2019/11/12");
        Thread.sleep(2000);
        createApplicationKAF006("2019/11/13");
        Thread.sleep(2000);
        createApplicationKAF006("2019/11/14");
        Thread.sleep(2000);
        createApplicationKAF006("2019/11/15");
       
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

        this.uploadTestLink(1137, 273);

    }
    public void createApplicationKAF006(String inputdate) throws InterruptedException {
        WaitElementLoad(By.xpath("//input[contains(@id,'inputdate')]")); 
        driver.findElement(By.xpath("//input[contains(@id,'inputdate')]")).click();
        WaitElementLoad(By.xpath("//input[contains(@id,'inputdate')]")); 
        driver.findElement(By.xpath("//input[contains(@id,'inputdate')]")).sendKeys(inputdate);
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//button[contains(@class,'nts-switch-button')]"));
        driver.findElements(By.xpath("//button[contains(@class,'nts-switch-button')]")).get(2).click();
        WaitElementLoad(By.xpath("//button[contains(@class,'nts-switch-button')]"));
        WaitPageLoad(); 
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        Thread.sleep(1000);
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