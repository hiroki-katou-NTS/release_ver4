package kmw006;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;

public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kmw006/Scenario1Case1";
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
        driver.findElement(By.id("inpMonth")).sendKeys("2021/09");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("btn_save")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        driver.findElement(By.xpath("//button[@class='large']")).click();
        screenShot();
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
        WaitPageLoad();
        screenShot();
        driver.findElement(By.id("A1_31")).click();
        WaitPageLoad();
        WebElement button = driver.findElements(By.xpath("//div[@role='dialog']")).get(1);
        button.findElement(By.xpath("//button[@class='yes large proceed']")).click();
        WaitPageLoad();
        WebElement dialogExtract = driver.findElement(By.xpath("//iframe[contains(@name,'window')]"));
        driver.switchTo().frame(dialogExtract);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.id("F3_2")).click();
        // KMK012A 処理年月の設定
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//tr[@data-id='1']")).click();
        screenShot();
        this.uploadTestLink(457, 102);
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