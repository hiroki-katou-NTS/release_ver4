package kdw004;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import common.TestRoot;
import kdw003.Kdw003Common;

public class Scenario3Case1 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw004/Scenario3Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {

        // login申請者
    	login("009173", "Jinjikoi5");

        Calendar inputStartDate = Calendar.getInstance();
        inputStartDate.set(2020, 4, 1);
        Calendar inputEndDate = Calendar.getInstance();
        inputEndDate.set(2020, 4, 31);

        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).sendKeys(df1.format(inputStartDate.getTime()));
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).sendKeys(df1.format(inputEndDate.getTime()));
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        // Check checkbox
        driver.findElement(By.xpath("//td[contains(.,'05/08')]")).findElements(By.xpath("following::td")).get(0).click();
        driver.findElement(By.xpath("//td[contains(.,'05/09')]")).findElements(By.xpath("following::td")).get(0).click();
        driver.findElement(By.xpath("//td[contains(.,'05/10')]")).findElements(By.xpath("following::td")).get(0).click();
        driver.findElement(By.xpath("//td[contains(.,'05/11')]")).findElements(By.xpath("following::td")).get(0).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@class='proceed']")).click();


        // login承認者
    	login("004515", "Jinjikoi5");

        // Go to screen Kdw004a
        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        setKdw004Period(inputStartDate, inputEndDate);

        WaitPageLoad();
        screenShot();
        Thread.sleep(2000);

        // Go to screen Kdw003a
        selectItemKdw004("社員名", "009173").click();
        WaitPageLoad();

        if(driver.findElements(By.xpath("//iframe[@name='window_1']")).size() !=0){
            driver.switchTo().frame("window_1");
            driver.findElement(By.id("dialogClose")).click();
        }

        screenShot();

        if(!selectItemKdw003_1("承認", "05/08(金)").findElement(By.xpath("./label/input")).isSelected()){
            selectItemKdw003_1("承認", "05/08(金)").click();
        }

        if(!selectItemKdw003_1("承認", "05/09(土)").findElement(By.xpath("./label/input")).isSelected()){
            selectItemKdw003_1("承認", "05/09(土)").click();
        }

        if(!selectItemKdw003_1("承認", "05/10(日)").findElement(By.xpath("./label/input")).isSelected()){
            selectItemKdw003_1("承認", "05/10(日)").click();
        }

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();
        // tacke a photo
        Thread.sleep(2000);
        screenShot();
        // Go to screen Kdw004a
        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        setKdw004Period(inputStartDate, inputEndDate);
        WaitPageLoad();
        screenShot();
        WaitPageLoad();
        this.uploadTestLink(862, 207);
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