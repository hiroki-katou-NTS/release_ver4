package kdw004;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import common.TestRoot;


public class Scenario2Case3 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw004/Scenario2Case3";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
    	login("007102", "Jinjikoi5");

        Calendar inputDate = Calendar.getInstance();
        inputDate.set(2020, 4, 15);
        Calendar inputStartDate = Calendar.getInstance();
        inputStartDate.set(2020, 4, 1);
        Calendar inputEndDate = Calendar.getInstance();
        inputEndDate.set(2020, 4, 31);

        // Setting screen cmm053
        driver.get(domain + "nts.uk.com.web/view/cmm/053/a/index.xhtml");

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        WaitElementLoad(By.id("A1_2"));
        driver.findElement(By.id("A1_2")).click();

        WaitElementLoad(By.id("A2_3"));
        driver.findElement(By.id("A2_3")).clear();
        driver.findElement(By.id("A2_3")).click();
        driver.findElement(By.id("A2_3")).sendKeys(df1.format(inputDate.getTime()));

        jse.executeScript("document.activeElement.blur();");    //leave focus
        WaitPageLoad();

        WaitElementLoad(By.id("A2_7"));
        driver.findElement(By.id("A2_7")).clear();
        driver.findElement(By.id("A2_7")).click();
        driver.findElement(By.id("A2_7")).sendKeys("017170");

        jse.executeScript("document.activeElement.blur();");    //leave focus

        WaitElementLoad(By.id("A1_3"));
        driver.findElement(By.id("A1_3")).click();

        driver.get(domain + "nts.uk.com.web/view/cmm/053/a/index.xhtml");
        WaitPageLoad();

        WaitElementLoad(By.id("A1_5"));
        driver.findElement(By.id("A1_5")).click();
        WaitPageLoad();
        screenShot();

        driver.switchTo().frame(0);

        WaitElementLoad(By.xpath("//tr[@data-row-idx='1']"));
        driver.findElement(By.xpath("//tr[@data-row-idx='1']")).click();
        driver.findElement(By.className("fixed-flex-layout-right")).click();
        WaitPageLoad();
        screenShot();

        // login承認者
    	login("017170", "Jinjikoi5");

        // Go to screen Kdw004a
        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        setKdw004Period(inputStartDate, inputEndDate);
        WaitPageLoad();
        screenShot();

        // login承認者
    	login("004515", "Jinjikoi5");

        // Go to screen Kdw004a
        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        setKdw004Period(inputStartDate, inputEndDate);
        WaitPageLoad();
        screenShot();

        WaitPageLoad();
        this.uploadTestLink(860, 206);
    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private void setKdw004Period(Calendar startdate, Calendar enddate) {
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
	}
}