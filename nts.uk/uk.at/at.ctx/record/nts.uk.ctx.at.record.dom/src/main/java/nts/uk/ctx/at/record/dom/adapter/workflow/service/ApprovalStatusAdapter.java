/**
 * 5:04:30 PM Mar 9, 2018
 */
package nts.uk.ctx.at.record.dom.adapter.workflow.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootOfEmpMonthImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootSttMonthEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootOfEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootStateStatusImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproverApproveImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.EmpPerformMonthParamImport;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author hungnm
 *
 */
public interface ApprovalStatusAdapter {
	
	default List<ApproveRootStatusForEmpImport> getApprovalByEmplAndDate(
			GeneralDate startDate, GeneralDate endDate, String employeeID,String companyID,Integer rootType) {
		return this.getApprovalByEmplAndDate(startDate, endDate, Arrays.asList(employeeID), companyID, rootType);
	}
	
	List<ApproveRootStatusForEmpImport> getApprovalByEmplAndDate(GeneralDate startDate, GeneralDate endDate, List<String> employeeID,String companyID,Integer rootType); 
	
	/**
	 * <=>RequestList133
	 * @param startDate
	 * @param endDate
	 * @param approverID
	 * @param companyID
	 * @param rootType
	 * @return
	 */
	ApprovalRootOfEmployeeImport getApprovalRootOfEmloyee(GeneralDate startDate, GeneralDate endDate, String approverID,String companyID,Integer rootType);
	
	ApprovalRootOfEmployeeImport getApprovalRootOfEmloyee(DatePeriod date, String approverID, Integer rootType);
	
	/**
	 * <=>RequestList133
	 * @param startDate
	 * @param endDate
	 * @param approverID
	 * @param companyID
	 * @param rootType
	 * @return
	 */
	ApprovalRootOfEmployeeImport getApprovalRootOfEmloyeeNew(GeneralDate startDate, GeneralDate endDate, String approverID,String companyID,Integer rootType);
	
	/**
	 * <=>RequestList229
	 * @param approvalRecordDates
	 * @param employeeID
	 * @param rootType
	 * @return
	 */
	List<ApproveRootStatusForEmpImport> getApprovalByListEmplAndListApprovalRecordDate(GeneralDate startDate, GeneralDate endDate,
			List<String> employeeIDs, String companyID, Integer rootType);
	
	/**
	 * RequestList356
	 * 実績の承認を解除する
	 * @param approverID
	 * @param approvalRecordDates
	 * @param employeeID
	 * @param rootType
	 * @return
	 */
	public boolean releaseApproval(String approverID, List<Pair<String, GeneralDate>> empAndDate, Integer rootType,String companyID);
	/**
	 * RequestList347
	 * 実績の承認を登録する
	 * @param approverID
	 * @param approvalRecordDates
	 * @param employeeID
	 * @param rootType
	 * @return
	 */
	public void registerApproval(String approverID, List<Pair<String, GeneralDate>> empAndDate, Integer rootType,String companyID);
	
	/**
	 * RequestList155
	 * [No.155]承認対象者リストと日付リストから承認状況を取得する
	 * getApprovalByListEmplAndListApprovalRecordDate
	 * @param approvalRecordDates
	 * @param employeeID
	 * @param companyID
	 * @param rootType
	 * @return
	 */
	public List<ApproveRootStatusForEmpImport> getApprovalByListEmplAndListApprovalRecordDate(List<GeneralDate> approvalRecordDates, List<String> employeeID,Integer rootType);
	
	/**
	 * RequestList 403
	 * 承認状態をすべてクリアする
	 * @param rootStateID
	 */
	public void cleanApprovalRootState(String rootStateID, GeneralDate date, Integer rootType);
	
	/**
	 * [No.113](中間データ版)承認対象者と期間から承認状況を取得する を呼び出す。
	 * @param employeeID
	 * @param startDate
	 * @param endDate
	 * @param rootType
	 * @return
	 */
	public List<ApprovalRootStateStatusImport> getStatusByEmpAndDate(String employeeID, DatePeriod datePeriod, 
			Integer rootType);
	
	/**
	 * RequestList155
	 * [No.155]承認対象者リストと日付リストから承認状況を取得する
	 * getApprovalByListEmplAndListApprovalRecordDate
	 * @param approvalRecordDates
	 * @param employeeID
	 * @param companyID
	 * @param rootType
	 * @return
	 */
	public List<ApproveRootStatusForEmpImport> getApprovalByListEmplAndListApprovalRecordDateNew(List<GeneralDate> approvalRecordDates, List<String> employeeID,Integer rootType);
	
	/**
	 * RequestList 532
	 * [No.532](中間データ版)承認対象者と期間から承認状況を取得する（月別）
	 * @param employeeID
	 * @param period
	 * @return
	 */
	public List<ApproveRootStatusForEmpImport> getAppRootStatusByEmpPeriodMonth(String employeeID, DatePeriod period); 
	
	/**
	 * RequestList 533
	 * [No.533](中間データ版)承認対象者リストと日付リストから承認状況を取得する（月別）
	 * @param empPerformMonthParamLst
	 * @return
	 */
	public List<AppRootSttMonthEmpImport> getAppRootStatusByEmpsMonth(List<EmpPerformMonthParamImport> empPerformMonthParamLst);
	
	/**
	 * RequestList 534
	 * [No.534](中間データ版)承認状況を取得する （月別）
	 * @param approverID
	 * @param yearMonth
	 * @param closureID
	 * @param closureDate
	 * @param baseDate
	 * @return
	 */
	public AppRootOfEmpMonthImport getApprovalEmpStatusMonth(String approverID, YearMonth yearMonth, Integer closureID,
			ClosureDate closureDate, GeneralDate baseDate);
	
	/**
	 * RequestList 528
	 * [No.528](中間データ版)実績の承認を登録する（月別）
	 * @param approverID
	 * @param empPerformMonthParamLst
	 */
	public void approveMonth(String approverID, List<EmpPerformMonthParamImport> empPerformMonthParamLst);
	
	/**
	 * RequestList 529
	 * [No.529](中間データ版)実績の承認を解除する（月別）
	 * @param approverID
	 * @param empPerformMonthParamLst
	 * @return
	 */
	public boolean cancelMonth(String approverID, List<EmpPerformMonthParamImport> empPerformMonthParamLst);
	
	/**
	 * RequestList 115
	 */
	public List<ApproverApproveImport> getApproverByDateLst(List<String> employeeIDLst, List<GeneralDate> dateLst, Integer rootType); 
	
	/**
	 * RequestList 538
	 */
	public ApproverApproveImport getApproverByPeriodMonth(String employeeID, Integer closureID, YearMonth yearMonth, ClosureDate closureDate, GeneralDate date);
}
