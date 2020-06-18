package ktg029;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import kdw003.Kdw003Common;

public class Scenario1Case4 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg029/Scenario1Case4";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
        login("005517", "Jinjikoi5");

// 1.4  未承認件数

        setProcessYearMonth(1, "2020/05");

        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();

        screenShot();

        // Go to screen kaf006a
        driver.get(domain + "nts.uk.at.web/view/kaf/006/a/index.xhtml");
        WaitPageLoad();

        driver.findElement(By.xpath(".//*[@id='functions-area']/div/label/span[1]")).click();

        // Input into Date
        WaitElementLoad(By.id("inputdate"));
        driver.findElement(By.id("inputdate")).sendKeys("2020/05/27");
        WaitElementLoad(By.className("left-content-kaf006"));
        driver.findElement(By.className("left-content-kaf006")).click();
        // Select switch button
        Thread.sleep(2000);
        WaitElementLoad(By.xpath(".//*[@id='hdType']/button[3]"));
        driver.findElement(By.xpath(".//*[@id='hdType']/button[3]")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath(".//*[@id='hdType']/button[3]")).click();
        WaitPageLoad();

        // Click save
        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();

        // Take a photo screen kaf006a
        screenShot();

        // Go to screen cgg008
        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();
        screenShot();

        driver.switchTo().frame(1);
        Thread.sleep(5000);

        // Click go to screen cmm045
        driver.findElement(By.xpath(".//*[@id='contents-area']/table[1]/tbody/tr[5]/td[2]/table/tbody/tr/td[2]/button"))
                .click();
        WaitPageLoad();

        screenShot();

        WaitPageLoad();
        this.uploadTestLink(540, 126);
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