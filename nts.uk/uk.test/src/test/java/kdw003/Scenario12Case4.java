package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.*;
import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario12Case4 extends TestRoot {
   

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario12Case4";
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

        //KAF007A 休暇・休出 取消申請     
        /**
         *  @param1 = basedate
         *  @parame2 = reson 
         */  
        createAppKAF007("2019/11/01","11/1 休暇・休出 取消申請");
        Thread.sleep(2000);
        createAppKAF007("2019/11/02","11/2 休暇・休出 取消申請");
        Thread.sleep(2000);
        createAppKAF007("2019/11/03","11/3 休暇・休出 取消申請");
        Thread.sleep(2000);
        createAppKAF007("2019/11/04","11/4 休暇・休出 取消申請");
        Thread.sleep(2000);
        createAppKAF007("2019/11/05","11/5 休暇・休出 取消申請");
       
        //KDW003A 勤務報告書
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
        js.executeScript("$('.mgrid-free').scrollLeft(2000)"); 
        
        screenShotFull();
        this.uploadTestLink(1143, 275);

    }

    public void createAppKAF007(final String inputdate, final String inpReasonTextarea) throws InterruptedException {
        driver.get(domain + "nts.uk.at.web/view/kaf/007/a/index.xhtml");
        WaitPageLoad();  
        WaitElementLoad(By.id("singleDate"));
        driver.findElement(By.id("singleDate")).sendKeys(inputdate);
        driver.findElement(By.xpath("//body")).click();
        // inpReasonTextarea
        WaitElementLoad(By.id("inpReasonTextarea"));
        driver.findElement(By.id("inpReasonTextarea")).sendKeys(inpReasonTextarea);
        Thread.sleep(2000);
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//button[contains(@class,'proceed')]"));
        driver.findElements(By.xpath("//button[contains(@class,'proceed')]")).get(0).click();
        // 情報 close dang ky neu co

        try {
            WebElement msgError = driver.findElement(By.xpath("//*[contains(.,'エラー')]"));
            if (msgError.isDisplayed()) {
                WaitElementLoad(By.xpath("//button[@class ='large']"));
                driver.findElement(By.xpath("//button[@class ='large']")).click();
                
                driver.findElement(By.id("workSelect-kaf007")).click();
                WaitPageLoad();
                driver.switchTo().frame("window_1");
                WaitElementLoad(By.xpath("//tr[@data-id = '090']"));
                driver.findElement(By.xpath("//tr[@data-id = '090']")).click();
                WaitElementLoad(By.className("x-large"));
                driver.findElement(By.className("x-large")).click();
                // ntsCheckBox-label
                WaitPageLoad();
                js.executeScript("$('.ntsCheckBox-label').get(2).click()");
                WaitElementLoad(By.xpath("//button[contains(@class,'proceed')]"));
                driver.findElements(By.xpath("//button[contains(@class,'proceed')]")).get(0).click();
                Thread.sleep(1000);
            }
        } catch (final Exception ex) {
            System.out.println("Khong co error" + ex.getMessage());
        }
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