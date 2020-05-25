package cmm044;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario1Case3 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm044/Scenario1Case3";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //代行者の登録
        login("006310", "Jinjikoi5");
        WaitPageLoad();

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        Calendar startdate = Calendar.getInstance();
        startdate.set(yesterday.get(Calendar.YEAR), yesterday.get(Calendar.MONTH), 1);

        Calendar enddate = Calendar.getInstance();
        enddate.set(yesterday.get(Calendar.YEAR), yesterday.get(Calendar.MONTH) + 1, 1);
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
        driver.findElement(By.xpath("//th[@aria-label='" + yesterday.get(Calendar.DATE) + "']")).click();
        WaitPageLoad();

        //KDW003A 勤務報告書
        WebElement chk = driver.findElements(By.xpath("//tr[contains(.,'017267')]/td/label[@class='ntsCheckBox']")).get(1);
        if ("true".equals(chk.findElement(By.xpath("input")).getAttribute("checked")) ) {
        	chk.click();
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

        this.uploadTestLink(427, 96);

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