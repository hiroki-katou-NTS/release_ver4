package nts.uk.ctx.workflow.dom.approverstatemanagement;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface ApprovalRootStateRepository {
	
	public Optional<ApprovalRootState> findByID(String rootStateID, Integer rootType);
	/**
	 * @param startDate
	 * @param endDate
	 * @return List<ApprovalRootState>
	 */
	public List<ApprovalRootState> findEmployeeAppByApprovalRecordDate(GeneralDate startDate, GeneralDate endDate,String approverID,Integer rootType);
	/**
	 * @param startDate
	 * @param endDate
	 * @return List<ApprovalRootState>
	 */
	public List<ApprovalRootState> findEmployeeAppByApprovalRecordDateAndNoRootType(String companyID, 
			GeneralDate startDate, GeneralDate endDate,String approverID);
	
	/** 
	 * 対象者と期間から承認ルートインスタンスを取得する
	 * @param startDate
	 * @param endDate
	 * @param employeeID
	 * @param rootType
	 * @return
	 */
	public List<ApprovalRootState> findAppByEmployeeIDRecordDate(GeneralDate startDate, GeneralDate endDate,String employeeID,Integer rootType);
	/**
	 * 対象者と期間から承認ルートインスタンスを取得する(for List EmployeeID)
	 * @param startDate
	 * @param endDate
	 * @param employeeID
	 * @param rootType
	 * @return
	 */
	public List<ApprovalRootState> findAppByListEmployeeIDRecordDate(GeneralDate startDate, GeneralDate endDate,List<String> employeeID,Integer rootType);
	
	/**
	 * 対象者リストと日付リストから承認ルートインスタンスを取得する
	 * @param approvalRecordDates
	 * @param employeeIDs
	 * @param rootType
	 * @return
	 */
	public List<ApprovalRootState> findAppByListEmployeeIDAndListRecordDate(List<GeneralDate> approvalRecordDates,List<String> employeeIDs,Integer rootType);
	
	public List<ApprovalRootState> findEmploymentApps(List<String> rootStateIDs, String approverID);
	
	public Optional<ApprovalRootState> findEmploymentApp(String rootStateID);
	
	public void insert(String companyID, ApprovalRootState approvalRootState, Integer rootType);

	public void update(ApprovalRootState approvalRootState, Integer rootType);
	
	public void delete(String rootStateID, Integer rootType);
	
	public List<ApprovalRootState> getRootStateByDateAndType(GeneralDate date, Integer rootType);
	
	public List<ApprovalRootState> findByApprover(String companyID, GeneralDate startDate, GeneralDate endDate,
			String approverID, Integer rootType);
	
}
