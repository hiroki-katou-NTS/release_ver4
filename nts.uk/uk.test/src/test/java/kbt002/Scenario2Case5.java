package kbt002;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario2Case5 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kbt002/Scenario2Case5";
        this.init();   
    }

    @Test
    public void test() throws Exception {
        
        //login
        login("006638", "Jinjikoi5");

        //KBT002F
        driver.get(domain + "nts.uk.at.web/view/kbt/002/f/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@data-value=12 and contains(.,'詳細')]")).click();
        WaitPageLoad();
        screenShot();
        driver.switchTo().frame("window_1");
        WebElement el = driver.findElements(By.xpath("//button[contains(.,'業務エラー内容')]")).stream()
            .filter(el1 -> el1.isEnabled()).findFirst().orElse(null);
        if (null != el) {
            el.click();
        }
        WaitPageLoad();
        screenShot();
        this.uploadTestLink(1278, 312);
        
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