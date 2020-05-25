package nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.shared.dom.common.color.ColorCode;
import nts.uk.shr.com.enumcommon.NotUseAtr;

public class PortalStampSettingsHelper {

	public static PortalStampSettings getDefault() {
		List<ButtonSettings> lstBS = new ArrayList<ButtonSettings>();

		lstBS.add(new ButtonSettings(new ButtonPositionNo(1),
				new ButtonDisSet(new ButtonNameSet(new ColorCode("DUMMY"), new ButtonName("DUMMY")),
						new ColorCode("DUMMY")),
				new ButtonType(ReservationArt.CANCEL_RESERVATION, Optional.empty()), NotUseAtr.NOT_USE,
				AudioType.GOOD_JOB));
		
		lstBS.add(new ButtonSettings(new ButtonPositionNo(2),
				new ButtonDisSet(new ButtonNameSet(new ColorCode("DUMMY"), new ButtonName("DUMMY")),
						new ColorCode("DUMMY")),
				new ButtonType(ReservationArt.CANCEL_RESERVATION, Optional.empty()), NotUseAtr.NOT_USE,
				AudioType.GOOD_JOB));

		return new PortalStampSettings("000-0000000001",
				new DisplaySettingsStampScreen(new CorrectionInterval(1),
						new SettingDateTimeColorOfStampScreen(new ColorCode("DUMMY"), new ColorCode("DUMMY")),
						new ResultDisplayTime(1)),
				lstBS, true, true);
	}
}
