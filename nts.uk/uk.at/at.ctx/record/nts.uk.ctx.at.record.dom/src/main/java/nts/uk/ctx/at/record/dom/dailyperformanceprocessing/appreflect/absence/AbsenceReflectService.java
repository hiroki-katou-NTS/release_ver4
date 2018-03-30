package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.absence;

import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ApplicationReflectOutput;

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
	public ApplicationReflectOutput absenceReflect(AbsenceReflectParameter absencePara, boolean isPre);
	/**
	 * 予定勤種の反映
	 * @param absencePara
	 */
	public void reflectScheWorkTimeWorkType(AbsenceReflectParameter absencePara, boolean isPre);
	/**
	 * 予定勤種を反映できるかチェックする
	 * @param absencePara
	 * @return
	 */
	public boolean checkReflectScheWorkTimeType(AbsenceReflectParameter absencePara, boolean isPre);
}
