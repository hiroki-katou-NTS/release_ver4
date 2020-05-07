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

public class Scenario2Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm045/Scenario2Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login申請者 ; KAF006A 休暇申請
        login("025445", "Jinjikoi5");
        driver.get(domain + "nts.uk.at.web/view/kaf/006/a/index.xhtml");
        WaitPageLoad();    
        driver.findElement(By.xpath("//input[contains(@id,'inputdate')]")).clear();
        driver.findElement(By.xpath("//input[contains(@id,'inputdate')]")).sendKeys("2019/12/26");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//button[contains(@class,'nts-switch-button')]"));
        driver.findElements(By.xpath("//button[contains(@class,'nts-switch-button')]")).get(2).click();
        WaitElementLoad(By.xpath("//button[contains(@class,'nts-switch-button')]"));
        WaitPageLoad(); 
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitElementLoad(By.xpath("//button[@class='large']"));
        screenShot();
        driver.findElement(By.xpath("//button[@class='large']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElements(By.xpath("//button")).get(1).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
        WaitPageLoad();

        // login承認者 
        login("022497", "Jinjikoi5");
        //承認
        driver.get(domain + "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        driver.findElement(By.className("ui-igcombo-buttonicon")).click();
        driver.findElement(By.xpath("//li[@data-value='1']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='1']"));
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
        driver.findElement(By.xpath("//li[@data-value='1']")).click();
        screenShot();

        this.uploadTestLink(169, 39);
    }

    @AfterEach
    public void tearDown() throws Exception {
        //driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}