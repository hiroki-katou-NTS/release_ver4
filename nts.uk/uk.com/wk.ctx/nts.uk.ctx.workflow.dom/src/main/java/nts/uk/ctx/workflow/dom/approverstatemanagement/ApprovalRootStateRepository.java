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
	/**
	 * @param startDate
	 * @param endDate
	 * @return List<ApprovalRootState>
	 */
	public List<ApprovalRootState> findEmployeeAppByApprovalRecordDate(GeneralDate startDate, GeneralDate endDate,String approverID,Integer rootType);
	
	/**
	 * 対象者と期間から承認ルートインスタンスを取得する
	 * @param startDate
	 * @param endDate
	 * @param employeeID
	 * @param rootType
	 * @return
	 */
	public List<ApprovalRootState> findAppByEmployeeIDRecordDate(GeneralDate startDate, GeneralDate endDate,String employeeID,Integer rootType);
	
	public List<ApprovalRootState> findEmploymentApps(List<String> rootStateIDs);
	
	public Optional<ApprovalRootState> findEmploymentApp(String rootStateID);
	
	public void insert(ApprovalRootState approvalRootState);

	public void update(ApprovalRootState approvalRootState);
	
	public void delete(String rootStateID);
	
}
