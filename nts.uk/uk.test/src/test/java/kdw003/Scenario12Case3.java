
package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.*;
import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario12Case3 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario12Case3";
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

        //KAF010A 休日出勤申請
          /**
         *  @param1 = basedate
         */
        driver.get(domain + "nts.uk.at.web/view/kaf/010/a/index.xhtml");
        WaitPageLoad();        
        createApplicationKAF010("2019/11/02");
        Thread.sleep(2000);
        createApplicationKAF010("2019/11/03");
        Thread.sleep(2000);
        createApplicationKAF010("2019/11/04");
        Thread.sleep(2000);
        createApplicationKAF010("2019/11/05");
        Thread.sleep(2000);
        createApplicationKAF010("2019/11/06");
       
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

        this.uploadTestLink(1140, 274);

    }
    public void createApplicationKAF010(String inputdate) throws InterruptedException {
        
        driver.findElement(By.xpath("//input[contains(@id,'inputdate')]")).click();   
        WaitElementLoad(By.id("inputdate"));      
        driver.findElement(By.id("inputdate")).sendKeys(inputdate);
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("inpStartTime1"));  
        driver.findElement(By.id("inpStartTime1")).click();
        driver.findElement(By.id("inpStartTime1")).clear();
        WaitElementLoad(By.id("inpStartTime1"));  
        driver.findElement(By.id("inpStartTime1")).sendKeys("当日8:00");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("inpEndTime1"));  
        driver.findElement(By.id("inpEndTime1")).click();
        driver.findElement(By.id("inpEndTime1")).clear();
        WaitElementLoad(By.id("inpEndTime1"));  
        driver.findElement(By.id("inpEndTime1")).sendKeys("当日18:00");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.xpath("//button[@class='caret-bottom']")).click();
        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();        
        WaitElementLoad(By.xpath("//button[@class='large']"));
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitPageLoad();
       try {
        driver.switchTo().frame("window_1");
        Thread.sleep(1000);
        driver.findElements(By.xpath("//button")).get(1).click();
        WaitPageLoad();
       } catch (Exception ex) {
          System.out.println("Khong cai dat gui mai tu dong" +ex.getMessage());
       }
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