package kif001;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import common.TestRoot;

public class Scenario1Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kif001/Scenario1Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login
        login("000001", "0");

        //CPS001A
        driver.get(domain + "nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//a[contains(.,'カテゴリ')]")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@title='ドロップダウンの表示']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00021']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00021']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("(//div[@title='ドロップダウンの表示'])[2]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//li[@data-value='0000000230']")).click();
        screenShot();

        driver.findElement(By.xpath("//button[contains(.,'登録')]")).click();
        WaitElementLoad(By.xpath("(//button[contains(.,'閉じる')])[2]"));
        driver.findElement(By.xpath("(//button[contains(.,'閉じる')])[2]")).click();
        WaitPageLoad();

        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();

        //login
        login("001369", "Jinjikoi5");

        //個人スケジュールの作成 - 実行
        driver.get(domain + "nts.uk.at.web/view/ksc/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).sendKeys("2019/11/08");
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).sendKeys("2019/12/31");

        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ccg001-btn-only-me"));
        driver.findElement(By.id("ccg001-btn-only-me")).click();
        WaitPageLoad();
        screenShot();

        driver.findElement(By.xpath("//div[@class='buttonNext']/button")).click();
        WaitElementLoad(By.xpath("//button[contains(.,'再作成')]"));
        driver.findElement(By.xpath("//button[contains(.,'再作成')]")).click();
        screenShot();

        WaitElementLoad(By.xpath("//div[@class='buttonBeginNext']/button"));
        driver.findElement(By.xpath("//div[@class='buttonBeginNext']/button")).click();
        screenShot();

        WaitElementLoad(By.xpath("(//div[@class='buttonBeginNext']/button)[2]"));
        driver.findElement(By.xpath("(//div[@class='buttonBeginNext']/button)[2]")).click();
        screenShot();

        WaitElementLoad(By.id("buttonFinishPageE"));
        driver.findElement(By.id("buttonFinishPageE")).click();
        screenShot();

        WaitElementLoad(By.xpath("(//button[contains(@class,'yes')])[2]"));
        driver.findElement(By.xpath("(//button[contains(@class,'yes')])[2]")).click();
        screenShot();

        try {
            WaitElementLoad(By.xpath("(//button[contains(@class,'yes')])[2]"));
            driver.findElement(By.xpath("(//button[contains(@class,'yes')])[2]")).click();
            screenShot();
        } catch (ElementNotInteractableException e) {}
        try {
            WaitElementLoad(By.xpath("(//button[contains(@class,'yes')])[2]"));
            driver.findElement(By.xpath("(//button[contains(@class,'yes')])[2]")).click();
            screenShot();
        } catch (ElementNotInteractableException e) {}

        driver.switchTo().frame("window_1");
        WaitElementLoad(By.xpath("//button[@id='btn-f-close']"));
        screenShot();
        driver.findElement(By.xpath("//button[@id='btn-f-close']")).click();

        //KSU001
        driver.get(domain + "nts.uk.at.web/view/ksu/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(0).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(0).sendKeys("2019/11/01");
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(0).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(0).sendKeys("2019/11/30");
        WaitElementLoad(By.id("ccg001-btn-only-me"));
        driver.findElement(By.id("ccg001-btn-only-me")).click();
        WaitPageLoad();
        screenShot();

        //KDW001B
        driver.get(domain + "nts.uk.at.web/view/kdw/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).sendKeys("2019/11/08");
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).sendKeys("2019/12/31");

        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ccg001-btn-only-me"));
        driver.findElement(By.id("ccg001-btn-only-me")).click();
        WaitPageLoad();
        screenShot();

        driver.findElement(By.xpath("//button[@id='button22']")).click();
        WaitElementLoad(By.xpath("//button[contains(.,'再作成')]"));
        driver.findElement(By.xpath("//button[contains(.,'再作成')]")).click();
        driver.findElement(By.xpath("//button[contains(.,'再反映')]")).click();
        driver.findElement(By.xpath("(//label[@class='ntsRadioBox'])[1]")).click();
        screenShot();

        driver.findElement(By.xpath("//button[@id='button6']")).click();
        screenShot();

        WaitElementLoad(By.xpath("(//button[contains(@class,'yes')])[2]"));
        driver.findElement(By.xpath("(//button[contains(@class,'yes')])[2]")).click();
        screenShot();

        WaitElementLoad(By.xpath("//button[@id='button113']"));
        driver.findElement(By.xpath("//button[@id='button113']")).click();

        driver.switchTo().frame("window_1");
        WebDriverWait waitCalculate = new WebDriverWait(driver, 300);
        waitCalculate.until(d -> !d.findElement(By.xpath("//button[@class='danger']")).isEnabled());
        screenShot();
        driver.findElement(By.xpath("//button[@id='closeDialogButton']")).click();

        //KDW003A
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).sendKeys("2019/11/01");
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).sendKeys("2019/11/30");
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('#contents-area').scrollTop($('#contents-area').height())");
        screenShot();

        this.uploadTestLink(598, 148);
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