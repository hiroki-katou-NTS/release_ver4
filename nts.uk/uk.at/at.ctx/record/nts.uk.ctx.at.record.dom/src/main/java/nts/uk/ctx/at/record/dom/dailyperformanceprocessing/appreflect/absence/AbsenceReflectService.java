package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.absence;

import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonReflectParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.workchange.WorkChangeCommonReflectPara;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.arc.time.GeneralDate;

/**
 * 勤務実績に反映: 休暇申請処理
 * @author do_dt
 *
 */
public interface AbsenceReflectService {
	/**
	 * 	(休暇申請)
	 * @param absencePara
	 * @param isPre: True - 事前申請の処理, False - 事後申請の処理
	 * @return
	 */
	public boolean absenceReflect(WorkChangeCommonReflectPara absencePara, boolean isPre);
	/**
	 * 予定開始終了時刻の反映
	 * @param employeeId
	 * @param baseDate
	 * @param workTypeCode
	 * @param isReflect
	 */
	public WorkInfoOfDailyPerformance reflectScheStartEndTime(String employeeId, GeneralDate baseDate, String workTypeCode, boolean isReflect, WorkInfoOfDailyPerformance dailyInfor);
	/**
	 * 開始終了時刻の反映
	 * @param employeeId
	 * @param baseDate
	 * @param workTypeCode
	 */
	public TimeLeavingOfDailyPerformance reflectRecordStartEndTime(String employeeId, GeneralDate baseDate, String workTypeCode);
	/**
	 * 開始終了時刻をクリアするかチェックする
	 * @param workTypeCode
	 * @return
	 */
	public boolean checkTimeClean(String employeeId, GeneralDate baseDate, String workTypeCode);
	
	
}
