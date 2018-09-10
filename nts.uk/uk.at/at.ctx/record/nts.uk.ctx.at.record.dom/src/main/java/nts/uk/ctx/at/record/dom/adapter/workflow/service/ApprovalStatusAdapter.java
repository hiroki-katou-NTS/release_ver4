/**
 * 5:04:30 PM Mar 9, 2018
 */
package nts.uk.ctx.at.record.dom.adapter.workflow.service;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootOfEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootStateStatusImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author hungnm
 *
 */
public interface ApprovalStatusAdapter {
	List<ApproveRootStatusForEmpImport> getApprovalByEmplAndDate(GeneralDate startDate, GeneralDate endDate, String employeeID,String companyID,Integer rootType); 
	
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
	public boolean releaseApproval(String approverID, List<GeneralDate> approvalRecordDates, List<String> employeeID,Integer rootType,String companyID);
	/**
	 * RequestList347
	 * 実績の承認を登録する
	 * @param approverID
	 * @param approvalRecordDates
	 * @param employeeID
	 * @param rootType
	 * @return
	 */
	public void registerApproval(String approverID, List<GeneralDate> approvalRecordDates, List<String> employeeID,Integer rootType,String companyID);
	
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
	public void cleanApprovalRootState(String rootStateID, Integer rootType);
	
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
}
