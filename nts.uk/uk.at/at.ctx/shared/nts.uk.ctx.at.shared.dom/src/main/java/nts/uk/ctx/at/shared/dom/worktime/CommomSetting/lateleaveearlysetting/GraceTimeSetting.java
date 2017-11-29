package nts.uk.ctx.at.shared.dom.worktime.CommomSetting.lateleaveearlysetting;

import lombok.Value;
import lombok.val;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.CommomSetting.TimeSheetWithUseAtr;

/**
 * 猶予時間設定
 * @author ken_takasu
 *
 */
@Value
public class GraceTimeSetting {

	private LateLeaveEarlyGraceTime graceTime;
	private boolean includeInWorkingHours;//true:含める　false：含めない
	
	/**
	 * 遅刻猶予時間帯を作成する
	 * @param baseTimeSheet
	 * @return
	 */
	public TimeSpanForCalc createLateGraceTimeSheet(TimeSheetWithUseAtr baseTimeSheet) {
		//猶予時間帯の終了時刻の作成
		val correctedEndTime = baseTimeSheet.getStartTime().forwardByMinutes(this.graceTime.minute());
		//猶予時間帯の作成
		return new TimeSpanForCalc(baseTimeSheet.getStartTime(), correctedEndTime);
	}
	
	/**
	 * 早退猶予時間帯を作成する
	 * @param baseTimeSheet
	 * @return
	 */
	public TimeSpanForCalc createLeaveEarlyGraceTimeSheet(TimeSheetWithUseAtr baseTimeSheet) {
		//猶予時間帯の開始時刻の作成
		val correctedStartTime = baseTimeSheet.getEndTime().backByMinutes(this.graceTime.minute());
		//猶予時間帯の作成
		return new TimeSpanForCalc(correctedStartTime, baseTimeSheet.getEndTime());
	}
	
	
	/**
	 * 猶予時間が0：00かどうか判断する
	 * @return　0：00の場合：true　0：00でない場合：false
	 */
	public boolean isZero() {
		return this.graceTime.v() == 0;
	}
}
