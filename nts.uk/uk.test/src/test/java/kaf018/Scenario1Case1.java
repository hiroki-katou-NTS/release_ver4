package kaf018;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import common.TestRoot;

public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kaf018/Scenario1Case1";
        this.init();
    }

    @Test
    public void testCase1() throws Exception {
        // login申請者
        login("020905", "Jinjikoi5");
        // Test case 1-1, 1-2
        driver.get(domain + "nts.uk.at.web/view/kaf/018/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.className("ntsSearchBox")).sendKeys("003429");
        driver.findElement(By.className("search-btn")).click();
        threadSleep(100);
        driver.findElement(By.xpath("//span[contains(.,'日別・月別承認')]")).click();
        threadSleep(100);
        driver.findElement(By.className("btn-excute")).click();
        WaitPageLoad();
        screenShot();
        driver.findElements(By.className("link-button")).get(1).click();
        WaitPageLoad();
        screenShot();
        // KDW003
        // 004515
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(100);
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/11/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/11/30");
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[@role='tab']")).get(2).click();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("004515");
        driver.findElements(By.className("proceed")).get(2).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad300();
        screenShot();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        threadSleep(100);
        screenShot();
        // 006310
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("006310");
        driver.findElement(By.xpath(".//*[@id='ccg001-part-g']/div[1]/button")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        threadSleep(100);
        screenShot();
        // 007102
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("007102");
        driver.findElement(By.xpath(".//*[@id='ccg001-part-g']/div[1]/button")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        threadSleep(100);
        screenShot();
        // 009173
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("009173");
        driver.findElement(By.xpath(".//*[@id='ccg001-part-g']/div[1]/button")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        threadSleep(100);
        screenShot();
        // 012439
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("012439");
        driver.findElement(By.xpath(".//*[@id='ccg001-part-g']/div[1]/button")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        threadSleep(100);
        screenShot();
        // 012461
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("012461");
        driver.findElement(By.xpath(".//*[@id='ccg001-part-g']/div[1]/button")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        threadSleep(100);
        screenShot();
        // 013232
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("013232");
        driver.findElement(By.xpath(".//*[@id='ccg001-part-g']/div[1]/button")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        threadSleep(100);
        screenShot();
        // 016109
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("016109");
        driver.findElement(By.xpath(".//*[@id='ccg001-part-g']/div[1]/button")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        threadSleep(100);
        screenShot();
        // 016470
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("016470");
        driver.findElement(By.xpath(".//*[@id='ccg001-part-g']/div[1]/button")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        threadSleep(100);
        screenShot();
        // 017267
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("017267");
        driver.findElement(By.xpath(".//*[@id='ccg001-part-g']/div[1]/button")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        threadSleep(100);
        screenShot();
        // 027166
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("027166");
        driver.findElement(By.xpath(".//*[@id='ccg001-part-g']/div[1]/button")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        threadSleep(100);
        screenShot();

        // Test case 1-3
        // logout
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();
        driver.findElement(By.id("password-input")).sendKeys("Jinjikoi5");
        driver.findElement(By.id("employee-code-inp")).clear();
        driver.findElement(By.id("employee-code-inp")).sendKeys("004515");
        screenShot();
        driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();
        driver.get(domain + "nts.uk.at.web/view/kmw/003/a/index.xhtml?initmode=2");
        WaitPageLoad();
        screenShot();
        // Test case 1-4
        // logout
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();

        driver.findElement(By.id("password-input")).sendKeys("Jinjikoi5");
        driver.findElement(By.id("employee-code-inp")).clear();
        driver.findElement(By.id("employee-code-inp")).sendKeys("020905");
        driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();
        driver.get(domain + "nts.uk.at.web/view/kaf/018/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath(".//*[@id='multiple-tree-grid_headers']/thead/tr/th[1]/span")).click();
        threadSleep(100);
        driver.findElement(By.xpath("//span[contains(.,'日別・月別承認')]")).click();
        screenShot();
        driver.findElement(By.className("btn-excute")).click();
        WaitPageLoad300();
        screenShot();
        //
        driver.findElement(By.className("goback")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//span[contains(.,'未確認データがある職場のみ抽出')]")).click();
        WaitElementLoad(By.xpath(".//*[@id='multiple-tree-grid_headers']/thead/tr/th[1]/span"));
        screenShot();
        driver.findElement(By.className("btn-excute")).click();
        WaitPageLoad300();
        WaitElementLoad(By.className("goback"));
        screenShot();

        this.uploadTestLink(259, 54);
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
}