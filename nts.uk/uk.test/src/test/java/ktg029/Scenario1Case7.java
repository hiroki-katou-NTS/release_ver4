package ktg029;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import kdw003.Kdw003Common;


public class Scenario1Case7 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg029/Scenario1Case7";
        this.init();
    }

    @Test
    public void test() throws Exception {

// 1.7  今月の申請締め切り日 - 有りの場合
        // login申請者
        login("005517", "Jinjikoi5");

        // Go to screen kaf022a
        driver.get(domain + "nts.uk.at.web/view/kaf/022/a/index.xhtml");
        WaitPageLoad();

        // Click check box
        if(!driver.findElement(By.id("a4_6")).findElement(By.xpath("label/input")).isSelected()) {
        	WaitElementLoad(By.id("a4_6"));
        	driver.findElement(By.id("a4_6")).click();
        	WaitElementLoad(By.className("proceed"));
        	driver.findElement(By.className("proceed")).click();
            WaitPageLoad();
        }
        screenShot();

        // Setting screen kmk012
        setProcessYearMonth(1, "2020/05");

        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();

        // tacke a photo
        screenShot();

        WaitPageLoad();
        this.uploadTestLink(546, 129);
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