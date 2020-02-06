package nts.uk.ctx.at.request.dom.application.common.service.other;
/**
 * 
 * 16.その他
 *
 */


import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AppCompltLeaveSyncOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.MailResult;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.PeriodCurrentMonth;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReason;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.InitValueAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.arc.time.calendar.period.DatePeriod;

public interface OtherCommonAlgorithm {
	/**
	 * 1.職場別就業時間帯を取得
	 * @param companyID
	 * @param employeeID
	 * @param referenceDate
	 */
	public List<String> getWorkingHoursByWorkplace(String companyID,String employeeID,GeneralDate referenceDate);
	/**
	 * 3.事前事後の判断処理(事前事後非表示する場合)
	 * @param appType
	 * @param appDate
	 * @return enum PrePostAtr
	 */
	public PrePostAtr preliminaryJudgmentProcessing(ApplicationType appType,GeneralDate appDate,int overTimeAtr);
	/**
	 * 4.社員の当月の期間を算出する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param date 基準日
	 * @return List<String>: [0]: startDate, [1]: endDate <=> 締め期間(開始年月日,終了年月日) 
	 */
	public PeriodCurrentMonth employeePeriodCurrentMonthCalculate(String companyID, String employeeID, GeneralDate date);
	/**
	 * 5.事前事後区分の判断
	 * @param appType
	 * @param appDate
	 */
	public InitValueAtr judgmentPrePostAtr(ApplicationType appType,GeneralDate appDate,boolean checkCaller);
	/**
	 * 9.同時申請された振休振出申請を取得する
	 * @author hoatt
	 * @param companyId
	 * @param appId
	 * @return
	 */
	public AppCompltLeaveSyncOutput getAppComplementLeaveSync(String companyId, String appId);
	
	/**
	 * 10.申請メール自動送信
	 */
	/**
	 * 承認者へ送る（新規登録、更新登録、承認）
	 * @param employeeIDList
	 * @param application
	 * @return
	 */
	public MailResult sendMailApproverApprove(List<String> employeeIDList, Application_New application);
	
	/**
	 * 承認者へ送る（削除）
	 * @param employeeIDList
	 * @param application
	 * @return
	 */
	public MailResult sendMailApproverDelete(List<String> employeeIDList, Application_New application);
	
	/**
	 * 申請者へ送る（承認）
	 * @param application
	 * @return
	 */
	public MailResult sendMailApplicantApprove(Application_New application);
	
	/**
	 * 申請者へ送る（否認）
	 * @param application
	 * @return
	 */
	public MailResult sendMailApplicantDeny(Application_New application);
	
	/**
	 * 承認者へ送る
	 * @param listDestination
	 * @param application
	 * @param text
	 * @return
	 */
	public MailResult sendMailApprover(List<String> listDestination, Application_New application, String text);
	
	/**
	 * 申請者へ送る
	 * @param application
	 * @param text
	 * @return
	 */
	public MailResult sendMailApplicant(Application_New application, String text);
	/**
	 * 申請期間から休日の申請日を取得する
	 * @param cid
	 * @param sid
	 * @param dates
	 * @return
	 */
	public List<GeneralDate> lstDateIsHoliday(String cid, String sid, DatePeriod dates);
	
	/**
	 * 11.指定日の勤務実績（予定）の勤務種類を取得
	 * @param companyID
	 * @param employeeID
	 * @param appDate
	 * @return
	 */
	public WorkType getWorkTypeScheduleSpec(String companyID, String employeeID, GeneralDate appDate);
	/**
	 * 申請理由出力_共通
	 * @author hoatt
	 * @param 申請 application
	 * @param 休暇種類(Optional) holidayType
	 * @return 結果(使用/未使用)
	 */
	public boolean appReasonOutFlg(Application_New application, Optional<Integer> holidayType);
	
	/**
	 * 01-05_申請定型理由を取得
	 * @param companyID 会社ID
	 * @param typicalReasonDisplayFlg 定型理由の表示区分
	 * @param appType 申請種類
	 * @return
	 */
	public List<ApplicationReason> getApplicationReasonType(String companyID, AppDisplayAtr typicalReasonDisplayFlg, ApplicationType appType);
	
	/**
	 * 01-06_申請理由を取得
	 * @param displayReasonFlg 申請理由の表示区分
	 * @return
	 */
	public boolean displayAppReasonContentFlg(AppDisplayAtr displayReasonFlg);
	
	/**
	 * 01-09_事前申請を取得
	 * @param employeeID 申請者
	 * @param prePostAtr 事前事後区分
	 * @param preDisplayAtr 事前表示区分
	 * @param appDate 申請日
	 * @param appType 申請種類
	 * @return
	 */
	public AppOverTime getPreApplication(String employeeID, PrePostAtr prePostAtr, UseAtr preDisplayAtr, GeneralDate appDate, ApplicationType appType);
}
