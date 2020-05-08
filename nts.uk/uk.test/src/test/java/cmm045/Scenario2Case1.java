package cmm045;

import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;

import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario2Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm045/Scenario2Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login承認者一覧
        login("010392", "Jinjikoi5");
        
        //承認
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        driver.findElement(By.className("ui-igcombo-buttonicon")).click();
        WaitElementLoad(By.xpath("ui-igcombo-buttonicon"));
        driver.findElement(By.xpath("//li[@data-value='10']")).click();
        screenShot();
        WaitPageLoad();
        List<WebElement> listEl = driver.findElements(By.xpath("//td[contains(.,'025445')]"));
        WebElement el = listEl.get(listEl.size() - 1);
        el.findElements(By.xpath("preceding-sibling::td")).get(0).click();
        new Actions(driver).moveToElement(el).perform();
        screenShot();
        WaitElementLoad(By.xpath("//button[@tabindex='1']"));
        driver.findElement(By.xpath("//button[@tabindex='1']")).click();
        _wait.until(d -> "承認しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        screenShot();
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitElementLoad(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]"));
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();

        //login申請者
        login("025445", "Jinjikoi5");
        WaitPageLoad();
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=0");
        WaitPageLoad();
        driver.findElement(By.className("ui-igcombo-buttonicon")).click();
        driver.findElement(By.xpath("//li[@data-value='10']")).click();
        List<WebElement> listEl2 = driver.findElements(By.xpath("//td[contains(.,'025445')]"));
        WebElement el2 = listEl2.get(listEl2.size() - 1);
        new Actions(driver).moveToElement(el2).perform();
        screenShot();

        this.uploadTestLink(164, 37);
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