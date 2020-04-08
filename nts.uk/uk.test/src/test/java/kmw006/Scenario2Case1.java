package kmw006;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;

public class Scenario2Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kmw006/Scenario2Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        String empCode = "010207";
        // login申請者
        login(empCode, "Jinjikoi5");
        // KMK012A 処理年月の設定
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//tr[@data-id='1']")).click();
        WaitPageLoad();
        driver.findElement(By.id("inpMonth")).clear();
        driver.findElement(By.id("inpMonth")).sendKeys("2021/12");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("btn_save")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        driver.findElement(By.xpath("//button[@class='large']")).click();
        screenShot();
        WaitPageLoad();
        driver.findElement(By.xpath("//tr[@data-id='2']")).click();
        WaitPageLoad();
        driver.findElement(By.id("inpMonth")).clear();
        driver.findElement(By.id("inpMonth")).sendKeys("2022/01");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("btn_save")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        driver.findElement(By.xpath("//button[@class='large']")).click();
        // CPS001A 個人情報の登録
        driver.get(domain + "nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ui-id-3")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WebElement dropdown = driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00024']"));
        js.executeScript("arguments[0].scrollIntoView();", dropdown);
        Thread.sleep(1000);
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00024']")).click();
        WaitPageLoad();
        driver.findElement(By.id("COM1000000000000000CS00024IS00276")).click();
        WaitPageLoad();
        js.executeScript("$('iframe').parent().parent().css(`left`,`840px`)");
        WebElement dialogCPS001 = driver.findElement(By.xpath("//iframe[contains(@name,'window')]"));
        driver.switchTo().frame(dialogCPS001);
        screenShot();
        // KMF003A 年休付与の登録
        driver.get(domain + "nts.uk.at.web/view/kmf/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//tr[@data-id='01']")).click();
        WaitElementLoad(By.id("a7_9"));
        driver.findElement(By.id("a7_9")).click();
        WaitElementLoad(By.xpath("//iframe[contains(@name,'window')]"));
        WebElement dialogKMF003 = driver.findElement(By.xpath("//iframe[contains(@name,'window')]"));
        driver.switchTo().frame(dialogKMF003);
        screenShot();
        // KDW003A 勤務報告書
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("btnVacationRemaining")).click();
        driver.findElement(By.xpath("//td[contains(.,'有休')]")).click();
        WaitElementLoad(By.xpath("//iframe[contains(@name,'window')]"));
        WebElement dialogKDW003 = driver.findElement(By.xpath("//iframe[contains(@name,'window')]"));
        driver.switchTo().frame(dialogKDW003);
        screenShot();
        driver.findElement(By.id("btnCancel")).click();
        this.uploadTestLink(460, 103);
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