package ktg029;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import kdw003.Kdw003Common;


public class Scenario1Case6 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg029/Scenario1Case6";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
        login("005517", "Jinjikoi5");

 // 1.6
        // Setting screen kmk012
        setProcessYearMonth(1, "2020/05");

        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();
        screenShot();

        // Go to kaf006
        driver.get(domain + "nts.uk.at.web/view/kaf/006/a/index.xhtml");
        WaitPageLoad();

        driver.findElement(By.xpath(".//*[@id='functions-area']/div/label/span[1]")).click();

        // Input into Date
        WaitElementLoad(By.id("inputdate"));
        driver.findElement(By.id("inputdate")).sendKeys("2020/05/21");
        WaitElementLoad(By.className("left-content-kaf006"));
        driver.findElement(By.className("left-content-kaf006")).click();
        // Select switch button
        Thread.sleep(2000);
        WaitElementLoad(By.xpath(".//*[@id='hdType']/button[3]"));
        driver.findElement(By.xpath(".//*[@id='hdType']/button[3]")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath(".//*[@id='hdType']/button[3]")).click();
        WaitPageLoad();

        // Click save
        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        // login承認者
        login("002363", "Jinjikoi5");

        // Go to cmm045
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();

        // Click item last
        WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime.clear();
        startTime.sendKeys("2020/05/01");
        WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime.clear();
        endTime.sendKeys("2020/05/31");
        WaitElementLoad(By.xpath("//button[text()='検索']"));
        driver.findElement(By.xpath("//button[text()='検索']")).click();
        WaitPageLoad();

        int index2 = driver.findElements(By.className("details")).size();
        driver.findElements(By.className("details")).get(index2 - 1).click();
        WaitPageLoad();

        // Go to kaf010b
        WaitElementLoad(By.xpath("//button[text()='差し戻し']"));
        driver.findElement(By.xpath("//button[text()='差し戻し']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        WaitElementLoad((By.className("proceed")));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();
        driver.switchTo().defaultContent();
        WaitPageLoad();
//        WebElement dialog = driver.findElement(By.xpath("//div[@role='dialog'][4]"));
//        driver.switchTo().frame(dialog);
        driver.findElement(By.xpath("//div[@role='dialog'][4]//div[3]/div[1]/button[1]")).click();

        WaitPageLoad();
        screenShot();

         // login申請者
        login("005517", "Jinjikoi5");

        WaitPageLoad();
        screenShot();
        // Click go to cmm045
        driver.switchTo().frame(1);
        Thread.sleep(2000);
        WaitElementLoad(By.xpath("//*[@id='contents-area']/table[1]/tbody/tr[7]/td[2]/table/tbody/tr/td[2]/button"));
        driver.findElement(By.xpath("//*[@id='contents-area']/table[1]/tbody/tr[7]/td[2]/table/tbody/tr/td[2]/button")).click();
        WaitPageLoad();
        screenShot();

        WaitPageLoad();
        this.uploadTestLink(544, 128);
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