package kmw006;

import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import common.TestRoot;

public class Scenario2Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kmw006/Scenario2Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {

        String empCode = "010207";
        // login申請者
        login(empCode, "Jinjikoi5");
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
        driver.findElement(By.id("inputdate")).sendKeys("2022/01/11");
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
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitElementLoad(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]"));
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();
        // Login 承認一覧
        login("007227", "Jinjikoi5");
        // CMM045A 承認一覧
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        driver.findElement(By.xpath("//input[contains(@id,'startInput')]")).clear();
        driver.findElement(By.xpath("//input[contains(@id,'startInput')]")).sendKeys("2022/01/11");
        driver.findElement(By.xpath("//input[contains(@id,'endInput')]")).clear();
        driver.findElement(By.xpath("//input[contains(@id,'endInput')]")).sendKeys("2022/01/12");
        WaitElementLoad(By.xpath("//button[@tabindex='6']"));
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id='combo-box']")).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath("//li[@data-value='1']")).click();
        List<WebElement> listEl =
        driver.findElements(By.xpath("//td[contains(.,'010207')]"));
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
        login(empCode, "Jinjikoi5");
         // KDW003A 勤務報告書
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime.clear();
        startTime.sendKeys("2022/01/01");
        WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime.clear();
        endTime.sendKeys("2022/01/31");
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        WaitPageLoad();
        driver.findElement(By.id("btnVacationRemaining")).click();
        driver.findElement(By.xpath("//td[contains(.,'有休')]")).click();
        WaitPageLoad();
        js.executeScript("$('iframe').parent().parent().css(`top`,`229.5px`)");
        js.executeScript("$('#vacationRemaining-content').css(`visibility`,`hidden`)");
        WaitElementLoad(By.xpath("//iframe[contains(@name,'window')]"));
        WebElement dialogKDW003 = driver.findElement(By.xpath("//iframe[contains(@name,'window')]"));
        driver.switchTo().frame(dialogKDW003);
        screenShot();
        driver.findElement(By.id("btnCancel")).click();
        this.uploadTestLink(462, 104);
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