package nts.uk.ctx.at.record.pub.dailyperform.appreflect;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback.GobackReflectPubParameter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.overtime.PreOvertimePubParameter;

/**
 * 反映状況によるチェック
 * @author do_dt
 *
 */
public interface AppReflectProcessRecordPub {
	/**
	 * チェック処理(Xử lý check)
	 * @param para
	 * @return True 反映する、
	 * False：反映しない
	 */
	public boolean appReflectProcess(AppCommonPara para);
	/**
	 * 事前申請の処理(Xử lý xin trước) 　直行直帰
	 * @param para
	 * @return
	 */
	public boolean preGobackReflect(GobackReflectPubParameter para);
	/**
	 * 事後申請の処理 　直行直帰
	 * @param para
	 * @return
	 */
	public boolean afterGobackReflect(GobackReflectPubParameter para);
	/**
	 * 	勤務実績に反映
	 * 事前申請の処理　残業申請
	 * @param param
	 * @return
	 */
	public boolean preOvertimeReflect(PreOvertimePubParameter param);
	/**
	 * 勤務実績に反映: 事後残業申請処理
	 * @param param
	 * @return
	 */
	public boolean afterOvertimeReflect(PreOvertimePubParameter param);
	/**
	 * (休暇申請)
	 * @param param
	 * @param isPre : true: 事前申請処理, false: 事後申請処理
	 * @return
	 */
	public boolean absenceReflect(WorkChangeCommonReflectPubPara param, boolean isPre);
	/**
	 * 	勤務実績に反映: 事前申請の処理(休日出勤申請)
	 * @param param
	 * @return
	 */
	public boolean holidayWorkReflect(HolidayWorkReflectPubPara param, boolean isPre);
	/**
	 * 勤務変更申請
	 * @param param
	 * @param isPre : true: 事前申請処理, false: 事後申請処理
	 * @return
	 */
	public boolean workChangeReflect(WorkChangeCommonReflectPubPara param, boolean isPre);
	/**
	 * 振休申請
	 * @param param
	 * @param isPre
	 * @return
	 */
	public boolean absenceLeaveReflect(CommonReflectPubParameter param, boolean isPre);
	/**
	 * 振出申請
	 * @param param
	 * @param isPre
	 * @return
	 */
	public boolean recruitmentReflect(CommonReflectPubParameter param, boolean isPre);
	
	public boolean isRecordData(String employeeId, GeneralDate baseDate);
	/**
	 * 確定状態によるチェック
	 * @param cid
	 * @param prePost
	 * @param appType
	 * @return
	 */
	public boolean checkConfirmStatus(ConfirmStatusCheck chkParam);
	
}
