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

public class Scenario4Case1 extends TestRoot {

    private WebDriverWait cli003_4_1_wait;

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cli003/Scenario4Case1";
        this.init();
        cli003_4_1_wait = new WebDriverWait(driver, 3600);
    }

    @Test
    public void test() throws Exception {
        //login申請者

        login("010392", "Jinjikoi5");


        driver.get(domain+"nts.uk.com.web/view/cli/003/a/index.xhtml");

        WaitPageLoad();
        driver.findElement(By.id("buttonToScreen-b")).click();

        WaitPageLoad();
        driver.findElement(By.xpath("//table[@id='list-box_b_grid']//tr[@data-id='6']")).click();

        WaitPageLoad();
        driver.findElement(By.xpath("//table[@id='list-box_b1_grid']//tr[@data-id='1']")).click();

        WaitPageLoad();
        screenShot();
        driver.findElement(By.id("button_next_b")).click();

        setDatePicker("2019/7/1", "2019/7/31");

        WaitElementLoad(By.xpath("//div[@id='C2_5']/button[2]"));
        driver.findElement(By.xpath("//div[@id='C2_5']/button[2]")).click();

        WaitElementLoad(By.id("C3_2"));
        screenShotFull();
        driver.findElement(By.id("C3_2")).click();

        setTimePicker("2019/7/1", "2019/7/31");

        WaitElementLoad(By.xpath("//div[@id='D2_5']/button[2]"));
        driver.findElement(By.xpath("//div[@id='D2_5']/button[2]")).click();

        progress("//li[@title='ページ 6']", "//iframe[contains(@name,'window_1')]");

        cli003_4_1_WaitPageLoad();
        screenShotFull();
        driver.findElement(By.id("E2_1")).click();

        WaitPageLoad();
        driver.findElement(By.id("D3_1")).click();

        setDatePicker("2019/6/1", "2019/6/30");

        WaitElementLoad(By.xpath("//div[@id='C2_5']/button[1]"));
        driver.findElement(By.xpath("//div[@id='C2_5']/button[1]")).click();

        WaitElementLoad(By.id("ccg001-btn-search-drawer"));
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();

        setCode();

        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id='employeeSearch']//span[1]/span[1]")).click();

        WaitElementLoad(By.id("C3_2"));
        screenShotFull();
        driver.findElement(By.id("C3_2")).click();

        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id='D1_4']//div[contains(@class,'start-datetime-editor')]//div[contains(@class,'date-container')]//input[1]")).clear();
        driver.findElement(By.xpath("//div[@id='D1_4']//div[contains(@class,'start-datetime-editor')]//div[contains(@class,'date-container')]//input[1]")).sendKeys("2019/6/1");
        driver.findElement(By.xpath("//div[@id='D1_4']//div[contains(@class,'start-datetime-editor')]//div[contains(@class,'time-container')]//input[1]")).clear();
        driver.findElement(By.xpath("//div[@id='D1_4']//div[contains(@class,'start-datetime-editor')]//div[contains(@class,'time-container')]//input[1]")).sendKeys("0:00:00");

        driver.findElement(By.xpath("//div[@id='D1_4']//div[contains(@class,'end-datetime-editor')]//div[contains(@class,'date-container')]//input[1]")).clear();
        driver.findElement(By.xpath("//div[@id='D1_4']//div[contains(@class,'end-datetime-editor')]//div[contains(@class,'date-container')]//input[1]")).sendKeys("2019/6/30");
        driver.findElement(By.xpath("//div[@id='D1_4']//div[contains(@class,'end-datetime-editor')]//div[contains(@class,'time-container')]//input[1]")).clear();
        driver.findElement(By.xpath("//div[@id='D1_4']//div[contains(@class,'end-datetime-editor')]//div[contains(@class,'time-container')]//input[1]")).clear();
        driver.findElement(By.xpath("//div[@id='D1_4']//div[contains(@class,'end-datetime-editor')]//div[contains(@class,'time-container')]//input[1]")).sendKeys("23:59:59");


        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id='D2_5']/button[2]")).click();

        progressCase("//li[@title='ページ 1']");
        WaitElementLoad(By.id("F1_1"));
        driver.findElement(By.id("F1_1")).click();

        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.id("F3_1")).click();

        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.id("E2_3")).click();

        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.id("F1_1")).click();

        WaitPageLoad();
        screenShotFull();

        this.uploadTestLink(653, 154);
    }

    public void setCode() {
        WaitPageLoad();
        driver.findElement(By.id("ui-id-8")).click();

        WaitElementLoad(By.id("ui-id-9"));
        driver.findElement(By.xpath("//div[@class='input-wrapper']//input[@tabindex='1']")).clear();
        driver.findElement(By.xpath("//div[@class='input-wrapper']//input[@tabindex='1']")).sendKeys("001800");

        WaitElementLoad(By.xpath("//div[@class='input-wrapper']//button[@tabindex='1']"));
        driver.findElement(By.xpath("//div[@class='input-wrapper']//button[@tabindex='1']")).click();

        WaitElementLoad(By.xpath("//div[@id='nts-component-tree']/div[3]/div[2]//button[@tabindex='1']"));
        driver.findElement(By.xpath("//div[@id='nts-component-tree']/div[3]/div[2]//button[@tabindex='1']")).click();

        WaitPageLoad();
        screenShot();
        driver.findElement(By.id("ccg001-btn-advanced-search")).click();
    }

    public void progress(String title, String wd) {
        progressCase(title);

        WaitElementLoad(By.id("F1_1"));
        screenShotFull();
        driver.findElement(By.id("F1_1")).click();

        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.id("F3_1")).click();

        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.id("E2_2")).click();

        WaitPageLoad();
        WebElement dialogCli003 = driver.findElement(By.xpath(wd));
        driver.switchTo().frame(dialogCli003);
        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.xpath("//button[@tabindex='2']")).click();

    }

    public void progressCase(String title) {
        WaitElementLoad(By.id("D3_2"));
        screenShotFull();
        driver.findElement(By.id("D3_2")).click();

        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.id("E2_3")).click();

        if (title.equals("//li[@title='ページ 6']")) {

            WaitPageLoad();
            WaitElementLoad(By.xpath("//table[@id='igGridLog']//tr[3]/td[1]"));
            screenShotFull();
            driver.findElement(By.xpath("//table[@id='igGridLog']//tr[3]/td[1]")).click();

            WaitPageLoad();
            screenShotFull();
            driver.findElement(By.xpath("//li[@title='ページ 5']")).click();

            WaitPageLoad();
            driver.findElement(By.xpath(title)).click();

            WaitPageLoad();
            screenShotFull();
            driver.findElement(By.xpath("//li[@title='ページ 8']")).click();

            WaitPageLoad();
            driver.findElement(By.xpath("//li[@title='ページ 10']")).click();

        }
        WaitPageLoad();
    }

    public void setDatePicker(String startDate, String endDate) {
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id='daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]")).clear();
        driver.findElement(By.xpath("//div[@id='daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]")).sendKeys(startDate);

        driver.findElement(By.xpath("//div[@id='daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]")).clear();
        driver.findElement(By.xpath("//div[@id='daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]")).sendKeys(endDate);
    }

    public void setTimePicker(String startDate, String endDate) {
        WaitPageLoad();
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

    private void cli003_4_1_WaitPageLoad() {
        try {
            Thread.sleep(1000);
			cli003_4_1_wait.until(d -> {
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
}