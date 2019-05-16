package nts.uk.ctx.workflow.dom.service.resultrecord;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRootStateStatus;
import nts.uk.ctx.workflow.dom.service.output.Request113Output;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
/**
 * 中間データ版
 * @author Doan Duy Hung
 *
 */
public interface AppRootInstanceService {
	
	/**
	 * [No.113](中間データ版)承認対象者と期間から承認状況を取得する
	 * @param employeeIDLst 対象者社員ID
	 * @param period 期間
	 * @param rootType ルート種類（日別確認／月別確認）
	 * @return 承認ルートの状況
	 */
	public Request113Output getAppRootStatusByEmpsPeriod(List<String> employeeIDLst, DatePeriod period, RecordRootType rootType);
	
	public List<ApprovalRootStateStatus> getAppRootStatusByEmpsPeriod(String compID, List<String> employeeIDLst, DatePeriod period, RecordRootType rootType);
	
	/**
	 * 対象者と期間から承認ルート中間データを取得する
	 * @param employeeIDLst
	 * @param period
	 * @param rootType
	 * @return
	 */
	public List<AppRootInstancePeriod> getAppRootInstanceByEmpPeriod(List<String> employeeIDLst, DatePeriod period, RecordRootType rootType);
	
	public List<AppRootInstancePeriod> getAppRootInstanceByEmpPeriod(String compID, List<String> employeeIDLst, DatePeriod period, RecordRootType rootType);
	
	/**
	 * 対象日の承認ルート中間データを取得する
	 * @param date
	 * @param appRootInstanceLst
	 * @return
	 */
	public AppRootInstance getAppRootInstanceByDate(GeneralDate date, List<AppRootInstance> appRootInstanceLst);
	
	/**
	 * 対象日の就業実績確認状態を取得する
	 * @param companyID
	 * @param employeeID
	 * @param date
	 * @param rootType
	 * @return
	 */
	public AppRootConfirm getAppRootConfirmByDate(String companyID, String employeeID, GeneralDate date, RecordRootType rootType);
	
	/**
	 * 中間データから承認ルートインスタンスに変換する
	 * @param appRootInstance
	 * @param appRootConfirm
	 * @return
	 */
	public ApprovalRootState convertFromAppRootInstance(AppRootInstance appRootInstance, AppRootConfirm appRootConfirm);
	
	/**
	 * [No.116](中間データ版)承認対象者リストと期間から未承認社員リストを取得する
	 * @param employeeIDLst
	 * @param period
	 * @param rootType
	 * @return
	 */
	public List<ApproverToApprove> getApproverByPeriod(List<String> employeeIDLst, DatePeriod period, RecordRootType rootType);
	
	/**
	 * 承認するべき承認者を取得する
	 * @param approvalRootState
	 * @return
	 */
	public ApproverToApprove getApproverToApprove(ApprovalRootState approvalRootState);
	
	/**
	 * 承認者(承認代行を含め)と期間から承認ルート中間データを取得する
	 * @param approverID
	 * @param period
	 * @param rootType
	 * @return
	 */
	public ApprovalPersonInstance getApproverAndAgent(String approverID, DatePeriod period, RecordRootType rootType);
	
	/**
	 * [No.190-191](中間データ版)承認すべきデータ有無を取得する
	 * @param approverID
	 * @param period
	 * @param rootType
	 * @return
	 */
	public boolean isDataExist(String approverID, DatePeriod period, RecordRootType rootType);
	
	/**
	 * 承認者としての承認すべきデータがあるか
	 * @param period
	 * @param approverRouteLst
	 * @return
	 */
	public boolean isDataApproverExist(DatePeriod period, List<ApprovalRouteDetails> approverRouteLst);
	
	/**
	 * 代行者としての承認すべきデータがあるか
	 * @param period
	 * @param agentRouteLst
	 * @return
	 */
	public boolean isDataAgentExist(DatePeriod period, List<ApprovalRouteDetails> agentRouteLst);
	
	/**
	 * 基準社員を元にルート状況を取得する
	 * @param approvalRootState
	 * @param employeeID
	 * @param agentLst
	 * @return
	 */
	public RouteSituation getRouteSituationByEmp(ApprovalRootState approvalRootState, String employeeID, List<String> agentLst);
	
	/**
	 * [No.133](中間データ版)承認状況を取得する
	 * @param employeeID
	 * @param period
	 * @param rootType
	 * @return
	 */
	public ApprovalEmpStatus getApprovalEmpStatus(String employeeID, DatePeriod period, RecordRootType rootType);
	
	/**
	 * [No.595]
	 * @param approverId
	 * @param targetEmployeeIds
	 * @param period
	 * @return
	 */
	public ApprovalEmpStatus getDailyApprovalStatus(String companyId, String approverId, List<String> targetEmployeeIds, DatePeriod period);
	
	/**
	 * 承認者としてのルート状況を取得する
	 * @param period
	 * @param appRootInstanceLst
	 * @param agentLst
	 * @return
	 */
	public List<RouteSituation> getApproverRouteSituation(DatePeriod period, List<ApprovalRouteDetails> approverRouteLst, List<String> agentLst, RecordRootType rootType,
			boolean useDayApproverConfirm, DatePeriod closurePeriod, YearMonth yearMonth, Integer closureID, ClosureDate closureDate);
	
	/**
	 * 代行者としてのルート状況を取得する
	 * @param period
	 * @param appRootInstanceLst
	 * @param agentLst
	 * @return
	 */
	public List<RouteSituation> getAgentRouteSituation(DatePeriod period, List<ApprovalRouteDetails> agentRouteLst, List<String> agentLst, RecordRootType rootType,
			boolean useDayApproverConfirm, DatePeriod closurePeriod, YearMonth yearMonth, Integer closureID, ClosureDate closureDate);
	
	/**
	 * outputの整合
	 * @param approverLst
	 * @param agentLst
	 * @return
	 */
	public List<RouteSituation> mergeRouteSituationLst(List<RouteSituation> approverRouteLst, List<RouteSituation> agentRouteLst);
	
	public AppRootConfirm getAppRootCFByMonth(String companyID, String employeeID, YearMonth yearMonth,
			Integer closureID, ClosureDate closureDate, RecordRootType rootType);
	
	public boolean isDataApproverExistMonth(YearMonth yearMonth, List<ApprovalRouteDetails> approverRouteLst);
	
	public boolean isDataAgentExistMonth(YearMonth yearMonth, List<ApprovalRouteDetails> agentRouteLst);
}
