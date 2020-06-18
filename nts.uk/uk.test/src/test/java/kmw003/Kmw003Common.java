package kmw003;

import java.io.File;

import org.openqa.selenium.By;

import common.TestRoot;

public class Kmw003Common extends TestRoot{
    String employeeCode = "";
    String password = "";
    String kmk012 = "";
    String kmw003 = "";
    public void login(File screenshotFile, String empCode, String passWord)throws Exception{
        driver.get(domain + "nts.uk.com.web/view/ccg/007/d/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("company-code-select")).click();
        WaitElementLoad(By.xpath("//li[@data-value='0001']"));
        driver.findElement(By.xpath("//li[@data-value='0001']")).click();
        driver.findElement(By.id("employee-code-inp")).click();
        driver.findElement(By.id("employee-code-inp")).clear();
        driver.findElement(By.id("employee-code-inp")).sendKeys(empCode);

        driver.findElement(By.id("password-input")).click();
        driver.findElement(By.id("password-input")).clear();
        driver.findElement(By.id("password-input")).sendKeys(passWord);
        driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();
    }

    public static String linkFullScreen(String link){
        return domain + link;
    }

    public void setValueGrid(int rowNumber, int columnNumber, String value) throws InterruptedException{
    	WaitElementLoad(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]"));
        driver.findElement(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]")).click();
        driver.findElement(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]")).click();
        driver.findElement(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]"+"/.//input")).clear();

        if(!value.isEmpty()){
            Thread.sleep(2000);
            driver.findElement(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]/div/input")).sendKeys(value);
            Thread.sleep(2000);
        }
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("function-content"));
        driver.findElement(By.id("function-content")).click();
        WaitPageLoad();
    }


    public void setValueInput(String id, String value) throws InterruptedException{
        driver.findElement(By.id(id)).click();
        driver.findElement(By.id(id)).clear();
        Thread.sleep(2000);
        driver.findElement(By.id(id)).sendKeys(value);
        Thread.sleep(2000);
        driver.findElement(By.xpath("//body")).click();
    }

}