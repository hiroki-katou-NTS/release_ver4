package kdw003;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Calendar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Scenario3Case12 extends Kdw003Common {
    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw/003/Scenario3/Case12";
        this.initDefault();
    }

    @Test
    public void test() throws Exception {
        // login
        login(employeeCode, password);
        WaitPageLoad();
        driver.get(kdw003);
        WaitPageLoad();
        
        Calendar inputStartDate = Calendar.getInstance();
        inputStartDate.set(2019, 10, 1);
        WebElement wEleStart = driver.findElement(By.xpath("//div[@id ='daterangepicker']/.//input[contains(@class,'ntsStartDatePicker')]"));
        wEleStart.clear();
        wEleStart.sendKeys(df1.format(inputStartDate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        Calendar inputEndDate = Calendar.getInstance();
        inputEndDate.set(2019, 10, 30);
        WebElement wEleEnd = driver.findElement(By.xpath("//div[@id ='daterangepicker']/.//input[contains(@class,'ntsEndDatePicker')]"));
        wEleEnd.clear();
        wEleEnd.sendKeys(df1.format(inputEndDate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//li[@class ='mgrid-sheet-button ui-state-default']"));
        driver.findElements(By.xpath("//li[@class ='mgrid-sheet-button ui-state-default']")).get(0).click();
        scrollToRowColumn(30, 2);
        setValueGrid("PCログオフ乖離時間理由（選択）", "11/29(金)", 0, "01");
        clickCheckBox("本人", "11/29(金)", 0);
        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        screenShot();

        // load kmw003
        driver.get(kmw003);
        WaitPageLoad();
        clickCheckBox("本人", "013235", 1);
        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        screenShot();

        //login manager  012467
        employeeCode = "012467";
        login(employeeCode, password);
        WaitPageLoad();
        driver.get(kdw004);
        WaitPageLoad();
        
        inputStartDate.clear();
        inputStartDate.set(2019, 10, 1);
        wEleStart = driver.findElement(By.xpath("//div[@id ='daterangepicker']/.//input[contains(@class,'ntsStartDatePicker')]"));
        wEleStart.clear();
        wEleStart.sendKeys(df1.format(inputStartDate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        inputEndDate.clear();
        inputEndDate.set(2019, 10, 30);
        wEleEnd = driver.findElement(By.xpath("//div[@id ='daterangepicker']/.//input[contains(@class,'ntsEndDatePicker')]"));
        wEleEnd.clear();
        wEleEnd.sendKeys(df1.format(inputEndDate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        driver.findElement(By.id("extractBtn")).click();
        WaitPageLoad();

        driver.findElements(By.xpath("//table[@id ='approvalSttGrid']"));
        WaitElementLoad(By.linkText("013235"));
        driver.findElement(By.linkText("013235")).click();
        WaitPageLoad();
        scrollToRowColumn(30, 2);
        clickCheckBox("承認", "11/29(金)", 0);
        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        screenShot();

        // load kmw003
        driver.get(kmw003Approval);
        WaitPageLoad();
        clickCheckBox("承認", "013235", 1);
        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        screenShot();

        this.uploadTestLink(923, 229);
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