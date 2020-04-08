package kmw006;


import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;

import common.TestRoot;

public class Scenario4Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kmw006/Scenario4Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        String msgexpected = "Msg_15";
        // login申請者
        login("010207", "Jinjikoi5");
        // KMK012A 処理年月の設定
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//tr[@data-id='1']")).click();
        WaitPageLoad();
        driver.findElement(By.id("inpMonth")).clear();
        driver.findElement(By.id("inpMonth")).sendKeys("2020/06");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("btn_save")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//tr[@data-id='2']")).click();
        WaitPageLoad();
        driver.findElement(By.id("inpMonth")).clear();
        driver.findElement(By.id("inpMonth")).sendKeys("2021/10");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("btn_save")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        driver.findElement(By.xpath("//button[@class='large']")).click();
        // 振休振出申請
        driver.get(domain + "nts.uk.at.web/view/kaf/011/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("recDatePicker")).clear();
        driver.findElement(By.id("recDatePicker")).sendKeys("2020/06/07");
        driver.findElement(By.xpath("//input[@id='absDatePicker']")).click();
        driver.findElement(By.xpath("//input[@id='absDatePicker']")).clear();
        WaitElementLoad(By.xpath("//input[@id='absDatePicker']"));
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@id='absDatePicker']")).sendKeys("2020/06/05");
        WaitElementLoad(By.xpath("//textarea[@id='appReason']"));
        driver.findElement(By.xpath("//textarea[@id='appReason']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//textarea[@id='appReason']")).clear();
        driver.findElement(By.xpath("//textarea[@id='appReason']")).sendKeys("autotest");
        WaitElementLoad(By.xpath("//span[@class='box']"));
        WebElement checkbox = driver.findElement(By.xpath("//span[@class='box']"));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        String msgactual = driver.findElement(By.id("ui-id-4")).findElement(By.className("control")).getText();
        if(msgexpected.equals(msgactual)){
            screenShot();
        }
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        //login承認者
        login("007227", "Jinjikoi5");
        //承認
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        driver.findElement(By.xpath("//input[contains(@id,'startInput')]")).clear();
        driver.findElement(By.xpath("//input[contains(@id,'startInput')]")).sendKeys("2020/06/05");
        driver.findElement(By.xpath("//input[contains(@id,'endInput')]")).clear();
        driver.findElement(By.xpath("//input[contains(@id,'endInput')]")).sendKeys("2020/06/07");
        WaitElementLoad(By.xpath("//button[@tabindex='6']"));
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id='combo-box']")).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath("//li[@data-value='10']")).click();
        List<WebElement> listEl =
        driver.findElements(By.xpath("//td[contains(.,'010207')]"));
        WebElement el = listEl.get(listEl.size() - 1);
        el.findElements(By.xpath("preceding-sibling::td")).get(0).click();
        new Actions(driver).moveToElement(el).perform();
        screenShot();
        WaitElementLoad(By.xpath("//button[@tabindex='1']"));
        driver.findElement(By.xpath("//button[@tabindex='1']")).click();
        _wait.until(d ->"承認しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitElementLoad(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]"));
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();
        //login申請者
        login("010207", "Jinjikoi5");
        // KDW003A 勤務報告書
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime.clear();
        startTime.sendKeys("2020/06/01");
        WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime.clear();
        endTime.sendKeys("2020/06/30");
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        WaitPageLoad();
        driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[8]/td[1]")).get(0).click();
        driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[8]/td[1]")).get(0).sendKeys("102");
        driver.findElement(By.xpath("//body")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@class = 'proceed']")).click();     
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@class='large']")).click();
        screenShot();
        // KMK012A 処理年月の設定
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//tr[@data-id='1']")).click();
        WaitPageLoad();
        driver.findElement(By.id("inpMonth")).clear();
        driver.findElement(By.id("inpMonth")).sendKeys("2020/06");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("btn_save")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//tr[@data-id='2']")).click();
        WaitPageLoad();
        driver.findElement(By.id("inpMonth")).clear();
        driver.findElement(By.id("inpMonth")).sendKeys("2021/10");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("btn_save")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        driver.findElement(By.xpath("//button[@class='large']")).click();
        // KMW006A 月締め更新
        driver.get(domain + "nts.uk.at.web/view/kmw/006/a/index.xhtml");
        WaitPageLoad();
        WebElement dropdown = driver.findElement(By.id("combo-box"));
        WaitElementLoad(By.className("ui-igcombo-button"));
        dropdown.findElement(By.className("ui-igcombo-button")).click();
        WaitElementLoad(By.xpath("//li[@data-value='1']"));
        dropdown.findElement(By.xpath("//li[@data-value='1']")).click();
        screenShot();
        driver.findElement(By.id("A1_31")).click();
        WaitElementLoad(By.xpath("//div[@role='dialog']"));
        WebElement button = driver.findElements(By.xpath("//div[@role='dialog']")).get(1);
        button.findElement(By.xpath("//button[@class='yes large proceed']")).click();
        WaitElementLoad(By.xpath("//iframe[contains(@name,'window')]"));
        WebElement dialogExtract = driver.findElement(By.xpath("//iframe[contains(@name,'window')]"));
        driver.switchTo().frame(dialogExtract);
        WaitElementLoad(By.xpath("//span[contains(.,'完了')]"));
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[@id='functions-area-bottom']//button[1]")).click();
        // CPS001A 個人情報の登録
        driver.get(domain + "nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ui-id-3")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WebElement dropdown1 = driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00035']"));
        js.executeScript("arguments[0].scrollIntoView();", dropdown1);
        Thread.sleep(1000);
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00035']")).click();
        WaitPageLoad();
        screenShot();
        this.uploadTestLink(474, 109);
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