package nts.uk.ctx.at.function.dom.workledgeroutputitem;

import lombok.val;
import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.arc.testing.assertion.NtsAssert;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.function.dom.commonform.AttendanceItemToPrint;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemSettingCode;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemSettingName;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.enums.SettingClassificationCommon;
import nts.uk.shr.com.context.AppContexts;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * WorkLedgerOutputItem Test
 *
 * @author khai.dh
 */
@RunWith(JMockit.class)
public class WorkLedgerOutputItemTest {
    @Injectable
    WorkLedgerOutputItem.Require require;

    /**
     * Test:
     * - method checkDuplicateStandardSelections: is true
     */
    @Test
    public void testDuplicateStandardSelection_01() {
        val code = new OutputItemSettingCode("OutputItemSettingCode01");
        new Expectations() {{
            require.standardCheck(code);
            result = true;
        }};
        val actual = WorkLedgerOutputItem.checkDuplicateStandardSelection(require, code);
        assertThat(actual).isTrue();
    }

    /**
     * Test:
     * - method checkDuplicateFreeSettings: is true
     */
    @Test
    public void testDuplicateFreeSettings_02() {
        val code = new OutputItemSettingCode("OutputItemSettingCode02");

        new Expectations() {{
            require.freeCheck(code, "emp02");
            result = true;
        }};
        val actual = WorkLedgerOutputItem.checkDuplicateFreeSettings(require, code, "emp02");
        assertThat(actual).isTrue();
    }

    /**
     * Test:
     * - Getter: WorkLedgerOutputItem
     * - SettingClassificationCommon: FREE_SETTING
     */
    @Test
    public void testGetterOutputSettings_03() {
        val code = new OutputItemSettingCode("OutputItemSettingCode03");
        val name = new OutputItemSettingName("OutputItemSettingName03");
        val outputSettings = new WorkLedgerOutputItem(
                "id03",
                code,
                Arrays.asList(new AttendanceItemToPrint(1, 1)),
                name,
                SettingClassificationCommon.STANDARD_SELECTION,
                "emp03"
        );

        NtsAssert.invokeGetters(outputSettings);
    }

    /**
     * Test:
     * - Getter: WorkLedgerOutputItem
     * - SettingClassificationCommon: STANDARD_SELECTION
     */
    @Test
    public void testGetterOutputSettings_04() {
        val code = new OutputItemSettingCode("OutputItemSettingCode03");
        val name = new OutputItemSettingName("OutputItemSettingName03");
        val outputSettings = new WorkLedgerOutputItem(
                "id03",
                code,
                Arrays.asList(new AttendanceItemToPrint(1, 1)),
                name,
                SettingClassificationCommon.FREE_SETTING,
                "emp03"
        );

        NtsAssert.invokeGetters(outputSettings);
    }
}
