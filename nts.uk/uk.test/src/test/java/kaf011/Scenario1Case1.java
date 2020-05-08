package kaf011;

import java.util.Calendar;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import common.TestRoot;

public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kaf011/Scenario1Case1";
        this.init();
    }
    @Test
    public void test() throws Exception {
        String msgexpected = "Msg_15";
        Calendar calendar1 = Calendar.getInstance();
	    Calendar calendar2 = Calendar.getInstance();
		int weekdays = calendar1.get(Calendar.DAY_OF_WEEK);
		switch (weekdays) {
		case 2:
	        calendar1.add(Calendar.DATE, 5);
	        calendar2.add(Calendar.DATE, 4);
			break;
		case 3:
	        calendar1.add(Calendar.DATE, 4);
	        calendar2.add(Calendar.DATE, 3);
			break;
		case 4:
	        calendar1.add(Calendar.DATE, 3);
	        calendar2.add(Calendar.DATE, 2);
			break;
		case 5:
	        calendar1.add(Calendar.DATE, 2);
	        calendar2.add(Calendar.DATE, 1);
			break;
		case 6:
	        calendar1.add(Calendar.DATE, 1);
	        calendar2.add(Calendar.DATE, 0);
            break;
        case 7:
            calendar1.add(Calendar.DATE, 0);
            calendar2.add(Calendar.DATE, -1);
            break;
		default:
            break;
        }
        String inputdate1 = df1.format(calendar1.getTime());
        String inputdate2 = df1.format(calendar2.getTime());
        // login申請者
        login("025445", "Jinjikoi5");
        // 勤務報告書
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime.clear();
        startTime.sendKeys(inputdate2);
        WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime.clear();
        endTime.sendKeys(inputdate1);
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        WaitPageLoad();
        screenShot();
        // 振休振出申請
        driver.get(domain + "nts.uk.at.web/view/kaf/011/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("recDatePicker")).clear();
        driver.findElement(By.id("recDatePicker")).sendKeys(inputdate1);
        driver.findElement(By.xpath("//input[@id='absDatePicker']")).click();
        driver.findElement(By.xpath("//input[@id='absDatePicker']")).clear();
        WaitElementLoad(By.xpath("//input[@id='absDatePicker']"));
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@id='absDatePicker']")).sendKeys(inputdate2);
        WaitElementLoad(By.xpath("//textarea[@id='appReason']"));
        driver.findElement(By.xpath("//textarea[@id='appReason']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//textarea[@id='appReason']")).clear();
        driver.findElement(By.xpath("//textarea[@id='appReason']")).sendKeys("autotest");
        WaitElementLoad(By.xpath("//span[@class='box']"));
        WebElement checkbox = driver.findElement(By.xpath("//span[@class='box']"));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        String msgactual = driver.findElement(By.id("ui-id-4")).findElement(By.className("control")).getText();
        if(msgexpected.equals(msgactual)){
            screenShot();
        }
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();
        //login承認者
        login("016976", "Jinjikoi5");
        //承認
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        driver.findElement(By.xpath("//input[contains(@id,'startInput')]")).clear();
        driver.findElement(By.xpath("//input[contains(@id,'startInput')]")).sendKeys(inputdate2);
        driver.findElement(By.xpath("//input[contains(@id,'endInput')]")).clear();
        driver.findElement(By.xpath("//input[contains(@id,'endInput')]")).sendKeys(inputdate1);
        WaitElementLoad(By.xpath("//button[@tabindex='6']"));
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id='combo-box']")).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath("//li[@data-value='10']")).click();
        List<WebElement> listEl =
        driver.findElements(By.xpath("//td[contains(.,'025445')]"));
        WebElement el = listEl.get(listEl.size() - 1);
        el.findElements(By.xpath("preceding-sibling::td")).get(0).click();
        new Actions(driver).moveToElement(el).perform();
        WaitElementLoad(By.xpath("//button[@tabindex='1']"));
        driver.findElement(By.xpath("//button[@tabindex='1']")).click();
        _wait.until(d ->"承認しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitElementLoad(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]"));
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();
        //login申請者
        login("025445", "Jinjikoi5");
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=0");
        WaitPageLoad();
        WebElement startDate = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startDate.clear();
        startDate.sendKeys(inputdate2);
        WebElement endDate = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker")).click();
        endDate.clear();
        endDate.sendKeys(inputdate1);
        WaitElementLoad(By.xpath("//button[@tabindex='6']"));
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        WaitPageLoad();     
        driver.findElement(By.xpath("//div[@id='combo-box']")).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath("//li[@data-value='10']")).click();
        WaitPageLoad();
        List<WebElement> listEl2 =driver.findElements(By.xpath("//td[contains(.,'025445')]"));
        WebElement el2 = listEl2.get(listEl2.size() - 1);
        new Actions(driver).moveToElement(el2).perform();
        screenShot();
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        WebElement startTime1 = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime1.clear();
        startTime1.sendKeys(inputdate2);
        WebElement endTime1 = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime1.clear();
        endTime1.sendKeys(inputdate1);
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        WaitPageLoad();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        screenShot();
        this.uploadTestLink(113, 24);
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