package kdw005;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.openqa.selenium.*;

import common.TestRoot;


public class Scenario1Case2 extends TestRoot {

    public int randomInt;
    public int randomInt2;
    String inpMonth ="inpMonth";//id
    String btnsave = "btn_save";//id
    String locklist ="//*[@id='actualLock-list']/tbody/tr[1]";
    String lock1 ="//*[@id='dailyActualLock']/button[1]";
    String unlock1 ="//*[@id='dailyActualLock']/button[2]";
    String closeMsg15="/html/body/div[5]/div[3]/div/button";
    
    


    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw005/Scenario1Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {
 // phải xóa db log trước khi chạy Tc
        //login 000001/0
        login("000001", "0");

        //change closure 1
        driver.get(domain+ "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.xpath("//td[contains(.,'10日締め')]"));
        driver.findElements(By.xpath("//td[contains(.,'10日締め')]")).get(2).click();
        driver.findElement(By.id(inpMonth)).click();
        driver.findElement(By.id(inpMonth)).clear();
        driver.findElement(By.id(inpMonth)).sendKeys("2019/12");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id(btnsave)).click();

       
        // click checkbox KDW006C
        driver.get(domain + "nts.uk.at.web/view/kdw/006/c/index.xhtml");
        WaitPageLoad();
        WebElement a = driver.findElement(By.xpath("//*[@id='checkBox161']"));
        WaitElementLoad(By.xpath("//*[@id='checkBox161']/label/span[1]"));
        if (a.getAttribute("class").indexOf("checked") == -1) {
            driver.findElement(By.id("register-button")).click();
        } else {
            driver.findElement(By.xpath("//*[@id='checkBox161']/label/span[1]")).click();
            driver.findElement(By.id("register-button")).click();
        }
        WaitPageLoad();
        driver.findElement(By.xpath("//body")).click();
        
        // go kdw003
        driver.get(domain+"nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.xpath("//*[@id='tab-panel']/ul/li[3]"));
        driver.findElement(By.xpath("//*[@id='tab-panel']/ul/li[3]")).click();
        WaitElementLoad(By.id("cbb-closure"));
        driver.findElement(By.id("cbb-closure")).click();
        WaitElementLoad(By.xpath("//div[contains(.,'10日締め')]"));
        driver.findElement(By.xpath("//div[contains(.,'10日締め')]")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).sendKeys("017893");
        WaitElementLoad(By.xpath("//*[@id='ccg001-part-g']/div[1]/button"));
        driver.findElement(By.xpath("//*[@id='ccg001-part-g']/div[1]/button")).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        screenShotFull();
        WaitElementLoad(By.xpath("//*[@id='function-content']/button[10]"));
        driver.findElement(By.xpath("//*[@id='function-content']/button[10]")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[contains(.,'閉じる')]"));
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(2).click();

        driver.switchTo().frame("window_1");
        WaitElementLoad(By.xpath("//input[contains(@class,'ui-igedit-input')]"));
        driver.findElements(By.xpath("//input[contains(@class,'ui-igedit-input')]")).get(1).click();
        driver.findElements(By.xpath("//input[contains(@class,'ui-igedit-input')]")).get(1).sendKeys("2019/11/13");
        driver.findElement(By.xpath("//body")).click();
        screenShotFull();

        WaitElementLoad(By.xpath("//*[@id='functions-area']/button[2]"));
        driver.findElement(By.xpath("//*[@id='functions-area']/button[2]")).click();

        WaitPageLoad();
        Random rd = new Random();
        randomInt = rd.nextInt((1959 - 1900) + 1) + 1900;
        String value = String.valueOf(randomInt);
        selectItemKdw003_2("退勤時刻1", "11/13(水)").click();
        selectItemKdw003_2("退勤時刻1", "11/13(水)").sendKeys(value);
        driver.findElement(By.xpath("//body")).click();
        WaitPageLoad();

        randomInt2 = rd.nextInt((1859 - 1800) + 1) + 1800;
        String value2 = String.valueOf(randomInt2);
        setValueGrid(4,9,value2);
        selectItemKdw003_2("休憩開始時刻2", "11/13(水)").click();
        selectItemKdw003_2("休憩開始時刻2", "11/13(水)").sendKeys(value2);
        driver.findElement(By.xpath("//body")).click();
        WaitPageLoad();

        WaitElementLoad(By.xpath("//*[@id='function-content']/button[1]"));
        driver.findElement(By.xpath("//*[@id='function-content']/button[1]")).click();
        WaitPageLoad();
        Thread.sleep(2000);
        screenShotFull();

        WaitElementLoad(By.xpath("//button[contains(.,'閉じる')]"));
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();

        WaitElementLoad(By.xpath("//*[@id='function-content']/button[10]"));
        driver.findElement(By.xpath("//*[@id='function-content']/button[10]")).click();
        WaitPageLoad();
        screenShotFull();//done case 1-2

        this.uploadTestLink(822, 190);
    }

        @AfterEach
    public void tearDown() throws Exception {
       driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
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

}