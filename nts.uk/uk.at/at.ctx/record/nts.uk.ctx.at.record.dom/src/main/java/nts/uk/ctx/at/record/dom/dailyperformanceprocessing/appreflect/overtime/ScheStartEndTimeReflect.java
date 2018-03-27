package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import nts.arc.time.GeneralDate;

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
	public ScheStartEndTimeReflectOutput reflectScheStartEndTime(OvertimeParameter para, WorkTimeTypeOutput timeTypeData);
	/**
	 * 反映する開始終了時刻を求める
	 * @param para
	 * @param timeTypeData
	 * @return
	 */
	public ScheStartEndTimeReflectOutput findStartEndTime(OvertimeParameter para, WorkTimeTypeOutput timeTypeData);
	/**
	 * 予定開始時刻を反映できるかチェックする
	 * @param employeeId
	 * @param datadata
	 * @param frameNo
	 * @return
	 */
	public boolean checkStartEndTimeReflect(String employeeId, GeneralDate datadata, Integer frameNo, String workTypeCode, boolean isPre);
}
