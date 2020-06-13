package nts.uk.ctx.workflow.dom.approverstatemanagement;

import java.util.List;
import java.util.Optional;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface ApprovalRootStateRepository {
	
	/**
	 * INSERT
	 * @param approvalRootState
	 */
	public void insert(ApprovalRootState approvalRootState);
	
	/**
	 * UPDATE
	 * @param approvalRootState
	 */
	public void update(ApprovalRootState approvalRootState);
	
	/**
	 * DELETE
	 * @param rootStateID
	 */
	public void delete(String rootStateID);

	
	/**
	 * 申請IDから申請の承認状況を取得する
	 * @param appID
	 * @return
	 */
	public Optional<ApprovalRootState> findByID(String appID);
	
	/**
	 * 申請IDから一番承認が進んでいるフェーズの承認状況を取得する
	 * @param appID
	 * @return
	 */
	public List<ApprovalPhaseState> findAppApvMaxPhaseStateByID(String appID);
	
	/**
	 * 承認すべき申請があるかチェックする
	 * @param period
	 * @return
	 */
	public boolean checkAppShouldApproval(DatePeriod period);
	
	/**
	 * 
	 * @param appIDLst
	 * @param approverID
	 * @return
	 */
	public List<ApprovalRootState> findAppApvRootStateByIDApprover(List<String> appIDLst, String approverID);

	/**
	 * 期間から申請者の申請の承認状況を取得する
	 * @param period
	 * @param employeeID
	 * @return
	 */
	public List<ApprovalRootState> findAppApvRootStateByEmployee(DatePeriod period, String employeeID);
	
	/**
	 * 期間から申請者の申請の承認状況を取得する
	 * @param period
	 * @param employeeIDLst
	 * @return
	 */
	public List<ApprovalRootState> findAppApvRootStateByEmployee(DatePeriod period, List<String> employeeIDLst);
	
	/**
	 * 年月日のリストから申請者の申請の承認状況を取得する
	 * @param dates
	 * @param employeeIDLst
	 * @return
	 */
	public List<ApprovalRootState> findAppApvRootStateByEmployee(List<GeneralDate> dates, List<String> employeeIDLst);

	/**
	 * 期間から承認者の申請の承認状況を取得する
	 * @param period
	 * @param approverID
	 * @return
	 */
	public List<ApprovalRootState> findAppApvRootStateByApprover(DatePeriod period, String approverID);
	
	/**
	 * 年月日から承認者の申請の承認状況を取得する
	 * @param date
	 * @param approverID
	 * @return
	 */
	public List<ApprovalRootState> findAppApvRootStateByApprover(GeneralDate date, String approverID);

	/**
	 * 年月日と承認状況から承認者の申請の承認状況を取得する
	 * @param approverIDLst
	 * @param period
	 * @param unapprovalStatus
	 * @param approvalStatus
	 * @param denialStatus
	 * @param agentApprovalStatus
	 * @param remandStatus
	 * @param cancelStatus
	 * @return
	 */
	public List<ApprovalRootState> findAppApvRootStateByApprover(List<String> approverIDLst, DatePeriod period, 
			boolean unapprovalStatus,boolean approvalStatus, boolean denialStatus, boolean agentApprovalStatus, boolean remandStatus, boolean cancelStatus);
	
	
	

}
