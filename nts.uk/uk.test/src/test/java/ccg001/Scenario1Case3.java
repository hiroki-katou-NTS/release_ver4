package ccg001;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario1Case3 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ccg001/Scenario1Case3";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login
        login("015046", "Jinjikoi5");

        //open cps001
        driver.get(domain + "nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.xpath("//a[contains(.,'詳細検索')]"));
        screenShot();
        driver.findElement(By.xpath("//a[contains(.,'詳細検索')]")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//h3")).get(2).click();
        Thread.sleep(1000);
        screenShot();
        driver.findElement(By.xpath("//a[contains(.,'入力検索')]")).click();
        WaitPageLoad();
        screenShot();

        this.uploadTestLink(282, 58);
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