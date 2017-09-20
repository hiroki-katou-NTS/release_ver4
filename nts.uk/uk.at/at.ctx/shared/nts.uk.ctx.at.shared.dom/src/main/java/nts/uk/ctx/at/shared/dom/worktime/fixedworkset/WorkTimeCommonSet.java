package nts.uk.ctx.at.shared.dom.worktime.fixedworkset;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.worktime.CommonSetting.lateleaveearly.LateLeaveEarlySettingOfWorkTime;

/**
 * 就業時間帯の共通設定
 * @author ken_takasu
 *
 */
public class WorkTimeCommonSet {
	@Getter
	private LateLeaveEarlySettingOfWorkTime lateSetting;
	
	@Getter
	private LateLeaveEarlySettingOfWorkTime leaveEarlySetting;
}
