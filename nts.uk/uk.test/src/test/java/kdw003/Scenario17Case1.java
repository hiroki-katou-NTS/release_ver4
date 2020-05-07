package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;

public class Scenario17Case1 extends TestRoot {

    String inpMonth = "inpMonth";// id
    String btnsave = "btn_save";// id

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario17Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {

         //login 018234/Jinjikoi5
         login("018234", "Jinjikoi5");

        // change closure 1
        driver.get(domain+ "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id(inpMonth)).click();
        driver.findElement(By.id(inpMonth)).clear();
        driver.findElement(By.id(inpMonth)).sendKeys("2019/11");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id(btnsave)).click();

        // go kdw010
        driver.get(domain+ "nts.uk.at.web/view/kdw/010/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.xpath("//input[contains(@class,'nts-input')]"));
        driver.findElements(By.xpath("//input[contains(@class,'nts-input')]")).get(0).click();
        driver.findElements(By.xpath("//input[contains(@class,'nts-input')]")).get(0).clear();
        driver.findElements(By.xpath("//input[contains(@class,'nts-input')]")).get(0).sendKeys("5");

        WaitElementLoad(By.xpath("//*[@id='functions-area']/button"));
        driver.findElement(By.xpath("//*[@id='functions-area']/button")).click();
        WaitPageLoad();

        // click checkbox KDW006C
        driver.get(domain + "nts.uk.at.web/view/kdw/006/c/index.xhtml");
        WaitPageLoad();
        WebElement a = driver.findElement(By.xpath("//*[@id='checkBox161']"));
        WaitElementLoad(By.xpath("//*[@id='checkBox161']/label/span[1]"));
        if (a.getAttribute("class").indexOf("checked") == -1) {
            driver.findElement(By.id("register-button")).click();
        } else {
            driver.findElement(By.xpath("//*[@id='checkBox161']/label/span[1]")).click();
            driver.findElement(By.id("register-button")).click();
        }
        driver.findElement(By.xpath("//body")).click();

        // go kdw003/ tháng 12
        driver.get(domain+"nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        selectItemKdw003_2("勤務種類", "12/04(水)").click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//td[contains(.,'008')]"));
        driver.findElements(By.xpath("//td[contains(.,'008')]")).get(1).click();
        driver.findElements(By.xpath("//td[contains(.,'008')]")).get(1).click();
        WaitElementLoad(By.id("btnSetting"));
        driver.findElements(By.id("btnSetting")).get(0).click();
        WaitPageLoad();

        selectItemKdw003_2("勤務種類", "12/05(木)").click();
        WaitPageLoad();
        driver.switchTo().frame("window_2");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//td[contains(.,'008')]"));
        driver.findElements(By.xpath("//td[contains(.,'008')]")).get(1).click();
        driver.findElements(By.xpath("//td[contains(.,'008')]")).get(1).click();
        WaitElementLoad(By.id("btnSetting"));
        driver.findElements(By.id("btnSetting")).get(0).click();
        WaitPageLoad();

        selectItemKdw003_2("勤務種類", "12/06(金)").click();
        WaitPageLoad();
        driver.switchTo().frame("window_3");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//td[contains(.,'008')]"));
        driver.findElements(By.xpath("//td[contains(.,'008')]")).get(1).click();
        driver.findElements(By.xpath("//td[contains(.,'008')]")).get(1).click();
        WaitElementLoad(By.id("btnSetting"));
        driver.findElements(By.id("btnSetting")).get(0).click();
        WaitPageLoad();

        selectItemKdw003_2("勤務種類", "12/09(月)").click();
        WaitPageLoad();
        driver.switchTo().frame("window_4");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//td[contains(.,'008')]"));
        driver.findElements(By.xpath("//td[contains(.,'008')]")).get(1).click();
        driver.findElements(By.xpath("//td[contains(.,'008')]")).get(1).click();
        WaitElementLoad(By.id("btnSetting"));
         driver.findElements(By.id("btnSetting")).get(0).click();
        WaitPageLoad();

        selectItemKdw003_2("勤務種類", "12/10(火)").click();
        WaitPageLoad();
        driver.switchTo().frame("window_5");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//td[contains(.,'008')]"));
        driver.findElements(By.xpath("//td[contains(.,'008')]")).get(1).click();
        driver.findElements(By.xpath("//td[contains(.,'008')]")).get(1).click();
        WaitElementLoad(By.id("btnSetting"));
        driver.findElements(By.id("btnSetting")).get(0).click();
        WaitPageLoad();

        selectItemKdw003_2("勤務種類", "12/07(土)").click();
        WaitPageLoad();
        driver.switchTo().frame("window_6");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//td[contains(.,'090')]"));
        driver.findElements(By.xpath("//td[contains(.,'090')]")).get(1).click();
        driver.findElements(By.xpath("//td[contains(.,'090')]")).get(1).click();
        WaitElementLoad(By.id("btnSetting"));
        driver.findElements(By.id("btnSetting")).get(0).click();
        WaitPageLoad();

        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[contains(.,'閉じる')]"));
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        WaitElementLoad(By.xpath("//button[contains(.,'エラー参照')]"));
        driver.findElement(By.xpath("//button[contains(.,'エラー参照')]")).click();
        driver.switchTo().frame("window_7");
        // JavascriptExecutor js = (JavascriptExecutor)driver;
        Thread.sleep(1000);
        js.executeScript("$('.ui-widget-content').scrollTop(1000)");
        WaitPageLoad();
        screenShotFull();

        this.uploadTestLink(1131, 271);
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