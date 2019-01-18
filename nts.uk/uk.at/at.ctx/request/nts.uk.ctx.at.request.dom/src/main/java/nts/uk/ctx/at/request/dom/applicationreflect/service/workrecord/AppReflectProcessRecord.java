package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.applicationreflect.service.WorkChangeCommonReflectPara;

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
	public boolean appReflectProcessRecord(Application_New appInfor, boolean chkRecord);
	/**
	 * 事前申請の処理(Xử lý xin trước) 　直行直帰
	 * 事後申請の処理
	 * isPre：事前申請
	 * @return
	 */
	public boolean gobackReflectRecord(GobackReflectPara para, boolean isPre);
	/**
	 * 残業申請：　 事前申請の処理   
	 * @return
	 */
	public boolean overtimeReflectRecord(OvertimeReflectPara para, boolean isPre);
	/**
	 * 休暇申請
	 * @param para
	 * @param isPre True: 事前, False: 事後
	 * @return
	 */
	public boolean absenceReflectRecor(WorkChangeCommonReflectPara para, boolean isPre);
	/**
	 * 勤務実績に反映: 事前申請の処理(休日出勤申請)
	 * @param para
	 * @param isPre
	 * @return
	 */
	public boolean holidayWorkReflectRecord(HolidayWorkReflectPara para, boolean isPre);
	/**
	 * 勤務変更申請
	 * @param para
	 * @param isPre
	 * @return
	 */
	public boolean workChangeReflectRecord(WorkChangeCommonReflectPara para, boolean isPre);
	/**
	 * 振休申請
	 * @param para
	 * @param isPre
	 * @return
	 */
	public boolean absenceLeaveReflectRecord(CommonReflectPara para, boolean isPre);
	/**
	 * 振出申請
	 * @param para
	 * @param isPre
	 * @return
	 */
	public boolean recruitmentReflectRecord(CommonReflectPara para, boolean isPre);
	
	public boolean isRecordData(String employeeId, GeneralDate baseDate);
}
