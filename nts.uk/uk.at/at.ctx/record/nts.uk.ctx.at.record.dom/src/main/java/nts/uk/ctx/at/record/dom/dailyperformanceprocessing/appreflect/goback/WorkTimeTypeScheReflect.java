package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback;

import nts.arc.time.GeneralDate;

/**
 * 予定勤種・就時の反映
 * @author do_dt
 *
 */
public interface WorkTimeTypeScheReflect {
	/**
	 * 予定勤種・就時の反映
	 * @param para
	 * @return
	 */
	public boolean reflectScheWorkTimeType(GobackReflectParameter para);
	/**
	 * 予定勤務種類による勤種・就時を反映できるかチェックする
	 * @param para
	 * @return
	 */
	public boolean checkReflectWorkTimeType(GobackReflectParameter para);
	/**
	 * 振出・休出時反映する区分をチェックする
	 * @param employeeId
	 * @param dateData
	 * @param isOutResReflectAtr 振出・休出時反映する区分をチェックする
	 * @return
	 */
	public boolean checkScheAndRecordSamseChange(String employeeId, GeneralDate dateData, boolean isOutResReflectAtr);
	/**
	 * 勤種・就時の反映
	 * @param para
	 * @return
	 */
	public boolean reflectRecordWorktimetype(GobackReflectParameter para);
	/**
	 * 実績勤務種類による勤種・就時を反映できるかチェックする
	 * @param employeeId
	 * @param baseDate
	 * @param outResReflectAtr
	 * @param changeAppGobackAtr
	 * @return
	 */
	public boolean checkReflectRecordForActual(String employeeId, GeneralDate baseDate, boolean outResReflectAtr, ChangeAppGobackAtr changeAppGobackAtr);
}
