package ktg029;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import kdw003.Kdw003Common;

public class Scenario1Case10 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg029/Scenario1Case10";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
        login("005517", "Jinjikoi5");

// 1.10 日別実績のエラー有無 - エラー有りの場合（□エラー一覧表示）
        // Setting screen kmk012
        setProcessYearMonth(1, "2020/05");

        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();

        driver.switchTo().frame(1);

        WaitPageLoad();
        // Check check box
        WaitElementLoad(By.xpath("//*[@id='contents-area']/table[3]/tbody/tr/td/div/label/span[1]"));
        driver.findElement(By.xpath("//*[@id='contents-area']/table[3]/tbody/tr/td/div/label/span[1]")).click();

        // Click go to kdw003a
        WaitElementLoad(By.xpath("//*[@id='contents-area']/table[2]/tbody/tr/td[2]/table/tbody/tr/td[2]/button"));
        screenShot();
        driver.findElement(By.xpath("//*[@id='contents-area']/table[2]/tbody/tr/td[2]/table/tbody/tr/td[2]/button")).click();
        WaitPageLoad();

        // tacke a photo
        screenShot();

        this.uploadTestLink(552, 132);
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