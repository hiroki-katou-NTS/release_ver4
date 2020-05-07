package kdw003;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Calendar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Scenario3Case9 extends Kdw003Common {
    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw/003/Scenario3/Case9";
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
        screenShot();

        // kdw001
        driver.get(domain+"nts.uk.at.web/view/kdw/001/b/index.xhtml");
        WaitPageLoad();
        Calendar inputKdw001 = Calendar.getInstance();
        inputKdw001.set(2019, 10, 29);
        wEleStart = driver.findElement(By.xpath("//div[@id ='daterangepicker']/.//input[contains(@class,'ntsStartDatePicker')]"));
        wEleStart.clear();
        wEleStart.sendKeys(df1.format(inputKdw001.getTime()));
        driver.findElement(By.xpath("//body")).click();

        inputKdw001.clear();
        inputKdw001.set(2019, 10, 29);
        wEleEnd = driver.findElement(By.xpath("//div[@id ='daterangepicker']/.//input[contains(@class,'ntsEndDatePicker')]"));
        wEleEnd.clear();
        wEleEnd.sendKeys(df1.format(inputKdw001.getTime()));
        driver.findElement(By.xpath("//body")).click();

        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        Thread.sleep(1000);
        // WaitElementLoad(By.xpath("//label[contains(.,'参照可能な社員すべて')]"));
        // driver.findElements(By.xpath("//label[contains(.,'参照可能な社員すべて')]")).get(0).click();
        WaitElementLoad(By.xpath("//i[contains(@class,'ccg001-icon-btn-big icon-26-onlyemployee')]"));
        driver.findElements(By.xpath("//i[contains(@class,'ccg001-icon-btn-big icon-26-onlyemployee')]")).get(0).click();//de tam
        WaitPageLoad();
        driver.findElement(By.id("button22")).click();
        // WaitElementLoad(By.xpath("//span[contains(.,'日別作成（打刻反映）')]"));
        // driver.findElements(By.xpath("//span[contains(.,'日別作成（打刻反映）')]")).get(0).click();
        WaitElementLoad(By.xpath("//span[contains(.,'承認結果反映')]"));
        driver.findElements(By.xpath("//span[contains(.,'承認結果反映')]")).get(0).click();
        WaitElementLoad(By.xpath("//span[contains(.,'月別集計')]"));
        driver.findElements(By.xpath("//span[contains(.,'月別集計')]")).get(0).click();
        WaitElementLoad(By.id("button6"));
        driver.findElement(By.id("button6")).click();
        Thread.sleep(1000);
        WaitElementLoad(By.id("button113"));
        driver.findElement(By.id("button113")).click();
        Thread.sleep(1000);
        WebDriverWait _wait300 = new WebDriverWait(driver, 300000);
        driver.switchTo().frame("window_1");
        _wait300 = new WebDriverWait(driver, 300000);
        _wait300.until(d -> (false == d.findElement(By.className("danger")).isEnabled()));

        // load kdw/003 after process kdw/001
        driver.get(kdw003);
        WaitPageLoad();
        
        inputStartDate.clear();
        inputStartDate.set(2019, 10, 01);
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

        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//li[@class ='mgrid-sheet-button ui-state-default']"));
        driver.findElements(By.xpath("//li[@class ='mgrid-sheet-button ui-state-default']")).get(0).click();
        scrollToRowColumn(30, 2);
        screenShot();

        // load kmw003
        driver.get(kmw003);
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
        screenShot();

        // load kmw003
        driver.get(kmw003Approval);
        WaitPageLoad();
        screenShot();

        this.uploadTestLink(921, 228);
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