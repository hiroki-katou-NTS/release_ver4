package ktg029;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import kdw003.Kdw003Common;


public class Scenario1Case3 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg029/Scenario1Case3";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
        login("005517", "Jinjikoi5");

// 1.3  承認された件数
        setProcessYearMonth(1, "2020/05");

        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();

        // Take a photo 0件の状態 screen ccg008
        screenShot();

        // Go to screen kaf006a
        driver.get(domain + "nts.uk.at.web/view/kaf/006/a/index.xhtml");
        WaitPageLoad();

        driver.findElement(By.xpath(".//*[@id='functions-area']/div/label/span[1]")).click();
        Thread.sleep(2000);

        // Input into Date
        WaitElementLoad(By.id("inputdate"));
        driver.findElement(By.id("inputdate")).sendKeys("2020/05/20");
        Thread.sleep(5000);

        // Select switch button
        driver.findElement(By.xpath(".//*[@id='hdType']/button[1]")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath(".//*[@id='hdType']/button[1]")).click();
        WaitPageLoad();

        // Click save
        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();

        // Take a photo screen kaf006a
        WaitPageLoad();
        screenShot();

        // login承認者
        login("002363", "Jinjikoi5");

        // Go to cmm045
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();

        WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime.clear();
        startTime.sendKeys("2020/05/20");
        WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime.clear();
        endTime.sendKeys("2020/05/20");
        WaitElementLoad(By.xpath("//button[text()='検索']"));
        driver.findElement(By.xpath("//button[text()='検索']")).click();
        WaitPageLoad();

        // Click item last
        int index = driver.findElements(By.className("details")).size();
        driver.findElements(By.className("details")).get(index - 1).click();
        WaitPageLoad();

        WaitElementLoad((By.className("proceed")));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();
        screenShot();

        // login申請者
        login("005517", "Jinjikoi5");

        // Click go to cmm045
        WaitPageLoad();
        screenShot();
        driver.switchTo().frame(1);
        WaitElementLoad(By.className("approved-no"));
        driver.findElement(By.className("approved-no")).click();
        WaitPageLoad();

        // tacke a photo
        screenShot();

        WaitPageLoad();
        this.uploadTestLink(538, 125);
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