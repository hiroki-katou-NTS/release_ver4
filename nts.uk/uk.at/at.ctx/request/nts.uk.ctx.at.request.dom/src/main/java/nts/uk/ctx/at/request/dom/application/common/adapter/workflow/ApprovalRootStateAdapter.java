package nts.uk.ctx.at.request.dom.application.common.adapter.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.AgentPubImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.AppRootInsPeriodImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalPhaseStateImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootContentImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootStateImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproveResultImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverApproveImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverApprovedImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverPersonImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverRemandImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.EmpPerformMonthParamAt;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.Request533Import;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

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
	
	public void insertByAppType(String companyID, String employeeID, Integer appTypeValue, GeneralDate appDate, String appID, GeneralDate baseDate);
	
	public void insertFromCache(String companyID, String appID, GeneralDate date, String employeeID, List<ApprovalPhaseStateImport_New> listApprovalPhaseState);
	
	public List<String> getNextApprovalPhaseStateMailList(String rootStateID, Integer approvalPhaseStateNumber);
	
	public ApproveResultImport doApprove(String rootStateID, String employeeID, String memo);
	
	public Boolean isApproveAllComplete(ApprovalRootStateImport_New approvalRootState);
	
	public void doReleaseAllAtOnce(String companyID, String rootStateID);
	
	public ApproverApprovedImport_New getApproverApproved(String rootStateID); 
	/**
	 * RequestList No.484
	 * ?????????????????????????????????
	 * @param companyID
	 * @param approver
	 * @return
	 */
	public AgentPubImport getApprovalAgencyInformation(String companyID, List<String> approver);
	
	public List<String> getMailNotifierList(String companyID, String rootStateID);
	
	public void deleteApprovalRootState(String rootStateID);
	
	public Boolean doRelease(String companyID, String rootStateID, String employeeID);
	
	public Boolean doDeny(String rootStateID, String employeeID, String memo);
	
	public Boolean judgmentTargetPersonIsApprover(String companyID, String rootStateID, String employeeID);
	
	public ApproverPersonImport judgmentTargetPersonCanApprove(String companyID, String rootStateID, String employeeID);
	/**
	 * RequestList No.482
	 * ??????????????????(???????????????)
	 * @param companyID
	 * @param rootStateID
	 * @param order
	 * @return
	 */
	public List<String> doRemandForApprover(String companyID, String rootStateID, Integer order);
	/**
	 * RequestList No.480
	 * ??????????????????(????????????)
	 * @param companyID
	 * @param rootStateID
	 */
	public void doRemandForApplicant(String companyID, String rootStateID);
	/**
	 * RequestList No.483
	 * 1.????????????????????????????????????????????????
	 * @param appID
	 * @return
	 */
	public List<String> getApproverFromPhase(String appID);
	/**
	 * RequestList 479
	 * ????????????????????????????????????
	 * @param appID
	 * @return
	 */
	public List<ApproverRemandImport> getListApproverRemand(String appID);
	
	public Boolean isApproveApprovalPhaseStateComplete(String companyID, String rootStateID, Integer phaseNumber);
	
	/**
	 * RequestList 532
	 * [No.532](??????????????????)?????????????????????????????????????????????????????????????????????
	 * @param employeeID
	 * @param period
	 * @return
	 */
	public List<ApproveRootStatusForEmpImPort> getAppRootStatusByEmpPeriodMonth(String employeeID, DatePeriod period);

	/**
	 * get data Appr RQ309 -> CMM045
	 * @param appIDs
	 * @param companyID
	 * @return
	 */
	public Map<String,List<ApprovalPhaseStateImport_New>> getApprovalRootContentCMM045(String companyID, String approverID,
			List<String> lstAgent, DatePeriod period, boolean unapprovalStatus, boolean approvalStatus, boolean denialStatus, 
			boolean agentApprovalStatus, boolean remandStatus, boolean cancelStatus);
    
    public List<ApprovalPhaseStateImport_New> getApprovalDetail(String appID);
    
    /**
     * refactor 4
     * @param listApprovalPhaseState
     */
    public void insertApp(String appID, GeneralDate appDate, String employeeID, List<ApprovalPhaseStateImport_New> listApprovalPhaseState);
    
    /**
     * refactor 4
     * @param appIDs
     * @param companyID
     * @return
     */
    public Map<String,List<ApprovalPhaseStateImport_New>> getApprovalPhaseByID(List<String> appIDLst);
    
    /**
     * Request 533
     * @param empPerformMonthParamLst
     * @return
     */
    public Request533Import getAppRootStatusByEmpsMonth(List<EmpPerformMonthParamAt> empPerformMonthParamLst);
    
    /**
	 * RequestList 672
	 * @param employeeIDLst
	 * @param period
	 * @param rootType
	 * @return
	 */
	public List<ApprovalRootStateImport_New> getAppRootInstanceDayByEmpPeriod(List<String> employeeIDLst, DatePeriod period, Integer rootType);
	
	/**
	 * RequestList 673
	 * @param employeeID
	 * @param period
	 * @return
	 */
	public ApprovalRootStateImport_New getAppRootInstanceMonthByEmpPeriod(String employeeID, DatePeriod period, YearMonth yearMonth, Integer closureID,
			ClosureDate closureDate, GeneralDate baseDate);
	
	/**
	 * RequestList 115
	 * @param employeeIDLst
	 * @param dateLst
	 * @param rootType
	 * @return
	 */
	public List<ApproverApproveImport> getApproverByDateLst(List<String> employeeIDLst, List<GeneralDate> dateLst, Integer rootType); 
	
	/**
	 * RequestList 538
	 * @param employeeID
	 * @param closureID
	 * @param yearMonth
	 * @param closureDate
	 * @param date
	 * @return
	 */
	public ApproverApproveImport getApproverByPeriodMonth(String employeeID, Integer closureID, YearMonth yearMonth, ClosureDate closureDate, GeneralDate date); 
	
	/**
	 * RequestList 709
	 * @param employeeIDLst
	 * @param period
	 * @param rootType
	 * @return
	 */
	public List<AppRootInsPeriodImport> getAppRootInstanceByEmpPeriod(List<String> employeeIDLst, DatePeriod period, Integer rootType);
    
}
