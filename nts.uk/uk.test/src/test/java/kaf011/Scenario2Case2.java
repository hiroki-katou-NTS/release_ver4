package kaf011;

import java.util.Calendar;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;

public class Scenario2Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kaf011/Scenario2Case2";
        this.init();
    }
    @Test
    public void test() throws Exception {
        String msgexpected = "Msg_448";
        Calendar calendar1 = Calendar.getInstance();
	    Calendar calendar2 = Calendar.getInstance();
		int weekdays = calendar1.get(Calendar.DAY_OF_WEEK);
		switch (weekdays) {
		case 2:
	        calendar1.add(Calendar.DATE, 6);
	        calendar2.add(Calendar.DATE, 7);
			break;
		case 3:
	        calendar1.add(Calendar.DATE, 5);
	        calendar2.add(Calendar.DATE, 6);
			break;
		case 4:
	        calendar1.add(Calendar.DATE, 4);
	        calendar2.add(Calendar.DATE, 5);
			break;
		case 5:
	        calendar1.add(Calendar.DATE, 3);
	        calendar2.add(Calendar.DATE, 4);
			break;
		case 6:
	        calendar1.add(Calendar.DATE, 2);
	        calendar2.add(Calendar.DATE, 3);
            break;
        case 7: 
            calendar1.add(Calendar.DATE, 1);
            calendar2.add(Calendar.DATE, 2);
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
        WebElement startDate = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startDate.clear();
        startDate.sendKeys(inputdate1);
        WebElement endDate = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endDate.clear();
        endDate.sendKeys(inputdate2);
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        WaitPageLoad();
        WebElement el = driver.findElement(By.xpath("//td[contains(.,'" + df2.format(calendar1.getTime()) + "')]"));
        if (!"true".equals(el.findElements(By.xpath("following::td")).get(0).findElement(By.xpath("label/input")).getAttribute("checked")) ) {
            el.findElements(By.xpath("following::td")).get(0).click();
            driver.findElement(By.xpath("//button[@class='proceed']")).click();
            WaitPageLoad();
        }      
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
        this.uploadTestLink(185, 43);
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