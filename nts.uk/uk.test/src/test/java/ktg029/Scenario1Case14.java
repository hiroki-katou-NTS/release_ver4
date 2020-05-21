package ktg029;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kdw003.Kdw003Common;


public class Scenario1Case14 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg029/Scenario1Case14";
        this.init();
    }

    @Test
    public void test() throws Exception {


// 1.14 代休残数
        // login申請者
        login("027166", "Jinjikoi5");

        // Setting screen kmk012
        setProcessYearMonth(1, "2020/05");

        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();

        // Go to screen kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        // Tacke a photo
        screenShot();

        // Go to screen cgg008
        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();

        // Tacke a photo
        screenShot();

        WaitPageLoad();
        this.uploadTestLink(560, 136);
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