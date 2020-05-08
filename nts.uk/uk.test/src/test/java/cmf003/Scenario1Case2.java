package cmf003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import common.TestRoot;

public class Scenario1Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmf003/Scenario1Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //復旧
        //login
        login("000001", "0");
        driver.get(domain + "nts.uk.com.web/view/cmf/004/a/index.xhtml");
        WaitPageLoad();
        recoverData("テストマスタ前半");
        recoverData("テストマスタ後半");
        recoverData("テスト個人情報");
        recoverData("打刻データ");
        recoverData("テスト前準備実績");
        recoverData("テストスケデータ");
        recoverData("テスト日次データ");
        recoverData("テスト週次データ");
        recoverData("テスト月次データ");
        recoverData("テスト３６データ");
        recoverData("テストスケデータログ");
        recoverData("テスト日次残数データ");
        recoverData("テスト月次残数データ");

        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();
        //login
        login("025445", "Jinjikoi5");
        
        //名称マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cmm/007/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//a[contains(.,'休日出勤')]")).click();
        WaitPageLoad();
        screenShot();

        //雇用マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cmm/008/a/index.xhtml");
        WaitPageLoad();
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='97']"))).click().perform();
        WaitPageLoad();
        screenShot();

        //雇用マスタ／追加
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='96']"))).click().perform();
        WaitPageLoad();
        screenShot();

        //分類マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cmm/014/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//td[.='0000000990']")).click();
        WaitPageLoad();
        screenShot();

        //分類マスタ／追加
        driver.findElement(By.xpath("//td[.='0000000999']")).click();
        WaitPageLoad();
        screenShot();

        //職位マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cmm/013/a/index.xhtml");
        WaitPageLoad();
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='99998']"))).click().perform();
        WaitPageLoad();
        screenShot();

        //職位マスタ／追加
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='99997']"))).click().perform();
        WaitPageLoad();
        screenShot();

        //職場マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cmm/011/a/index.xhtml");
        WaitPageLoad();
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//a[contains(.,'007777')]"))).click().perform();
        WaitPageLoad();
        screenShot();

        //職場マスタ／追加
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//a[contains(.,'777777')]"))).click().perform();
        WaitPageLoad();
        screenShot();

        //勤務種別マスタ／変更
        driver.get(domain + "nts.uk.at.web/view/kdw/009/a/index.xhtml");
        WaitPageLoad();
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='9999999999']"))).click().perform();
        WaitPageLoad();
        screenShot();

        //勤務種別マスタ／追加
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='6666666666']"))).click().perform();
        WaitPageLoad();
        screenShot();

        //権限マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cas/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//td[.='個人基本情報']")).click();
        WaitPageLoad();
        screenShot();

        //メニューマスタ／追加
        driver.get(domain + "nts.uk.com.web/view/ccg/013/a/index.xhtml");
        WaitPageLoad();
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='755']"))).click().perform();
        WaitPageLoad();
        screenShot();

        //休暇設定マスタ／変更
        driver.get(domain + "nts.uk.at.web/view/kmf/001/c/index.xhtml");
        WaitPageLoad();
        screenShot();

        //年休付与マスタ／変更
        driver.get(domain + "nts.uk.at.web/view/kmf/003/a/index.xhtml");
        WaitPageLoad();
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='99']"))).click().perform();
        WaitPageLoad();
        screenShot();

        //年休付与マスタ／追加
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='95']"))).click().perform();
        WaitPageLoad();
        screenShot();

        //特別休暇マスタ／変更
        driver.get(domain + "nts.uk.at.web/view/kmf/004/a/index.xhtml");
        WaitPageLoad();
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='17']"))).click().perform();
        WaitPageLoad();
        screenShot();

        //特別休暇マスタ／追加
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='16']"))).click().perform();
        WaitPageLoad();
        screenShot();

        //勤務種類マスタ／変更
        driver.get(domain + "nts.uk.at.web/view/kmk/007/a/index.xhtml");
        WaitPageLoad();
        js.executeScript("$('#single-list_scrollContainer').scrollTop($('#single-list_scrollContainer > div').height())");
        driver.findElement(By.xpath("//td[.='999']")).click();
        WaitPageLoad();
        screenShot();

        //勤務種類マスタ／追加
        driver.findElement(By.xpath("//td[.='993']")).click();
        WaitPageLoad();
        screenShot();

        //就業時間帯マスタ／変更
        driver.get(domain + "nts.uk.at.web/view/kmk/003/a/index.xhtml");
        WaitPageLoad();
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='A77']"))).click().perform();
        WaitPageLoad();
        screenShot();

        //就業時間帯マスタ／追加
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//td[.='A78']"))).click().perform();
        WaitPageLoad();
        screenShot();

        //前準備実績／変更

        //前準備実績／追加

        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();
        //login
        login("020905", "Jinjikoi5");

        //個人情報マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        screenShot();

        //個人情報マスタ／追加

        //個人情報関連マスタ／変更
        driver.get(domain + "nts.uk.com.web/view/cps/008/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//td[.='777']")).click();
        WaitPageLoad();
        screenShot();

        //個人情報関連マスタ／追加
        driver.findElement(By.xpath("//td[.='888']")).click();
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
        screenShot();

        //日次データ／変更

        //月次データ／変更
        driver.get(domain + "nts.uk.at.web/view/kmw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//input[@id='yearMonthPicker']")).clear();
        driver.findElement(By.xpath("//input[@id='yearMonthPicker']")).sendKeys("2019/08");
        driver.findElement(By.xpath("//label[.='処理年月']")).click();
        WaitPageLoad();
        screenShot();

        this.uploadTestLink(1070, 270);

    }

    public void recoverData(String fileName) throws Exception {
        driver.findElement(By.xpath("//button[@id='buttonSave']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//span[.='復旧ファイルをサーバーの保存ファイルから選択します。']")).click();
        List<WebElement> els = driver.findElements(By.xpath("//td[.='" + fileName + "']"));
        new Actions(driver).moveToElement(els.get(els.size() - 1)).click().perform();
        driver.findElement(By.xpath("//button[.='次へ→']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@id='E7_3']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("(//button[.='次へ→'])[3]")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("(//button[.='次へ→'])[4]")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@id='H9_2']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        WaitElementLoad(By.xpath("//button[@id='I5_2']"));
        screenShot();
        driver.findElement(By.xpath("//button[@id='I5_2']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//a[.='操作選択に戻る']")).click();
        Thread.sleep(1000);
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