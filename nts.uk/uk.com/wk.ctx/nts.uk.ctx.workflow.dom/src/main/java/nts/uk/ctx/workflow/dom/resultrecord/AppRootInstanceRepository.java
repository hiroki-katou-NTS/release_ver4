package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.List;
import java.util.Optional;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface AppRootInstanceRepository {
	
	public void insert(AppRootInstance appRootInstance);
	
	public void update(AppRootInstance appRootInstance);
	
	public void delete(AppRootInstance appRootInstance);
	
	/**
	 * 指定した年月日以降の日別実績の承認ルートを削除する
	 * @param employeeID
	 * @param recordDate
	 */
	public void deleteDailyFromDate(String employeeID, GeneralDate recordDate);

	/**
	 * 指定した年月日以降の月別実績の承認ルートを削除する
	 * @param employeeID
	 * @param recordDate
	 */
	public void deleteMonthlyFromDate(String employeeID, GeneralDate recordDate);

	public Optional<AppRootInstance> findDayInsByID(String rootID);
	
	/**
	 * 対象者の日別実績の承認ルートを取得する（単一日）
	 * @param employeeIDLst
	 * @param date
	 * @return
	 */
	public List<AppRootInstance> findAppRootInstanceDailyByTarget(List<String> employeeIDLst, GeneralDate date);

	/**
	 * 対象者の日別実績の承認ルートを取得する（期間）
	 * @param employeeIDLst
	 * @param period
	 * @return
	 */
	public List<AppRootInstance> findAppRootInstanceDailyByTarget(List<String> employeeIDLst, DatePeriod period);
	
	/**
	 * 基準日時点で最新の日別実績の承認ルートを取得する
	 * @param employeeID
	 * @param date
	 * @return
	 */
	public Optional<AppRootInstance> findAppRootInstanceDailyNewestBelow(String employeeID, GeneralDate date);

	/**
	 * 承認者の日別実績の承認ルートを取得する（単一日）
	 * @param employeeIDLst
	 * @param date
	 * @return
	 */
	public List<AppRootInstance> findAppRootInstanceDailyByApprover(List<String> employeeIDLst, GeneralDate date);
	
	/**
	 * 承認者の日別実績の承認ルートを取得する（期間）
	 * @param approverIDLst
	 * @param period
	 * @return
	 */
	public List<AppRootInstance> findAppRootInstanceDailyByApprover(List<String> approverIDLst, DatePeriod period);

	/**
	 * 承認者の日別実績の承認ルートを取得する（対象者指定）（単一日）
	 * @param approverID
	 * @param employeeIDLst
	 * @param date
	 * @return
	 */
	public List<AppRootInstance> findAppRootInstanceDailyByApproverTarget(String approverID, List<String> employeeIDLst, GeneralDate date);

	/**
	 * 承認者の日別実績の承認ルートを取得する（対象者指定）（期間）
	 * @param approverID
	 * @param employeeIDLst
	 * @param period
	 * @return
	 */
	public List<AppRootInstance> findAppRootInstanceDailyByApproverTarget(String approverID, List<String> employeeIDLst, DatePeriod period);
	
	public Optional<AppRootInstance> findMonInsByID(String rootID);
	
	/**
	 * 対象者の月別実績の承認ルートを取得する（単一日）
	 * @param employeeIDLst
	 * @param date
	 * @return
	 */
	public List<AppRootInstance> findAppRootInstanceMonthlyByTarget(List<String> employeeIDLst, GeneralDate date);

	/**
	 * 対象者の月別実績の承認ルートを取得する（期間）
	 * @param employeeIDLst
	 * @param period
	 * @return
	 */
	public List<AppRootInstance> findAppRootInstanceMonthlyByTarget(List<String> employeeIDLst, DatePeriod period);
	
	/**
	 * 基準日時点で最新の月別実績の承認ルートを取得する
	 * @param employeeID
	 * @param date
	 * @return
	 */
	public Optional<AppRootInstance> findAppRootInstanceMonthlyNewestBelow(String employeeID, GeneralDate date);

	/**
	 * 承認者の月別実績の承認ルートを取得する（単一日）
	 * @param approverIDLst
	 * @param date
	 * @return
	 */
	public List<AppRootInstance> findAppRootInstanceMonthlyByApprover(List<String> approverIDLst, GeneralDate date);

	/**
	 * 承認者の月別実績の承認ルートを取得する（期間）
	 * @param approverIDLst
	 * @param period
	 * @return
	 */
	public List<AppRootInstance> findAppRootInstanceMonthlyByApprover(List<String> approverIDLst, DatePeriod period);
	
	/**
	 * 承認者の月別実績の承認ルートを取得する（対象者指定）（期間）
	 * @param approverID
	 * @param employeeIDLst
	 * @param date
	 * @return
	 */
	public List<AppRootInstance> findAppRootInstanceMonthlyByApproverTarget(String approverID, List<String> employeeIDLst, GeneralDate date);

	/**
	 * 承認者の月別実績の承認ルートを取得する（対象者指定）（期間）
	 * @param approverID
	 * @param employeeIDLst
	 * @param period
	 * @return
	 */
	public List<AppRootInstance> findAppRootInstanceMonthlyByApproverTarget(String approverID, List<String> employeeIDLst, DatePeriod period);

	
	/**
	 * 日別実績の承認対象者を取得する
	 * @param approverID
	 * @param period
	 * @return
	 */
	public List<String> findDailyApprovalTarget(String approverID, DatePeriod period);
	
	/**
	 * 月別実績の承認対象者を取得する
	 * @param approverID
	 * @param period
	 * @return
	 */
	public List<String> findMonthlyApprovalTarget(String approverID, DatePeriod period);
	
	
}
