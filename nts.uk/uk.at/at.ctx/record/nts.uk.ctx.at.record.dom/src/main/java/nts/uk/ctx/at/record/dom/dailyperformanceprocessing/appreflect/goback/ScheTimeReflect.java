package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback;

import java.util.Optional;

import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;

/**
 * 事前申請の処理: 予定時刻の反映
 * 時刻の反映
 * @author do_dt
 *
 */
public interface ScheTimeReflect {
	/**
	 * 予定時刻の反映
	 * @param para
	 * @param timeTypeScheReflect: 予定勤種・就時の反映
	 * @return
	 */
	public WorkInfoOfDailyPerformance reflectScheTime(GobackReflectParameter para, boolean timeTypeScheReflect,
			WorkInfoOfDailyPerformance dailyInfor);
	
	/**
	 * 
	 * @param timeTypeScheReflect 勤種・就時の反映できるフラグ
	 * @param timeData 申請する時刻
	 * @param applyTimeAtr 申請する時刻区分(開始/終了/開始2/終了2)
	 * @param workTimeCode 申請する就業時間帯コード
	 * @param scheTimeReflectAtr 予定時刻反映区分
	 * @return
	 */
	public TimeOfDayReflectOutput getTimeOfDayReflect(boolean timeTypeScheReflect, 
			Integer timeData,
			ApplyTimeAtr applyTimeAtr,
			String workTimeCode,
			ScheTimeReflectAtr scheTimeReflectAtr);
	/**
	 * 時刻の反映
	 * @param para
	 */
	public Optional<TimeLeavingOfDailyPerformance> reflectTime(GobackReflectParameter para, boolean workTypeTimeReflect);
	/**
	 * 1.出勤時刻を反映できるか
	 * 2.退勤時刻を反映できるか
	 * @param para
	 */
	public boolean checkAttendenceReflect(GobackReflectParameter para, Integer frameNo, boolean isPre);
	/**
	 * ジャスト遅刻により時刻を編集する
	 * ジャスト早退により時刻を編集する
	 * @param workTimeCode
	 * @param timeData
	 * @param frameNo
	 * @param isPre True:開始時刻, false:退勤時刻
	 * @return
	 */
	public Integer justTimeLateLeave(String workTimeCode, Integer timeData, Integer frameNo, boolean isPre);
	/**
	 * 予定時刻反映できるかチェックする
	 * @param worktimeCode
	 * @param scheReflectAtr
	 * @param scheAndRecordSameChangeFlg
	 * 
	 * @return
	 */
	public boolean checkScheReflect(String worktimeCode, boolean scheReflectAtr, ScheAndRecordSameChangeFlg scheAndRecordSameChangeFlg);
}
