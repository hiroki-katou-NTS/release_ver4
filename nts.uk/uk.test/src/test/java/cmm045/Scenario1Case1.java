package cmm045;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm045/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        login("025445", "Jinjikoi5");
        WaitPageLoad();

        //KAF011A 振休振出申請
        driver.get(domain + "nts.uk.at.web/view/kaf/011/a/index.xhtml");
        WaitPageLoad();
        //振出申請
        driver.findElement(By.id("recDatePicker")).sendKeys("2019/12/28");
        driver.findElement(By.id("recTime1Start")).clear();
        WaitElementLoad(By.id("recTime1Start"));
        driver.findElement(By.id("recTime1Start")).sendKeys("当日9:00");
        driver.findElement(By.id("recTime1End")).click();
        driver.findElement(By.id("recTime1End")).clear();
        driver.findElement(By.id("recTime1End")).sendKeys("当日17:30");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("recTime1End"));
        WaitPageLoad();
        //振休申請    
        driver.findElement(By.id("absDatePicker")).sendKeys("2019/12/30");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("absDatePicker"));
        WaitElementLoad(By.id("appReason"));
        driver.findElement(By.id("appReason")).sendKeys("Autotest KAF011A 振休振出申請");
        WaitElementLoad(By.id("appReason"));
        screenShot();
        //登録
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        screenShot();
        driver.findElement(By.xpath("//button[@class='large']")).click();
        //登録時にメールを送信する
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElements(By.xpath("//button")).get(1).click();
        //CMM045A 申請履歴一覧
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=0");       
        WaitPageLoad();       
        WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime.clear();
        startTime.sendKeys("2019/12/25");   
        WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime.clear();
        endTime.sendKeys("2020/1/1");   
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        driver.findElement(By.className("ui-igcombo-buttonicon")).click();
        driver.findElement(By.xpath("//li[@data-value='10']")).click();
        screenShot();
        this.uploadTestLink(117, 39);
     
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