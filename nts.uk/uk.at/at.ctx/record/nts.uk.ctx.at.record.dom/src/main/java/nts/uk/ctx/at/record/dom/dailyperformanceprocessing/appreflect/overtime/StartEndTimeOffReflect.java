package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;

/**
 * 開始終了時刻の反映(事前申請)
 * @author do_dt
 *
 */
public interface StartEndTimeOffReflect {
	/**
	 * 開始終了時刻の反映(事前申請)
	 * @param param
	 */
	public void startEndTimeOffReflect(OvertimeParameter param, WorkInfoOfDailyPerformance workInfo);
	/**
	 * 自動打刻をクリアする
	 * @param employeeId
	 * @param dateData
	 * @param worktypeCode
	 * @param isClearAuto
	 */
	public void clearAutomaticEmbossing(String employeeId, GeneralDate dateData, String worktypeCode, boolean isClearAuto, 
			OvertimeAppParameter overInfor);
	/**
	 * 開始終了時刻の反映(事前)
	 * @param param
	 * @param workInfo
	 * @return
	 */
	public TimeLeavingOfDailyPerformance startEndTimeOutput(StartEndTimeRelectCheck param, WorkInfoOfDailyPerformance workInfo,
			Optional<TimeLeavingOfDailyPerformance> attendanceLeave);
	/**
	 * ジャスト遅刻早退により時刻を編集する
	 * @param startEndTime
	 * @return
	 */
	public StartEndTimeOutput justLateEarly(String worktimeCode, ScheStartEndTimeReflectOutput startEndTime);

}
