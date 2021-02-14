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

/**
 * DuplicateWorkLedgerSettingDomainService UnitTest
 *
 * @author khai.dh
 */
@RunWith(JMockit.class)
public class DuplicateWorkLedgerSettingDomainServiceTest {
    @Injectable
	DuplicateWorkLedgerSettingDomainService.Require require;

	/**
	 * Test duplicateSetting method
	 * Condition:
	 * 		input param: settingCategory == FREE_SETTING
	 * 		require.getOutputItemDetail returns empty item
	 * Expect:
	 * 		BusinessException: "Msg_1928"
	 */
	@Test
    public void testDuplicateSetting(){
 		OutputItemSettingCode code = new OutputItemSettingCode("OutputItemSettingCode01");
		OutputItemSettingName name = new OutputItemSettingName("OutputItemSettingName01");

		new Expectations(AppContexts.class) {{
			AppContexts.user().employeeId();
			result = "employeeId";

			require.getOutputItemDetail("dupSrcId01");
			result = Optional.empty();
		}};

		NtsAssert.businessException("Msg_1928", () -> {
			DuplicateWorkLedgerSettingDomainService.duplicateSetting(
					require,
					SettingClassificationCommon.FREE_SETTING,
					"dupSrcId01",
					code,
					name
			);
		});
    }/**
	 * Test duplicateSetting method
	 * Condition:
	 * 		input param: settingCategory == STANDARD_SELECTION
	 * 		require.getOutputItemDetail returns empty item
	 * Expect:
	 * 		BusinessException: "Msg_1928"
	 */
	@Test
    public void testDuplicateSetting_01(){
 		OutputItemSettingCode code = new OutputItemSettingCode("OutputItemSettingCode01");
		OutputItemSettingName name = new OutputItemSettingName("OutputItemSettingName01");

		new Expectations(AppContexts.class) {{
			AppContexts.user().employeeId();
			result = "employeeId00";

			require.getOutputItemDetail("dupSrcId01");
			result = Optional.empty();
		}};

		NtsAssert.businessException("Msg_1928", () -> {
			DuplicateWorkLedgerSettingDomainService.duplicateSetting(
					require,
					SettingClassificationCommon.STANDARD_SELECTION,
					"dupSrcId01",
					code,
					name
			);
		});
    }

	/**
	 * Test duplicateSetting method
	 *
	 * Condition:
	 * 		input param: settingCategory == STANDARD_SELECTION
	 * 		require.getOutputItemDetail returns non empty item
	 * 		require.standardCheck TRUE
	 * Expect:
	 * 		BusinessException: "Msg_1927"
	 */
	@Test
	public void testDuplicateSetting_02(){
		OutputItemSettingCode code = new OutputItemSettingCode("OutputItemSettingCode02");
		OutputItemSettingName name = new OutputItemSettingName("OutputItemSettingName02");
		val item = new WorkLedgerOutputItem(
				"dupSrcId02",
				code,
				Arrays.asList(new AttendanceItemToPrint(1, 1)),
				name,
				SettingClassificationCommon.STANDARD_SELECTION,
				Optional.of("sid")
		);
		new Expectations(AppContexts.class) {{
			AppContexts.user().employeeId();
			result = "employeeId02";

			require.getOutputItemDetail("dupSrcId02");
			result = Optional.of(item);

			require.standardCheck(code);
			result = true;
		}};

		NtsAssert.businessException("Msg_1927", () -> {
			DuplicateWorkLedgerSettingDomainService.duplicateSetting(
					require,
					SettingClassificationCommon.STANDARD_SELECTION,
					"dupSrcId02",
					code,
					name
			);
		});
	}

	/**
	 * Test duplicateSetting method
	 *
	 * Condition:
	 * 		input param: settingCategory == FREE_SETTING
	 * 		require.getOutputItemDetail returns non empty item
	 * 		require.freeCheck TRUE
	 * Expect:
	 * 		BusinessException: "Msg_1927"
	 */
	@Test
	public void testDuplicateSetting_03(){
		OutputItemSettingCode code = new OutputItemSettingCode("OutputItemSettingCode03");
		OutputItemSettingName name = new OutputItemSettingName("OutputItemSettingName03");
		val item = new WorkLedgerOutputItem(
				"dupSrcId03",
				code,
				Arrays.asList(new AttendanceItemToPrint(1, 1)),
				name,
				SettingClassificationCommon.FREE_SETTING,
				Optional.of("sid")
		);
		new Expectations(AppContexts.class) {{
			AppContexts.user().employeeId();
			result = "employeeId03";

			require.getOutputItemDetail("dupSrcId03");
			result = Optional.of(item);

			require.freeCheck(code, "employeeId03");
			result = true;
		}};

		NtsAssert.businessException("Msg_1927", () -> {
			DuplicateWorkLedgerSettingDomainService.duplicateSetting(
					require,
					SettingClassificationCommon.FREE_SETTING,
					"dupSrcId03",
					code,
					name
			);
		});
	}

	/**
	 * Test duplicateSetting method
	 *
	 * Condition:
	 * 		input param: settingCategory == STANDARD_SELECTION
	 * 		require.getOutputItemDetail returns non empty item
	 * 		require.standardCheck FALSE
	 * Expect:
	 * 		returns AtomTask with invocation to require.duplicateWorkLedgerOutputItem
	 */
	@Test
	public void testDuplicateSetting_04(){
		OutputItemSettingCode code = new OutputItemSettingCode("OutputItemSettingCode04");
		OutputItemSettingName name = new OutputItemSettingName("OutputItemSettingName04");
		val item = new WorkLedgerOutputItem(
				"dupSrcId04",
				code,
				Arrays.asList(new AttendanceItemToPrint(1, 1)),
				name,
				SettingClassificationCommon.STANDARD_SELECTION,
				Optional.of("sid")
		);
		new Expectations(AppContexts.class,IdentifierUtil.class) {{
			AppContexts.user().employeeId();
			result = "employeeId04";

			require.getOutputItemDetail("dupSrcId04");
			result = Optional.of(item);

			require.standardCheck(code);
			result = false;

			IdentifierUtil.randomUniqueId();
			result = "uid04";
		}};

		val actual = DuplicateWorkLedgerSettingDomainService.duplicateSetting(
				require,
				SettingClassificationCommon.STANDARD_SELECTION,
				"dupSrcId04",
				code,
				name
		);

		NtsAssert.atomTask(
				() -> actual,
				any-> require.duplicateWorkLedgerOutputItem( "dupSrcId04", "uid04", code, name)
		);
	}

	/**
	 * Test duplicateSetting method
	 *
	 * Condition:
	 * 		input param: settingCategory == FREE_SETTING
	 * 		require.getOutputItemDetail returns non empty item
	 * 		require.freeCheck  FALSE
	 * Expect:
	 * 		returns AtomTask with invocation to require.duplicateWorkLedgerOutputItem
	 */
	@Test
	public void testDuplicateSetting_05(){
		OutputItemSettingCode code = new OutputItemSettingCode("OutputItemSettingCode05");
		OutputItemSettingName name = new OutputItemSettingName("OutputItemSettingName05");
		val item = new WorkLedgerOutputItem(
				"dupSrcId05",
				code,
				Arrays.asList(new AttendanceItemToPrint(1, 1)),
				name,
				SettingClassificationCommon.FREE_SETTING,
				Optional.of("sid")
		);
		new Expectations(AppContexts.class,IdentifierUtil.class) {{
			AppContexts.user().employeeId();
			result = "employeeId05";

			require.getOutputItemDetail("dupSrcId05");
			result = Optional.of(item);

			require.freeCheck(code, "employeeId05");
			result = false;

			IdentifierUtil.randomUniqueId();
			result = "uid05";
		}};

		val actual = DuplicateWorkLedgerSettingDomainService.duplicateSetting(
				require,
				SettingClassificationCommon.FREE_SETTING,
				"dupSrcId05",
				code,
				name
		);

		NtsAssert.atomTask(
				() -> actual,
				any-> require.duplicateWorkLedgerOutputItem( "dupSrcId05", "uid05", code, name)
		);
	}
}