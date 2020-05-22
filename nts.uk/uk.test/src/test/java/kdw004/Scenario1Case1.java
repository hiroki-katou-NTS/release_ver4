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


public class Scenario1Case1 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw004/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
    	login("007102", "Jinjikoi5");

        // Go to screen Kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        Calendar inputStartDate = Calendar.getInstance();
        inputStartDate.set(2020, 4, 1);
        Calendar inputEndDate = Calendar.getInstance();
        inputEndDate.set(2020, 4, 31);

        extractData(inputStartDate, inputEndDate);
        WaitPageLoad();

        if(!selectItemKdw003_1("本人", "05/12(火)").isSelected()){
            selectItemKdw003_1("本人", "05/12(火)").click();
        }
        if(!selectItemKdw003_1("本人", "05/13(水)").isSelected()){
            selectItemKdw003_1("本人", "05/13(水)").click();
        }
        if(!selectItemKdw003_1("本人", "05/14(木)").isSelected()){
            selectItemKdw003_1("本人", "05/14(木)").click();
        }
        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();

         // login承認者
     	login("004515", "Jinjikoi5");

        // Go to screen Kdw004a
        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");

        setKdw004Period(inputStartDate, inputEndDate);

        WaitPageLoad();
        screenShot();
        WaitPageLoad();
        this.uploadTestLink(847, 201);
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