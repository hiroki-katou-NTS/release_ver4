package cli003;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import common.TestRoot;

public class Scenario5Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cli003/Scenario5Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login申請者

        login("010392", "Jinjikoi5");


        driver.get(domain+"nts.uk.com.web/view/cli/003/a/index.xhtml");

        WaitPageLoads();
        driver.findElement(By.id("buttonToScreen-b")).click();

        WaitPageLoads();
        driver.findElement(By.xpath("//table[@id='list-box_b_grid']//tr[@data-id='6']")).click();

        WaitPageLoads();
        driver.findElement(By.xpath("//table[@id='list-box_b1_grid']//tr[@data-id='0']")).click();

        WaitPageLoads();
        screenShot();
        driver.findElement(By.id("button_next_b")).click();

        caseTest("2019/7/1", "2019/7/31");

        progressCase(0);

        WaitPageLoads();
        screenShotFull();
        driver.findElement(By.id("F3_1")).click();

        WaitPageLoads();
        screenShotFull();
        driver.findElement(By.id("E2_2")).click();

        WaitPageLoads();
        WebElement dialogCli003 = driver.findElement(By.xpath("//iframe[contains(@name,'window_1')]"));
        driver.switchTo().frame(dialogCli003);
        WaitPageLoads();
        screenShotFull();
        driver.findElement(By.xpath("//button[@tabindex='2']")).click();

        WaitPageLoads();
        screenShotFull();
        driver.findElement(By.id("E2_1")).click();

        WaitPageLoads();
        driver.findElement(By.id("D3_1")).click();

        caseTest("2019/6/1", "2019/6/30");

        progressCase(1);

        WaitPageLoads();
        screenShotFull();
        driver.findElement(By.id("F3_1")).click();

        WaitPageLoads();
        screenShotFull();
        driver.findElement(By.id("E2_3")).click();

        WaitPageLoads();
        driver.findElement(By.id("F1_1")).click();

        WaitPageLoads();
        screenShotFull();

        this.uploadTestLink(671, 155);
    }

    public void WaitPageLoads() {
        WebDriverWait _wait = new WebDriverWait(driver, 3600);
        try {
            Thread.sleep(1000);
            _wait.until(d -> {
                try {
                    d.findElement(By.xpath("//div[contains(@class,'blockOverlay')]"));
                } catch (NoSuchElementException e) {
                    return true;
                }
                return false;
            });
        } catch (Exception e) {
        }
    }

    public void caseTest(String start, String end) {
        setDatePicker(start, end);

        WaitElementLoad(By.xpath("//div[@id='C2_5']/button[2]"));
        driver.findElement(By.xpath("//div[@id='C2_5']/button[2]")).click();

        WaitElementLoad(By.id("C3_2"));
        screenShot();
        driver.findElement(By.id("C3_2")).click();

        setTimePicker(start, end);

        WaitElementLoad(By.xpath("//div[@id='D2_5']/button[2]"));
        driver.findElement(By.xpath("//div[@id='D2_5']/button[2]")).click();
    }

    public void progressCase(Integer index) {
        WaitElementLoad(By.id("D3_2"));
        screenShotFull();
        driver.findElement(By.id("D3_2")).click();

        WaitPageLoads();
        screenShotFull();
        driver.findElement(By.id("E2_3")).click();

        if (index == 0) {
            WaitPageLoads();
            WaitElementLoad(By.xpath("//table[@id='igGridLog']//tr[2]/td[1]"));
            driver.findElement(By.xpath("//table[@id='igGridLog']//tr[2]/td[1]")).click();
        }

        WaitPageLoads();
        WaitElementLoad(By.id("F1_1"));
        driver.findElement(By.id("F1_1")).click();
    }

    public void setDatePicker(String startDate, String endDate) {
        WaitPageLoads();
        driver.findElement(By.xpath("//div[@id='daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]")).clear();
        driver.findElement(By.xpath("//div[@id='daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]")).sendKeys(startDate);

        driver.findElement(By.xpath("//div[@id='daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]")).clear();
        driver.findElement(By.xpath("//div[@id='daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]")).sendKeys(endDate);
    }

    public void setTimePicker(String startDate, String endDate) {
        WaitPageLoads();
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