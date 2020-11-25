package nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import nts.arc.task.tran.AtomTask;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.AutoCreateStampCardNumberService;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.ContractCode;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampCardCreateResult;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampMeans;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.AudioType;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonDisSet;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonName;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonNameSet;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonPositionNo;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonSettings;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonType;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.CorrectionInterval;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.DisplaySettingsStampScreen;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.PortalStampSettings;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ReservationArt;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ResultDisplayTime;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.SettingDateTimeColorOfStampScreen;
import nts.uk.ctx.at.shared.dom.common.color.ColorCode;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * 
 * @author chungnt
 *
 */

@RunWith(JMockit.class)
public class EnterStampFromPortalServiceTest {

	@Injectable
	private nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.EnterStampFromPortalService.Require require;
	
	/**
	 * require.getPortalStampSetting() isPresent()
	 * resutl : BusinessException("Msg_1632")
	 */
	@Test
	public void test_settingStampPotal_isPresent() {
		ContractCode contractCode = new ContractCode("DUMMY");
		String employeeId = "DUMMY";
		GeneralDateTime dateTime = GeneralDateTime.now();
		ButtonPositionNo buttonPositionNo = new ButtonPositionNo(1);
		
		new Expectations() {
			{
				require.getPortalStampSetting();
			}
		};
		
		NtsAssert.businessException("Msg_1632",
				() -> EnterStampFromPortalService.create(require, contractCode, employeeId,
						dateTime, buttonPositionNo, null));
	}
	
	/**
	 * $ボタン詳細設定 = $ポータルの打刻設定.ボタン詳細設定を取得する(ボタン位置NO)
	 * resutl : BusinessException("Msg_1632")
	 */
	@Test
	public void test_settingButton_isPresent() {
		ContractCode contractCode = new ContractCode("DUMMY");
		String employeeId = "DUMMY";
		GeneralDateTime dateTime = GeneralDateTime.now();
		ButtonPositionNo buttonPositionNo = new ButtonPositionNo(1);
		DisplaySettingsStampScreen displaySettingsStamScreen = new DisplaySettingsStampScreen(new CorrectionInterval(1),
				new SettingDateTimeColorOfStampScreen(new ColorCode("DUMMY"), new ColorCode("DUMMY")),
				new ResultDisplayTime(1));
		List<ButtonSettings> buttonSettings = new ArrayList<>();
		
		PortalStampSettings portalStampSettings = new PortalStampSettings("DUMMY", displaySettingsStamScreen, buttonSettings, true, true);
		
		new Expectations() {
			{
				require.getPortalStampSetting();
				result = Optional.of(portalStampSettings);
			}
		};
		
		NtsAssert.businessException("Msg_1632",
				() -> EnterStampFromPortalService.create(require, contractCode, employeeId,
						dateTime, buttonPositionNo, null));
	}
	
	/**
	 * $ボタン詳細設定 = $ポータルの打刻設定.ボタン詳細設定を取得する(ボタン位置NO)
	 * resutl : BusinessException("Msg_1632")
	 */
	@Test
	public void test() {
		ContractCode contractCode = new ContractCode("DUMMY");
		String employeeId = "DUMMY";
		GeneralDateTime dateTime = GeneralDateTime.now();
		ButtonPositionNo buttonPositionNo = new ButtonPositionNo(1);
		DisplaySettingsStampScreen displaySettingsStamScreen = new DisplaySettingsStampScreen(new CorrectionInterval(1),
				new SettingDateTimeColorOfStampScreen(new ColorCode("DUMMY"), new ColorCode("DUMMY")),
				new ResultDisplayTime(1));
		List<ButtonSettings> buttonSettings = new ArrayList<>();
		
		buttonSettings.add(new ButtonSettings(buttonPositionNo, 
				new ButtonDisSet(new ButtonNameSet(new ColorCode("DUMMY"), new ButtonName("DUMMY")), new ColorCode("DUMMY")), 
				new ButtonType(ReservationArt.NONE, Optional.empty()),
				NotUseAtr.USE,
				AudioType.NONE));
		
		PortalStampSettings portalStampSettings = new PortalStampSettings("DUMMY", displaySettingsStamScreen, buttonSettings, true, true);
		
		new Expectations() {
			{
				require.getPortalStampSetting();
				result = Optional.of(portalStampSettings);
			}
		};
		
		AtomTask atomTask = AtomTask.of(() -> {});// dummy
		StampCardCreateResult stampCardCreateResult = new StampCardCreateResult("1", Optional.of(atomTask));
		
		new MockUp<AutoCreateStampCardNumberService>() {
			@Mock
			public Optional<StampCardCreateResult> create(AutoCreateStampCardNumberService.Require require,
					String employeeID, StampMeans stampMeanss) {
				return Optional.of(stampCardCreateResult);
			}
		};
		
		TimeStampInputResult timeStampInputResult = EnterStampFromPortalService.create(require, contractCode,
				employeeId, dateTime, buttonPositionNo, null);
		
		assertThat(timeStampInputResult.at).isNotEmpty();
		assertThat(timeStampInputResult.stampDataReflectResult.getAtomTask()).isNotNull();
	}
}