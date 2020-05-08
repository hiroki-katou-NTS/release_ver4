package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario2Case3 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario2Case3";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login申請者
        login("015243", "Jinjikoi5");
        driver.get(domain+"nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//li[contains(.,'乖離シート')]")).get(0).click();
        setValueGrid(18,2,"1500");
        WaitPageLoad();
        setValueGrid(18,6,"1530");
        WaitPageLoad();
        selectItemKdw003_2("PCログオフ乖離時間理由（選択）", "12/17(火)").click();
        WaitPageLoad();
        driver.switchTo().frame(0);
        driver.findElements(By.xpath("//td[contains(.,'自己研鑽・自己学習')]")).get(1).click();
        driver.findElement(By.id("save")).click();
        WaitPageLoad();
        selectItemKdw003_2("本人", "12/17(火)").click();
        Thread.sleep(1000);
        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();;
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();;
        Thread.sleep(1000);
        driver.findElements(By.xpath("//button[contains(.,'エラー参照')]")).get(0).click();
        screenShot();
        //this.uploadTestLink(723, 160);
    }

    public boolean checkedBox(int rowNumber, int columnNumber) {
        String element = driver
                .findElements(By.xpath("//table[@class ='mgrid-fixed-table']/tbody/tr[" + rowNumber
                        + "]/.//label[@class = 'ntsCheckBox']"))
                .get(columnNumber).findElement(By.tagName("input")).getAttribute("checked");
        if (element != null) {
            return true;
        }
        return false;
    }

    public void setValueGrid(int rowNumber, int columnNumber, String value){
        if(value.isEmpty()){
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]")).get(0).click();
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]")).get(0).click();
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]"+"/.//input")).get(0).clear();
        }else{
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]")).get(0).click();
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]")).get(0).sendKeys(value);
        }
        driver.findElement(By.xpath("//body")).click();
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