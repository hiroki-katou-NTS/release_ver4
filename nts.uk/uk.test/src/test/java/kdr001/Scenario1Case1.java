package kdr001;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;



public class Scenario1Case1 extends TestRoot {
    String company_code = "company-code-select";//xpath
    String company_code_value = "//li[@data-value='0001']";//xpath
    String employee_code = "employee-code-inp";//id
    String password = "password-input" ;//id
    String login_btn = "login-btn";//id
    String ccg001_btn ="ccg001-btn-search-drawer";//id
    String closure_btn = "cbb-closure";//id
    String closure_select ="/html/body/div[6]/div[2]/ul/li[1]/div/div[2]";//xpath
    String start_date ="//input[contains(@class,'ntsStartDatePicker')]";//xpath
    String end_date ="//input[contains(@class,'ntsEndDatePicker')]";//xpath
    String workplace_child ="ccg001-btn-same-workplace-and-child";//xpath
    String checkbox ="//span[contains(@class,'ui-icon-check')]";//xpath
    String combo_box2 ="combo-box2";//id
    String combo_box2_select ="/html/body/div[4]/div[2]/ul/li[2]/div/div";//xpath
    String Excel_btn ="A1_1";//id


    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdr001/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {

        //login 020905/Jinjikoi5
        login("020905", "Jinjikoi5");

        //go kdr001
        driver.get(domain+ "nts.uk.at.web/view/kdr/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id(ccg001_btn)).click();
        WaitElementLoad(By.id(closure_btn));
        driver.findElement(By.id(closure_btn)).click();
        driver.findElement(By.xpath(closure_select)).click();
        WaitElementLoad(By.xpath(start_date));
        driver.findElement(By.xpath(start_date)).clear();
        driver.findElement(By.xpath(start_date)).sendKeys("2018/07");
        driver.findElement(By.xpath(end_date)).clear();
        driver.findElement(By.xpath(end_date)).sendKeys("2019/07");
        WaitElementLoad(By.id(workplace_child));
        driver.findElement(By.id(workplace_child)).click();
        screenShotFull();
        WaitElementLoad(By.xpath(checkbox));
        driver.findElements(By.xpath(checkbox)).get(2).click();
        driver.findElement(By.id(combo_box2)).click();
        driver.findElement(By.xpath(combo_box2_select)).click();
        screenShotFull();
        WaitElementLoad(By.id(Excel_btn));
        driver.findElement(By.id(Excel_btn)).click();
 
        //full man hinh
        WaitPageLoad();
        Thread.sleep(7000);
        screenShotFull();
     
    this.uploadTestLink(76, 13);
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