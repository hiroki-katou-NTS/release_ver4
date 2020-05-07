package kaf005;

import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import common.TestRoot;

public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kaf005/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login申請者
        login("025445", "Jinjikoi5");

        //残業申請
        driver.get(domain + "nts.uk.at.web/view/kaf/005/a/index.xhtml?overworkatr=0");
        WaitPageLoad();
        Calendar inputdate = Calendar.getInstance();
        inputdate.add(Calendar.DATE, 1);
        driver.findElement(By.id("inputdate")).sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.id("inpStartTime1")).click();
        WaitPageLoad();
        driver.findElement(By.id("inpStartTime1")).clear();
        driver.findElement(By.id("inpStartTime1")).sendKeys("当日8:00");
        driver.findElement(By.id("inpEndTime1")).click();
        driver.findElement(By.id("inpEndTime1")).clear();
        driver.findElement(By.id("inpEndTime1")).sendKeys("当日23:00");
        driver.findElement(By.xpath("//button[@tabindex='14']")).click();
        WaitPageLoad();
        driver.findElement(By.id("appReason")).click();
        driver.findElement(By.id("appReason")).clear();
        driver.findElement(By.id("appReason")).sendKeys("autotest");
        screenShot();
        driver.findElement(By.xpath("//button[@tabindex='1']")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        screenShot();
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElements(By.xpath("//button")).get(1).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();

        //login承認者
        login("010392", "Jinjikoi5");

        //承認
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        driver.findElement(By.xpath("//input[contains(@id,'endInput')]")).clear();
        driver.findElement(By.xpath("//input[contains(@id,'endInput')]")).sendKeys(df1.format(inputdate.getTime()));
        WaitElementLoad(By.xpath("//button[@tabindex='6']"));
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        WaitPageLoad();
        List<WebElement> listEl = driver.findElements(By.xpath("//td[contains(.,'025445')]"));
        WebElement el = listEl.get(listEl.size() - 1);
        el.findElements(By.xpath("preceding-sibling::td")).get(0).click();
        new Actions(driver).moveToElement(el).perform();
        screenShot();
        WaitElementLoad(By.xpath("//button[@tabindex='1']"));
        driver.findElement(By.xpath("//button[@tabindex='1']")).click();
        _wait.until(d -> "承認しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        screenShot();
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitElementLoad(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]"));
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();

        //login申請者
        login("025445", "Jinjikoi5");

        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=0");
        WaitPageLoad();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(2).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(2).sendKeys(df1.format(inputdate.getTime()));
        WaitElementLoad(By.xpath("//button[@tabindex='6']"));
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        WaitPageLoad();
        js.executeScript("$('#grid2_scrollContainer').scrollTop($('#grid2_scrollContainer > div').height())");
        screenShot();
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).sendKeys(df1.format(inputdate.getTime()));
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        screenShot();

     //   this.uploadTestLink(4, 1);
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