package kmw006;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;

public class Scenario3Case3 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kmw006/Scenario3Case3";
        this.init();
    }

    @Test
    public void test() throws Exception {

        String empCode = "029259";
        // login申請者
        login(empCode, "Jinjikoi5");
        // KMK012A 処理年月の設定
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//tr[@data-id='1']")).click();
        WaitPageLoad();
        driver.findElement(By.id("inpMonth")).clear();
        driver.findElement(By.id("inpMonth")).sendKeys("2021/09");
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
        // KDW001B 就業計算と集計 - 詳細実行
        driver.get(domain + "nts.uk.at.web/view/kdw/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-only-me")).click();
        WaitPageLoad();
        WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime.clear();
        startTime.sendKeys("2021/09/01");
        WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime.clear();
        endTime.sendKeys("2021/09/30");
        driver.findElement(By.xpath("//body")).click();
        screenShot();
        driver.findElement(By.id("button22")).click();
        WaitPageLoad();
        WebElement el1 = driver.findElement(By.cssSelector("#checkBox1"));
        WebElement checkbox1 = el1.findElement(By.cssSelector(".box"));
        if (!checkbox1.isSelected()) {
            checkbox1.click();
        }
        WebElement el2 = driver.findElement(By.cssSelector("#checkBox2"));
        WebElement checkbox2 = el2.findElement(By.cssSelector(".box"));
        if (!checkbox2.isSelected()) {
            checkbox2.click();
        }
        WebElement el3 = driver.findElement(By.cssSelector("#checkBox3"));
        WebElement checkbox3 = el3.findElement(By.cssSelector(".box"));
        if (!checkbox3.isSelected()) {
            checkbox3.click();
        }
        screenShot();
        driver.findElement(By.id("button6")).click();
        WaitPageLoad();
        driver.findElement(By.id("button113")).click();
        WaitPageLoad();
        WebElement dialogCount = driver.findElement(By.xpath("//iframe[contains(@name,'window')]"));
        driver.switchTo().frame(dialogCount);
        WaitElementLoad(By.xpath("//span[contains(.,'完了')]"));
        screenShot();
        driver.findElement(By.id("closeDialogButton")).click();
        // KMW003A 月別実績の修正
        driver.get(domain + "nts.uk.at.web/view/kmw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-only-me")).click();
        WaitPageLoad();
        screenShot();
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
        WebElement dropdown1 = driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00024']"));
        js.executeScript("arguments[0].scrollIntoView();", dropdown1);
        Thread.sleep(1000);
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00024']")).click();
        WaitPageLoad();
        driver.findElement(By.id("COM1000000000000000CS00024IS00276")).click();
        WaitPageLoad();
        js.executeScript("$('iframe').parent().parent().css(`left`,`840px`)");
        WebElement dialogCPS001 = driver.findElement(By.xpath("//iframe[contains(@name,'window')]"));
        driver.switchTo().frame(dialogCPS001);
        screenShot();
        this.uploadTestLink(472, 108);
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