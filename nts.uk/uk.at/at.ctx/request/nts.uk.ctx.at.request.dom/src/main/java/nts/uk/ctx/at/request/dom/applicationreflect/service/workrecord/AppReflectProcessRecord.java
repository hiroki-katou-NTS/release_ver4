package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.applicationreflect.service.WorkChangeCommonReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.ExecutionTypeExImport;

/**
 * 勤務実績に反映
 * @author do_dt
 *
 */
public interface AppReflectProcessRecord {
	/**
	 * 事前チェック処理
	 * @param info
	 * @return True: 反映する、False:　反映しない
	 */
    public ScheAndRecordIsReflect appReflectProcessRecord(Application_New appInfor, ExecutionTypeExImport executionType, GeneralDate appDate,Boolean isCalWhenLock);
	/**
	 * 事前申請の処理(Xử lý xin trước) 　直行直帰
	 * 事後申請の処理
	 * isPre：事前申請
	 * @return
	 */
	public void gobackReflectRecord(GobackReflectPara para, boolean isPre);
	/**
	 * 残業申請：　 事前申請の処理   
	 * @return
	 */
	public void overtimeReflectRecord(OvertimeReflectPara para, boolean isPre);
	/**
	 * 休暇申請
	 * @param para
	 * @param isPre True: 事前, False: 事後
	 * @return
	 */
	public void absenceReflectRecor(WorkChangeCommonReflectPara para, boolean isPre);
	/**
	 * 勤務実績に反映: 事前申請の処理(休日出勤申請)
	 * @param para
	 * @param isPre
	 * @return
	 */
	public void holidayWorkReflectRecord(HolidayWorkReflectPara para, boolean isPre);
	/**
	 * 勤務変更申請
	 * @param para
	 * @param isPre
	 * @return
	 */
	public void workChangeReflectRecord(WorkChangeCommonReflectPara para, boolean isPre);
	/**
	 * 振休申請
	 * @param para
	 * @param isPre
	 * @return
	 */
	public void absenceLeaveReflectRecord(CommonReflectPara para, boolean isPre);
	/**
	 * 振出申請
	 * @param para
	 * @param isPre
	 * @return
	 */
	public void recruitmentReflectRecord(CommonReflectPara para, boolean isPre);
	
	public void createLogError(String sid, GeneralDate ymd, String excLogId);
	
	public Optional<IdentityProcessUseSetAc> getIdentityProcessUseSet(String cid);
	
	Optional<ApprovalProcessingUseSettingAc> getApprovalProcessingUseSetting(String cid);
}
