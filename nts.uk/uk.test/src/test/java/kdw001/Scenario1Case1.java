package kdw001;

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
        screenshotPath = "images/kdw001/Scenario1Case1";
        this.init();
    }

    @Test
    public void testCase1() throws Exception {
        // login 申請者
        login("020905", "Jinjikoi5");
        // CPS001
        // 031100
        driver.get(domain + "nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(1000);
        driver.findElements(By.xpath("//li[@role='tab']")).get(2).click();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("031100");
        threadSleep(1000);
        driver.findElements(By.className("proceed")).get(2).click();
        WaitPageLoad();
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(1).click();
        WaitPageLoad();
        driver.findElement(By.xpath(".//*[@id='lefttabs']/ul/li[2]")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        driver.findElement(By.xpath("//li[contains(.,'勤務種別')]")).click();
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//div[@title='勤務種別CD']")).click();
        driver.findElement(By.xpath("//li[contains(.,'010')]")).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.xpath(".//*[@id='functions-area']/button[1]")).click();
        _wait.until(d -> "登録しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        WaitPageLoad();
        // 031110
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("031110");
        threadSleep(1000);
        driver.findElements(By.className("proceed")).get(2).click();
        WaitPageLoad();
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(1).click();
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//div[@title='勤務種別CD']")).click();
        driver.findElement(By.xpath("//li[contains(.,'0110')]")).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.xpath(".//*[@id='functions-area']/button[1]")).click();
        _wait.until(d -> "登録しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        WaitPageLoad();
        // 031085
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("031085");
        threadSleep(1000);
        driver.findElements(By.className("proceed")).get(2).click();
        WaitPageLoad();
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(1).click();
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//div[@title='勤務種別CD']")).click();
        driver.findElement(By.xpath("//li[contains(.,'0130')]")).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.xpath(".//*[@id='functions-area']/button[1]")).click();
        _wait.until(d -> "登録しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        // KSU001
        driver.get(domain + "nts.uk.at.web/view/ksu/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/08/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/08/31");
        threadSleep(1000);
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[@role='tab']")).get(1).click();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("031");
        driver.findElements(By.className("proceed")).get(8).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//span[@name='hchk']")).get(6).click();
        js.executeScript("arguments[0].scrollTo(0, 650)",
                driver.findElements(By.xpath("//div[contains(@id,'_scrollContainer')]")).get(5));
        driver.findElement(By.xpath("//tr[@data-id='031100']/th")).click();
        driver.findElement(By.xpath("//tr[@data-id='031110']/th")).click();
        driver.findElement(By.xpath("//tr[@data-id='031085']/th")).click();
        threadSleep(1000);
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft($('.ex-body-detail')[0].scrollWidth)");
        threadSleep(1000);
        screenShot();
        //
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitPageLoad();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/12/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/12/31");
        threadSleep(1000);
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft($('.ex-body-detail')[0].scrollWidth)");
        threadSleep(1000);
        screenShot();
        // set closure = 08/2019
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("inpMonth")).clear();
        driver.findElement(By.id("inpMonth")).sendKeys("2019/08");
        driver.findElement(By.id("inpname")).click();
        driver.findElement(By.id("btn_save")).click();
        _wait.until(d -> "登録しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        // KSC001
        driver.get(domain + "nts.uk.at.web/view/ksc/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[@role='tab']")).get(6).click();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("031");
        driver.findElements(By.className("proceed")).get(1).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//span[@name='hchk']")).get(1).click();
        js.executeScript("arguments[0].scrollTo(0, 650)",
                driver.findElements(By.xpath("//div[contains(@id,'_scrollContainer')]")).get(1));
        driver.findElement(By.xpath("//tr[@data-id='031100']/th")).click();
        driver.findElement(By.xpath("//tr[@data-id='031110']/th")).click();
        driver.findElement(By.xpath("//tr[@data-id='031085']/th")).click();
        threadSleep(1000);
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        driver.findElements(By.className("ntsEndDatePicker")).get(4).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(4).sendKeys("2019/12/31");
        driver.findElements(By.className("ntsStartDatePicker")).get(4).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(4).sendKeys("2019/08/01");
        driver.findElement(By.className("periodCovered")).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        threadSleep(1000);
        driver.findElement(By.xpath("//button[contains(.,'再作成')]")).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.xpath("//button[@tabindex='17']")).click();
        threadSleep(1000);
        driver.findElement(By.xpath("//button[@tabindex='5']")).click();
        threadSleep(1000);
        driver.findElement(By.id("buttonFinishPageE")).click();
        WaitElementLoad(By.xpath("//button[contains(.,'はい')]"));
        driver.findElements(By.className("yes")).get(1).click();
        threadSleep(1000);
        driver.findElements(By.className("yes")).get(1).click();
        threadSleep(1000);
        driver.switchTo().frame("window_1");
        WebDriverWait _wait300 = new WebDriverWait(driver, 300);
        _wait300.until(d -> "完了"
                .equals(d.findElements(By.xpath("//td[@class='valueScheduleSetting']/span")).get(0).getText()));
        screenShot();
        // KSU001
        driver.get(domain + "nts.uk.at.web/view/ksu/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/08/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/08/31");
        threadSleep(1000);
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[@role='tab']")).get(1).click();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("031");
        driver.findElements(By.className("proceed")).get(8).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//span[@name='hchk']")).get(6).click();
        js.executeScript("arguments[0].scrollTo(0, 650)",
                driver.findElements(By.xpath("//div[contains(@id,'_scrollContainer')]")).get(5));
        driver.findElement(By.xpath("//tr[@data-id='031100']/th")).click();
        driver.findElement(By.xpath("//tr[@data-id='031110']/th")).click();
        driver.findElement(By.xpath("//tr[@data-id='031085']/th")).click();
        threadSleep(1000);
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft($('.ex-body-detail')[0].scrollWidth)");
        threadSleep(1000);
        screenShot();
        //
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitPageLoad();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/12/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/12/31");
        threadSleep(1000);
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft($('.ex-body-detail')[0].scrollWidth)");
        threadSleep(1000);
        screenShot();
        // KDW001
        driver.get(domain + "nts.uk.at.web/view/kdw/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitPageLoad();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/08/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/08/31");
        threadSleep(1000);
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[@role='tab']")).get(5).click();
        threadSleep(1000);
        driver.findElement(By.id("ccg001-input-code")).sendKeys("031");
        driver.findElements(By.className("proceed")).get(1).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//span[@name='hchk']")).get(1).click();
        js.executeScript("arguments[0].scrollTo(0, 650)",
                driver.findElements(By.xpath("//div[contains(@id,'_scrollContainer')]")).get(1));
        driver.findElement(By.xpath("//tr[@data-id='031100']/th")).click();
        driver.findElement(By.xpath("//tr[@data-id='031110']/th")).click();
        driver.findElement(By.xpath("//tr[@data-id='031085']/th")).click();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        driver.findElements(By.className("ntsEndDatePicker")).get(4).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(4).sendKeys("2019/12/31");
        driver.findElements(By.className("ntsStartDatePicker")).get(4).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(4).sendKeys("2019/08/01");
        driver.findElement(By.id("panel11")).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.id("button22")).click();
        threadSleep(1000);
        driver.findElement(By.xpath("//button[contains(.,'再作成')]")).click();
        driver.findElement(By.xpath("//span[contains(.,'もう一度作り直す')]")).click();
        driver.findElement(By.xpath("//button[contains(.,'再反映')]")).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.id("button6")).click();
        threadSleep(1000);
        driver.findElements(By.className("yes")).get(1).click();
        threadSleep(1000);
        driver.findElement(By.id("button113")).click();
        threadSleep(1000);
        driver.switchTo().frame("window_1");
        _wait300.until(d -> (false == d.findElement(By.className("danger")).isEnabled()));
        screenShot();
        threadSleep(1000);
        // KDW006-Khong cho hien dialog khi vao KDW003
        driver.get(domain + "nts.uk.at.web/view/kdw/006/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("COMMON")).click();
        threadSleep(1000);
        driver.findElement(By.id("button2")).click();
        WaitPageLoad();
        if (driver.findElement(By.id("checkBox161")).getAttribute("class").indexOf("checked") > 0) {
            driver.findElement(By.id("checkBox161")).click();
            threadSleep(1000);
            driver.findElement(By.id("register-button")).click();
            _wait.until(d -> "登録しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        }
        // KDW003
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad300();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(1000);
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/08/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/08/30");
        threadSleep(1000);
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[@role='tab']")).get(2).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("031085");
        driver.findElements(By.className("proceed")).get(2).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        threadSleep(1000);
        screenShot();

        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(1000);
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("031100");
        driver.findElements(By.className("proceed")).get(2).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        threadSleep(1000);
        screenShot();

        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(1000);
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("031110");
        driver.findElements(By.className("proceed")).get(2).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        threadSleep(1000);
        screenShot();
        // KDW001F
        driver.get(domain + "nts.uk.at.web/view/kdw/001/f/index.xhtml");
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("(//td[.='020905'])[1]/../td[7]")).click();
        threadSleep(1000);
        screenShot();
        driver.switchTo().frame("window_1");
        driver.findElements(By.className("hyperlink")).get(0).click();
        WaitPageLoad();
        screenShot();
        driver.switchTo().defaultContent();
        driver.switchTo().frame("window_2");
        driver.findElement(By.xpath("//button[contains(.,'閉じる')]")).click();
        driver.switchTo().defaultContent();
        driver.switchTo().frame("window_1");
        driver.findElement(By.xpath("//button[contains(.,'閉じる')]")).click();
        // Login
        login("031110", "Jinjikoi5");
        // KAF011A
        driver.get(domain + "nts.uk.at.web/view/kaf/011/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("recDatePicker")).sendKeys("2019/08/24");
        threadSleep(1000);
        driver.findElement(By.id("recTime1Start")).click();
        threadSleep(1000);
        driver.findElement(By.id("absDatePicker")).sendKeys("2019/08/26");
        threadSleep(1000);
        driver.findElement(By.id("recTime1Start")).click();
        threadSleep(1000);
        driver.findElement(By.id("recTimeBtn")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElement(By.xpath("//tr[@data-id='100']")).click();
        driver.findElement(By.className("x-large")).click();
        screenShot();
        driver.switchTo().defaultContent();
        driver.findElement(By.className("proceed")).click();
        _wait.until(d -> "登録しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        // KAF007A
        driver.get(domain + "nts.uk.at.web/view/kaf/007/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("singleDate")).sendKeys("2019/08/24");
        driver.findElement(By.xpath("//div[contains(.,'スケジュールが休日の場合は除く')]")).click();
        threadSleep(1000);
        driver.findElement(By.id("workSelect-kaf007")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElements(By.xpath("//tr[@data-id='090']")).get(0).click();
        driver.findElement(By.className("x-large")).click();
        threadSleep(1000);
        driver.switchTo().defaultContent();
        driver.findElement(By.xpath("//span[contains(.,'登録時にメールを送信する')]")).click();
        driver.findElement(By.xpath("//span[contains(.,'スケジュールが休日の場合は除く')]")).click();
        screenShot();
        driver.findElement(By.className("proceed")).click();
        _wait.until(d -> "登録しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        // Login
        login("021329", "Jinjikoi5");
        // CMM045
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        driver.findElement(By.className("ntsStartDatePicker")).clear();
        driver.findElement(By.className("ntsStartDatePicker")).sendKeys("2019/08/24");
        driver.findElement(By.className("ntsEndDatePicker")).clear();
        driver.findElement(By.className("ntsEndDatePicker")).sendKeys("2019/08/26");
        threadSleep(2000);
        driver.findElement(By.xpath("//span[contains(.,'承認済み')]")).click();
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        // 申請を承認する
        driver.findElements(By.className("ntsCheckBox")).get(7).click();
        driver.findElement(By.xpath("//button[@tabindex='1']")).click();
        _wait.until(d -> "承認しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        threadSleep(2000);
        driver.findElements(By.className("ntsCheckBox")).get(7).click();
        driver.findElement(By.xpath("//button[@tabindex='1']")).click();
        _wait.until(d -> "承認しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        threadSleep(1000);
        driver.findElement(By.xpath("(//td[.='休暇休出取消'])[1]/../td[2]")).click();
        WaitPageLoad();
        screenShot();
        driver.findElement(By.className("goback")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("(//td[.='振休振出'])[1]/../td[2]")).click();
        WaitPageLoad();
        screenShot();
        // KDW003
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad300();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(1000);
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/08/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/08/30");
        threadSleep(1000);
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[@role='tab']")).get(2).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("031110");
        driver.findElements(By.className("proceed")).get(2).click();
        WaitPageLoad();
        threadSleep(10000);
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShot();
        // Login
        login("020905", "Jinjikoi5");
        // CPS001
        driver.get(domain + "nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(1000);
        driver.findElements(By.xpath("//li[@role='tab']")).get(2).click();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("091290");
        threadSleep(1000);
        driver.findElements(By.className("proceed")).get(2).click();
        WaitPageLoad();
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(1).click();
        WaitPageLoad();
        driver.findElement(By.xpath(".//*[@id='lefttabs']/ul/li[2]")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        driver.findElement(By.xpath("//li[contains(.,'雇用')]")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//td[contains(.,'2018/10/01 ~ 2019/08/15')]")).get(1).click();
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        driver.findElement(By.xpath("//li[contains(.,'労働条件')]")).click();
        WaitPageLoad();
        screenShot();
        // KSC001
        driver.get(domain + "nts.uk.at.web/view/ksc/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[@role='tab']")).get(6).click();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("091290");
        driver.findElements(By.className("proceed")).get(1).click();
        threadSleep(1000);
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        driver.findElements(By.className("ntsEndDatePicker")).get(4).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(4).sendKeys("2019/12/31");
        driver.findElements(By.className("ntsStartDatePicker")).get(4).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(4).sendKeys("2019/08/01");
        driver.findElement(By.className("periodCovered")).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        threadSleep(1000);
        driver.findElement(By.xpath("//button[contains(.,'再作成')]")).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.xpath("//button[@tabindex='17']")).click();
        threadSleep(1000);
        driver.findElement(By.xpath("//button[@tabindex='5']")).click();
        threadSleep(1000);
        driver.findElement(By.id("buttonFinishPageE")).click();
        WaitElementLoad(By.xpath("//button[contains(.,'はい')]"));
        driver.findElements(By.className("yes")).get(1).click();
        threadSleep(1000);
        driver.findElements(By.className("yes")).get(1).click();
        threadSleep(1000);
        driver.switchTo().frame("window_1");
        _wait300.until(d -> "完了"
                .equals(d.findElements(By.xpath("//td[@class='valueScheduleSetting']/span")).get(0).getText()));
        screenShot();
        // KDW001
        driver.get(domain + "nts.uk.at.web/view/kdw/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitPageLoad();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/08/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/12/31");
        threadSleep(1000);
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[@role='tab']")).get(5).click();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("091290");
        driver.findElements(By.className("proceed")).get(1).click();
        WaitPageLoad300();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        driver.findElements(By.className("ntsEndDatePicker")).get(4).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(4).sendKeys("2019/12/31");
        driver.findElements(By.className("ntsStartDatePicker")).get(4).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(4).sendKeys("2019/08/01");
        driver.findElement(By.id("panel11")).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.id("button22")).click();
        threadSleep(1000);
        driver.findElement(By.xpath("//button[contains(.,'再作成')]")).click();
        driver.findElement(By.xpath("//span[contains(.,'もう一度作り直す')]")).click();
        driver.findElement(By.xpath("//button[contains(.,'再反映')]")).click();
        threadSleep(1000);
        screenShot();
        driver.findElement(By.id("button6")).click();
        threadSleep(1000);
        driver.findElements(By.className("yes")).get(1).click();
        threadSleep(1000);
        driver.findElement(By.id("button113")).click();
        threadSleep(1000);
        driver.switchTo().frame("window_1");
        _wait300.until(d -> (false == d.findElement(By.className("danger")).isEnabled()));
        screenShot();
        threadSleep(1000);
        // login
        login("091290", "Jinjikoi5");
        // KDW003
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad300();
        //
        driver.findElements(By.className("ntsStartDatePicker")).get(3).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(3).sendKeys("2019/08/01");
        driver.findElements(By.className("ntsEndDatePicker")).get(3).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(3).sendKeys("2019/08/31");
        driver.findElements(By.className("ntsStartDatePicker")).get(3).click();
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('#contents-area').scrollTop(165)");
        threadSleep(1000);
        screenShot();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        threadSleep(1000);
        driver.findElement(By.id("cbb-closure")).click();
        driver.findElement(By.xpath("//li[contains(.,'10日締め')]")).click();
        threadSleep(1000);
        driver.findElements(By.className("ntsStartDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsStartDatePicker")).get(0).sendKeys("2019/08/11");
        driver.findElements(By.className("ntsEndDatePicker")).get(0).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(0).sendKeys("2019/08/15");
        threadSleep(1000);
        driver.findElement(By.id("ccg001-header")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-only-me")).click();
        WaitPageLoad();
        js.executeScript("$('#contents-area').scrollTop(0)");
        threadSleep(1000);
        screenShot();
        js.executeScript("$('#contents-area').scrollTop(165)");
        threadSleep(1000);
        screenShot();
        //
        driver.findElements(By.className("ntsEndDatePicker")).get(3).clear();
        driver.findElements(By.className("ntsEndDatePicker")).get(3).sendKeys("2019/09/10");
        driver.findElements(By.className("ntsStartDatePicker")).get(3).click();
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('#contents-area').scrollTop(165)");
        threadSleep(1000);
        screenShot();

        this.uploadTestLink(370, 88);
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