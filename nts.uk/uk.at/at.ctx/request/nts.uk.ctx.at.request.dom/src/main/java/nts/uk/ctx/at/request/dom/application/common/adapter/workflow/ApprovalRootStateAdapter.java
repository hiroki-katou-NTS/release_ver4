package nts.uk.ctx.at.request.dom.application.common.adapter.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.AgentPubImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalPhaseStateImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootContentImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverApprovedImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverPersonImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverRemandImport;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface ApprovalRootStateAdapter {
	/**
	 * RequestList113
	 * @param startDate
	 * @param endDate
	 * @param employeeID
	 * @param companyID
	 * @param rootType
	 * @return
	 */
	public List<ApproveRootStatusForEmpImPort> getApprovalByEmplAndDate(GeneralDate startDate, GeneralDate endDate, String employeeID,String companyID,Integer rootType)
		throws BusinessException;
	
	public Map<String,List<ApprovalPhaseStateImport_New>> getApprovalRootContents(List<String> appIDs,String companyID);
	
	

	
	/**
	 * cache of IMailDestinationPub for performance
	 * @author m_kitahira
	 */
	@RequiredArgsConstructor
	public static class MailDestinationCache {
		private final Function<String, Object> pub;
		private final Map<String, Object> cache = new HashMap<>();
		
		@SuppressWarnings("unchecked")
		public <T> T get(String employeeId) {
			if (cache.containsKey(employeeId)) {
				return (T)cache.get(employeeId);
			}
			
			Object dests = this.pub.apply(employeeId); 
			
			cache.put(employeeId, dests);
			return (T)dests;
		}
	}
	
	public MailDestinationCache createMailDestinationCache(String companyID);
	
	public default ApprovalRootContentImport_New getApprovalRootContent(String companyID, String employeeID, Integer appTypeValue, GeneralDate appDate, String appID, Boolean isCreate) {
		val cache = this.createMailDestinationCache(companyID);
		return this.getApprovalRootContent(companyID, employeeID, appTypeValue, appDate, appID, isCreate, cache);
	}
	
	public ApprovalRootContentImport_New getApprovalRootContent(String companyID, String employeeID, Integer appTypeValue, GeneralDate appDate, String appID, Boolean isCreate,
			MailDestinationCache mailDestinationCache);
	
	public void insertByAppType(String companyID, String employeeID, Integer appTypeValue, GeneralDate date, String appID);
	
	public List<String> getNextApprovalPhaseStateMailList(String companyID, String rootStateID,
			Integer approvalPhaseStateNumber, Boolean isCreate, String employeeID, Integer appTypeValue, GeneralDate appDate);
	
	public Integer doApprove(String companyID, String rootStateID, String employeeID, Boolean isCreate, Integer appTypeValue, GeneralDate appDate, String memo);
	
	public Boolean isApproveAllComplete(String companyID, String rootStateID, String employeeID, Boolean isCreate, Integer appTypeValue, GeneralDate appDate);
	
	public void doReleaseAllAtOnce(String companyID, String rootStateID);
	
	public ApproverApprovedImport_New getApproverApproved(String rootStateID); 
	/**
	 * RequestList No.484
	 * 承認代行情報の取得処理
	 * @param companyID
	 * @param approver
	 * @return
	 */
	public AgentPubImport getApprovalAgencyInformation(String companyID, List<String> approver);
	
	public List<String> getMailNotifierList(String companyID, String rootStateID);
	
	public void deleteApprovalRootState(String rootStateID);
	
	public Boolean doRelease(String companyID, String rootStateID, String employeeID);
	
	public Boolean doDeny(String companyID, String rootStateID, String employeeID, String memo);
	
	public Boolean judgmentTargetPersonIsApprover(String companyID, String rootStateID, String employeeID);
	
	public ApproverPersonImport judgmentTargetPersonCanApprove(String companyID, String rootStateID, String employeeID);
	/**
	 * RequestList No.482
	 * 差し戻しする(承認者まで)
	 * @param companyID
	 * @param rootStateID
	 * @param order
	 * @return
	 */
	public List<String> doRemandForApprover(String companyID, String rootStateID, Integer order);
	/**
	 * RequestList No.480
	 * 差し戻しする(本人まで)
	 * @param companyID
	 * @param rootStateID
	 */
	public void doRemandForApplicant(String companyID, String rootStateID);
	/**
	 * RequestList No.483
	 * 1.承認フェーズ毎の承認者を取得する
	 * @param appID
	 * @return
	 */
	public List<String> getApproverFromPhase(String appID);
	/**
	 * RequestList 479
	 * 差し戻し対象者一覧を取得
	 * @param appID
	 * @return
	 */
	public List<ApproverRemandImport> getListApproverRemand(String appID);
	
	public Boolean isApproveApprovalPhaseStateComplete(String companyID, String rootStateID, Integer phaseNumber);
	
	/**
	 * RequestList 532
	 * [No.532](中間データ版)承認対象者と期間から承認状況を取得する（月別）
	 * @param employeeID
	 * @param period
	 * @return
	 */
	public List<ApproveRootStatusForEmpImPort> getAppRootStatusByEmpPeriodMonth(String employeeID, DatePeriod period);
}
