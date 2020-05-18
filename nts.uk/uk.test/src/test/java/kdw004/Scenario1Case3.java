package kdw004;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import kdw003.Kdw003Common;

public class Scenario1Case3 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw004/Scenario1Case3";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login承認者
    	login("004515", "Jinjikoi5");

        Calendar inputStartDate = Calendar.getInstance();
        inputStartDate.set(2020, 4, 1);
        Calendar inputEndDate = Calendar.getInstance();
        inputEndDate.set(2020, 4, 31);

    	// Go to screen Kdw004a
        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        setKdw004Period(inputStartDate, inputEndDate);

        driver.findElement(By.xpath("//tr[@data-id='007102']/td[2]/a")).click();
        WaitPageLoad();

        if (!selectItemKdw003_1("承認", "05/11(月)").isSelected()) {
            selectItemKdw003_1("承認", "05/11(月)").click();
        }

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();

        WaitPageLoad();
        screenShot();

        // Go to screen Kdw004a
        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        setKdw004Period(inputStartDate, inputEndDate);

        WaitPageLoad();
        screenShot();

        this.uploadTestLink(851, 203);
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