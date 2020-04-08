package ccg001;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario2Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ccg001/Scenario2Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login
        login("020905", "Jinjikoi5");

        //open cps001
        driver.get(domain + "nts.uk.com.web/view/cps/001/a/index.xhtml");
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
        js.executeScript("$($(\"div[id*='scrollContainer']\")[1]).scrollTop(120200)");
        screenShot();
        driver.findElement(By.xpath("//a[contains(.,'詳細検索')]")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//h3")).get(2).click();
        Thread.sleep(1000);
        screenShot();
        driver.findElement(By.xpath("//td[contains(.,'003430')]/span")).click();
        driver.findElement(By.xpath("//td[contains(.,'001842')]/span")).click();
        driver.findElement(By.xpath("//td[contains(.,'001848')]/span")).click();
        driver.findElement(By.xpath("//td[contains(.,'004117')]/preceding-sibling::th/span[2]")).click();
        driver.findElement(By.xpath("//td[contains(.,'001842')]/preceding-sibling::th/span[2]")).click();
        driver.findElement(By.xpath("//td[contains(.,'003315')]/preceding-sibling::th/span[2]")).click();
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();
        WaitPageLoad();
        screenShot();

        this.uploadTestLink(341, 77);
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