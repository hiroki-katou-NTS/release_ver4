package cmm053;

import static org.junit.jupiter.api.Assertions.fail;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import common.TestRoot;


public class Scenario2Case1 extends TestRoot {
    String company_code = "company-code-select";//xpath
    String company_code_value = "//li[@data-value='0001']";//xpath
    String employee_code = "employee-code-inp";//id
    String password = "password-input" ;//id
    String login_btn = "login-btn";//id
    String closeMsg15="/html/body/div[5]/div[3]/div/button";
    


    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm053/Scenario2Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {

        //login 022497/Jinjikoi5
        login("022497", "Jinjikoi5");
        driver.get(domain+ "nts.uk.com.web/view/ccg/015/a/index.xhtml");
        WaitPageLoad();

        driver.findElements(By.xpath("//td[contains(.,'0005')]")).get(2).click();
        driver.findElements(By.xpath("//td[contains(.,'0005')]")).get(2).click();
        screenShotFull();

        WaitElementLoad(By.xpath("//*[@id='tableRight']/tbody/tr[3]/td/button"));
        driver.findElement(By.xpath("//*[@id='tableRight']/tbody/tr[3]/td/button")).click();
        WaitPageLoad();
        screenShotFull();
        driver.switchTo().frame("window_1");
        WaitElementLoad(By.id("cell-4-6"));
        driver.findElement(By.id("cell-4-6")).click();
        WaitPageLoad();
        driver.switchTo().defaultContent();//ve man chinh trc khi chuyen sang window khac
        driver.switchTo().frame("window_2");
        WaitElementLoad(By.xpath("//td[contains(.,'0002')]"));
        driver.findElements(By.xpath("//td[contains(.,'0002')]")).get(1).click();
        WaitPageLoad();
        screenShotFull();
        WaitElementLoad(By.xpath("//*[@id='functions-area-bottom']/button[1]"));
        driver.findElement(By.xpath("//*[@id='functions-area-bottom']/button[1]")).click();
        driver.switchTo().frame("window_1");
        WaitElementLoad(By.xpath("//*[@id='functions-area']/button[1]"));
        driver.findElement(By.xpath("//*[@id='functions-area']/button[1]")).click();
        Thread.sleep(1000);
        screenShotFull();
        driver.switchTo().defaultContent();
        _wait.until(d -> "登録しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        driver.findElements(By.xpath("//button[contains(@class,'large')]")).get(12).click();
        driver.switchTo().frame("window_1");
        WaitElementLoad(By.xpath("//*[@id='functions-area']/button[3]"));
        driver.findElement(By.xpath("//*[@id='functions-area']/button[3]")).click();

        logout();

    
        //login 016976/Jinjikoi5
        login("016976", "Jinjikoi5");
        driver.get(domain+ "nts.uk.com.web/view/ccg/015/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//td[contains(.,'0004')]")).get(2).click();
        driver.findElements(By.xpath("//td[contains(.,'0004')]")).get(2).click();
        screenShotFull();

        WaitElementLoad(By.xpath("//*[@id='tableRight']/tbody/tr[3]/td/button"));
        driver.findElement(By.xpath("//*[@id='tableRight']/tbody/tr[3]/td/button")).click();
        WaitPageLoad();
        screenShotFull();
        driver.switchTo().frame("window_1");
        WaitElementLoad(By.id("cell-4-6"));
        driver.findElement(By.id("cell-4-6")).click();
        WaitPageLoad();
        driver.switchTo().defaultContent();//ve man chinh trc khi chuyen sang window khac
        driver.switchTo().frame("window_2");
        WaitElementLoad(By.xpath("//td[contains(.,'0002')]"));
        driver.findElements(By.xpath("//td[contains(.,'0002')]")).get(1).click();
        WaitPageLoad();
        screenShotFull();
        WaitElementLoad(By.xpath("//*[@id='functions-area-bottom']/button[1]"));
        driver.findElement(By.xpath("//*[@id='functions-area-bottom']/button[1]")).click();
        driver.switchTo().frame("window_1");
        WaitElementLoad(By.xpath("//*[@id='functions-area']/button[1]"));
        driver.findElement(By.xpath("//*[@id='functions-area']/button[1]")).click();
        Thread.sleep(1000);
        screenShotFull();
        driver.switchTo().defaultContent();
        _wait.until(d -> "登録しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        driver.findElements(By.xpath("//button[contains(@class,'large')]")).get(12).click();
        driver.switchTo().frame("window_1");
        WaitElementLoad(By.xpath("//*[@id='functions-area']/button[3]"));
        driver.findElement(By.xpath("//*[@id='functions-area']/button[3]")).click();

        logout();
      

        //login 010392/Jinjikoi5 - TC2-1
        login("010392", "Jinjikoi5");
        WaitPageLoad();
        screenShotFull();
        logout();

        //login 022497/Jinjikoi5 - TC2-2
        login("022497", "Jinjikoi5");
        WaitPageLoad();
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("$('.placement-wraper').scrollTop(1008)");
        WaitPageLoad();
        screenShotFull();
        logout();

        //login 018937/Jinjikoi5 - TC2-3
        login("018937", "Jinjikoi5");
        WaitPageLoad();
        screenShotFull();
        logout();

        //login 016976/Jinjikoi5 - TC2-4
        login("016976", "Jinjikoi5");
        WaitPageLoad();
        jse.executeScript("$('.placement-wraper').scrollTop(1008)");
        WaitPageLoad();
        screenShotFull();
        logout();//done

        // //loai bo dieu kien
        // login("022497", "Jinjikoi5");
        // driver.get(domain+ "nts.uk.com.web/view/ccg/015/a/index.xhtml");
        // WaitPageLoad();

        // // xoa 005
        // driver.findElements(By.xpath("//td[contains(.,'0005')]")).get(2).click();
        // WaitElementLoad(By.xpath("//*[@id='tableRight']/tbody/tr[3]/td/button"));
        // driver.findElement(By.xpath("//*[@id='tableRight']/tbody/tr[3]/td/button")).click();
        // WaitPageLoad();

        // driver.switchTo().frame("window_1");
        // driver.findElements(By.xpath("//div[contains(@class,'remove-button')]")).get(2).click();
        // WaitElementLoad(By.xpath("//*[@id='functions-area']/button[1]"));
        // driver.findElement(By.xpath("//*[@id='functions-area']/button[1]")).click();
        // Thread.sleep(1000);
        // driver.switchTo().defaultContent();
        // _wait.until(d -> "登録しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        // driver.findElements(By.xpath("//button[contains(@class,'large')]")).get(12).click();
        // driver.switchTo().frame("window_1");
        // WaitElementLoad(By.xpath("//*[@id='functions-area']/button[3]"));
        // driver.findElement(By.xpath("//*[@id='functions-area']/button[3]")).click();

        // //xoa 004
        // WaitPageLoad();
        // driver.findElements(By.xpath("//td[contains(.,'0004')]")).get(2).click();
        // WaitElementLoad(By.xpath("//*[@id='tableRight']/tbody/tr[3]/td/button"));
        // driver.findElement(By.xpath("//*[@id='tableRight']/tbody/tr[3]/td/button")).click();
        // WaitPageLoad();

        // driver.switchTo().frame("window_1");
        // driver.findElements(By.xpath("//div[contains(@class,'remove-button')]")).get(2).click();
        // WaitElementLoad(By.xpath("//*[@id='functions-area']/button[1]"));
        // driver.findElement(By.xpath("//*[@id='functions-area']/button[1]")).click();
        // Thread.sleep(1000);
        // driver.switchTo().defaultContent();
        // _wait.until(d -> "登録しました。".equals(d.findElement(By.xpath("//div[@class='text']")).getText()));
        // driver.findElements(By.xpath("//button[contains(@class,'large')]")).get(12).click();
        // driver.switchTo().frame("window_1");
        // WaitElementLoad(By.xpath("//*[@id='functions-area']/button[3]"));
        // driver.findElement(By.xpath("//*[@id='functions-area']/button[3]")).click();

        // logout();


        this.uploadTestLink(740, 165);
    }

        @AfterEach
    public void tearDown() throws Exception {
        //driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
    public void logout() {
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
    }
}