package nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.shared.dom.common.color.ColorCode;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * 
 * @author chungnt
 *
 */

public class SettingsSmartphoneStampHelper {

	public static SettingsSmartphoneStamp getSettingsSmartphoneStampDefault() {

		return new SettingsSmartphoneStamp("000-0000000001", new DisplaySettingsStampScreen(new CorrectionInterval(1), // dummy
				new SettingDateTimeColorOfStampScreen(new ColorCode("DUMMY"), new ColorCode("DUMMY")),
				new ResultDisplayTime(1)), // dummy
				new ArrayList<StampPageLayout>(), true);
	}

	public static SettingsSmartphoneStamp getSettingsSmartphoneStamp() {
		List<StampPageLayout> lstSPL = new ArrayList<>();
		List<ButtonSettings> lstBS = new ArrayList<>();
		
		lstBS.add(new ButtonSettings(new ButtonPositionNo(1),
				new ButtonDisSet(new ButtonNameSet(new ColorCode("DUMMY"), new ButtonName("DUMMY")), new ColorCode("DUMMY")),
				new ButtonType(ReservationArt.CANCEL_RESERVATION, Optional.empty()),
				NotUseAtr.NOT_USE,
				AudioType.GOOD_JOB));
		
		lstSPL.add(new StampPageLayout(new PageNo(1),
				new StampPageName("DUMMY"),
				new StampPageComment(new PageComment("DUMMY"), new ColorCode("DUMMY")),
				ButtonLayoutType.LARGE_2_SMALL_4,
				lstBS));
		
		return new SettingsSmartphoneStamp("000-0000000001", new DisplaySettingsStampScreen(new CorrectionInterval(1), // dummy
				new SettingDateTimeColorOfStampScreen(new ColorCode("DUMMY"), new ColorCode("DUMMY")),
				new ResultDisplayTime(1)), // dummy
				lstSPL, true);
	}
	
	public static Optional<ButtonSettings> getOPTButtonSeting(){
		ButtonSettings bSettings = new ButtonSettings(new ButtonPositionNo(1),
				new ButtonDisSet(new ButtonNameSet(new ColorCode("DUMMY"), new ButtonName("DUMMY")), new ColorCode("DUMMY")),
				new ButtonType(ReservationArt.CANCEL_RESERVATION, Optional.empty()),
				NotUseAtr.NOT_USE,
				AudioType.GOOD_JOB);
		
		return Optional.ofNullable(bSettings);
	}

}