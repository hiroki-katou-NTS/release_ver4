package nts.uk.ctx.at.shared.dom.worktime.CommonSetting.lateleaveearly;

import lombok.Value;

/**
 * 就業時間帯の遅刻・早退設定
 * @author ken_takasu
 *
 */
@Value
public class LateLeaveEarlySettingOfWorkTime {
	//遅刻
	private LateLeaveEarlyEachSettingOfWorkTime lateSettingOfWorkTime;
	//早退
	private LateLeaveEarlyEachSettingOfWorkTime leaveEarlyEachSettingOfWorkTime;
	//共通
	private LateLeaveEarlyCommonSettingOfWorkTime commonSetting;
}



