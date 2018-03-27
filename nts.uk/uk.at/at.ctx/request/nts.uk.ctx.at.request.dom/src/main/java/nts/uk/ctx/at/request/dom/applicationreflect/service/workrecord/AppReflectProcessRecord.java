package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

/**
 * 勤務実績に反映
 * @author do_dt
 *
 */
public interface AppReflectProcessRecord {
	/**
	 * 事前チェック処理
	 * @param info
	 * @return
	 */
	public boolean appReflectProcessRecord(AppReflectInfor info);
	/**
	 * 事前申請の処理(Xử lý xin trước) 　直行直帰
	 * 事後申請の処理
	 * isPre：事前申請
	 * @return
	 */
	public WorkReflectedStatesInfo gobackReflectRecord(GobackReflectPara para, boolean isPre);
	/**
	 * 残業申請：　 事前申請の処理   
	 * @return
	 */
	public WorkReflectedStatesInfo overtimeReflectRecord(OvertimeReflectPara para, boolean isPre);
}
