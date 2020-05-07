package kmw006;

import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import common.TestRoot;

public class Scenario3Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kmw006/Scenario3Case1";
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
        screenShot();
        WaitPageLoad();
        driver.findElement(By.xpath("//tr[@data-id='2']")).click();
        WaitPageLoad();
        driver.findElement(By.id("inpMonth")).clear();
        driver.findElement(By.id("inpMonth")).sendKeys("2021/10");
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
        driver.findElement(By.xpath("//tr[@data-id='03']")).click();
        WaitElementLoad(By.id("a7_9"));
        driver.findElement(By.id("a7_9")).click();
        WaitElementLoad(By.xpath("//iframe[contains(@name,'window')]"));
        WebElement dialogKMF003 = driver.findElement(By.xpath("//iframe[contains(@name,'window')]"));
        driver.switchTo().frame(dialogKMF003);
        screenShot();
        // KAF006A 休暇申請
        driver.get(domain + "nts.uk.at.web/view/kaf/006/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id='functions-area']/button[2]")).click();
        WaitElementLoad(By.xpath("//iframe[contains(@name,'window')]"));
        WebElement dialogKAF006 = driver.findElement(By.xpath("//iframe[contains(@name,'window')]"));
        driver.switchTo().frame(dialogKAF006);
        screenShot();
        Thread.sleep(2000);
        driver.findElement(By.id("btnCancel")).click();
        driver.findElement(By.id("inputdate")).clear();
        driver.findElement(By.id("inputdate")).sendKeys("2021/10/07");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//div[@id ='hdType']/button"));
        driver.findElement(By.xpath("//div[@id ='hdType']/button")).click();
        WaitElementLoad(By.id("workTypes"));
        driver.findElement(By.id("workTypes")).click();
        WaitElementLoad(By.xpath("//li[@data-value='107']"));
        driver.findElement(By.xpath("//li[@data-value='107']")).click();;
        WebElement checkbox = driver.findElement(By.xpath("//span[@class='box']"));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        screenShot();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitElementLoad(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]"));
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        // Login 承認一覧
        login("004438", "Jinjikoi5");
        // CMM045A 承認一覧
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        driver.findElement(By.xpath("//input[contains(@id,'startInput')]")).clear();
        driver.findElement(By.xpath("//input[contains(@id,'startInput')]")).sendKeys("2021/01/01");
        driver.findElement(By.xpath("//input[contains(@id,'endInput')]")).clear();
        driver.findElement(By.xpath("//input[contains(@id,'endInput')]")).sendKeys("2021/12/31");
        WaitElementLoad(By.xpath("//button[@tabindex='6']"));
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id='combo-box']")).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath("//li[@data-value='1']")).click();
        List<WebElement> listEl =
        driver.findElements(By.xpath("//td[contains(.,'029259')]"));
        WebElement el = listEl.get(listEl.size() - 1);
        el.findElements(By.xpath("preceding-sibling::td")).get(0).click();
        new Actions(driver).moveToElement(el).perform();
        WaitElementLoad(By.xpath("//button[@tabindex='1']"));
        screenShot();
        driver.findElement(By.xpath("//button[@tabindex='1']")).click();
        _wait.until(d ->"承認しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitElementLoad(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]"));
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        this.uploadTestLink(468, 106);
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