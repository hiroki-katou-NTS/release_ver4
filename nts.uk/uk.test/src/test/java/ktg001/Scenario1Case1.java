package ktg001;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario1Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg001/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
    	//本人確認事前準備
        //login申請者
        login("017267", "Jinjikoi5");

        setProcessYearMonth(1, "2020/05");

        // Go to screen Kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        Calendar inputStart = Calendar.getInstance();
        inputStart.set(2020, 4, 1);
        Calendar inputEnDate = Calendar.getInstance();
        inputEnDate.set(2020, 4, 31);

        WebElement wEleStartKdw003 = driver
                .findElement(By.xpath("//div[@id ='daterangepicker']/.//input[contains(@class,'ntsStartDatePicker')]"));
        wEleStartKdw003.clear();
        wEleStartKdw003.sendKeys(df1.format(inputStart.getTime()));
        driver.findElement(By.xpath("//body")).click();

        WebElement wEleEndKdw003 = driver
                .findElement(By.xpath("//div[@id ='daterangepicker']/.//input[contains(@class,'ntsEndDatePicker')]"));
        wEleEndKdw003.clear();
        wEleEndKdw003.sendKeys(df1.format(inputEnDate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();

        WebElement el1 = selectItemKdw003_1("本人", "05/12(火)");
        WebElement el2 = selectItemKdw003_1("本人", "05/13(水)");
        WebElement el3 = selectItemKdw003_1("本人", "05/14(木)");
        if(!el1.findElement(By.xpath("./label/input")).isSelected() &&
        	el1.findElement(By.xpath("./label/input")).isEnabled()){
        	el1.click();
        }
        if(!el2.findElement(By.xpath("./label/input")).isSelected() &&
        	el2.findElement(By.xpath("./label/input")).isEnabled()){
        	el2.click();
        }
        if(!el3.findElement(By.xpath("./label/input")).isSelected() &&
        	el3.findElement(By.xpath("./label/input")).isEnabled()){
        	el3.click();
        }

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();

        WaitPageLoad();

        //login承認者
        login("004515", "Jinjikoi5");

        driver.get(domain+"nts.uk.at.web/view/kdw/004/a/index.xhtml");
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//a[contains(.,'017267')]")).click();
        WaitPageLoad();

        WebElement el4 = selectItemKdw003_1("承認", "05/12(火)");
        WebElement el5 = selectItemKdw003_1("承認", "05/13(水)");
        WebElement el6 = selectItemKdw003_1("承認", "05/14(木)");
        if(el4.findElement(By.xpath("./label/input")).isSelected() &&
        	el4.findElement(By.xpath("./label/input")).isEnabled()){
        	el4.click();
        }
        if(el5.findElement(By.xpath("./label/input")).isSelected() &&
        	el5.findElement(By.xpath("./label/input")).isEnabled()){
        	el5.click();
        }
        if(el6.findElement(By.xpath("./label/input")).isSelected() &&
        	el6.findElement(By.xpath("./label/input")).isEnabled()){
        	el6.click();
        }
        screenShot();
        driver.findElement(By.xpath("//button[contains(.,'確定')]")).click();
        WaitPageLoad();
        driver.get(domain+"nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();
        screenShot();
        driver.switchTo().frame(2); //chuyển frame
        driver.findElement(By.xpath("//button")).click();
        WaitPageLoad();
        screenShot();
        this.uploadTestLink(91, 18);
    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    public void setProcessYearMonth(int closureId, String yearMonth) {
        //KMK012A 処理年月の設定
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");

        WaitPageLoad();
        WebElement clsId = driver.findElement(By.xpath("//tr[@data-id = '" + closureId + "']"));

        WaitPageLoad();
        clsId.click();

        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).click();

        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).clear();

        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).sendKeys(yearMonth);

        driver.findElement(By.xpath("//body")).click();

        WaitElementLoad(By.id("btn_save"));
        driver.findElement(By.id("btn_save")).click();

        WaitElementLoad(By.xpath("//button[@class ='large']"));
        driver.findElement(By.xpath("//button[@class ='large']")).click();

        screenShot();

      }
}