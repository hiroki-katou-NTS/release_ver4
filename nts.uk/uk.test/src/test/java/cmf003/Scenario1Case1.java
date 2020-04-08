package cmf003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import common.TestRoot;

public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmf003/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login
        login("020905", "Jinjikoi5");

        //名称マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cmm/007/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//a[contains(.,'休日出勤')]")).click();
        driver.findElement(By.xpath("//input[@id='work_day_off_name2']/../../following-sibling::td/div/button[2]")).click();
        driver.findElement(By.xpath("(//input[@id='work_day_off_name4'])[1]/../../following-sibling::td/div/button[2]")).click();
        driver.findElement(By.xpath("(//input[@id='work_day_off_name4'])[2]/../../following-sibling::td/div/button[2]")).click();
        driver.findElement(By.xpath("//input[@id='work_day_off_name5']/../../following-sibling::td/div/button[2]")).click();
        driver.findElement(By.xpath("//input[@id='work_day_off_name6']/../../following-sibling::td/div/button[2]")).click();
        driver.findElement(By.xpath("(//button[@class='proceed'])[4]")).click();
        WaitPageLoad();
        screenShot();

        //雇用マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cmm/008/a/index.xhtml");
        WaitPageLoad();
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='97']"))).click().perform();
        driver.findElement(By.xpath("//input[@id='empName']")).clear();
        driver.findElement(By.xpath("//input[@id='empName']")).sendKeys("テスト雇用");
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("(//button[.='閉じる'])[2]"));
        screenShot();
        driver.findElement(By.xpath("(//button[.='閉じる'])[2]")).click();
        WaitPageLoad();

        //雇用マスタ／追加
        driver.findElement(By.xpath("//button[.='新規']")).click();
        driver.findElement(By.xpath("//input[@id='empCode']")).sendKeys("96");
        driver.findElement(By.xpath("//input[@id='empName']")).sendKeys("テスト１２１７");
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        screenShot();

        //分類マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cmm/014/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//td[.='0000000990']")).click();
        driver.findElement(By.xpath("//input[@id='clfName']")).clear();
        driver.findElement(By.xpath("//input[@id='clfName']")).sendKeys("テスト１２１７");
        driver.findElement(By.xpath("//button[@id='A_BTN_002']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("(//button[.='閉じる'])[2]"));
        screenShot();
        driver.findElement(By.xpath("(//button[.='閉じる'])[2]")).click();
        WaitPageLoad();

        //分類マスタ／追加
        driver.findElement(By.xpath("//button[@id='A_BTN_001']")).click();
        driver.findElement(By.xpath("//input[@id='clfCode']")).sendKeys("0000000999");
        driver.findElement(By.xpath("//input[@id='clfName']")).sendKeys("テスト１２１８");
        driver.findElement(By.xpath("//button[@id='A_BTN_002']")).click();
        WaitPageLoad();
        screenShot();

        //職位マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cmm/013/a/index.xhtml");
        WaitPageLoad();
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='99998']"))).click().perform();
        WaitElementLoad(By.xpath("//input[@id='job-title-name']"));
        driver.findElement(By.xpath("//input[@id='job-title-name']")).clear();
        driver.findElement(By.xpath("//input[@id='job-title-name']")).sendKeys("テスト１２１７");
        driver.findElement(By.xpath("//button[@class='proceed btnStyle']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("(//button[.='閉じる'])[2]"));
        screenShot();
        driver.findElement(By.xpath("(//button[.='閉じる'])[2]")).click();
        WaitPageLoad();

        //職位マスタ／追加
        driver.findElement(By.xpath("(//button[@class='btnStyle'])[1]")).click();
        driver.findElement(By.xpath("//input[@id='job-title-code']")).sendKeys("99997");
        driver.findElement(By.xpath("//input[@id='job-title-name']")).sendKeys("テスト１２１８");
        driver.findElement(By.xpath("//button[@class='proceed btnStyle']")).click();
        WaitPageLoad();
        screenShot();

        //職場マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cmm/011/a/index.xhtml");
        WaitPageLoad();
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//a[contains(.,'007777')]"))).click().perform();
        WaitPageLoad();
        driver.findElement(By.xpath("//input[@id='wkpName']")).clear();
        driver.findElement(By.xpath("//input[@id='wkpName']")).sendKeys("テスト１２１７");
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("(//button[.='閉じる'])[2]"));
        screenShot();
        driver.findElement(By.xpath("(//button[.='閉じる'])[2]")).click();
        WaitPageLoad();

        //職場マスタ／追加
        driver.findElement(By.xpath("(//button[contains(.,'職場の作成')])")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElement(By.xpath("(//button[contains(.,'決定')])")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//input[@id='wkpCd']")).sendKeys("777777");
        driver.findElement(By.xpath("//input[@id='wkpName']")).sendKeys("テスト１２１８");
        driver.findElement(By.xpath("//input[@id='wkpDisplayName']")).sendKeys("テスト１２１８");
        driver.findElement(By.xpath("//input[@id='wkpFullName']")).sendKeys("テスト１２１８");
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        screenShot();

        //勤務種別マスタ／変更
        driver.get(domain + "nts.uk.at.web/view/kdw/009/a/index.xhtml");
        WaitPageLoad();
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='9999999999']"))).click().perform();
        driver.findElement(By.xpath("//input[@id='inpPattern']")).clear();
        driver.findElement(By.xpath("//input[@id='inpPattern']")).sendKeys("テスト１２１７");
        driver.findElement(By.xpath("//button[@id='btn_002']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("(//button[.='閉じる'])[2]"));
        screenShot();
        driver.findElement(By.xpath("(//button[.='閉じる'])[2]")).click();
        WaitPageLoad();

        //勤務種別マスタ／追加
        driver.findElement(By.xpath("(//button[@id='btn_001'])")).click();
        driver.findElement(By.xpath("//input[@id='inpCode']")).sendKeys("6666666666");
        driver.findElement(By.xpath("//input[@id='inpPattern']")).sendKeys("テスト１２１８");
        driver.findElement(By.xpath("//button[@id='btn_002']")).click();
        WaitPageLoad();
        screenShot();

        //権限マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cas/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//td[.='個人基本情報']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//td[.='個人名']/following-sibling::td[1]/div/div/button[.='参照のみ']")).click();
        driver.findElement(By.xpath("//td[.='個人名カナ']/following-sibling::td[1]/div/div/button[.='参照のみ']")).click();
        driver.findElement(By.xpath("//td[.='表示氏名(ビジネスネーム)']/following-sibling::td[1]/div/div/button[.='参照のみ']")).click();
        driver.findElement(By.xpath("//td[.='表示氏名(ビジネスネーム)カナ']/following-sibling::td[1]/div/div/button[.='参照のみ']")).click();
        driver.findElement(By.xpath("//td[.='生年月日']/following-sibling::td[1]/div/div/button[.='参照のみ']")).click();
        driver.findElement(By.xpath("//td[.='性別']/following-sibling::td[1]/div/div/button[.='参照のみ']")).click();
        driver.findElement(By.xpath("//button[@id='A1_002']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("(//button[.='閉じる'])[2]"));
        screenShot();

        //メニューマスタ／追加
        driver.get(domain + "nts.uk.com.web/view/ccg/013/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("(//button[@id='A_BTN_001'])")).click();
        driver.findElement(By.xpath("//input[@id='webMenuCode']")).sendKeys("755");
        driver.findElement(By.xpath("//input[@id='webMenuName']")).sendKeys("テスト１２１７");
        driver.findElement(By.xpath("//button[@id='A_BTN_002']")).click();
        WaitPageLoad();
        screenShot();

        //休暇設定マスタ／変更
        driver.get(domain + "nts.uk.at.web/view/kmf/001/c/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//label[.='出勤日数として加算']/../following-sibling::td[1]/div/button[.='管理する']")).click();
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        screenShot();

        //年休付与マスタ／変更
        driver.get(domain + "nts.uk.at.web/view/kmf/003/a/index.xhtml");
        WaitPageLoad();
        new
        Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='99']"))).click().perform();
        driver.findElement(By.xpath("//input[@id='input-name']")).clear();
        driver.findElement(By.xpath("//input[@id='input-name']")).sendKeys("テスト１２１７");
        driver.findElement(By.xpath("//input[@id='cond01']")).clear();
        driver.findElement(By.xpath("//input[@id='cond01']")).sendKeys("80");
        driver.findElement(By.xpath("//input[@id='input-code']")).click();
        driver.findElement(By.xpath("//button[@id='A_BTN_002']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("(//button[.='閉じる'])[2]"));
        screenShot();
        driver.findElement(By.xpath("(//button[.='閉じる'])[2]")).click();
        WaitPageLoad();

        //年休付与マスタ／追加
        driver.findElement(By.xpath("(//button[@id='A_BTN_001'])")).click();
        driver.findElement(By.xpath("//input[@id='input-code']")).sendKeys("95");
        driver.findElement(By.xpath("//input[@id='input-name']")).sendKeys("テスト１２１８");
        driver.findElement(By.xpath("//span[.='一斉付与を利用する']")).click();
        WaitElementLoad(By.xpath("//div[@id='monthdays']/div/div"));
        driver.findElement(By.xpath("//div[@id='monthdays']/div/div")).click();
        WaitElementLoad(By.xpath("//li[@data-value='4']"));
        driver.findElement(By.xpath("//li[@data-value='4']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[.='労働日数']")).click();
        driver.findElement(By.xpath("//button[@id='A_BTN_002']")).click();
        WaitPageLoad();
        screenShot();

        //特別休暇マスタ／変更
        driver.get(domain + "nts.uk.at.web/view/kmf/004/a/index.xhtml");
        WaitPageLoad();
        new
        Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='17']"))).click().perform();
        driver.findElement(By.xpath("//input[@id='input-name']")).clear();
        driver.findElement(By.xpath("//input[@id='input-name']")).sendKeys("テスト１２１７");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[@id='REGISTER_BTN']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("(//button[.='閉じる'])[2]"));
        screenShot();
        driver.findElement(By.xpath("(//button[.='閉じる'])[2]")).click();
        WaitPageLoad();

        //特別休暇マスタ／追加
        driver.findElement(By.xpath("(//button[@id='NEW_BTN'])")).click();
        driver.findElement(By.xpath("//input[@id='input-code']")).sendKeys("16");
        driver.findElement(By.xpath("//input[@id='input-name']")).sendKeys("テスト１２１８");
        driver.findElement(By.xpath("//input[@id='years']")).sendKeys("1");
        driver.findElement(By.xpath("//input[@id='days']")).sendKeys("3");
        driver.findElement(By.xpath("//a[.='期限設定']")).click();
        driver.findElement(By.xpath("//input[@id='limitedDays']")).sendKeys("3");
        driver.findElement(By.xpath("//button[.='設定']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElement(By.xpath("//td[.='資格取得']")).click();
        driver.findElement(By.xpath("//button[.='決定']")).click();
        driver.findElement(By.xpath("//a[.='付与設定']")).click();
        WaitElementLoad(By.xpath("//button[@id='REGISTER_BTN']"));
        driver.findElement(By.xpath("//button[@id='REGISTER_BTN']")).click();
        WaitPageLoad();
        screenShot();

        //勤務種類マスタ／変更
        driver.get(domain + "nts.uk.at.web/view/kmk/007/a/index.xhtml");
        WaitPageLoad();
        js.executeScript("$('#single-list_scrollContainer').scrollTop($('#single-list_scrollContainer > div').height())");
        driver.findElement(By.xpath("//td[.='999']")).click();
        driver.findElement(By.xpath("//input[@id='input-workTypeName']")).clear();
        driver.findElement(By.xpath("//input[@id='input-workTypeName']")).sendKeys("テスト121７");
        driver.findElement(By.xpath("//button[@id='register-button']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("(//button[.='閉じる'])[2]"));
        screenShot();
        driver.findElement(By.xpath("(//button[.='閉じる'])[2]")).click();
        WaitPageLoad();

        //勤務種類マスタ／追加
        driver.findElement(By.xpath("(//button[@id='clear-button'])")).click();
        driver.findElement(By.xpath("//input[@id='input-workTypeCode']")).sendKeys("993");
        driver.findElement(By.xpath("//input[@id='input-workTypeName']")).sendKeys("テストテスト");
        driver.findElement(By.xpath("//input[@id='abbreviation-name-input']")).sendKeys("テスト");
        driver.findElement(By.xpath("//input[@id='symbolic-name-input']")).sendKeys("テ");
        driver.findElement(By.xpath("//button[@id='register-button']")).click();
        WaitPageLoad();
        screenShot();

        //就業時間帯マスタ／変更
        driver.get(domain + "nts.uk.at.web/view/kmk/003/a/index.xhtml");
        WaitPageLoad();
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='A77']"))).click().perform();
        WaitElementLoad(By.xpath("//input[@id='inp-worktimename']"));
        driver.findElement(By.xpath("//input[@id='inp-worktimename']")).clear();
        driver.findElement(By.xpath("//input[@id='inp-worktimename']")).sendKeys("テスト121７");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[@id='saveButton']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("(//button[.='閉じる'])[2]"));
        screenShot();
        driver.findElement(By.xpath("(//button[.='閉じる'])[2]")).click();
        WaitPageLoad();

        //就業時間帯マスタ／追加
        driver.findElement(By.xpath("(//button[@id='createButton'])")).click();
        driver.findElement(By.xpath("//input[@id='inp-worktimecode']")).sendKeys("A78");
        driver.findElement(By.xpath("//input[@id='inp-worktimename']")).sendKeys("テストテスト");
        driver.findElement(By.xpath("//input[@tabindex='12']")).sendKeys("テスト");
        driver.findElement(By.xpath("//input[@tabindex='13']")).sendKeys("テ");
        driver.findElement(By.xpath("//div[@id='cbb-worktime-atr']")).click();
        driver.findElement(By.xpath("//div[@dropdown-for='cbb-worktime-atr']/div[2]/ul/li[@data-value='1']")).click();
        driver.findElement(By.xpath("//input[@id='coreTimeStart']")).clear();
        driver.findElement(By.xpath("//input[@id='coreTimeStart']")).sendKeys("10:00");
        driver.findElement(By.xpath("//input[@id='coreTimeEnd']")).clear();
        driver.findElement(By.xpath("//input[@id='coreTimeEnd']")).sendKeys("15:00");
        driver.findElement(By.xpath("//input[@tabindex='25']")).clear();
        driver.findElement(By.xpath("//input[@tabindex='25']")).sendKeys("5:00");
        driver.findElement(By.xpath("//input[@id='workTimeStartInput']")).clear();
        driver.findElement(By.xpath("//input[@id='workTimeStartInput']")).sendKeys("9:00");
        driver.findElement(By.xpath("//input[@id='workTimeEndInput']")).clear();
        driver.findElement(By.xpath("//input[@id='workTimeEndInput']")).sendKeys("22:00");
        driver.findElement(By.xpath("//input[@id='breakTimeStartInput']")).clear();
        driver.findElement(By.xpath("//input[@id='breakTimeStartInput']")).sendKeys("12:00");
        driver.findElement(By.xpath("//input[@id='breakTimeEndInput']")).clear();
        driver.findElement(By.xpath("//input[@id='breakTimeEndInput']")).sendKeys("13:00");
        driver.findElement(By.xpath("//input[@id='oneDayInput']")).clear();
        driver.findElement(By.xpath("//input[@id='oneDayInput']")).sendKeys("7:00");
        driver.findElement(By.xpath("//a[.='休出時間帯']")).click();
        driver.findElement(By.xpath("(//input[@tabindex='89'])[1]")).clear();
        driver.findElement(By.xpath("(//input[@tabindex='89'])[1]")).sendKeys("8:00");
        driver.findElement(By.xpath("(//input[@tabindex='89'])[2]")).clear();
        driver.findElement(By.xpath("(//input[@tabindex='89'])[2]")).sendKeys("12:00");
        driver.findElement(By.xpath("//a[.='勤務時間帯']")).click();
        driver.findElement(By.xpath("(//table[@class='table-fixed-kmk003 fixed-table nts-fixed-body-table'])[1]/tbody/tr/td[2]/div/div/div[1]/span/input")).clear();
        driver.findElement(By.xpath("(//table[@class='table-fixed-kmk003 fixed-table nts-fixed-body-table'])[1]/tbody/tr/td[2]/div/div/div[1]/span/input")).sendKeys("8:00");
        driver.findElement(By.xpath("(//table[@class='table-fixed-kmk003 fixed-table nts-fixed-body-table'])[1]/tbody/tr/td[2]/div/div/div[3]/span/input")).clear();
        driver.findElement(By.xpath("(//table[@class='table-fixed-kmk003 fixed-table nts-fixed-body-table'])[1]/tbody/tr/td[2]/div/div/div[3]/span/input")).sendKeys("12:00");
        driver.findElement(By.xpath("//a[.='所定']")).click();
        driver.findElement(By.xpath("//button[@id='saveButton']")).click();
        WaitPageLoad();
        screenShot();

        //前準備実績／変更
        driver.get(domain + "nts.uk.at.web/view/kdw/007/a/index.xhtml");
        WaitPageLoad();
        js.executeScript("$('#lstErrorAlarm_scrollContainer').scrollTop($('#lstErrorAlarm_scrollContainer > div').height())");
        driver.findElement(By.xpath("//td[.='999']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@id='errorAlarmWorkRecordName']")).clear();
        driver.findElement(By.xpath("//input[@id='errorAlarmWorkRecordName']")).sendKeys("日通テスト１２１７");
        driver.findElement(By.xpath("//button[@id='register-button']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("(//button[.='閉じる'])[2]"));
        screenShot();
        driver.findElement(By.xpath("(//button[.='閉じる'])[2]")).click();
        WaitPageLoad();

        //前準備実績／追加
        driver.findElement(By.xpath("(//button[.='新規'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@id='errorAlarmWorkRecordCode']")).sendKeys("888");
        driver.findElement(By.xpath("//input[@id='errorAlarmWorkRecordName']")).sendKeys("テストテスト");
        driver.findElement(By.xpath("//button[@id='register-button']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("(//button[.='閉じる'])[2]"));
        screenShot();
        driver.findElement(By.xpath("(//button[.='閉じる'])[2]")).click();
        WaitPageLoad();

        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();
        //login
        login("020905", "Jinjikoi5");

        //個人情報マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.xpath("//input[@title='表示氏名(ビジネスネーム)カナ']"));
        driver.findElement(By.xpath("//input[@title='表示氏名(ビジネスネーム)カナ']")).clear();
        driver.findElement(By.xpath("//input[@title='表示氏名(ビジネスネーム)カナ']")).sendKeys("テストテストテスト");
        driver.findElement(By.xpath("//input[@title='個人名カナ']")).clear();
        driver.findElement(By.xpath("//input[@title='個人名カナ']")).sendKeys("カナ　カナ");
        driver.findElement(By.xpath("//button[.='登録']")).click();
        WaitElementLoad(By.xpath("(//button[.='閉じる'])[2]"));
        screenShot();
        driver.findElement(By.xpath("(//button[.='閉じる'])[2]")).click();
        WaitPageLoad();

        //個人情報マスタ／追加
        driver.get(domain + "nts.uk.com.web/view/cps/002/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//input[@id='employeeCode']")).sendKeys("099366");
        driver.findElement(By.xpath("//input[@id='employeeName']")).sendKeys("テスト　１２１７");
        driver.findElement(By.xpath("//input[@id='cardNumber']")).sendKeys("000006");
        driver.findElement(By.xpath("//input[@id='loginId']")).sendKeys("123456");
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys("0");
        driver.findElement(By.xpath("//button[@id='step1_next_btn']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//input[@title='個人名カナ']")).sendKeys("カナ　カナ");
        driver.findElement(By.xpath("//div[@title='生年月日']/input")).sendKeys("1988/01/01");
        driver.findElement(By.xpath("//div[@data-code='IS00066']/input")).sendKeys("2020/01/01");
        driver.findElement(By.xpath("//div[@data-code='IS00082']/input")).sendKeys("2020/01/01");

        driver.findElement(By.xpath("//button[@title='職場CD']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElement(By.xpath("//td[.='000100 大塚商会']")).click();
        driver.findElement(By.xpath("//button[.='決定']")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("//div[@data-code='IS00077']/input")).sendKeys("2020/01/01");
        driver.findElement(By.xpath("//div[@data-code='IS00255']/input")).sendKeys("2020/01/01");
        driver.findElement(By.xpath("//div[@data-code='IS00026']/input")).sendKeys("2020/01/01");
        driver.findElement(By.xpath("//div[@data-code='IS00119']/input")).sendKeys("2020/01/01");

        driver.findElement(By.xpath("//button[@title='平日の勤務種類']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_2");
        driver.findElement(By.xpath("//td[.='営900-1730']")).click();
        driver.findElement(By.xpath("//button[.='決定']")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("//button[@title='休日の勤務種類']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_3");
        driver.findElement(By.xpath("//td[.='090']")).click();
        driver.findElement(By.xpath("//button[.='決定']")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("//button[@title='休出の勤務種類']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_4");
        driver.findElement(By.xpath("//td[.='006']")).click();
        driver.findElement(By.xpath("//td[.='010']")).click();
        driver.findElement(By.xpath("//button[.='決定']")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("//div[@data-code='IS00279']/input")).sendKeys("2020/01/01");
        driver.findElement(By.xpath("//button[.='登録']")).click();
        WaitPageLoad();
        screenShot();

        //個人情報関連マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cps/008/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//td[.='777']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@id='A_INP_NAME']")).clear();
        driver.findElement(By.xpath("//input[@id='A_INP_NAME']")).sendKeys("テスト１２１７");
        driver.findElement(By.xpath("//button[@id='A_BTN_REGISTER']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("(//button[.='閉じる'])[2]"));
        screenShot();
        driver.findElement(By.xpath("(//button[.='閉じる'])[2]")).click();
        WaitPageLoad();

        //個人情報関連マスタ／追加
        driver.findElement(By.xpath("(//button[@id='A_BTN_NEW'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@id='A_INP_CODE']")).sendKeys("888");
        driver.findElement(By.xpath("//input[@id='A_INP_NAME']")).sendKeys("テスト１２１８");
        driver.findElement(By.xpath("//button[@id='A_BTN_REGISTER']")).click();
        WaitPageLoad();
        screenShot();

        //スケジュールデータ／変更
        driver.get(domain + "nts.uk.at.web/view/ksu/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(0).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(0).sendKeys("2019/07/01");
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(0).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(0).sendKeys("2019/07/31");
        driver.findElement(By.xpath("//button[@id='ccg001-btn-apply-search-condition']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//a[contains(.,'入力検索')]")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//input[@id='ccg001-input-code']")).sendKeys("020905");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//button[.='時刻']")).click();
        WaitPageLoad();
        selectItemKdw003_1("24水", "020905 020905").findElement(By.xpath("./following-sibling::div")).click();
        selectItemKdw003_1("24水", "020905 020905").findElement(By.xpath("./following-sibling::div/div/input")).clear();
        selectItemKdw003_1("24水", "020905 020905").findElement(By.xpath("./following-sibling::div/div/input")).sendKeys("19:00");
        driver.findElement(By.xpath("//button[@id='saveData']")).click();
        WaitPageLoad();
        screenShot();

        //日次データ／変更
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        //driver.switchTo().frame("window_1");
        //driver.findElement(By.xpath("//button[@id='dialogClose']")).click();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(4).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(4).sendKeys("2019/07/01");
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(4).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(4).sendKeys("2019/07/31");
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        //driver.switchTo().frame("window_2");
        //driver.findElement(By.xpath("//button[@id='dialogClose']")).click();
        js.executeScript("$($(\".mgrid-free\")[1]).scrollTop($($(\".mgrid-free\")[1]).height())");
        selectItemKdw003_1("退勤時刻1", "07/24(水)").click();
        selectItemKdw003_1("退勤時刻1", "07/24(水)").click();
        selectItemKdw003_1("退勤時刻1", "07/24(水)").findElement(By.xpath("./div/input")).clear();
        selectItemKdw003_1("退勤時刻1", "07/24(水)").findElement(By.xpath("./div/input")).sendKeys("17:30");
        selectItemKdw003_1("実働時間", "07/24(水)").click();
        Thread.sleep(1000);
        selectItemKdw003_1("実働時間", "07/24(水)").click();
        selectItemKdw003_1("実働時間", "07/24(水)").findElement(By.xpath("./div/input")).clear();
        selectItemKdw003_1("実働時間", "07/24(水)").findElement(By.xpath("./div/input")).sendKeys("9:00");
        driver.findElement(By.xpath("//button[.='確定']")).click();
        WaitPageLoad();
        screenShot();

        //月次データ／変更
        driver.get(domain + "nts.uk.at.web/view/kmw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//input[@id='yearMonthPicker']")).clear();
        driver.findElement(By.xpath("//input[@id='yearMonthPicker']")).sendKeys("2019/08");
        driver.findElement(By.xpath("//label[.='処理年月']")).click();
        WaitPageLoad();
        selectItemKdw003_3("フレックス法定内時間", "020905").click();
        selectItemKdw003_3("フレックス法定内時間", "020905").click();
        selectItemKdw003_3("フレックス法定内時間", "020905").findElement(By.xpath("./div/input")).clear();
        selectItemKdw003_3("フレックス法定内時間", "020905").findElement(By.xpath("./div/input")).sendKeys("10:00");
        driver.findElement(By.xpath("//button[.='確定']")).click();
        WaitPageLoad();
        screenShot();

        //保存
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();
        //login
        login("000001", "0");
        driver.get(domain + "nts.uk.com.web/view/cmf/003/a/index.xhtml");
        WaitPageLoad();
        String[] items = new String[]{"会社マスタ", "名称マスタ", "雇用マスタ", "分類マスタ", "職位マスタ", "部門・職場マスタ", "勤務種別マスタ", "権限マスタ"};
        saveData("テストマスタ前半", items, "025445", 1);

        items = new String[]{"メニューマスタ", "休暇設定マスタ", "年休付与マスタ", "特別休暇マスタ", "勤務種類マスタ", "就業時間帯マスタ"};
        saveData("テストマスタ後半", items, "025445", 1);
        
        items = new String[]{"個人情報マスタ", "個人情報関連マスタ"};
        saveData("テスト個人情報", items, "020905", 2);

        items = new String[]{"打刻データ"};
        saveData("打刻データ", items, "025445", 2);

        items = new String[]{"前準備-実績"};
        saveData("テスト前準備実績", items, "025445", 2);
        
        items = new String[]{"スケデータ"};
        saveData("テストスケデータ", items, "020905", 3);
        
        items = new String[]{"日次データ"};
        saveData("テスト日次データ", items, "020905", 3);

        items = new String[]{"週次データ"};
        saveData("テスト週次データ", items, "020905", 3);

        items = new String[]{"月次データ"};
        saveData("テスト月次データ", items, "020905", 3);

        items = new String[]{"３６データ"};
        saveData("テスト３６データ", items, "020905", 3);

        items = new String[]{"スケデータログ"};
        saveData("テストスケデータログ", items, "020905", 3);

        items = new String[]{"日次残数データ"};
        saveData("テスト日次残数データ", items, "020905", 3);

        items = new String[]{"月次残数データ"};
        saveData("テスト月次残数データ", items, "020905", 3);

        this.uploadTestLink(1068, 269);

    }

    public void saveData(String fileName, String[] items, String id, int checkBoxPosition) throws Exception {
        driver.findElement(By.xpath("//button[@id='buttonSave']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//input[@id='B4_2']")).sendKeys(fileName);
        driver.findElement(By.xpath("//button[@id='B3_15']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        if (checkBoxPosition == 2) {
            js.executeScript("$('#swap-list-grid1_scrollContainer').scrollTop(355)");
        } else if (checkBoxPosition == 3) {
            js.executeScript("$('#swap-list-grid1_scrollContainer').scrollTop(500)");
        }           
        Thread.sleep(1000);
        for (String item:items) {
            driver.findElement(By.xpath("//td[.='" + item + "']/../th")).click();
        }
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//button[@class='move-button move-forward ntsSwap_Component'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[@id='btnLeft']")).click();
        WaitElementLoad(By.xpath("//button[@id='B5_2']"));
        driver.findElement(By.xpath("//button[@id='B5_2']")).click();
        driver.findElement(By.xpath("//span[.='下記の指定社員（チェック有）を対象とする']")).click();
        driver.findElement(By.xpath("//div[@id='ccg001-btn-search-drawer']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//a[contains(.,'入力検索')]")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//input[@id='ccg001-input-code']")).sendKeys(id);
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@id='D5_2']")).click();
        driver.findElement(By.xpath("//button[@id='E5_2']")).click();
        WaitElementLoad(By.xpath("//button[.='はい']"));
        driver.findElement(By.xpath("//button[.='はい']")).click();
        driver.switchTo().frame("window_2");
        WaitElementLoad(By.xpath("//button[@id='F3_3']"));
        driver.findElement(By.xpath("//button[@id='F3_3']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//a[.='操作選択に戻る']")).click();
        WaitPageLoad();
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