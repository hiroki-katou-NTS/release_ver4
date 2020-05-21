package ktg029;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import kdw003.Kdw003Common;

public class Scenario1Case2 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg029/Scenario1Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
        login("005517", "Jinjikoi5");

// 1.2  翌月切替ボタン
        setProcessYearMonth(1, "2020/05");

        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();

        // Take a photo screen ccg008 button 当月切替
        screenShot();
        driver.switchTo().frame(1);
        WaitPageLoad();
        // Click button 当月切替 screen ccg008
        WaitElementLoad(By.xpath("//button[@class='small']"));
        driver.findElement(By.xpath("//button[@class='small']")).click();
        WaitPageLoad();
        // Take a photo screen ccg008 button 翌月切替
        screenShot();

        // Click button 翌月切替 screen ccg008
        WaitElementLoad(By.xpath("//button[@class='small']"));
        driver.findElement(By.xpath("//button[@class='small']")).click();
        // Take a photo screen ccg008 button 当月切替
        WaitPageLoad();
        screenShot();

        this.uploadTestLink(536, 124);
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