package cli003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario2Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cli003/Scenario2Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login申請者

        login("010392", "Jinjikoi5");
        

        driver.get(domain+"nts.uk.com.web/view/cli/003/a/index.xhtml");

        WaitPageLoad();
        driver.findElement(By.id("buttonToScreen-b")).click();

        WaitPageLoad();
        driver.findElement(By.xpath("//tr[@data-id='1']")).click();

        WaitPageLoad();
        screenShot();
        driver.findElement(By.id("button_next_b")).click();

        setTimePicker("2019/7/1", "2019/7/31");

        WaitElementLoad(By.xpath("//div[@id='D2_5']/button[2]"));
        driver.findElement(By.xpath("//div[@id='D2_5']/button[2]")).click();
        
        progress("//iframe[contains(@name,'window_1')]");

        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.id("E2_1")).click();
        
        WaitPageLoad();
        driver.findElement(By.id("D3_1")).click();

        WaitPageLoad();
        screenShot();
        driver.findElement(By.id("button_next_b")).click();

        setTimePicker("2019/6/1", "2019/6/30");

        WaitElementLoad(By.xpath("//div[@id='D2_5']/button[1]"));
        driver.findElement(By.xpath("//div[@id='D2_5']/button[1]")).click();

        WaitElementLoad(By.id("ccg001-btn-search-drawer"));
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();

        WaitPageLoad();
        driver.findElement(By.id("ui-id-8")).click();

        WaitElementLoad(By.id("ui-id-9"));
        driver.findElement(By.xpath("//div[@class='input-wrapper']//input[@tabindex='1']")).clear();
        driver.findElement(By.xpath("//div[@class='input-wrapper']//input[@tabindex='1']")).sendKeys("001800");
        
        WaitElementLoad(By.xpath("//div[@class='input-wrapper']//button[@tabindex='1']"));
        driver.findElement(By.xpath("//div[@class='input-wrapper']//button[@tabindex='1']")).click();

        WaitElementLoad(By.xpath("//div[@id='nts-component-tree']/div[3]/div[2]//button[@tabindex='1']"));
        driver.findElement(By.xpath("//div[@id='nts-component-tree']/div[3]/div[2]//button[@tabindex='1']")).click();

        screenShotFull();
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();

        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id='employeeSearchD']//span[1]/span[1]")).click();

        progress("//iframe[contains(@name,'window_2')]");

        WaitPageLoad();
        driver.findElement(By.id("E2_2")).click();

        WebElement dialogCli003 = driver.findElement(By.xpath("//iframe[contains(@name,'window_1')]"));
        driver.switchTo().frame(dialogCli003);
        screenShotFull();
        driver.findElement(By.xpath("//button[@tabindex='2']")).click();

       WaitPageLoad();
       screenShotFull();
        
       this.uploadTestLink(625, 152);
    }

    public void progress(String wd) {
        WaitElementLoad(By.id("D3_2"));
        screenShotFull();
        driver.findElement(By.id("D3_2")).click();

        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.id("E2_3")).click();

        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.id("F1_1")).click();

        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.id("F3_1")).click();

        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.id("E2_2")).click();

        WebElement dialogCli003 = driver.findElement(By.xpath(wd));
        driver.switchTo().frame(dialogCli003);
        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.xpath("//button[@tabindex='2']")).click();
    }

    public void setTimePicker(String startDate, String endDate) {
        driver.findElements(By.xpath("//input[contains(@class,'ntsDatepicker')]")).get(9).clear();
        driver.findElements(By.xpath("//input[contains(@class,'ntsDatepicker')]")).get(9).sendKeys(startDate);
        driver.findElements(By.xpath("//input[contains(@class,'time-editor')]")).get(0).clear();
        driver.findElements(By.xpath("//input[contains(@class,'time-editor')]")).get(0).sendKeys("0:00:00");

        driver.findElements(By.xpath("//input[contains(@class,'ntsDatepicker')]")).get(10).clear();
        driver.findElements(By.xpath("//input[contains(@class,'ntsDatepicker')]")).get(10).sendKeys(endDate);
        driver.findElements(By.xpath("//input[contains(@class,'time-editor')]")).get(1).clear();
        driver.findElements(By.xpath("//input[contains(@class,'time-editor')]")).get(1).clear();
        driver.findElements(By.xpath("//input[contains(@class,'time-editor')]")).get(1).sendKeys("23:59:59");
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