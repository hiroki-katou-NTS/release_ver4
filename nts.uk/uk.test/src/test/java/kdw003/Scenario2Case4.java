package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario2Case4 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario2Case4";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login申請者
        //13
        login("015243", "Jinjikoi5");
        driver.get(domain+"nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        if(!checkedBox(18,0)){
            selectItemKdw003_2("本人", "12/17(火)").click();
            WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
            driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();;
            WaitPageLoad();
            screenShot();
            driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        }else{
            screenShot();
            Thread.sleep(1000);
        }
        //14 - 15
        login("013938", "Jinjikoi5");
        driver.get(domain+"nts.uk.at.web/view/kdw/004/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//a[contains(.,'015243')]")).get(0).click();
        WaitPageLoad();
        if(!checkedBox(18,1)){
            selectItemKdw003_2("承認", "12/17(火)").click();
            screenShot();
            WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
            driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();;
            WaitPageLoad();
            screenShot();
            driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        }else{
            screenShot();
            Thread.sleep(1000);
        }
        this.uploadTestLink(725, 161);
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