package kaf018;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import common.TestRoot;

public class Scenario2Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kaf018/Scenario2Case1";
        this.init();
    }

    @Test
    public void testCase1() throws Exception {
        // login申請者
        login("010392", "Jinjikoi5");

        // Test case 2-1
        driver.get(domain + "nts.uk.at.web/view/kaf/018/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.className("ntsSearchBox"));
        driver.findElement(By.className("ntsSearchBox")).sendKeys("003130");
        driver.findElement(By.className("search-btn")).click();
        threadSleep(100);
        screenShot();
        driver.findElement(By.className("btn-excute")).click();
        WaitPageLoad();
        screenShot();
        // Test case 2-2
        driver.findElements(By.className("link-button")).get(1).click();
        WaitPageLoad300();
        screenShot();
        // Test case 2-3
        driver.findElements(By.className("x-link")).get(2).click();
        WaitPageLoad300();
        WaitElementLoad(By.id("grid1"));
        driver.switchTo().frame("window_1");
        screenShot();
        // Test case 2-4
        driver.get(domain + "nts.uk.at.web/view/kaf/018/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.xpath(".//*[@id='multiple-tree-grid_headers']/thead/tr/th[1]/span"));
        screenShot();
        driver.findElement(By.className("btn-excute")).click();
        WaitPageLoad300();
        screenShot();

        this.uploadTestLink(262, 55);
    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private void threadSleep(int x) {
        try {
            Thread.sleep(x);
        } catch (Exception e) {
        }
    }

    private void WaitPageLoad300() {
        WebDriverWait _wait300 = new WebDriverWait(driver, 300);
        try {
            Thread.sleep(1000);
            _wait300.until(d -> {
                try {
                    d.findElement(By.xpath("//div[contains(@class,'blockOverlay')]"));
                } catch (Exception e) {
                    return true;
                }
                return false;
            });
        } catch (Exception e) {
        }
    }

    public void WaitElementLoad300(By locator) {
        WebDriverWait _wait300 = new WebDriverWait(driver, 300);
        try {
            Thread.sleep(1000);
            _wait300.until(ExpectedConditions.presenceOfElementLocated(locator));
            _wait300.until(d -> ExpectedConditions.elementToBeClickable(d.findElement(locator)));
        } catch (Exception e) {
        }
    }
}