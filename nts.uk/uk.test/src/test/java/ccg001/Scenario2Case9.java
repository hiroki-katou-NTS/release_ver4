package ccg001;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario2Case9 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ccg001/Scenario2Case9";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login
        login("020937", "Jinjikoi5");

        //open cps001
        driver.get(domain + "nts.uk.at.web/view/kdr/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.xpath("//a[contains(.,'入力検索')]"));
        screenShot();
        driver.findElement(By.id("ccg001-btn-search-all")).click();
        WaitPageLoad();
        screenShot();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.xpath("//a[contains(.,'入力検索')]"));
        driver.findElement(By.xpath("//a[contains(.,'入力検索')]")).click();
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("0");
        driver.findElements(By.xpath("//button[contains(@class,'proceed caret-bottom pull-right')]")).get(0).click();
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//a[contains(.,'詳細検索')]")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//h3")).get(2).click();
        Thread.sleep(1000);
        driver.findElements(By.xpath("//span[contains(@name,'hchk')]")).get(1).click();
        screenShot();
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();
        WaitPageLoad();
        screenShot();

        this.uploadTestLink(357, 85);
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