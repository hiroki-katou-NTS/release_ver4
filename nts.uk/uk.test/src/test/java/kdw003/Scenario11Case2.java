package kdw003;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
/**
 * シナリオ内変更：
 *  システム日を含む月に処理月を変更
 *
 * 前提：
 * 社員091636でログイン、日別実績の修正が見られる
 * システム日を含む月の実績がある、エラーアラーム無し
 * システム日が平日
 * @throws Exception
 */
public class Scenario11Case2 extends Kdw003Common {
    private Integer i=1;
    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario11Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login申請者
        login("091636", "Jinjikoi5");

        Calendar today = Calendar.getInstance();

        Calendar startdate = Calendar.getInstance();
        startdate.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), 1);

        Calendar enddate = Calendar.getInstance();
        enddate.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, 1);
        enddate.add(Calendar.DATE, -1);

        //kmk012 change closure 1
        setProcessYearMonth(1, df3.format(today.getTime()));

        showScreen003(startdate, enddate);

        if((today.get(Calendar.DAY_OF_MONTH)  + 1) >= 15) {
        	js.executeScript("$('.mgrid-free').scrollTop(400)");
        }
        if(!checkedBox(today.get(Calendar.DAY_OF_MONTH) + 1, 0)){
            clickCheckBox(today.get(Calendar.DAY_OF_MONTH) + 1, 0);
            driver.findElement(By.xpath("//div[@id='function-content']//button[1]")).click();

            WaitPageLoad();
            driver.findElement(By.xpath("//div[@class='ui-dialog-buttonset']//button[@class='large']")).click();
            dialog("window_" + i);
        }

        js.executeScript("$('.mgrid-free').scrollLeft(2000)");
        WaitPageLoad();
        screenShot();

        driver.get(domain + "nts.uk.at.web/view/kaf/005/a/index.xhtml");

        WaitPageLoad();
        driver.findElement(By.id("inputdate")).clear();
        driver.findElement(By.id("inputdate")).sendKeys(df1.format(today.getTime()));

        driver.findElement(By.id("inpStartTime1")).click();
        WaitPageLoad();
        driver.findElement(By.id("inpStartTime1")).clear();
        driver.findElement(By.id("inpStartTime1")).sendKeys("当日9:00");
        driver.findElement(By.id("inpEndTime1")).click();
        driver.findElement(By.id("inpEndTime1")).clear();
        driver.findElement(By.id("inpEndTime1")).sendKeys("当日20:00");

        WaitElementLoad(By.xpath("//button[@class='caret-bottom']"));
        driver.findElement(By.xpath("//button[@class='caret-bottom']")).click();

        WaitPageLoad();
        driver.findElement(By.xpath("//div[@class='ui-igcombo-buttonicon']")).click();

        WaitPageLoad();
        WaitElementLoad(By.xpath("//div[@class = 'ui-igcombo-list']//li[2]"));
        driver.findElement(By.xpath("//div[@class = 'ui-igcombo-list']//li[2]")).click();

        WaitElementLoad(By.xpath("//div[@id='functions-area']/button[1]"));
        driver.findElement(By.xpath("//div[@id='functions-area']/button[1]")).click();
        Thread.sleep(1000);
        screenShot();

        showScreen003(startdate, enddate);
        js.executeScript("$('.mgrid-free').scrollLeft(2000)");
        js.executeScript("$('.mgrid-free').scrollTop(400)");
        WaitPageLoad();
        screenShot();

        this.uploadTestLink(1153, 278);
    }
    public void showScreen003(Calendar startdate, Calendar enddate) {
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        dialog("window_" + i);
        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]"))
                .clear();
        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]"))
                .sendKeys(df1.format(startdate.getTime()));

        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]"))
                .clear();
        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]"))
                .sendKeys(df1.format(enddate.getTime()));
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@id = 'btnExtraction']")).click();

        dialog("window_" + i);
    }
    public void dialog(String wd) {
        try {
            WebElement dialogError = driver.findElement(By.xpath("//iframe[contains(@name,'" + wd + "')]"));
            WaitPageLoad();
            if (dialogError.isDisplayed()) {
                driver.switchTo().frame(dialogError);
                WaitPageLoad();
                driver.findElement(By.id("dialogClose")).click();
                i++;
            }
        } catch (Exception ex) {

        }
        WaitPageLoad();
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