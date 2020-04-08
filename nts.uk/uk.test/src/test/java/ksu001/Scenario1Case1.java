package ksu001;

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
        screenshotPath = "images/ksu001/Scenario1Case1";
        this.init();
    }

    @Test
    public void testCase1() throws Exception {
        // login申請者
        login("020905", "Jinjikoi5");
        // Test case 1-1
        // 初期状態（スケジュール）
        driver.get(domain + "nts.uk.at.web/view/ksu/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/12/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/12/31");
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElement(By.id("WorkplaceList")).click();
        threadSleep(100);
        driver.findElements(By.className("ntsSearchBox")).get(1).clear();
        driver.findElements(By.className("ntsSearchBox")).get(1).sendKeys("004323");
        driver.findElements(By.className("search-btn")).get(1).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();
        WaitPageLoad();
        screenShot();

        // KDW003
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/12/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/12/31");
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[@role='tab']")).get(2).click();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("006205");
        driver.findElements(By.className("proceed")).get(2).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();

        // test case 1-2
        // 任意の変更
        driver.get(domain + "nts.uk.at.web/view/ksu/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/12/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/12/31");
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElement(By.id("WorkplaceList")).click();
        threadSleep(100);
        driver.findElements(By.className("ntsSearchBox")).get(1).clear();
        driver.findElements(By.className("ntsSearchBox")).get(1).sendKeys("004323");
        driver.findElements(By.className("search-btn")).get(1).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();
        WaitPageLoad();
        driver.findElement(By.id("combo-box1")).click();
        WaitElementLoad(By.xpath("//li[@data-value='103']"));
        driver.findElement(By.xpath("//li[@data-value='103']")).click();
        threadSleep(100);
        driver.findElement(By.xpath(".//*[@id='extable']/div[5]/table/tbody/tr[2]/td[2]")).click();
        threadSleep(100);
        screenShot();
        driver.findElement(By.id("saveData")).click();
        WaitPageLoad();
        //
        driver.get(domain + "nts.uk.at.web/view/kdw/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(1000);
        driver.findElements(By.xpath("//li[@role='tab']")).get(4).click();
        WaitPageLoad();
        driver.findElement(By.id("WorkplaceList")).click();
        driver.findElements(By.className("ntsSearchBox")).get(0).clear();
        driver.findElements(By.className("ntsSearchBox")).get(0).sendKeys("004323");
        driver.findElements(By.className("search-btn")).get(0).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();
        WaitPageLoad();
        driver.findElements(By.className("ntsEndDatePicker")).get(4).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(4).sendKeys("2019/12/31");
        driver.findElements(By.className("ntsStartDatePicker")).get(4).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(4).sendKeys("2019/12/01");
        driver.findElement(By.id("button22")).click();
        threadSleep(1000);
        driver.findElements(By.xpath("//button[contains(.,'再作成')]")).get(0).click();
        WaitElementLoad(By.id("radioArea"));
        driver.findElement(By.xpath("//span[contains(.,'もう一度作り直す')]")).click();
        driver.findElement(By.id("button6")).click();
        threadSleep(1000);
        driver.findElements(By.className("yes")).get(1).click();
        threadSleep(1000);
        driver.findElement(By.id("button113")).click();
        threadSleep(1000);
        // 就業計算を集計で実績の再作成
        driver.switchTo().frame("window_1");
        WebDriverWait _wait600 = new WebDriverWait(driver, 600);
        _wait600.until(d -> (false == d.findElement(By.className("danger")).isEnabled()));
        screenShot();

        // test case 1-3
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/12/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/12/31");
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[@role='tab']")).get(2).click();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("006205");
        driver.findElements(By.className("proceed")).get(2).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();

        this.uploadTestLink(100, 21);
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
}