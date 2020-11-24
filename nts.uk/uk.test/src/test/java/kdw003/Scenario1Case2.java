package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;
import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario1Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario1Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {
        Calendar inputdate = Calendar.getInstance();
        inputdate.add(Calendar.MONTH, -1);

        //ログイン（正社員）
        login("018234", "Jinjikoi5");
        
        //処理年月の変更
        new Kdw003Common().setProcessYearMonth(1, df3.format(inputdate.getTime()));

        //KDW003A 勤務報告書
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        
        WaitPageLoad();
        
        screenShot();  
        
        this.uploadTestLink(581, 143);
     
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