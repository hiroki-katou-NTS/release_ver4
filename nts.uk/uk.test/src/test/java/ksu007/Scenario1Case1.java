package ksu007;

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
        screenshotPath = "images/ksu007/Scenario1Case1";
        this.init();
    }

    // 1-IW
    @Test
    public void testCase1() throws Exception {
        // login申請者
        login("020905", "Jinjikoi5");

        // Test case 1-1
        // 所定休日を出勤、就業時間帯（IW)に変更して登録
        driver.get(domain + "nts.uk.at.web/view/ksu/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/09/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/09/30");
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
        // select combo-box
        driver.findElement(By.id("combo-box2")).click();
        WaitElementLoad(By.xpath("//li[@data-value='100IW900-1730']"));
        driver.findElement(By.xpath("//li[@data-value='100IW900-1730']")).click();
        threadSleep(100);
        driver.findElement(By.xpath(".//*[@id='extable']/div[5]/table/tbody/tr[8]/td[8]")).click();
        driver.findElement(By.xpath(".//*[@id='extable']/div[5]/table/tbody/tr[9]/td[8]")).click();
        js.executeScript("$('.ex-body-detail').scrollLeft(80)");
        threadSleep(100);
        screenShot();
        driver.findElement(By.id("saveData")).click();
        WaitPageLoad();
        // set closure = 08/2019
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("inpMonth")).clear();
        driver.findElement(By.id("inpMonth")).sendKeys("2019/08");
        driver.findElement(By.id("inpname")).click();
        driver.findElement(By.id("btn_save")).click();
        _wait.until(d -> "登録しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));

        // 就業計算と集計を実施
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
        driver.findElements(By.className("ntsEndDatePicker")).get(4).sendKeys("2019/09/30");
        driver.findElements(By.className("ntsStartDatePicker")).get(4).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(4).sendKeys("2019/09/01");
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
        WebDriverWait _wait300 = new WebDriverWait(driver, 300);
        _wait300.until(d -> (false == d.findElement(By.className("danger")).isEnabled()));
        screenShot();

        // Test case 1-2
        // 実績も出勤、就業時間帯（IW)に変更されている
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(1000);
        driver.findElements(By.xpath("//li[@role='tab']")).get(1).click();
        WaitPageLoad();
        driver.findElement(By.id("WorkplaceList")).click();
        driver.findElements(By.className("ntsSearchBox")).get(1).clear();
        driver.findElements(By.className("ntsSearchBox")).get(1).sendKeys("004323");
        driver.findElements(By.className("search-btn")).get(1).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();
        WaitPageLoad();
        driver.findElement(By.id("cbDisplayFormat")).click();
        threadSleep(100);
        driver.findElements(By.xpath("//div[contains(.,'日付別')]")).get(0).click();
        threadSleep(1000);
        driver.findElements(By.className("ntsStartDatePicker")).get(4).click();
        threadSleep(1000);
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        driver.findElement(By.id("choosenDate")).clear();
        driver.findElement(By.id("choosenDate")).sendKeys("2019/09/08");
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        screenShot();
        // 個人スケジュール一括修正にて、職場＋勤務種別（IW)で社員を抽出
        driver.get(domain + "nts.uk.at.web/view/ksu/007/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(1000);
        driver.findElements(By.xpath("//li[@role='tab']")).get(1).click();
        WaitPageLoad();
        driver.findElement(By.id("WorkplaceList")).click();
        driver.findElements(By.className("ntsSearchBox")).get(1).clear();
        driver.findElements(By.className("ntsSearchBox")).get(1).sendKeys("004323");
        driver.findElements(By.className("search-btn")).get(1).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.xpath("//div[@id='WorkplaceList']/h3")).click();
        driver.findElement(By.id("WorkTypeList")).click();
        threadSleep(1000);
        driver.findElements(By.className("ntsSearchBox")).get(4).clear();
        driver.findElements(By.className("ntsSearchBox")).get(4).sendKeys("0110");
        driver.findElements(By.className("search-btn")).get(4).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();
        WaitPageLoad();
        screenShot();

        // Test case 1-3
        // 個人スケジュール一括修正にて、対象期間（9/8）の勤務方法を（出勤→）「休日出勤」に変更して実行
        driver.findElements(By.xpath("//th[@role='rowheader']")).get(95).click();
        driver.findElements(By.xpath("//th[@role='rowheader']")).get(96).click();
        driver.findElements(By.className("ntsStartDatePicker")).get(3).sendKeys("2019/09/08");
        driver.findElements(By.className("ntsEndDatePicker")).get(3).sendKeys("2019/09/08");
        driver.findElement(By.className("buttonWorkSetting")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        threadSleep(100);
        driver.findElement(By.xpath("//table[@id='list-worktype']/tbody/tr[@data-id='006']")).click();
        threadSleep(100);
        js.executeScript("$('#day-list-tbl_displayContainer').scrollTop($('#day-list-tbl')[0].scrollHeight/2)");
        threadSleep(100);
        driver.findElement(By.xpath("//table[@id='day-list-tbl']/tbody/tr[@data-id='100']")).click();
        threadSleep(100);
        driver.findElement(By.xpath("//button[@tabindex='11']")).click();
        threadSleep(100);
        driver.switchTo().defaultContent();
        screenShot();
        // 実行完了
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_2");
        _wait.until(d -> "完了".equals(d.findElement(By.className("executionStateTime")).getText()));
        screenShot();
        driver.findElement(By.xpath("//button[@tabindex='17']")).click();
        driver.switchTo().defaultContent();

        // Test case 1-5
        // 対象期間（9/15）の勤務方法を（所定休日→）「出勤」に変更して実行
        driver.findElements(By.className("ntsEndDatePicker")).get(3).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(3).sendKeys("2019/09/15");
        driver.findElements(By.className("ntsStartDatePicker")).get(3).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(3).sendKeys("2019/09/15");
        driver.findElement(By.className("buttonWorkSetting")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_3");
        js.executeScript("$('#day-list-tbl_displayContainer').scrollTop($('#day-list-tbl')[0].scrollHeight/2)");
        driver.findElement(By.xpath("//table[@id='day-list-tbl']/tbody/tr[@data-id='100']")).click();
        driver.findElement(By.xpath("//button[@tabindex='11']")).click();
        driver.switchTo().defaultContent();
        screenShot();
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_4");
        _wait.until(d -> "完了".equals(d.findElement(By.className("executionStateTime")).getText()));
        screenShot();
        driver.findElement(By.xpath("//button[@tabindex='17']")).click();
        driver.switchTo().defaultContent();
        // 出勤→「休日出勤」に変更された
        // 所定休日→「出勤」に変更された
        driver.get(domain + "nts.uk.at.web/view/ksu/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/09/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/09/30");
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
        js.executeScript("$('.ex-body-detail').scrollLeft(80)");
        threadSleep(100);
        screenShot();
        // 就業計算と集計を実施
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
        driver.findElements(By.className("ntsEndDatePicker")).get(4).sendKeys("2019/09/30");
        driver.findElements(By.className("ntsStartDatePicker")).get(4).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(4).sendKeys("2019/09/01");
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
        driver.switchTo().frame("window_1");
        _wait300.until(d -> (false == d.findElement(By.className("danger")).isEnabled()));
        screenShot();
        // Test case 1-4
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("cbDisplayFormat")).click();
        threadSleep(100);
        driver.findElements(By.xpath("//div[contains(.,'個人別')]")).get(3).click();
        threadSleep(1000);
        driver.findElement(By.id("choosenDate")).click();
        threadSleep(1000);
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(1000);
        driver.findElements(By.xpath("//li[@role='tab']")).get(2).click();
        threadSleep(100);
        driver.findElement(By.id("ccg001-input-code")).sendKeys("020765");
        driver.findElement(By.xpath(".//*[@id='ccg001-part-g']/div[1]/button")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();

        // Test case 1-6
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(1000);
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("024694");
        driver.findElement(By.xpath(".//*[@id='ccg001-part-g']/div[1]/button")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();

        this.uploadTestLink(202, 49);
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