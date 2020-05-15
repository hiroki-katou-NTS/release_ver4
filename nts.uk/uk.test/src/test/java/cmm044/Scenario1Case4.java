package cmm044;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.openqa.selenium.*;

import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario1Case4 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm044/Scenario1Case4";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //代行者の登録
        login("004515", "Jinjikoi5");
        WaitPageLoad();

        Calendar today = Calendar.getInstance();

        Calendar startdate = Calendar.getInstance();
        startdate.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), 1);

        Calendar enddate = Calendar.getInstance();
        enddate.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, 1);
        enddate.add(Calendar.DATE, -1);

        //KDW004A 日別実績の確認
        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        WaitPageLoad();

        WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime.clear();
        startTime.sendKeys(df1.format(startdate.getTime()));
        WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime.clear();
        endTime.sendKeys(df1.format(enddate.getTime()));

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("document.activeElement.blur();");    //leave focus

        WaitElementLoad(By.xpath("//button[@id='extractBtn']"));
        driver.findElement(By.xpath("//button[@id='extractBtn']")).click();
        WaitPageLoad();
        screenShot();

        driver.findElement(By.xpath("//tr[@data-id='017267']/td[2]/a")).click();
        WaitPageLoad();

        //KDW003A 勤務報告書
        WebElement el1 = driver.findElement(By.xpath("//td[contains(.,'" + df2.format(today.getTime()) + "')]"));
        if ("true".equals(el1.findElements(By.xpath("following::td")).get(1).findElement(By.xpath("label/input")).getAttribute("checked")) ) {
            el1.findElements(By.xpath("following::td")).get(1).click();
        }

        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        screenShot();

        //kdw004
        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        WaitPageLoad();

        startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime.clear();
        startTime.sendKeys(df1.format(startdate.getTime()));
        endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime.clear();
        endTime.sendKeys(df1.format(enddate.getTime()));

        jse.executeScript("document.activeElement.blur();");    //leave focus

        WaitElementLoad(By.xpath("//button[@id='extractBtn']"));
        driver.findElement(By.xpath("//button[@id='extractBtn']")).click();
        WaitPageLoad();
        screenShot();

        //this.uploadTestLink(430, 97);

    }

    @AfterEach
    public void tearDown() throws Exception {
        //driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}