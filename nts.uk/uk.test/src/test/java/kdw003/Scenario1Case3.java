package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.openqa.selenium.*;

import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario1Case3 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario1Case3";
        this.init();
    }

    @Test
    public void test() throws Exception {
        
        Calendar inputdate = Calendar.getInstance();
        inputdate.add(Calendar.MONTH, 0);
        //login
        login("018234", "Jinjikoi5");

        //KMK012A 処理年月の設定
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        //click 締め日の割付
        driver.findElement(By.id("button12")).click();
        driver.switchTo().frame("window_1");
        WaitElementLoad(By.xpath("//tr[@data-id ='01']"));
        driver.findElement(By.xpath("//tr[@data-id ='01']")).click();
        WaitElementLoad(By.xpath("//div[contains(.,'10日締め')]"));
        driver.findElements(By.xpath("//div[contains(.,'10日締め')]")).get(2).click();
        WaitElementLoad(By.id("btnRegistry"));   
        driver.findElement(By.id("btnRegistry")).click();
        WaitElementLoad(By.xpath("//body"));   
        driver.findElement(By.xpath("//body")).sendKeys(Keys.RETURN);
        WaitElementLoad(By.id("btnClose"));   
        driver.findElement(By.id("btnClose")).click();

        WebElement clsId2 = driver.findElement(By.xpath("//tr[@data-id = '2']"));
        clsId2.click();
        WaitElementLoad(By.id("inpMonth"));   
        driver.findElement(By.id("inpMonth")).click();
        driver.findElement(By.id("inpMonth")).clear();        
        driver.findElement(By.id("inpMonth")).sendKeys(df3.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.className("ui-igcombo-buttonicon")).click();
        WaitElementLoad(By.xpath("//li[@data-value='10']"));
        driver.findElement(By.xpath("//li[@data-value='10']")).isSelected();       
        driver.findElement(By.xpath("//li[@data-value='10']")).click();
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("btn_save")).click();
        
        WaitElementLoad(By.xpath("//button[@class ='large']"));
        driver.findElement(By.xpath("//button[@class ='large']")).click();
        screenShotFull(); 

        //KDW003A 勤務報告書
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        screenShotFull();    
        
        this.uploadTestLink(584, 144);
     
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