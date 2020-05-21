package cmm044;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.*;

import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario1Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm044/Scenario1Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {
    	//承認する対象データの準備
        //login申請者
        login("017267", "Jinjikoi5");

        Calendar today = Calendar.getInstance();

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        Calendar startdate = Calendar.getInstance();
        startdate.set(yesterday.get(Calendar.YEAR), yesterday.get(Calendar.MONTH), 1);

        Calendar enddate = Calendar.getInstance();
        enddate.set(yesterday.get(Calendar.YEAR), yesterday.get(Calendar.MONTH) + 1, 1);
        enddate.add(Calendar.DATE, -1);

        //本人チェックを確定
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).sendKeys(df1.format(startdate.getTime()));
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).sendKeys(df1.format(enddate.getTime()));
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        // Check checkbox
        WebElement chk1 = driver.findElement(By.xpath("//td[contains(.,'" + df2.format(today.getTime()) + "')]"));
        if (!"true".equals(chk1.findElements(By.xpath("following::td")).get(0).findElement(By.xpath("label/input")).getAttribute("checked")) ) {
        	chk1.findElements(By.xpath("following::td")).get(0).click();
            driver.findElement(By.xpath("//button[@class='proceed']")).click();
            WaitPageLoad();
        }
        WebElement chk2 = driver.findElement(By.xpath("//td[contains(.,'" + df2.format(yesterday.getTime()) + "')]"));
        if (!"true".equals(chk2.findElements(By.xpath("following::td")).get(0).findElement(By.xpath("label/input")).getAttribute("checked")) ) {
        	chk2.findElements(By.xpath("following::td")).get(0).click();
            driver.findElement(By.xpath("//button[@class='proceed']")).click();
            WaitPageLoad();
        }

        //代行者の登録
        login("006310", "Jinjikoi5");

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
        if (!"true".equals(el1.findElements(By.xpath("following::td")).get(1).findElement(By.xpath("label/input")).getAttribute("checked")) ) {
            el1.findElements(By.xpath("following::td")).get(1).click();
        }
        WebElement el2 = driver.findElement(By.xpath("//td[contains(.,'" + df2.format(yesterday.getTime()) + "')]"));
        if (!"true".equals(el2.findElements(By.xpath("following::td")).get(1).findElement(By.xpath("label/input")).getAttribute("checked")) ) {
            el2.findElements(By.xpath("following::td")).get(1).click();
        }

        WaitPageLoad();
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

        this.uploadTestLink(424, 95);

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