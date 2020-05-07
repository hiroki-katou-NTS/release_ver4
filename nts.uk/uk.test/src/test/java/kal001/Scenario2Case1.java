package kal001;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;

public class Scenario2Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kal001/Scenario2Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
        login("020905", "Jinjikoi5");
        driver.get(domain + "nts.uk.at.web/view/kdw/010/a/index.xhtml");
        WaitPageLoad();
        screenShot();
        // KAL003A アラームリストの前準備 - チェック条件
        driver.get(domain + "nts.uk.at.web/view/kal/003/a/index.xhtml");
        WaitPageLoad();
        WebElement dropdown = driver.findElement(By.id("combo-box"));
        WaitElementLoad(By.className("ui-igcombo-button"));
        dropdown.findElement(By.className("ui-igcombo-button")).click();
        WaitElementLoad(By.xpath("//li[@data-value='5']"));
        dropdown.findElement(By.xpath("//li[@data-value='5']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//tr[@data-id='010']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//a[@id='ui-id-4']")).click();
        screenShot();
        // KAL004A アラームリストの前準備 - パターン設定
        driver.get(domain + "nts.uk.at.web/view/kal/004/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//tr[@data-id='10']")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//a[@id='ui-id-1']")).click();
        screenShot(); 
        driver.findElement(By.xpath("//a[@id='ui-id-2']")).click();
        screenShot();
        // KDW003A 勤務報告書
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-all")).click();
        WaitPageLoad();
        js.executeScript("__viewContext.vm.selectedEmployee(_.find(__viewContext.vm.lstEmployee(), o => o.code == `027958`).id)");
        WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime.clear();
        startTime.sendKeys("2019/11/01");
        WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime.clear();
        endTime.sendKeys("2019/11/30");
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        screenShot();
        js.executeScript("__viewContext.vm.selectedEmployee(_.find(__viewContext.vm.lstEmployee(), o => o.code == `028851`).id)");
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        WaitPageLoad();
        screenShot();
        js.executeScript("$('.mgrid-free').scrollTop($('.mgrid-free')[1].scrollHeight)");
        screenShot();
        // KAL001A アラームリスト
        driver.get(domain + "nts.uk.at.web/view/kal/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-all")).click();
        WaitPageLoad();
        Object index = js.executeScript("return _.findIndex(__viewContext.viewmodel.listComponentOption.employeeInputList(), o => o.code == `027958`)");
        js.executeScript("$(`#component-employee-list div[id*='scrollContainer']`).scrollTop(" + index + "*24)");
        js.executeScript("$(`#component-employee-list .ui-widget-content.ui-iggrid-tablebody.ui-ig-record.ui-iggrid-record`).children('tr[data-id=\"027958\"]').children().click()");
        WaitPageLoad();
        Object index1 = js.executeScript("return _.findIndex(__viewContext.viewmodel.listComponentOption.employeeInputList(), o => o.code == `028851`)");
        js.executeScript("$(`#component-employee-list div[id*='scrollContainer']`).scrollTop(" + index1 + "*24)");
        js.executeScript("$(`#component-employee-list .ui-widget-content.ui-iggrid-tablebody.ui-ig-record.ui-iggrid-record`).children('tr[data-id=\"028851\"]').children('th[role=\"rowheader\"]').children('span[name=\"chk\"]').click()");
        WaitPageLoad();
        driver.findElement(By.id("combo-box")).click();
        WebElement dropdown1 = driver.findElement(By.xpath("//div[@dropdown-for='combo-box']"));
        WaitElementLoad(By.xpath("//div[@class='ui-igcombo-list']"));
        dropdown1.findElement(By.xpath("//div[@class='ui-igcombo-list']"));
        WaitElementLoad(By.xpath("//li[@data-value='10']"));
        dropdown1.findElement(By.xpath("//li[@data-value='10']")).click();
        WaitPageLoad();
        WebElement category =driver.findElement(By.xpath("//tr[@categorynumber='6']"));
        category.findElement(By.xpath("//span[@class='box']")).click();
        WebElement endDatePicker= driver.findElement(By.className("inputDate")).findElement(By.className("ntsEndDatePicker"));
        WebElement startDatePicker= driver.findElement(By.className("inputDate")).findElement(By.className("ntsStartDatePicker"));
        WaitPageLoad();
        // input time
        startDatePicker.clear();
        startDatePicker.sendKeys("2019/11/01");
        endDatePicker.clear(); 
        endDatePicker.sendKeys("2019/11/30");
        startDatePicker.click();
        // click to extract
        driver.findElement(By.id("extract")).click();
        WaitPageLoad();
        WebElement dialogKAL001 = driver.findElement(By.xpath("//iframe[contains(@name,'window')]"));
        driver.switchTo().frame(dialogKAL001);
        WaitPageLoad();
        screenShot();
        driver.findElement(By.id("D3_2")).click();
        WaitPageLoad();
        WebElement dialogExtract = driver.findElement(By.xpath("//iframe[contains(@name,'window')]"));
        driver.switchTo().frame(dialogExtract);
        screenShot();
        driver.findElement(By.xpath("//button[@tabindex='3']")).click();
        this.uploadTestLink(401, 90);
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