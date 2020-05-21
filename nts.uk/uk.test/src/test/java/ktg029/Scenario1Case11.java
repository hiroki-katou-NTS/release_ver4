package ktg029;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import kdw003.Kdw003Common;


public class Scenario1Case11 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg029/Scenario1Case11";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
        login("005517", "Jinjikoi5");

// 1.11 日別実績のエラー有無 - エラー無しの場合
        // Setting screen kmk012
        setProcessYearMonth(1, "2020/05");

        // Go to screen kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        dialog();

        // Clear Error
        setValueGrid2(2, 6, "1715");

        // Click save
        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        // Go to screen cgg008
        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.xpath("//span[contains(.,'日別実績のエラー有無')]"));

        // Tacke a photo
        screenShot();

        // Go to screen kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        WaitElementLoad(By.className("danger"));
        driver.findElement(By.className("danger")).click();
        WaitPageLoad();

        // Tacke a photo
        screenShot();

        WaitPageLoad();
        this.uploadTestLink(554, 133);
    }

	private void setValueGrid2(int rowNumber, int columnNumber, String value) throws InterruptedException {
		WaitElementLoad(By.xpath("//*[@id='dpGrid']/div[4]/table/tbody/tr[" + rowNumber + "]/td[" + columnNumber + "]"));
        driver.findElement(By.xpath("//*[@id='dpGrid']/div[4]/table/tbody/tr[" + rowNumber + "]/td[" + columnNumber + "]")).click();
        driver.findElement(By.xpath("//*[@id='dpGrid']/div[4]/table/tbody/tr[" + rowNumber + "]/td[" + columnNumber + "]")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id='dpGrid']/div[4]/table/tbody/tr[" + rowNumber + "]/td[" + columnNumber + "]/div/input")).sendKeys(value);
        Thread.sleep(2000);
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("function-content"));
        driver.findElement(By.id("function-content")).click();
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

    public void dialog() {
        try {
            WebElement dialogError = driver.findElement(By.xpath("//iframe[contains(@name,'window_1')]"));
            WaitPageLoad();
            if (dialogError.isDisplayed()) {
                driver.switchTo().frame(dialogError);
                WaitPageLoad();
                driver.findElement(By.xpath("//button[@id = 'dialogClose']")).click();
                WaitPageLoad();
            }
        } catch (Exception ex) {

        }
    }
}