package nts.uk.ctx.at.record.pub.dailyperform.appreflect;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ExecutionType;
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
    public ScheAndRecordIsReflectPub appReflectProcess(AppCommonPara para, ExecutionType reflectSetting,Boolean isCalWhenLock);
	/**
	 * 事前申請の処理(Xử lý xin trước) 　直行直帰
	 * @param para
	 * @return
	 */
	public void preGobackReflect(GobackReflectPubParameter para, boolean isPre);
	/**
	 * 	勤務実績に反映
	 * 事前申請の処理　残業申請
	 * @param param
	 * @return
	 */
	public void preOvertimeReflect(PreOvertimePubParameter param);
	/**
	 * (休暇申請)
	 * @param param
	 * @param isPre : true: 事前申請処理, false: 事後申請処理
	 * @return
	 */
	public void absenceReflect(WorkChangeCommonReflectPubPara param, boolean isPre);
	/**
	 * 	勤務実績に反映: 事前申請の処理(休日出勤申請)
	 * @param param
	 * @return
	 */
	public void holidayWorkReflect(HolidayWorkReflectPubPara param, boolean isPre);
	/**
	 * 勤務変更申請
	 * @param param
	 * @param isPre : true: 事前申請処理, false: 事後申請処理
	 * @return
	 */
	public void workChangeReflect(WorkChangeCommonReflectPubPara param, boolean isPre);
	/**
	 * 振休申請
	 * @param param
	 * @param isPre
	 * @return
	 */
	public void absenceLeaveReflect(CommonReflectPubParameter param, boolean isPre);
	/**
	 * 振出申請
	 * @param param
	 * @param isPre
	 * @return
	 */
	public void recruitmentReflect(CommonReflectPubParameter param, boolean isPre);
	
	/**
	 * 確定状態によるチェック
	 * @param cid
	 * @param prePost
	 * @param appType
	 * @return
	 */
	public boolean checkConfirmStatus(ConfirmStatusCheck chkParam);
	
	public void createLogError(String sid, GeneralDate ymd, String excLogId);
	
	public Optional<IdentityProcessUseSetPub> getIdentityProcessUseSet(String cid);
	
	public Optional<ApprovalProcessingUseSettingPub> getApprovalProcessingUseSetting(String cid);
}
