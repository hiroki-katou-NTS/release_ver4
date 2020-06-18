package cmm044;

import java.util.Calendar;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;

import common.TestRoot;
/**
 * @author ThuHT
 */

public class Scenario1Case9 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm044/Scenario1Case9";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login申請者
        login("025445", "Jinjikoi5");

        Calendar nextweekend = Calendar.getInstance();
        nextweekend.add(Calendar.DATE, Calendar.SATURDAY - nextweekend.get(Calendar.DAY_OF_WEEK));

        //KAF010A 休日出勤申請
        driver.get(domain + "nts.uk.at.web/view/kaf/010/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("inputdate")).click();
        driver.findElement(By.id("inputdate")).sendKeys(df1.format(nextweekend.getTime()));
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("inputdate"));
        driver.findElement(By.id("inpStartTime1")).click();
        driver.findElement(By.id("inpStartTime1")).clear();
        driver.findElement(By.id("inpStartTime1")).sendKeys("当日9:00");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("inpStartTime1"));
        driver.findElement(By.id("inpEndTime1")).click();
        driver.findElement(By.id("inpEndTime1")).clear();
        driver.findElement(By.id("inpEndTime1")).sendKeys("当日17:30");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("inpEndTime1"));
        driver.findElement(By.xpath("//button[@class='caret-bottom']")).click();
        WaitElementLoad(By.xpath("//button[@class='caret-bottom']"));
        driver.findElement(By.xpath("//textarea[@id='appReason']")).sendKeys("Auto KAF010 休日出勤申請 ");
        WaitElementLoad(By.xpath("//textarea[@id='appReason']"));
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
        login("022497", "Jinjikoi5");
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        driver.findElement(By.className("ui-igcombo-buttonicon")).click();
        driver.findElement(By.xpath("//li[@data-value='6']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='6']"));
        List<WebElement> listEl = driver.findElements(By.xpath("//td[contains(.,'025445')]"));
        WebElement el = listEl.get(listEl.size() - 1);
        el.findElements(By.xpath("preceding-sibling::td")).get(0).click();
        new Actions(driver).moveToElement(el).perform();

        WaitElementLoad(By.xpath("//button[@class='details']"));
        driver.findElement(By.xpath("//button[@class='details']")).click();
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//*[@id='functions-area']/div/div[2]/div[1]/button")).click();
        WaitElementLoad(By.xpath("//*[@id='functions-area']/div/div[2]/div[1]/button"));
        screenShot();
        this.uploadTestLink(403, 91);
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