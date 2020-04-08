package kdw003;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Scenario16Case3 extends Kdw003Common {
    private Integer i=1;
    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario16Case3";
        this.initDefault();
    }

    @Test
    public void test() throws Exception {
        // login
        login("018234", "Jinjikoi5");

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
        
        WaitPageLoad();

        driver.get(kdw003);
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

        screenShot();

        WaitPageLoad();
        setValueGrid(8, 9, "010");

        WaitPageLoad();
        setValueGrid(8, 7, "006");

        WaitPageLoad();
        setValueGrid(10, 7, "011");

        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id='function-content']//button[1]")).click();

        try {
            WaitPageLoad();
            WaitPageLoad();
            driver.findElement(By.xpath("//div[@class='ui-dialog-buttonset']//button[@class='large']")).click();
            dialog("window_" + i);  
        } catch (Exception e) {
            
        }
    
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//button[@id = 'btnVacationRemaining']")).click();

        WaitElementLoad(By.xpath("//div[@id='vacationRemaining-content']"));
        driver.findElement(By.xpath("//div[@id='vacationRemaining-content']//tr[1]/td[1]")).click();
        
        WebElement dialogInfor = driver.findElement(By.xpath("//iframe[contains(@name,'window_" + i + "')]"));
        driver.switchTo().frame(dialogInfor);

        WaitPageLoad();
        screenShot();

        this.uploadTestLink(1061, 267);
    }

    public void dialog(String wd) {
        try {
            WebElement dialogError = driver.findElement(By.xpath("//iframe[contains(@name,'" + wd + "')]"));
            WaitPageLoad();
            if (dialogError.isDisplayed()) {
                driver.switchTo().frame(dialogError);
                WaitPageLoad();
                driver.findElement(By.xpath("//button[@id = 'dialogClose']")).click();
                WaitPageLoad();
                i++;
            }
        } catch (Exception ex) {

        }
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