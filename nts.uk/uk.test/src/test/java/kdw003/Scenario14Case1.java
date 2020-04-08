package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario14Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario14Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login
        login("018234", "Jinjikoi5");
        // kmk012 change closure 1

        driver.get(domain+ "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("inpMonth")).click();
        driver.findElement(By.id("inpMonth")).clear();
        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).sendKeys("2019/12");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("btn_save"));
        driver.findElement(By.id("btn_save")).click();

        // KDW003A 勤務報告書
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        
         //chon ngay    
        final WebElement startDate = driver.findElement(By.id("daterangepicker"))
            .findElement(By.className("ntsStartDatePicker"));
        startDate.clear();
        startDate.sendKeys("2019/12/01");
        driver.findElement(By.xpath("//body")).click();
        Thread.sleep(1000);
        final WebElement endDate = driver.findElement(By.id("daterangepicker"))
            .findElement(By.className("ntsEndDatePicker"));
        endDate.clear();
        endDate.sendKeys("2019/12/31");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//button[@id='btnExtraction']"));
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();  
        WaitPageLoad();    
        //check khoi dong mac dinh dialog error
        try{
            WebElement dialogError = driver.findElement(By.xpath("//*[contains(.,'エラー・アラーム参照')]"));
            if(dialogError.isDisplayed()){
                driver.switchTo().frame("window_1");
                WaitElementLoad(By.id("dialogClose"));
                driver.findElement(By.id("dialogClose")).click();
                Thread.sleep(1000);

            }
        }catch(Exception ex){

        }
        WaitElementLoad(By.xpath("//li[contains(.,'勤怠シート')]"));
        driver.findElement(By.xpath("//li[contains(.,'勤怠シート')]")).isSelected();
        WaitPageLoad(); 
        selectItemKdw003_2("勤務種類", "12/02(月)").click();
        Thread.sleep(2000);
        driver.switchTo().frame("window_2");
        WaitElementLoad(By.xpath("//tr[@data-id = '002']"));      
        driver.findElement(By.xpath("//tr[@data-id = '002']")).click();  
        WaitElementLoad(By.id("btnSetting"));
        driver.findElement(By.id("btnSetting")).click();            
       
        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();

        //cach trên: 
        js.executeScript("$('.notice-dialog').parent().css(`top`,`379.5px`)");
        Thread.sleep(1000);
        // cach trai:        
        js.executeScript("$('.notice-dialog').parent().css(`left`,`649.5px`)");
        screenShotFull();   

        this.uploadTestLink(926, 230);

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