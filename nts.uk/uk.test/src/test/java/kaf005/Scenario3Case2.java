package kaf005;

import java.util.Calendar;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario3Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kaf005/Scenario3Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login申請者
        login("025497", "Jinjikoi5");

        //本人未チェックを確定
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        Calendar inputdate = Calendar.getInstance();
        inputdate.add(Calendar.DATE, -1);
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).sendKeys(df1.format(inputdate.getTime()));
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        WebElement el = driver.findElement(By.xpath("//td[contains(.,'" + df2.format(inputdate.getTime()) + "')]"));
        if (el.findElements(By.xpath("following::td")).get(0).getAttribute("class").indexOf("mgrid-manual-edit-target") == -1) {
            el.findElements(By.xpath("following::td")).get(0).click();
            driver.findElement(By.xpath("//button[@class='proceed']")).click();
            WaitPageLoad();
        }       
        screenShot();

        //残業申請
        driver.get(domain + "nts.uk.at.web/view/kaf/005/a/index.xhtml?overworkatr=0");
        WaitPageLoad();
        driver.findElement(By.id("inputdate")).sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.id("inpStartTime1")).click();
        WaitPageLoad();
        driver.findElement(By.id("inpStartTime1")).clear();
        driver.findElement(By.id("inpStartTime1")).sendKeys("当日9:00");
        driver.findElement(By.id("inpEndTime1")).click();
        driver.findElement(By.id("inpEndTime1")).clear();
        driver.findElement(By.id("inpEndTime1")).sendKeys("当日20:00");
        driver.findElement(By.xpath("//button[@tabindex='14']")).click();
        WaitPageLoad();
        driver.findElement(By.id("appReason")).click();
        driver.findElement(By.id("appReason")).clear();
        driver.findElement(By.id("appReason")).sendKeys("autotest");
        screenShot();

        driver.findElement(By.xpath("//button[@tabindex='1']")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        screenShot();

        this.uploadTestLink(171, 40);
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