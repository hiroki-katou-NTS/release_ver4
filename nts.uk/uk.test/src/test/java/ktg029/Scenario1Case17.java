
package ktg029;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import kdw003.Kdw003Common;


public class Scenario1Case17 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg029/Scenario1Case17";
        this.init();
    }

    @Test
    public void test() throws Exception {

// 1.17
        // login申請者
        login("000001", "0");

        // Setting screen kmk012
        setProcessYearMonth(1, "2020/05");

        // Go to screen cps001
        driver.get(domain + "nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();

        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.xpath("//a[contains(.,'入力検索')]"));
        driver.findElement(By.xpath("//a[contains(.,'入力検索')]")).click();
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("005517");
        driver.findElements(By.xpath("//button[contains(@class,'proceed caret-bottom pull-right')]")).get(0).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//div[@id='ccg001-btn-KCP005-apply']/div")).get(1).click();
        WaitPageLoad();

        WaitElementLoad(By.xpath("//*[@id='lefttabs']/ul/li[2]"));
        driver.findElement(By.xpath("//*[@id='lefttabs']/ul/li[2]")).click();
        WaitPageLoad();

        WaitElementLoad(By.xpath("//*[@class='ui-igcombo-wrapper ntsControl']"));
        driver.findElement(By.xpath("//*[@class='ui-igcombo-wrapper ntsControl']")).click();
        WaitPageLoad();
        js.executeScript("$('.ui-igcombo-list').scrollTop($('.ui-igcombo-list')[0].scrollHeight)");
        WaitPageLoad();
        WaitElementLoad(By.xpath("//li[contains(.,'年間褒賞休暇情報')]"));
        driver.findElement(By.xpath("//li[contains(.,'年間褒賞休暇情報')]")).click();
        WaitPageLoad();

        driver.findElement(By.id("COM1000000000000000CS00058IS00628")).click();
        driver.switchTo().frame(0);
        WaitPageLoad();
        driver.findElement(By.id("idDeadline")).click();
        driver.findElement(By.id("idDeadline")).clear();
        driver.findElement(By.id("idDeadline")).sendKeys("2021/03/31");
        driver.findElement(By.id("idDateGrantInp")).click();
        driver.findElement(By.id("idDateGrantInp")).clear();
        driver.findElement(By.id("idDateGrantInp")).sendKeys("2020/04/01");
        driver.findElement(By.id("dayNumberOfGrants")).click();
        driver.findElement(By.id("dayNumberOfGrants")).clear();
        driver.findElement(By.id("dayNumberOfGrants")).sendKeys("1.0");
        driver.findElement(By.id("dayNumberOfUse")).click();
        driver.findElement(By.id("dayNumberOfUse")).clear();
        driver.findElement(By.id("dayNumberOfUse")).sendKeys("0.0");
        driver.findElement(By.id("dayNumberOver")).click();
        driver.findElement(By.id("dayNumberOver")).clear();
        driver.findElement(By.id("dayNumberOver")).sendKeys("0.0");
        driver.findElement(By.id("dayNumberOfReam")).click();
        driver.findElement(By.id("dayNumberOfReam")).clear();
        driver.findElement(By.id("dayNumberOfReam")).sendKeys("1.0");

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        driver.switchTo().defaultContent();
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitPageLoad();
        driver.switchTo().frame(0);
        driver.findElement(By.xpath("//div[@id='functions-area']/button[4]")).click();

        // Tacke a photo
        screenShot();

        // login申請者
        login("005517", "Jinjikoi5");

        WaitPageLoad();
        js.executeScript("$('.placement-wraper').scrollTop($('.placement-wraper')[1].scrollHeight)");
        WaitPageLoad();
        Thread.sleep(30000);
        screenShot();

        driver.get(domain + "nts.uk.at.web/view/kaf/006/a/index.xhtml");
        WaitPageLoad();
        Calendar inputdate = Calendar.getInstance();
        inputdate.clear();
        inputdate.set(2020, 05, 26);
        driver.findElement(By.id("inputdate")).sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        WaitPageLoad();
        WaitElementLoad(By.xpath("//div[@id ='hdType']/button[2]"));
        driver.findElement(By.xpath("//div[@id ='hdType']/button[2]")).click();
        driver.findElement(By.xpath("//*[@id = 'workTypes']/.//*[@class ='ui-igcombo-buttonicon']")).click();
        js.executeScript("$('.ui-igcombo-list').scrollTop($('.ui-igcombo-list')[0].scrollHeight)");
        WaitPageLoad();
        driver.findElement(By.xpath("//li[@data-value='131']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();

        // Tacke a photo
        screenShot();

        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();
        js.executeScript("$('.placement-wraper').scrollTop($('.placement-wraper')[1].scrollHeight)");
        WaitPageLoad();

        Thread.sleep(30000);
        screenShot();

        WaitPageLoad();
        this.uploadTestLink(566, 139);
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