package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario10Case3 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario10Case3";
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
        driver.findElement(By.id("inpMonth")).sendKeys("2019/10");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("btn_save"));
        driver.findElement(By.id("btn_save")).click();

        // KDW003A 勤務報告書
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
       
        driver.findElement(By.xpath("//li[contains(.,'勤怠シート')]")).isSelected();
        WaitPageLoad();
        WebElement el1 = driver.findElement(By.xpath("//td[contains(.,'11/05')]"));
        if (!"true".equals(el1.findElements(By.xpath("following::td")).get(0).findElement(By.xpath("label/input")).getAttribute("checked")) ) {
            el1.findElements(By.xpath("following::td")).get(0).click();
            
        } 

        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad(); 
        screenShotFull();   

        this.uploadTestLink(812, 187);

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