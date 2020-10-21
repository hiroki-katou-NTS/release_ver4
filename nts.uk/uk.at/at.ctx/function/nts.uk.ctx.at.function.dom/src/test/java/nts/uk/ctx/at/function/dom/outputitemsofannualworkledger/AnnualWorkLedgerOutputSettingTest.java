package nts.uk.ctx.at.function.dom.outputitemsofannualworkledger;

import lombok.val;
import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.arc.testing.assertion.NtsAssert;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemSettingCode;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemSettingName;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.WorkStatusOutputSettings;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.enums.SettingClassificationCommon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.Any;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(JMockit.class)
public class AnnualWorkLedgerOutputSettingTest {
    @Injectable
    AnnualWorkLedgerOutputSetting.Require require;
    private final OutputItemSettingCode code = new OutputItemSettingCode("ABC");
    private final OutputItemSettingName name = new OutputItemSettingName("CBA");

    private final String cid = "companyId";

    private final String settingId = "settingId";
    private final String eplId = "employeeId";

    private final String yearHolidayCode = "yearHolidayCode";

    private final String iD = "id";
    @Test
    public void test_01(){
        val outputSettings = new AnnualWorkLedgerOutputSetting(
                iD,code,name,null,null,eplId,null
        );
        new Expectations() {
            {
                require.checkTheStandard(code, cid);
                result = true;
            }
        };
        val actual = outputSettings.checkDuplicateStandardSelection(require,code,cid);
        assertThat(actual).isEqualTo(true);
    }
    @Test
    public void test_02(){
        val outputSettings = new AnnualWorkLedgerOutputSetting(
                iD,code,name,null,null,eplId,null
        );
        new Expectations() {
            {
                require.checkFreedom(code, cid,eplId);
                result = true;
            }
        };
        val actual = outputSettings.checkDuplicateFreeSettings(require,code,cid,eplId);
        assertThat(actual).isEqualTo(true);
    }
    @Test
    public void test_03(){
        val outputSettings = new AnnualWorkLedgerOutputSetting(
                iD,code,name,null,null,eplId,null
        );
        NtsAssert.invokeGetters(outputSettings);
    }
}
