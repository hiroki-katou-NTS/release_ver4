package ksc001;

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
        screenshotPath = "images/ksc001/Scenario1Case1";
        this.init();
    }

    @Test
    public void testCase1() throws Exception {
        // login申請者
        login("020905", "Jinjikoi5");
        // Test case 1-1
        // 初期状態 2020年１月（スケジュール）
        driver.get(domain + "nts.uk.at.web/view/ksu/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2020/01/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2020/01/31");
        threadSleep(1000);
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[@role='tab']")).get(1).click();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("03067");
        driver.findElements(By.className("proceed")).get(8).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft($('.ex-body-detail')[0].scrollWidth)");
        threadSleep(1000);
        screenShot();

        // 初期状態 2020年12月（スケジュール）
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitPageLoad();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2020/12/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2020/12/31");
        threadSleep(1000);
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft($('.ex-body-detail')[0].scrollWidth)");
        threadSleep(1000);
        screenShot();

        // 月間パターン 2020年1月から2020年１２月まで作成
        driver.get(domain + "nts.uk.at.web/view/ksm/005/b/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[@tabindex='3']"));
        driver.findElement(By.xpath("//button[@tabindex='3']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElements(By.className("ntsDatepicker")).get(0).clear();
        driver.findElements(By.className("ntsDatepicker")).get(0).sendKeys("2020/01");
        driver.findElements(By.className("ntsDatepicker")).get(1).clear();
        driver.findElements(By.className("ntsDatepicker")).get(1).sendKeys("2020/12");

        // 稼働日
        driver.findElement(By.xpath("//button[@tabindex='3']")).click();
        WaitPageLoad();
        driver.switchTo().defaultContent();
        driver.switchTo().frame("window_2");
        driver.findElement(By.xpath("//table[@id='list-worktype']/tbody/tr[@data-id='001']")).click();
        driver.findElement(By.xpath("//table[@id='day-list-tbl']/tbody/tr[@data-id='010']")).click();
        driver.findElement(By.xpath("//button[@tabindex='11']")).click();
        threadSleep(1000);

        // 非稼働日（法内）
        driver.switchTo().defaultContent();
        driver.switchTo().frame("window_1");
        driver.findElement(By.xpath("//button[@tabindex='4']")).click();
        WaitPageLoad();
        driver.switchTo().defaultContent();
        driver.switchTo().frame("window_3");
        driver.findElement(By.xpath("//table[@id='list-worktype']/tbody/tr[@data-id='090']")).click();
        driver.findElement(By.xpath("//button[@tabindex='11']")).click();
        threadSleep(1000);

        // 非稼働日（法外）
        driver.switchTo().defaultContent();
        driver.switchTo().frame("window_1");
        driver.findElement(By.xpath("//button[@tabindex='5']")).click();
        WaitPageLoad();
        driver.switchTo().defaultContent();
        driver.switchTo().frame("window_4");
        driver.findElement(By.xpath("//table[@id='list-worktype']/tbody/tr[@data-id='090']")).click();
        driver.findElement(By.xpath("//button[@tabindex='11']")).click();
        threadSleep(1000);

        // 祝日
        driver.switchTo().defaultContent();
        driver.switchTo().frame("window_1");
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        WaitPageLoad();
        driver.switchTo().defaultContent();
        driver.switchTo().frame("window_5");
        driver.findElement(By.xpath("//table[@id='list-worktype']/tbody/tr[@data-id='090']")).click();
        driver.findElement(By.xpath("//button[@tabindex='11']")).click();
        threadSleep(1000);

        //
        screenShot();
        driver.switchTo().defaultContent();
        driver.switchTo().frame("window_1");
        driver.findElement(By.xpath("//button[@tabindex='8']")).click();
        threadSleep(1000);
        driver.switchTo().defaultContent();
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitPageLoad();

        // 作成された月間パターン 2020年1月から2020年１２月まで（その１）
        driver.findElement(By.id("yMPicker")).clear();
        driver.findElement(By.id("yMPicker")).sendKeys("2020/01");
        driver.findElement(By.id("inp_monthlyPatternName")).click();
        threadSleep(2000);
        screenShot();
        //
        driver.findElement(By.id("yMPicker")).clear();
        driver.findElement(By.id("yMPicker")).sendKeys("2020/12");
        driver.findElement(By.id("inp_monthlyPatternName")).click();
        threadSleep(2000);
        screenShot();

        //
        driver.findElement(By.xpath("//table[@id='lstMonthlyPattern']/tbody/tr[@data-id='002']")).click();
        threadSleep(2000);
        driver.findElement(By.xpath("//button[@tabindex='3']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_6");
        driver.findElements(By.className("ntsDatepicker")).get(0).clear();
        driver.findElements(By.className("ntsDatepicker")).get(0).sendKeys("2020/01");
        driver.findElements(By.className("ntsDatepicker")).get(1).clear();
        driver.findElements(By.className("ntsDatepicker")).get(1).sendKeys("2020/12");
        driver.findElement(By.xpath("//button[@tabindex='8']")).click();
        threadSleep(1000);
        driver.switchTo().defaultContent();
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitPageLoad();

        // 作成された月間パターン 2020年1月から2020年１２月まで（その2）
        driver.findElement(By.id("yMPicker")).clear();
        driver.findElement(By.id("yMPicker")).sendKeys("2020/01");
        driver.findElement(By.id("inp_monthlyPatternName")).click();
        threadSleep(2000);
        screenShot();
        //
        driver.findElement(By.id("yMPicker")).clear();
        driver.findElement(By.id("yMPicker")).sendKeys("2020/12");
        driver.findElement(By.id("inp_monthlyPatternName")).click();
        threadSleep(2000);
        screenShot();

        // Test case 1-2
        // KSC001B
        driver.get(domain + "nts.uk.at.web/view/ksc/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[@role='tab']")).get(6).click();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("03067");
        driver.findElements(By.className("proceed")).get(1).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        driver.findElements(By.className("ntsEndDatePicker")).get(4).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(4).sendKeys("2020/12/31");
        driver.findElements(By.className("ntsStartDatePicker")).get(4).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(4).sendKeys("2020/01/01");
        driver.findElement(By.className("periodCovered")).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.className("periodCovered")).click();
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        threadSleep(1000);
        driver.findElement(By.xpath("//button[@tabindex='17']")).click();
        threadSleep(1000);
        driver.findElement(By.xpath("//button[@tabindex='5']")).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.id("buttonFinishPageE")).click();
        threadSleep(1000);
        driver.findElements(By.className("yes")).get(1).click();
        threadSleep(1000);
        driver.findElements(By.className("yes")).get(1).click();
        threadSleep(1000);
        driver.switchTo().frame("window_1");
        WebDriverWait _wait300 = new WebDriverWait(driver, 300);
        _wait300.until(d -> "0/11人".equals(
                d.findElements(By.xpath("//td[@class='valueScheduleSetting']/span[@class='label']")).get(1).getText()));
        screenShot();
        _wait300.until(d -> "完了"
                .equals(d.findElements(By.xpath("//td[@class='valueScheduleSetting']/span")).get(0).getText()));
        screenShot();

        // 初期状態 2020年１月（スケジュール）
        driver.get(domain + "nts.uk.at.web/view/ksu/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2020/01/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2020/01/31");
        threadSleep(1000);
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[@role='tab']")).get(1).click();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("03067");
        driver.findElements(By.className("proceed")).get(8).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft($('.ex-body-detail')[0].scrollWidth)");
        threadSleep(1000);
        screenShot();

        // 初期状態 2020年12月（スケジュール）
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitPageLoad();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2020/12/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2020/12/31");
        threadSleep(1000);
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft($('.ex-body-detail')[0].scrollWidth)");
        threadSleep(1000);
        screenShot();
        
        this.uploadTestLink(79, 14);
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