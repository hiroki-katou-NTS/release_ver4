package ktg029;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import kdw003.Kdw003Common;


public class Scenario1Case5 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg029/Scenario1Case5";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
        login("005517", "Jinjikoi5");

        setProcessYearMonth(1, "2020/05");
// 1.5
        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();

        screenShot();

        // login承認者
        login("002363", "Jinjikoi5");

        // Go to cmm045
        Thread.sleep(5000);
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();

        WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime.clear();
        startTime.sendKeys("2020/05/01");
        WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime.clear();
        endTime.sendKeys("2020/05/31");
        WaitElementLoad(By.xpath("//button[text()='検索']"));
        driver.findElement(By.xpath("//button[text()='検索']")).click();
        WaitPageLoad();

        // Click item last
        int index1 = driver.findElements(By.className("details")).size();
        WaitElementLoad(By.className("details"));
        driver.findElements(By.className("details")).get(index1 - 1).click();
        WaitPageLoad();

        WaitElementLoad(By.xpath("//button[text()='否認']"));
        driver.findElement(By.xpath("//button[text()='否認']")).click();
        WaitPageLoad();
        screenShot();

        // login申請者
        login("005517", "Jinjikoi5");

        // Click go to cmm045
        WaitPageLoad();
        screenShot();
        driver.switchTo().frame(1);
        WaitPageLoad();
        WaitElementLoad(By.xpath("//*[@id='contents-area']/table[1]/tbody/tr[6]/td[2]/table/tbody/tr/td[2]/button"));
        driver.findElement(By.xpath("//*[@id='contents-area']/table[1]/tbody/tr[6]/td[2]/table/tbody/tr/td[2]/button"))
                .click();
        WaitPageLoad();
        screenShot();

        WaitPageLoad();
        this.uploadTestLink(542, 127);
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