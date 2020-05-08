package kif001;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import common.TestRoot;

public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kif001/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login申請者
        login("000001", "0");

        //個人スケジュールの作成 - 実行
        driver.get(domain + "nts.uk.at.web/view/ksc/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(4).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(4).sendKeys("2021/01/01");
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(4).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(4).sendKeys("2021/01/31");

        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.xpath("//a[contains(.,'詳細検索')]"));
        screenShot();
        driver.findElement(By.xpath("//a[contains(.,'詳細検索')]")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//h3")).get(2).click();
        Thread.sleep(1000);
        screenShot();
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();
        WaitPageLoad();
        screenShot();

        driver.findElement(By.xpath("//div[@class='buttonNext']/button")).click();
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
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(0).sendKeys("2021/01/01");
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(0).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(0).sendKeys("2021/01/31");
        driver.findElement(By.xpath("//button[@id='ccg001-btn-apply-search-condition']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//a[contains(.,'詳細検索')]")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//h3")).get(2).click();
        Thread.sleep(1000);
        screenShot();
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();
        WaitPageLoad();

        //KDW001B
        driver.get(domain + "nts.uk.at.web/view/kdw/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(4).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(4).sendKeys("2021/01/01");
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(4).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(4).sendKeys("2021/01/31");

        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.xpath("//a[contains(.,'詳細検索')]"));
        screenShot();
        driver.findElement(By.xpath("//a[contains(.,'詳細検索')]")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//h3")).get(2).click();
        Thread.sleep(1000);
        screenShot();
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();
        WaitPageLoad();
        screenShot();

        driver.findElement(By.xpath("//button[@id='button22']")).click();
        screenShot();

        WaitElementLoad(By.xpath("//button[@id='button6']"));
        driver.findElement(By.xpath("//button[@id='button6']")).click();
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
        screenShot();
        driver.findElement(By.xpath("//div[@id='cbDisplayFormat']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='1']"));
        driver.findElement(By.xpath("//li[@data-value='1']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//input[@id='choosenDate']")).clear();
        driver.findElement(By.xpath("//input[@id='choosenDate']")).sendKeys("2021/01/01");
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        WaitPageLoad();
        screenShot();

        driver.findElement(By.xpath("//input[@id='choosenDate']")).clear();
        driver.findElement(By.xpath("//input[@id='choosenDate']")).sendKeys("2021/01/31");
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        WaitPageLoad();
        screenShot();

        this.uploadTestLink(453, 101);
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