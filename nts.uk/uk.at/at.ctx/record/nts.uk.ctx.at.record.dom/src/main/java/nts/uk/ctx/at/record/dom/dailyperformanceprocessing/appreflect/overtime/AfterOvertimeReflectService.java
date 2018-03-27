package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ApplicationReflectOutput;

/**
 * 事後申請の処理(勤務変更申請)
 * @author do_dt
 *
 */
public interface AfterOvertimeReflectService {
	/**
	 * 予定勤種就時の反映
	 * @return
	 */
	public void scheReflectJobType(OvertimeParameter overtimePara);
	/**
	 * 勤種・就時の反映
	 * @param overtimePara
	 * @return
	 */
	public ApplicationReflectOutput reflectAfterOvertime(OvertimeParameter overtimePara);

}
