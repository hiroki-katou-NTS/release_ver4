package ktg001;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;
import kdw003.Kdw003Common;

public class Scenario1Case2 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg001/Scenario1Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login
        login("004515", "Jinjikoi5");

        setProcessYearMonth(1, "2020/05");

        driver.get(domain+"nts.uk.at.web/view/kdw/004/a/index.xhtml");
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//a[contains(.,'017267')]")).click();
        WaitPageLoad();
        WebElement el4 = selectItemKdw003_1("承認", "05/12(火)");
        WebElement el5 = selectItemKdw003_1("承認", "05/13(水)");
        WebElement el6 = selectItemKdw003_1("承認", "05/14(木)");
        if(!el4.findElement(By.xpath("./label/input")).isSelected() &&
        	el4.findElement(By.xpath("./label/input")).isEnabled()){
        	el4.click();
        }
        if(!el5.findElement(By.xpath("./label/input")).isSelected() &&
        	el5.findElement(By.xpath("./label/input")).isEnabled()){
        	el5.click();
        }
        if(!el6.findElement(By.xpath("./label/input")).isSelected() &&
        	el6.findElement(By.xpath("./label/input")).isEnabled()){
        	el6.click();
        }
        screenShot();
        driver.findElement(By.xpath("//button[contains(.,'確定')]")).click();
        WaitPageLoad();
        driver.get(domain+"nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();
        screenShot();
        driver.get(domain+"nts.uk.at.web/view/kdw/004/a/index.xhtml");
        WaitPageLoad();
        screenShot();
        this.uploadTestLink(93, 19);
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