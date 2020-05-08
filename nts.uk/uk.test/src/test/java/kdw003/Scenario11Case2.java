package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

public class Scenario11Case2 extends Kdw003Common {
    private Integer i=1;
    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario11Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login申請者

        login("016209", "Jinjikoi5");

        //kmk012 change closure 1
        driver.get(domain+ "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("inpMonth")).click();
        driver.findElement(By.id("inpMonth")).clear();
        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).sendKeys("2019/11");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("btn_save"));
        driver.findElement(By.id("btn_save")).click();
        
        showScreen003();

        if(!checkedBox(18, 0)){
            clickCheckBox(18, 0);
            driver.findElement(By.xpath("//div[@id='function-content']//button[1]")).click();

            WaitPageLoad();
            driver.findElement(By.xpath("//div[@class='ui-dialog-buttonset']//button[@class='large']")).click();
            dialog("window_" + i);
        }

        js.executeScript("$('.mgrid-free').scrollLeft(2000)"); 
        WaitPageLoad();
        screenShot();

        driver.get(domain + "nts.uk.at.web/view/kaf/005/a/index.xhtml");

        WaitPageLoad();
        driver.findElement(By.id("inputdate")).clear();
        driver.findElement(By.id("inputdate")).sendKeys("2019/12/17");

        driver.findElement(By.id("inpStartTime1")).click();
        WaitPageLoad();
        driver.findElement(By.id("inpStartTime1")).clear();
        driver.findElement(By.id("inpStartTime1")).sendKeys("当日9:00");
        driver.findElement(By.id("inpEndTime1")).click();
        driver.findElement(By.id("inpEndTime1")).clear();
        driver.findElement(By.id("inpEndTime1")).sendKeys("当日20:00");

        WaitElementLoad(By.xpath("//button[@class='caret-bottom']"));
        driver.findElement(By.xpath("//button[@class='caret-bottom']")).click();

        WaitPageLoad();
        driver.findElement(By.xpath("//div[@class='ui-igcombo-buttonicon']")).click();

        WaitPageLoad();
        WaitElementLoad(By.xpath("//div[@class = 'ui-igcombo-list']//li[2]"));
        driver.findElement(By.xpath("//div[@class = 'ui-igcombo-list']//li[2]")).click();

        WaitElementLoad(By.xpath("//div[@id='functions-area']/button[1]"));
        driver.findElement(By.xpath("//div[@id='functions-area']/button[1]")).click();
        WaitPageLoad();
        screenShot();

        showScreen003();
        js.executeScript("$('.mgrid-free').scrollLeft(2000)");  
        js.executeScript("$('.mgrid-free').scrollTop(400)");  
        WaitPageLoad();
        screenShot(); 

        this.uploadTestLink(1153, 278);
    }
    public void showScreen003() {
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml"); 
        WaitPageLoad();

        dialog("window_" + i);
        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]"))
                .clear();
        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]"))
                .sendKeys("2019/12/01");

        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]"))
                .clear();
        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]"))
                .sendKeys("2019/12/31");
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@id = 'btnExtraction']")).click();

        dialog("window_" + i);
    }
    public void dialog(String wd) {
        try {
            WebElement dialogError = driver.findElement(By.xpath("//iframe[contains(@name,'" + wd + "')]"));
            WaitPageLoad();
            if (dialogError.isDisplayed()) {
                driver.switchTo().frame(dialogError);
                WaitPageLoad();
                driver.findElement(By.id("dialogClose")).click();
                i++;
            }
        } catch (Exception ex) {

        }
        WaitPageLoad();
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