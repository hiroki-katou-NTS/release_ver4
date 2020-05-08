package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.*;
import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario12Case5 extends TestRoot {
   

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario12Case5";
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

        //KAF011A 振休振出申請
        /**
         *  @param1 = basedate workday
         *  @parame2 = basedate offday
         */
        createAppKAF011("2019/11/16","2019/11/18");
        Thread.sleep(2000);
        createAppKAF011("2019/11/17","2019/11/19");
        Thread.sleep(2000);
        createAppKAF011("2019/11/22","2019/11/20");
        Thread.sleep(2000);
        createAppKAF011("2019/11/23","2019/11/21");
        Thread.sleep(2000);
        createAppKAF011("2019/11/24","2019/11/25");
       
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
        WaitPageLoad();    
        js.executeScript("$('.mgrid-free').scrollTop(250)");    
        Thread.sleep(2000);
        js.executeScript("$('.mgrid-free').scrollLeft(2000)");         
        screenShotFull();

        this.uploadTestLink(1146, 276);

    }

    public void createAppKAF011(String recDatePicker, String absDatePicker) throws InterruptedException {
        //KAF011 振休振出申請
        driver.get(domain + "nts.uk.at.web/view/kaf/011/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("recDatePicker")).click();
        WaitElementLoad(By.id("recDatePicker"));
        driver.findElement(By.id("recDatePicker")).sendKeys(recDatePicker);
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("recDatePicker"));
        driver.findElement(By.xpath("//input[@id='absDatePicker']")).click();
        WaitElementLoad(By.xpath("//input[@id='absDatePicker']"));
        driver.findElement(By.xpath("//input[@id='absDatePicker']")).sendKeys(absDatePicker);
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//textarea[@id='appReason']"));
        driver.findElement(By.xpath("//textarea[@id='appReason']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//textarea[@id='appReason']")).clear();
        WaitElementLoad(By.xpath("//textarea[@id='appReason']"));
        driver.findElement(By.xpath("//textarea[@id='appReason']")).sendKeys("autotest");
        WaitElementLoad(By.xpath("//span[@class='box']"));
        WebElement checkbox = driver.findElement(By.xpath("//span[@class='box']"));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        driver.findElement(By.xpath("//button[@class='large']")).click();
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