package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;

/**
 * 予定開始終了時刻の反映(事前事後共通部分)
 * @author do_dt
 *
 */
public interface ScheStartEndTimeReflect {
	/**
	 * 予定開始終了時刻の反映(事前事後共通部分)
	 * @param para
	 * @param timeTypeData
	 * @return
	 */
	public void reflectScheStartEndTime(OvertimeParameter para, WorkTimeTypeOutput timeTypeData
			, IntegrationOfDaily dailyPerfor);
	/**
	 * 反映する開始終了時刻を求める
	 * @param para
	 * @param timeTypeData
	 * @return
	 */
	public ScheStartEndTimeReflectOutput findStartEndTime(StartEndTimeRelectCheck para, WorkTimeTypeOutput timeTypeData);
	/**
	 * 予定開始時刻を反映できるかチェックする
	 * @param employeeId
	 * @param datadata
	 * @param frameNo
	 * @return
	 */
	public boolean checkStartEndTimeReflect(String employeeId, GeneralDate datadata, Integer frameNo, String workTypeCode, OverTimeRecordAtr overTimeAtr, boolean isPre);
	/**
	 * 開始時刻を反映できるかチェックする
	 * @param employeeId
	 * @param datadata
	 * @param frameNo
	 * @param workTypeCode
	 * @param overTimeAtr
	 * @param isPre
	 * @return
	 */
	public boolean checkRecordStartEndTimereflect(String employeeId, GeneralDate datadata, Integer frameNo, String workTypeCode, OverTimeRecordAtr overTimeAtr, boolean isPre);
}
