package nts.uk.ctx.at.request.dom.application.applist.service;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.applist.extractcondition.AppListExtractCondition;
import nts.uk.ctx.at.request.dom.setting.company.request.approvallistsetting.ApprovalListDisplaySetting;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
/**
 * 
 * @author hoatt
 *
 */
public interface AppListInitialRepository {
	/**
	 * 0 - 申請一覧事前必須チェック
	 * @param appType
	 * @param wkpID
	 * @return
	 */
	public Boolean checkAppPredictRequire(int appType, String wkpID, String companyId);
	
	/**
	 * 1- 申請一覧リスト取得
	 * @param param
	 * @param displaySet
	 * @return
	 */
	public AppListOutPut getApplicationList(AppListExtractCondition param, ApprovalListDisplaySetting displaySet);
	/**
	 * 2 - 申請一覧リスト取得申請
	 * @param param
	 * @return
	 */
	public AppListOutPut getApplicationListByApp(AppListExtractCondition param);
	/**
	 * 3 - 申請一覧リスト取得承認
	 * @param param
	 * @param displaySet
	 * @return
	 */
	public AppListOutPut getAppListByApproval(AppListExtractCondition param, ApprovalListDisplaySetting displaySet);
	/**
	 * 4 - 申請一覧リスト取得承認件数
	 * @param lstApp
	 * @return
	 */
	public AppInfoStatus countAppListApproval(List<ApplicationFullOutput> lstApp, String sID);
	/**
	 * 5 - 申請一覧リスト取得実績
	 * @param lstApp
	 * @param displaySet
	 * @return
	 */
	public AppListAtrOutput getAppListAchievement(List<ApplicationFullOutput> lstApp, ApprovalListDisplaySetting displaySet, String companyId, String sID);
	/**
	 * 5.1 - 申請一覧リスト取得実績休出申請
	 * @param sID
	 * @param date
	 * @return
	 */
	public Boolean getAppListAchievementBreak(String sID, GeneralDate date);
	/**
	 * 5.2 - 申請一覧リスト取得実績残業申請
	 * @param sID
	 * @param date
	 * @param time
	 * @return
	 */
	public TimeResultOutput getAppListAchievementOverTime(String sID, GeneralDate date, List<OverTimeFrame> time, Integer restStart, Integer restEnd);
	/**
	 * 6 - 申請一覧リスト取得振休振出
	 * @param application
	 * @return
	 */
	public List<Application_New> getListAppComplementLeave(Application_New application, String companyId);
	/**
	 * 7 - 申請一覧リスト取得打刻取消
	 * @param application
	 * @return
	 */
	public Boolean getListAppStampIsCancel(Application_New application, String companyID);
	/**
	 * 8 - 申請一覧リスト取得休暇
	 * @param application
	 * @return
	 */
	public List<Application_New> getListAppAbsence(Application_New application, String companyID);
	/**
	 * 9 - 申請一覧リスト取得マスタ情報
	 * @param lstApp
	 * @return
	 */
	public List<AppMasterInfo> getListAppMasterInfo(List<Application_New> lstApp, String companyId);
	/**
	 * 12 - 申請一覧初期日付期間
	 * @param companyId
	 * @return
	 */
	public DatePeriod getInitialPeriod(String companyId);
	/**
	 * 12.1 - 申請一覧初期日付期間_申請
	 * @param companyId
	 * @return
	 */
	public DatePeriod getInitPeriodApp(String companyId);
}
