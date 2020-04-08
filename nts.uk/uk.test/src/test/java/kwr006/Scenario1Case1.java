package kwr006;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;


public class Scenario1Case1 extends TestRoot {
    String company_code = "company-code-select";//id
    String company_code_value = "//li[@data-value='0001']";//xpath
    String employee_code = "employee-code-inp";//id
    String password = "password-input" ;//id
    String login_btn = "login-btn";//id
    String ccg001_btn ="ccg001-btn-search-drawer";//id
    String closure_btn = "cbb-closure";//id
    String closure_select ="/html/body/div[6]/div[2]/ul/li[1]/div";//xpath
    String start_date ="//input[contains(@class,'ntsStartDatePicker')]";//xpath
    String end_date ="//input[contains(@class,'ntsEndDatePicker')]";//xpath
    String apply_search_btn="ccg001-btn-apply-search-condition";
    String tab_panel ="//*[@id='tab-panel']/ul/li[2]";
    String workplace ="ui-id-9";
    String input_search ="(//input[@type='text'])[2]";
    String select_employee ="/html/body/div[1]/div[2]/div[2]/div[1]/div[1]/div/div[2]/div[2]/div/div[1]/div[1]/div[4]/div/div/div/div[3]/div[1]/div[2]/button";
    String click_tree="//*[@id='nts-component-tree']/div[3]/div[2]/button";
    String btn_advanced_search="ccg001-btn-advanced-search";
    String combobox1 ="//*[@id='combo-box']/div/div[2]";
    String combobox1_select ="//li[contains(@class,'ui-igcombo-listitem')]";
    String combobox2="//td[@class='td-right']//div[2]//div[1]//div[2]";
    String combobox2_select ="/html/body/div[4]/div[2]/ul/li[3]/div/div";
    String checkbox ="//div[contains(@class,'ntsCheckBox')]";
    String btn_Excel="btnExportExcel";
    String btn_Pdf="btnExportPdf";


    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kwr006/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login 020905/ Jinjikoi5
        login("020905", "Jinjikoi5");

        // 1-1
       driver.get(domain+ "nts.uk.at.web/view/kwr/006/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id(ccg001_btn)).click();
        WaitPageLoad();
        //select closure
        WaitElementLoad(By.id(closure_btn));
        driver.findElement(By.id(closure_btn)).click();
        driver.findElement(By.xpath(closure_select)).click();
        WaitPageLoad();
        //input date
        WaitElementLoad(By.xpath(start_date));
        driver.findElement(By.xpath(start_date)).clear();
        driver.findElement(By.xpath(start_date)).sendKeys("2019/07");
        driver.findElement(By.xpath(end_date)).clear();
        driver.findElement(By.xpath(end_date)).sendKeys("2019/07");
        driver.findElement(By.id(apply_search_btn)).click();
        WaitPageLoad();
        //click tab
        WaitElementLoad(By.xpath(tab_panel));
        driver.findElement(By.xpath(tab_panel)).click();
        WaitPageLoad();
        // click workplace
        WaitElementLoad(By.id(workplace));
        driver.findElement(By.id(workplace)).click();
        //input key
        WaitPageLoad();
        WaitElementLoad(By.xpath(input_search));
        driver.findElement(By.xpath(input_search)).click();
        driver.findElement(By.xpath(input_search)).sendKeys("004484");
        //WaitPageLoad();
       
        //select employee
        WaitElementLoad(By.xpath(select_employee));
        driver.findElement(By.xpath(select_employee)).click();
        //click tree
        //WaitPageLoad();
        WaitElementLoad(By.xpath(click_tree));
        driver.findElement(By.xpath(click_tree)).click();
        WaitPageLoad();
        screenShotFull();

        //WaitPageLoad();
        WaitElementLoad(By.id(btn_advanced_search));
        driver.findElement(By.id(btn_advanced_search)).click();
        //1-2
        // select定型選択
        //WaitPageLoad();
        WaitElementLoad(By.xpath(combobox1));
        driver.findElement(By.xpath(combobox1)).click();
        
        driver.findElements(By.xpath(combobox1_select)).get(6).click();

        //select改ページの指定
        //WaitPageLoad();
        WaitElementLoad(By.xpath(combobox2));
        driver.findElement(By.xpath(combobox2)).click();
        driver.findElement(By.xpath(combobox2_select)).click();

        //checkbox 明細・合計出力形式
        Thread.sleep(1000);
        int i=0;
        for (i = 0; i<=9;i++){
            driver.findElements(By.xpath(checkbox)).get(i).click();
        };

        WaitPageLoad();
        screenShotFull();

       //print 1-4
       // WaitPageLoad();
        WaitElementLoad(By.id(btn_Excel));
        driver.findElement(By.id(btn_Excel)).click();
        WaitPageLoad();
        WaitElementLoad(By.id(btn_Pdf));
        driver.findElement(By.id(btn_Pdf)).click();

        //Thread.sleep(5000);
        WaitPageLoad();
        screenShotFull();

        this.uploadTestLink(84,16);
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