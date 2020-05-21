package cmm044;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm044/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login申請者
        login("004515", "Jinjikoi5");
        WaitPageLoad();

        //CMM044A 代行者の登録
        driver.get(domain + "nts.uk.com.web/view/cmm/044/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//*[@id='functions-area']/button[1]")).click();

        WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime.sendKeys("2019/05/01");
        driver.findElement(By.id("master-wrapper")).click();
        WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime.sendKeys("9999/12/31");
        driver.findElement(By.id("master-wrapper")).click();
        screenShot();
        driver.findElement(By.xpath("//button[@id='BTN_A4_003']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElement(By.className("tree-component-node-text-col")).click();
        driver.findElement(By.xpath("//button[text()='検索→']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//*[text()='006310']")).click();
        WaitElementLoad(By.xpath("//*[@id='functions-area-bottom']/button[1]"));
        driver.findElement(By.xpath("//*[@id='functions-area-bottom']/button[1]")).click();
        WaitElementLoad(By.xpath("//*[@id='functions-area']/button[2]"));
        driver.findElement(By.xpath("//*[@id='functions-area']/button[2]")).click();
        screenShot();
        this.uploadTestLink(420, 94);

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