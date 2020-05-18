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

import common.TestRoot;
import kdw003.Kdw003Common;

public class Scenario3Case2 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw004/Scenario3Case2";
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

        if(selectItemKdw003_1("承認", "05/08(金)").findElement(By.xpath("./label/input")).isSelected()){
            selectItemKdw003_1("承認", "05/08(金)").click();
        }

        if(selectItemKdw003_1("承認", "05/09(土)").findElement(By.xpath("./label/input")).isSelected()){
            selectItemKdw003_1("承認", "05/09(土)").click();
        }

        if(selectItemKdw003_1("承認", "05/10(日)").findElement(By.xpath("./label/input")).isSelected()){
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
        this.uploadTestLink(864, 207);
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