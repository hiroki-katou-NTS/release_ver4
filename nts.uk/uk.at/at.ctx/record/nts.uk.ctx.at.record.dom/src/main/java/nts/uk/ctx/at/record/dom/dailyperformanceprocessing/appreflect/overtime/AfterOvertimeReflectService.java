package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

/**
 * 事後申請の処理(勤務変更申請)
 * @author do_dt
 *
 */
public interface AfterOvertimeReflectService {
	/**
	 * 事後申請の反映処理(残業申請)
	 * @param overtimePara
	 * @return
	 */
	public void reflectAfterOvertime(OvertimeParameter overtimePara);
}
