package nts.uk.ctx.workflow.pub.resultrecord;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.pub.resultrecord.export.AppEmpStatusExport;
import nts.uk.ctx.workflow.pub.resultrecord.export.AppRootInsContentExport;
import nts.uk.ctx.workflow.pub.spr.export.AppRootStateStatusSprExport;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface IntermediateDataPub {
	
	/**
	 * RequestList 113
	 * [No.113](中間データ版)承認対象者と期間から承認状況を取得する
	 * @param employeeID 対象者社員ID
	 * @param period 期間
	 * @param rootType ルート種類（日別確認／月別確認）
	 * @return 承認ルートの状況
	 */
	public List<AppRootStateStatusSprExport> getAppRootStatusByEmpPeriod(String employeeID, DatePeriod period, Integer rootType);
	
	/**
	 * RequestList 155
	 * [No.155](中間データ版)承認対象者リストと日付リストから承認状況を取得する
	 * @param employeeID
	 * @param dateLst
	 * @param rootType
	 * @return
	 */
	public List<AppRootStateStatusSprExport> getAppRootStatusByEmpsDates(List<String> employeeIDLst, List<GeneralDate> dateLst, Integer rootType);
	
	/**
	 * RequestList 229
	 * [No.229](中間データ版)承認対象者（複数）と期間から承認状況を取得する
	 * @param employeeID
	 * @param period
	 * @param rootType
	 * @return
	 */
	public List<AppRootStateStatusSprExport> getAppRootStatusByEmpsPeriod(List<String> employeeIDLst, DatePeriod period, Integer rootType);
	
	public List<AppRootStateStatusSprExport> getAppRootStatusByEmpsPeriodV2(List<String> employeeIDLst, DatePeriod period, Integer rootType);
	
	/**
	 * RequestList 172
	 * [No.172](中間データ版)承認対象者の特定日の日別確認が承認済かチェックする
	 * @param employeeID
	 * @param period
	 * @param rootType
	 * @return
	 */
	public List<ApproveDoneExport> checkDateApprovedStatus(String employeeID, DatePeriod period, Integer rootType);
	
	/**
	 * RequestList 347
	 * [No.347](中間データ版)実績の承認を登録する
	 * @param approverID
	 * @param employeePerformLst
	 * @param rootType
	 */
	public void approve(String approverID, List<EmployeePerformParam> employeePerformLst, Integer rootType);
	
	/**
	 * RequestList 356
	 * [No.356](中間データ版)実績の承認を解除する
	 * @param approverID
	 * @param employeePerformLst
	 * @param rootType
	 * @return
	 */
	public boolean cancel(String approverID, List<EmployeePerformParam> employeePerformLst, Integer rootType);
	
	/**
	 * RequestList 116
	 * [No.116](中間データ版)承認対象者リストと期間から未承認社員リストを取得する
	 * @param employeeIDLst
	 * @param period
	 * @param rootType
	 * @return
	 */
	public List<ApproverApproveExport> getApproverByPeriod(List<String> employeeIDLst, DatePeriod period, Integer rootType); 
	
	/**
	 * RequestList 115
	 * [No.115](中間データ版)承認対象者リストと日付リストから未承認社員リストを取得する
	 * @param employeeIDLst
	 * @param period
	 * @param rootType
	 * @return
	 */
	public List<ApproverApproveExport> getApproverByDateLst(List<String> employeeIDLst, List<GeneralDate> dateLst, Integer rootType); 
	 
	/**
	 * RequestList 512
	 * 指定社員の中間データを作成する
	 * @param employeeID
	 * @param rootType
	 * @param recordDate
	 * @return
	 */
	public AppRootInsContentExport createDailyApprover(String employeeID, Integer rootType, GeneralDate recordDate);
	
	/**
	 * [No.190-191](中間データ版)承認すべきデータ有無を取得する
	 * @param approverID
	 * @param period
	 * @param rootType
	 * @return
	 */
	public boolean isDataExist(String approverID, DatePeriod period, Integer rootType);
	
	/**
	 * RequestList 133
	 * [No.133](中間データ版)承認状況を取得する
	 * @param employeeID
	 * @param period
	 * @param rootType
	 * @return
	 */
	public AppEmpStatusExport getApprovalEmpStatus(String employeeID, DatePeriod period, Integer rootType);
	
	/**
	 * RequestList 403
	 * [No.403](中間データ版)承認状態をクリアする
	 * @param employeeID
	 * @param date
	 * @param rootType
	 */
	public void cleanApprovalRootState(String employeeID, GeneralDate date, Integer rootType);
	
	/**
	 * RequestList 523
	 * [No.523]承認状態を作成する
	 * @param employeeID-対象者
	 * @param targetDate-対象日
	 * @param rootType-ルート種類
	 */
	public void createApprovalStatus(String employeeID, GeneralDate date, Integer rootType);
	
	/**
	 * RequestList 424
	 * [No.424](中間データ版)承認状態を削除する
	 * @param employeeID-対象者
	 * @param targetDate-対象日
	 * @param rootType-実績確認ルート種類
	 */
	public void deleteApprovalStatus(String employeeID, GeneralDate date, Integer rootType);
	
}
