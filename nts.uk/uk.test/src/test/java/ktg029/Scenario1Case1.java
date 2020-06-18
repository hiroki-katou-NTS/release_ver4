package ktg029;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import kdw003.Kdw003Common;


public class Scenario1Case1 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg029/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
        login("005517", "Jinjikoi5");

        setProcessYearMonth(1, "2020/05");

// 1.1  期間ラベル
        // Go to screen cgg008
        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.xpath("//li[@aria-controls='tab-1']"));
        driver.findElement(By.xpath("//li[@aria-controls='tab-1']")).click();

        screenShot();

        WaitPageLoad();
        this.uploadTestLink(218, 50);
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