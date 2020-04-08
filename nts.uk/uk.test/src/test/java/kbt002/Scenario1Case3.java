package kbt002;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario1Case3 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kbt002/Scenario1Case3";
        this.init();   
    }

    @Test
    public void test() throws Exception {
        
        //login
        login("006638", "Jinjikoi5");

        //KBT002F
        driver.get(domain + "nts.uk.at.web/view/kbt/002/f/index.xhtml");
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//button[@data-value=11 and contains(.,'即時実行')]")).click();
        WaitPageLoad();
        screenShot();
        this.uploadTestLink(1263, 307);
        
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