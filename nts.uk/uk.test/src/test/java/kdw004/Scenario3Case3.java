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


public class Scenario3Case3 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw004/Scenario3Case3";
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

        // 前準備
        driver.findElement(By.xpath("//*[@id='approvalSttGrid_20200511i']/span")).click();
        WaitPageLoad();

        if(selectItemKdw003_3("承認", "009173").findElement(By.xpath("./label/input")).isSelected()){
            selectItemKdw003_3("承認", "009173").click();
        }
        if(selectItemKdw003_3("承認", "007102").findElement(By.xpath("./label/input")).isSelected()){
            selectItemKdw003_3("承認", "007102").click();
        }
        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();

        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        setKdw004Period(inputStartDate, inputEndDate);

        WaitPageLoad();
        screenShot();
        Thread.sleep(2000);

        // Go to screen Kdw003a
        driver.findElement(By.xpath("//*[@id='approvalSttGrid_20200511i']/span")).click();
        WaitPageLoad();
        screenShot();
        WaitElementLoad((By.xpath("//*[@id='cbDisplayFormat']/div/div[2]")));
        driver.findElement(By.xpath("//*[@id='cbDisplayFormat']/div/div[2]")).click();

        driver.findElement(By.xpath("//li[contains(.,'日付別')]")).click();
        WaitPageLoad();

        WaitElementLoad(By.id("btnExtraction"));
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();

        if(driver.findElements(By.xpath("//iframe[@name='window_1']")).size() !=0){
            driver.switchTo().frame("window_1");
            driver.findElement(By.id("dialogClose")).click();
        }

        screenShot();

        if(!selectItemKdw003_3("承認", "009173").findElement(By.xpath("./label/input")).isSelected()){
            selectItemKdw003_3("承認", "009173").click();
        }

        if(!selectItemKdw003_3("承認", "007102").findElement(By.xpath("./label/input")).isSelected()){
            selectItemKdw003_3("承認", "007102").click();
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
        WaitPageLoad();
        this.uploadTestLink(866, 209);
    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    public void kmk012(String date) {
        // Setting screen kmk012
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();

        // Clear Input Month
        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).clear();

        // Input into Month
        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).sendKeys(date);
        driver.findElement(By.id("contents-right")).click();

        // Click button Save
        WaitElementLoad(By.id("btn_save"));
        driver.findElement(By.id("btn_save")).click();
        WaitPageLoad();
    }
}