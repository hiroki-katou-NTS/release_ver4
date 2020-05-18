package kdw003;

import java.util.Calendar;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import common.TestRoot;

public class Kdw003Common extends TestRoot {
    String employeeCode = "";
    String password = "";
    String kdw003 = "";
    String kdw004 = "";
    String kmw003Approval = "";
    String kmw003 = "";

    public static String linkFullScreen(String link) {
        return domain + link;
    }

    public void setValueGrid(int rowNumber, int columnNumber, String value) {
        if (value.isEmpty()) {
            driver.findElements(
                    By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber + "]/td[" + columnNumber + "]"))
                    .get(0).click();
            driver.findElements(
                    By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber + "]/td[" + columnNumber + "]"))
                    .get(0).click();
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber + "]/td["
                    + columnNumber + "]" + "/.//input")).get(0).clear();
        } else {
            driver.findElements(
                    By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber + "]/td[" + columnNumber + "]"))
                    .get(0).click();
            driver.findElements(
                    By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber + "]/td[" + columnNumber + "]"))
                    .get(0).sendKeys(value);
        }
        driver.findElement(By.xpath("//body")).click();
    }

    /*
     * mode 0 個人別 - mode 1 日付別|| Month
     */
    public void setValueGrid(String itemName, String dateOrEmpId, int mode, String value) {
        WebElement element = null;
        if (mode == 0) {
            element = selectItemKdw003_1(itemName, dateOrEmpId);
        } else {
            element = selectItemKdw003_3(itemName, dateOrEmpId);
        }

        if (value.isEmpty()) {
            element.click();
            element.click();
            element = element.findElement(By.tagName("input"));
            element.clear();
        } else {
            element.click();
            element.sendKeys(value);
        }
        driver.findElement(By.xpath("//body")).click();
        WaitPageLoad();
    }

    /*
     * rowNumber is row number of checkbox + 1 columnNumber 0 -> 1
     */
    public void clickCheckBox(int rowNumber, int columnNumber) {
        driver.findElements(
                By.xpath("//table[@class ='mgrid-fixed-table']/tbody/tr[" + rowNumber + "]/.//span[@class = 'box']"))
                .get(columnNumber).click();
    }

    /*
     * mode 0 個人別 - mode 1 日付別|| Month
     */
    public void clickCheckBox(String itemName, String dateOrEmpId, int mode) {
        WebElement element = null;
        if (mode == 0) {
            element = selectItemKdw003_1(itemName, dateOrEmpId);
        } else {
            element = selectItemKdw003_3(itemName, dateOrEmpId);
        }
        element.click();
        driver.findElement(By.xpath("//body")).click();
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

    public boolean checkedBox(String itemName, String dateOrEmpId, int mode) {
        WebElement element = null;
        if (mode == 0) {
            element = selectItemKdw003_1(itemName, dateOrEmpId);
        } else {
            element = selectItemKdw003_3(itemName, dateOrEmpId);
        }
        String elementValue = element.findElement(By.tagName("input")).getAttribute("checked");
        if (elementValue != null) {
            return true;
        }
        return false;
    }


    public void setValueInput(String id, String value) {
        driver.findElement(By.id(id)).click();
        driver.findElement(By.id(id)).clear();
        driver.findElement(By.id(id)).sendKeys(value);
        driver.findElement(By.xpath("//body")).click();
    }

    public void initDefault() throws Exception {
        this.init();
        employeeCode = "013235";
        password = "Jinjikoi5";
        kdw003 = linkFullScreen("nts.uk.at.web/view/kdw/003/a/index.xhtml");
        kdw004 = linkFullScreen("nts.uk.at.web/view/kdw/004/a/index.xhtml");
        kmw003Approval = linkFullScreen("nts.uk.at.web/view/kmw/003/a/index.xhtml?initmode=2");
        kmw003 = linkFullScreen("nts.uk.at.web/view/kmw/003/a/index.xhtml");
    }

    public void scrollToRowColumn(int rowNumber, int columnNumber) {
        WebElement element = driver.findElement(
                By.xpath("//table[@class ='mgrid-free-table']/tbody/tr[" + rowNumber + "]/td[" + columnNumber + "]"));
        js.executeScript("arguments[0].scrollIntoView();", element);
    }

    public void extractData(Calendar inputStart, Calendar inputEnDate) {
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

      screenShotFull();

    }

    public void setKdw004Period(Calendar startdate, Calendar enddate) {
        WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startTime.clear();
        startTime.sendKeys(df1.format(startdate.getTime()));
        WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endTime.clear();
        endTime.sendKeys(df1.format(enddate.getTime()));

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("document.activeElement.blur();");    //leave focus

        WaitElementLoad(By.xpath("//button[@id='extractBtn']"));
        driver.findElement(By.xpath("//button[@id='extractBtn']")).click();
        WaitPageLoad();
	}
}