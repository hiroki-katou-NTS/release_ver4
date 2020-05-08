package cli003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario7Case1 extends TestRoot {
    private Calendar calendar = Calendar.getInstance();

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cli003/Scenario7Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者

        login("010392", "Jinjikoi5");

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        String startDate = formatDate.format(calendar.getTime());
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endDate = formatDate.format(calendar.getTime());

        // ログイン
        selectedItem("//tr[@data-id='0']", "", startDate, endDate);
        showDialog();

        // 起動
        selectedItem("//tr[@data-id='1']", "", startDate, endDate);
        showDialog();

        // 個人情報
        selectedItem("//tr[@data-id='3']", "", startDate, endDate);
        showDialog();

        // データ修正_日別実績
        selectedItem("//tr[@data-id='6']", "//table[@id='list-box_b1_grid']//tr[@data-id='1']", startDate, endDate);
        showDialog();

        // データ修正_スケジュール
        selectedItem("//tr[@data-id='6']", "//table[@id='list-box_b1_grid']//tr[@data-id='0']", startDate, endDate);
        showDialog();

        // データ修正_月別実績
        selectedItem("//tr[@data-id='6']", "//table[@id='list-box_b1_grid']//tr[@data-id='2']", startDate, endDate);
        showDialog();

        this.uploadTestLink(704, 157);
    }

    public void selectedItem(String index, String index_b, String startDate, String endDate) {

        driver.get(domain + "nts.uk.com.web/view/cli/003/a/index.xhtml");

        WaitPageLoad();
        driver.findElement(By.id("buttonToScreen-b")).click();

        WaitPageLoad();
        driver.findElement(By.xpath(index)).click();

        if (index.equals("//tr[@data-id='6']")) {
            WaitPageLoad();
            driver.findElement(By.xpath(index_b)).click();
        }

        screenShot();
        driver.findElement(By.id("button_next_b")).click();

        if (index.equals("//tr[@data-id='3']")) {

            WaitPageLoad();
            driver.findElement(By.xpath("//div[@id='C2_5']/button[2]")).click();

            driver.findElement(By.id("C3_2")).click();
        }

        if (index.equals("//tr[@data-id='6']")) {
            if(index_b.equals("//table[@id='list-box_b1_grid']//tr[@data-id='2']")) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM");
                String startMonth = format.format(calendar.getTime());
                String endMonth = format.format(calendar.getTime());
                setDatePicker(startMonth, endMonth);
            } else {
                setDatePicker(startDate, endDate);
            }

            WaitPageLoad();
            driver.findElement(By.xpath("//div[@id='C2_5']/button[2]")).click();

            driver.findElement(By.id("C3_2")).click();
        }

        setTimePicker(startDate, endDate);
    }

    public void showDialog() {

        WaitElementLoad(By.xpath("//div[@id='D2_5']/button[2]"));
        driver.findElement(By.xpath("//div[@id='D2_5']/button[2]")).click();

        WaitElementLoad(By.id("D3_2"));
        driver.findElement(By.id("D3_2")).click();

        WaitPageLoad();
        screenShot();
        driver.findElement(By.id("E2_3")).click();

        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//div[@class='ui-dialog-buttonset']//button[@class='large']")).click();

        WaitPageLoad();
        driver.findElement(By.id("E2_2")).click();

        WebElement dialogCli003 = driver.findElement(By.xpath("//iframe[contains(@name,'window_1')]"));
        driver.switchTo().frame(dialogCli003);
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@tabindex='2']")).click();

        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//div[@class='ui-dialog-buttonset']//button[@class='large']")).click();
    }

    public void setDatePicker(String startDate, String endDate) {
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id='daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]"))
                .clear();
        driver.findElement(By.xpath("//div[@id='daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]"))
                .sendKeys(startDate);

        driver.findElement(By.xpath("//div[@id='daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]"))
                .clear();
        driver.findElement(By.xpath("//div[@id='daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]"))
                .sendKeys(endDate);
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
        final String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}