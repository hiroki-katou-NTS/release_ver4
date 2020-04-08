package kmw006;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;

public class Scenario3Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kmw006/Scenario3Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {

        String empCode = "029259";
        // login申請者
        login(empCode, "Jinjikoi5");
         // KDW003A 勤務報告書
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime.clear();
        startTime.sendKeys("2021/10/01");
        WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime.clear();
        endTime.sendKeys("2021/10/31");
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
        this.uploadTestLink(470, 107);
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