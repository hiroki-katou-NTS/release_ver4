package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario9Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario9Case2";
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
        js.executeScript("$('.mgrid-free').scrollTop(300)");  
        setValueGrid(19, 8, "19:05");
        Thread.sleep(1000);
        setValueGrid(20, 8, "17:45");
        Thread.sleep(1000);
        setValueGrid(21, 8, "18:55");
        Thread.sleep(700);
        setValueGrid(22, 8, "19:44");
        Thread.sleep(1000);
        setValueGrid(23, 8, "20:12");
        Thread.sleep(500);
        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad(); 
        screenShotFull();   

        this.uploadTestLink(772, 174);

    }

    public void setValueGrid(int rowNumber, int columnNumber, String value){
        if(value.isEmpty()){
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]")).get(0).click();
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]")).get(0).click();
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]"+"/.//input")).get(0).clear();
        }else{
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]")).get(0).click();
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]")).get(0).sendKeys(value);
        }
        driver.findElement(By.xpath("//body")).click();
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