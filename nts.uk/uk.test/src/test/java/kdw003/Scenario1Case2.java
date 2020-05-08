package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.openqa.selenium.*;

import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario1Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario1Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {
        
        Calendar inputdate = Calendar.getInstance();
        inputdate.add(Calendar.MONTH, -1);

        //login
        login("018234", "Jinjikoi5");

        //KMK012A 処理年月の設定
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        WebElement clsId1 = driver.findElement(By.xpath("//tr[@data-id = '1']"));
        clsId1.click();
        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).click();
        driver.findElement(By.id("inpMonth")).clear();
        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).sendKeys(df3.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.className("ui-igcombo-buttonicon"));
        driver.findElement(By.className("ui-igcombo-buttonicon")).click();
        driver.findElement(By.xpath("//li[@data-value='0']")).isSelected();
        WaitElementLoad(By.xpath("//li[@data-value='0']"));
        driver.findElement(By.xpath("//li[@data-value='0']")).click();
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("btn_save"));
        driver.findElement(By.id("btn_save")).click();
        WaitElementLoad(By.xpath("//button[@class ='large']"));
        driver.findElement(By.xpath("//button[@class ='large']")).click();
        Thread.sleep(1000);
        screenShotFull(); 

        //KDW003A 勤務報告書
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        screenShotFull();    
        
        this.uploadTestLink(581, 143);
     
    }

    @AfterEach
    public void tearDown() throws Exception {
        //driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}