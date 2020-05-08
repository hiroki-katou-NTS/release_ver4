package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;

public class Scenario15Case3 extends TestRoot {

    String inpMonth = "inpMonth";// id
    String btnsave = "btn_save";// id

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario15Case3";
        this.init();
    }

    @Test
    public void test() throws Exception {

         //login 000001/0
         login("000001", "0");

        // change closure 1
        driver.get(domain+ "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id(inpMonth)).click();
        driver.findElement(By.id(inpMonth)).clear();
        driver.findElement(By.id(inpMonth)).sendKeys("2019/11");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id(btnsave)).click();


        // go kdw003/ tháng 10
        driver.get(domain+"nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        if (driver.findElements(By.xpath("//iframe[@name='window_1']")).size() !=0) {
            driver.switchTo().frame("window_1");
            WaitElementLoad(By.id("dialogClose"));
            driver.findElement(By.id("dialogClose")).click();
            driver.findElement(By.xpath("//button[contains(.,'ロックを解除')]")).click();
        } else {
            driver.findElement(By.xpath("//button[contains(.,'ロックを解除')]")).click();
        }
        WaitElementLoad(By.xpath("//button[contains(.,'はい')]"));
        driver.findElement(By.xpath("//button[contains(.,'はい')]")).click();
        WaitPageLoad();

        if (driver.findElements(By.xpath("//iframe[@name='window_1']")).size() !=0) {
            driver.switchTo().frame("window_1");
            WaitElementLoad(By.id("dialogClose"));
            driver.findElement(By.id("dialogClose")).click();
            driver.findElements(By.xpath("//button[contains(@class,'ntsDatePrevButton')]")).get(1).click();
        } else {
            driver.findElements(By.xpath("//button[contains(@class,'ntsDatePrevButton')]")).get(1).click();
        }
        driver.findElements(By.xpath("//button[contains(@class,'ntsDatePrevButton')]")).get(1).click();
        WaitElementLoad(By.id("btnExtraction"));
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();


        if (driver.findElements(By.xpath("//iframe[@name='window_1']")).size() !=0) {
            driver.switchTo().frame("window_1");
            WaitElementLoad(By.id("dialogClose"));
            driver.findElement(By.id("dialogClose")).click();
            driver.findElement(By.xpath("//button[contains(.,'再ロック')]")).click();
        } else {
            driver.findElement(By.xpath("//button[contains(.,'再ロック')]")).click();
        }
        WaitElementLoad(By.xpath("//button[contains(.,'閉じる')]"));
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();// lỗi
        WaitPageLoad();
        screenShotFull();

        this.uploadTestLink(1001, 250);
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