package nts.uk.ctx.workflow.pubimp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.adapter.bs.PersonAdapter;
import nts.uk.ctx.workflow.dom.agent.Agent;
import nts.uk.ctx.workflow.dom.agent.AgentRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApplicationType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmationRootType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.EmploymentRootAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootStateRepository;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApproverState;
import nts.uk.ctx.workflow.dom.service.ApprovalRootStateService;
import nts.uk.ctx.workflow.dom.service.ApproveService;
import nts.uk.ctx.workflow.dom.service.CollectApprovalAgentInforService;
import nts.uk.ctx.workflow.dom.service.CollectApprovalRootService;
import nts.uk.ctx.workflow.dom.service.CollectMailNotifierService;
import nts.uk.ctx.workflow.dom.service.DenyService;
import nts.uk.ctx.workflow.dom.service.GenerateApprovalRootStateService;
import nts.uk.ctx.workflow.dom.service.JudgmentApprovalStatusService;
import nts.uk.ctx.workflow.dom.service.ReleaseAllAtOnceService;
import nts.uk.ctx.workflow.dom.service.ReleaseService;
import nts.uk.ctx.workflow.dom.service.RemandService;
import nts.uk.ctx.workflow.dom.service.output.AppRootStateConfirmOutput;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRepresenterOutput;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRootContentOutput;
import nts.uk.ctx.workflow.dom.service.output.ApprovalStatusOutput;
import nts.uk.ctx.workflow.dom.service.output.ApproverApprovedOutput;
import nts.uk.ctx.workflow.dom.service.output.ApproverPersonOutput;
import nts.uk.ctx.workflow.dom.service.output.ErrorFlag;
import nts.uk.ctx.workflow.pub.agent.AgentPubExport;
import nts.uk.ctx.workflow.pub.agent.ApproverRepresenterExport;
import nts.uk.ctx.workflow.pub.agent.RepresenterInformationExport;
import nts.uk.ctx.workflow.pub.service.ApprovalRootStatePub;
import nts.uk.ctx.workflow.pub.service.export.AppRootStateConfirmExport;
import nts.uk.ctx.workflow.pub.service.export.ApprovalActionByEmpl;
import nts.uk.ctx.workflow.pub.service.export.ApprovalBehaviorAtrExport;
import nts.uk.ctx.workflow.pub.service.export.ApprovalFrameExport;
import nts.uk.ctx.workflow.pub.service.export.ApprovalPhaseStateExport;
import nts.uk.ctx.workflow.pub.service.export.ApprovalRootContentExport;
import nts.uk.ctx.workflow.pub.service.export.ApprovalRootOfEmployeeExport;
import nts.uk.ctx.workflow.pub.service.export.ApprovalRootSituation;
import nts.uk.ctx.workflow.pub.service.export.ApprovalRootStateExport;
import nts.uk.ctx.workflow.pub.service.export.ApprovalStatus;
import nts.uk.ctx.workflow.pub.service.export.ApprovalStatusForEmployee;
import nts.uk.ctx.workflow.pub.service.export.ApproveRootStatusForEmpExport;
import nts.uk.ctx.workflow.pub.service.export.ApproverApprovedExport;
import nts.uk.ctx.workflow.pub.service.export.ApproverEmployeeState;
import nts.uk.ctx.workflow.pub.service.export.ApproverPersonExport;
import nts.uk.ctx.workflow.pub.service.export.ApproverStateExport;
import nts.uk.ctx.workflow.pub.service.export.ApproverWithFlagExport;
import nts.uk.ctx.workflow.pub.service.export.ErrorFlagExport;
import nts.uk.ctx.workflow.pub.service.export.ReleasedProprietyDivision;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class ApprovalRootStatePubImpl implements ApprovalRootStatePub {
	
	@Inject
	private ApprovalRootStateService approvalRootStateService;
	
	@Inject
	private CollectApprovalRootService collectApprovalRootService;
	
	@Inject
	private PersonAdapter personAdapter;
	
	@Inject
	private AgentRepository agentRepository;
	
	@Inject
	private ApprovalRootStateRepository approvalRootStateRepository;
	
	@Inject
	private ApproveService approveService;

	@Inject
	private ReleaseAllAtOnceService releaseAllAtOnceService;
	
	@Inject
	private CollectApprovalAgentInforService collectApprovalAgentInforService;
	
	@Inject
	private CollectMailNotifierService collectMailNotifierService;
	
	@Inject
	private ReleaseService releaseService;
	
	@Inject
	private DenyService denyService;
	
	@Inject
	private JudgmentApprovalStatusService judgmentApprovalStatusService;
	
	@Inject
	private RemandService remandService;
	
	@Inject
	private GenerateApprovalRootStateService generateApprovalRootStateService;
	
	@Override
	public Map<String,List<ApprovalPhaseStateExport>> getApprovalRoots(List<String> appIDs,String companyID) {
		Map<String,List<ApprovalPhaseStateExport>> approvalPhaseStateExportMs = new LinkedHashMap<>();
		ApprovalRootContentOutput approvalRootContentOutput =  null;
		List<ApprovalRootState> approvalRootStates = approvalRootStateRepository.findEmploymentApps(appIDs);
		if(!CollectionUtil.isEmpty(approvalRootStates)){
			for(ApprovalRootState approvalRootState :  approvalRootStates){
				approvalRootContentOutput = new ApprovalRootContentOutput(approvalRootState, ErrorFlag.NO_ERROR);
				
				List<ApprovalPhaseStateExport> approvalPhaseStateExports = approvalRootContentOutput.getApprovalRootState().getListApprovalPhaseState()
							.stream()
							.sorted(Comparator.comparing(ApprovalPhaseState::getPhaseOrder))
							.map(x -> {
								return new ApprovalPhaseStateExport(
										x.getPhaseOrder(),
										EnumAdaptor.valueOf(x.getApprovalAtr().value, ApprovalBehaviorAtrExport.class),
										x.getListApprovalFrame()
										.stream()
										.sorted(Comparator.comparing(ApprovalFrame::getFrameOrder))
										.map(y -> {
											return new ApprovalFrameExport(
													y.getPhaseOrder(), 
													y.getFrameOrder(), 
													EnumAdaptor.valueOf(y.getApprovalAtr().value, ApprovalBehaviorAtrExport.class),
													y.getListApproverState().stream().map(z -> { 
														return new ApproverStateExport(z.getApproverID(), "", "", "");
													}).collect(Collectors.toList()), 
													y.getApproverID(),
													"", 
													y.getRepresenterID(),		
													"",
													y.getApprovalReason());
										}).collect(Collectors.toList()));
							}).collect(Collectors.toList());
				approvalPhaseStateExportMs.put(approvalRootState.getRootStateID(), approvalPhaseStateExports);
			}
		}
		return approvalPhaseStateExportMs;
	}
	@Override
	public ApprovalRootContentExport getApprovalRoot(String companyID, String employeeID, Integer appTypeValue, GeneralDate date, String appID, Boolean isCreate) {
		ApprovalRootContentOutput approvalRootContentOutput = null;
		if(isCreate.equals(Boolean.TRUE)){
			approvalRootContentOutput = collectApprovalRootService.getApprovalRootOfSubjectRequest(
					companyID, 
					employeeID, 
					EmploymentRootAtr.APPLICATION, 
					EnumAdaptor.valueOf(appTypeValue, ApplicationType.class) , 
					date);
		} else {
			ApprovalRootState approvalRootState = approvalRootStateRepository.findEmploymentApp(appID).orElseThrow(()->
					new RuntimeException("data WWFDT_APPROVAL_ROOT_STATE error: ID ="+appID)
			);
			approvalRootContentOutput = new ApprovalRootContentOutput(approvalRootState, ErrorFlag.NO_ERROR);
		}
		return new ApprovalRootContentExport(
				new ApprovalRootStateExport(
					approvalRootContentOutput.getApprovalRootState().getListApprovalPhaseState()
					.stream()
					.sorted(Comparator.comparing(ApprovalPhaseState::getPhaseOrder))
					.map(x -> {
						return new ApprovalPhaseStateExport(
								x.getPhaseOrder(),
								EnumAdaptor.valueOf(x.getApprovalAtr().value, ApprovalBehaviorAtrExport.class),
								x.getListApprovalFrame()
								.stream()
								.sorted(Comparator.comparing(ApprovalFrame::getFrameOrder))
								.map(y -> {
									return new ApprovalFrameExport(
											y.getPhaseOrder(), 
											y.getFrameOrder(), 
											EnumAdaptor.valueOf(y.getApprovalAtr().value, ApprovalBehaviorAtrExport.class),
											y.getListApproverState().stream().map(z -> { 
												String approverName = personAdapter.getPersonInfo(z.getApproverID()).getEmployeeName();
												String representerID = "";
												String representerName = "";
												ApprovalRepresenterOutput approvalRepresenterOutput = 
														collectApprovalAgentInforService.getApprovalAgentInfor(companyID, Arrays.asList(z.getApproverID()));
												if(approvalRepresenterOutput.getAllPathSetFlag().equals(Boolean.FALSE)){
													if(!CollectionUtil.isEmpty(approvalRepresenterOutput.getListAgent())){
														representerID = approvalRepresenterOutput.getListAgent().get(0);
														representerName = personAdapter.getPersonInfo(representerID).getEmployeeName();
													}
												}
												return new ApproverStateExport(z.getApproverID(), approverName, representerID, representerName);
											}).collect(Collectors.toList()), 
											y.getApproverID(),
											Strings.isBlank(y.getApproverID()) ? "" : personAdapter.getPersonInfo(y.getApproverID()).getEmployeeName(), 
											y.getRepresenterID(),		
											Strings.isBlank(y.getRepresenterID()) ? "" : personAdapter.getPersonInfo(y.getRepresenterID()).getEmployeeName(),
											y.getApprovalReason());
								}).collect(Collectors.toList()));
					}).collect(Collectors.toList())
				), 
				EnumAdaptor.valueOf(approvalRootContentOutput.getErrorFlag().value, ErrorFlagExport.class));
		
		
	}
	
	@Override
	public void insertAppRootType(String companyID, String employeeID, Integer appTypeValue, GeneralDate date, String appID) {
		approvalRootStateService.insertAppRootType(companyID, employeeID, EnumAdaptor.valueOf(appTypeValue, ApplicationType.class), date, appID);
	}

	@Override
	public List<String> getNextApprovalPhaseStateMailList(String companyID, String rootStateID,
			Integer approvalPhaseStateNumber, Boolean isCreate, String employeeID, Integer appTypeValue,
			GeneralDate appDate) {
		return approveService.getNextApprovalPhaseStateMailList(
				companyID, 
				rootStateID, 
				approvalPhaseStateNumber, 
				isCreate, 
				employeeID, 
				EnumAdaptor.valueOf(appTypeValue, ApplicationType.class), 
				appDate);
	}

	@Override
	public Integer doApprove(String companyID, String rootStateID, String employeeID, Boolean isCreate, Integer appTypeValue, GeneralDate appDate, String memo) {
		return approveService.doApprove(companyID, rootStateID, employeeID, isCreate, EnumAdaptor.valueOf(appTypeValue, ApplicationType.class), appDate, memo);
	}

	@Override
	public Boolean isApproveAllComplete(String companyID, String rootStateID, String employeeID, Boolean isCreate,
			Integer appTypeValue, GeneralDate appDate) {
		return approveService.isApproveAllComplete(companyID, rootStateID, employeeID, isCreate, EnumAdaptor.valueOf(appTypeValue, ApplicationType.class), appDate);
	}

	@Override
	public void doReleaseAllAtOnce(String companyID, String rootStateID) {
		releaseAllAtOnceService.doReleaseAllAtOnce(companyID, rootStateID);
	}

	@Override
	public ApproverApprovedExport getApproverApproved(String rootStateID) {
		ApproverApprovedOutput approverApprovedOutput = releaseAllAtOnceService.getApproverApproved(rootStateID);
		return new ApproverApprovedExport(
				approverApprovedOutput.getListApproverWithFlagOutput().stream()
					.map(x -> new ApproverWithFlagExport(x.getEmployeeID(), x.getAgentFlag())).collect(Collectors.toList()), 
				approverApprovedOutput.getListApprover());
	}

	@Override
	public AgentPubExport getApprovalAgentInfor(String companyID, List<String> listApprover) {
		ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, listApprover);
		return new AgentPubExport(
				approvalRepresenterOutput.getListApprovalAgentInfor().stream()
					.map(x -> new ApproverRepresenterExport(x.getApprover(), new RepresenterInformationExport(x.getRepresenter().getValue())))
					.collect(Collectors.toList()), 
				approvalRepresenterOutput.getListAgent(), 
				approvalRepresenterOutput.getAllPathSetFlag());
	}

	@Override
	public List<String> getMailNotifierList(String companyID, String rootStateID) {
		return collectMailNotifierService.getMailNotifierList(companyID, rootStateID);
	}

	@Override
	public void deleteApprovalRootState(String rootStateID) {
		approvalRootStateRepository.delete(rootStateID);
	}

	@Override
	public Boolean doRelease(String companyID, String rootStateID, String employeeID) {
		return releaseService.doRelease(companyID, rootStateID, employeeID);
	}

	@Override
	public Boolean doDeny(String companyID, String rootStateID, String employeeID, String memo) {
		return denyService.doDeny(companyID, rootStateID, employeeID, memo);
	}

	@Override
	public Boolean judgmentTargetPersonIsApprover(String companyID, String rootStateID, String employeeID) {
		return judgmentApprovalStatusService.judgmentTargetPersonIsApprover(companyID, rootStateID, employeeID);
	}

	@Override
	public ApproverPersonExport judgmentTargetPersonCanApprove(String companyID, String rootStateID,
			String employeeID) {
		ApproverPersonOutput approverPersonOutput = judgmentApprovalStatusService.judgmentTargetPersonCanApprove(companyID, rootStateID, employeeID);
		return new ApproverPersonExport(
				approverPersonOutput.getAuthorFlag(), 
				EnumAdaptor.valueOf(approverPersonOutput.getApprovalAtr().value, ApprovalBehaviorAtrExport.class) , 
				approverPersonOutput.getExpirationAgentFlag());
	}

	@Override
	public List<String> doRemandForApprover(String companyID, String rootStateID, Integer order) {
		return remandService.doRemandForApprover(companyID, rootStateID, order);
	}

	@Override
	public void doRemandForApplicant(String companyID, String rootStateID) {
		remandService.doRemandForApplicant(companyID, rootStateID);
	}
	@Override
	public ApprovalRootOfEmployeeExport getApprovalRootOfEmloyee(GeneralDate startDate, GeneralDate endDate,
			String approverID,String companyID,Integer rootType) {
		// 承認者と期間から承認ルートインスタンスを取得する
		List<ApprovalRootState> approvalRootStates = this.approvalRootStateRepository.findEmployeeAppByApprovalRecordDate(startDate, endDate, approverID,rootType);
		// ドメインモデル「代行承認」を取得する
		List<Agent> agents = this.agentRepository.findByApproverAndDate(companyID, approverID, startDate, endDate);
		List<String> employeeApproverID = new ArrayList<>();
		employeeApproverID.add(approverID);
		if (!CollectionUtil.isEmpty(agents)) {
			for(Agent agent : agents){
				// ドメインモデル「承認ルートインスタンス」を取得する
				employeeApproverID.add(agent.getEmployeeId());
				List<ApprovalRootState> approvalRootStateAgents = this.approvalRootStateRepository.findEmployeeAppByApprovalRecordDate(startDate, endDate, agent.getEmployeeId(),rootType);
				if(!CollectionUtil.isEmpty(approvalRootStateAgents)){
					for(ApprovalRootState approver : approvalRootStateAgents){
						approvalRootStates.add(approver);
					}
				}
			}
		}
		ApprovalRootOfEmployeeExport result = new ApprovalRootOfEmployeeExport();
		
		if(CollectionUtil.isEmpty(approvalRootStates)){
			return result;
		}
		List<ApprovalRootSituation> approvalRootSituations = new ArrayList<>();
		//
		for(ApprovalRootState approverRoot : approvalRootStates){
			ApprovalRootSituation approvalRootSituation = new ApprovalRootSituation(approverRoot.getRootStateID(),
					null,
					approverRoot.getApprovalRecordDate(),
					approverRoot.getEmployeeID(),
					new ApprovalStatus(EnumAdaptor.valueOf(ApprovalActionByEmpl.NOT_APPROVAL.value, ApprovalActionByEmpl.class),
									EnumAdaptor.valueOf(ReleasedProprietyDivision.NOT_RELEASE.value, ReleasedProprietyDivision.class)));
			//承認中のフェーズの承認者か = false
			boolean approverPhaseFlag = false;
			//基準社員のフェーズ=0
			int employeephase = 0;
			approverRoot.getListApprovalPhaseState().sort((a,b) -> b.getPhaseOrder().compareTo(a.getPhaseOrder()));
			List<ApprovalPhaseState> listApprovalPhaseState = approverRoot.getListApprovalPhaseState();
			
			ApprovalStatus approvalStatus = new ApprovalStatus();
			List<Integer> phaseOfApprover = new ArrayList<>();
			for(int i =0; i < listApprovalPhaseState.size();i++){
				// add approver
				for(ApprovalFrame approvalFrame : listApprovalPhaseState.get(i).getListApprovalFrame()){
					for(ApproverState approverState : approvalFrame.getListApproverState())	{
						// xu li lay list phase chua approverID
						for(String employeeID : employeeApproverID ){
							if(approverState.getApproverID().equals(employeeID)){
								phaseOfApprover.add(listApprovalPhaseState.get(i).getPhaseOrder());
							}
						}
					}			
				}
				//1.承認フェーズ毎の承認者を取得する(getApproverFromPhase)
				List<String> approverFromPhases = judgmentApprovalStatusService.getApproverFromPhase(listApprovalPhaseState.get(i));
				if(!CollectionUtil.isEmpty(approverFromPhases)){
					// 承認中のフェーズ＝ループ中のフェーズ．順序
					int approverPhase = i;
					// フェーズ承認区分＝ループ中のフェーズ．承認区分
					int approverPhaseIndicator = listApprovalPhaseState.get(i).getApprovalAtr().value;
					//1.承認状況の判断
					ApprovalStatusOutput approvalStatusOutput = judgmentApprovalStatusService.judmentApprovalStatus(companyID, listApprovalPhaseState.get(i), approverRoot.getEmployeeID());
					if(approverPhaseFlag == true && approvalStatusOutput.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
						break;
					}
					if(approvalStatusOutput.getApprovalFlag() == true && approvalStatusOutput.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)){
						approverPhaseFlag = true;
					}
					// output「ルート状況」をセットする
					if(approvalStatusOutput.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED) && approvalStatusOutput.getApprovableFlag() == true){
						approvalStatus.setReleaseDivision(EnumAdaptor.valueOf(ReleasedProprietyDivision.RELEASE.value, ReleasedProprietyDivision.class));
					}else{
						approvalStatus.setReleaseDivision(EnumAdaptor.valueOf(ReleasedProprietyDivision.NOT_RELEASE.value, ReleasedProprietyDivision.class));
					}
					//承認状況．基準社員の承認アクション
					if(approvalStatusOutput.getApprovableFlag() == true){
						approvalStatus.setApprovalActionByEmpl(EnumAdaptor.valueOf(ApprovalActionByEmpl.NOT_APPROVAL.value, ApprovalActionByEmpl.class));
					}else if(approvalStatusOutput.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)){
						approvalStatus.setApprovalActionByEmpl(EnumAdaptor.valueOf(ApprovalActionByEmpl.APPROVAL_REQUIRE.value, ApprovalActionByEmpl.class));
					}else{
						approvalStatus.setApprovalActionByEmpl(EnumAdaptor.valueOf(ApprovalActionByEmpl.APPROVALED.value, ApprovalActionByEmpl.class));
					}
					//基準社員のフェーズ＝ループ中のフェーズ．順序
					if(approvalStatusOutput.getApprovalFlag() == true){
						employeephase = approverPhase;
					}
					if(approvalStatusOutput.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
						break;
					}
				}
				
			}
			approvalRootSituation.setApprovalStatus(approvalStatus);
			// output「ルート状況」をセットする
			if(checkPhase(approverRoot.getListApprovalPhaseState().get(employeephase).getPhaseOrder(),phaseOfApprover,0) && approverRoot.getListApprovalPhaseState().get(employeephase) .getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)){
				approvalRootSituation.setApprovalAtr(ApproverEmployeeState.PHASE_DURING);
			}else if(checkPhase(approverRoot.getListApprovalPhaseState().get(employeephase).getPhaseOrder(),phaseOfApprover,0) && approverRoot.getListApprovalPhaseState().get(employeephase) .getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
				approvalRootSituation.setApprovalAtr(ApproverEmployeeState.COMPLETE);
			}else if(checkPhase(approverRoot.getListApprovalPhaseState().get(employeephase).getPhaseOrder(),phaseOfApprover,1)){
				approvalRootSituation.setApprovalAtr(ApproverEmployeeState.PHASE_LESS);
			}else{
				approvalRootSituation.setApprovalAtr(ApproverEmployeeState.PHASE_PASS);
			}
			approvalRootSituations.add(approvalRootSituation);
		}
		result.setEmployeeStandard(approverID);
		result.setApprovalRootSituations(approvalRootSituations);
		return result;
	}
	private boolean checkPhase(int phaseOrderApprover,List<Integer> phaseOrders, int type){
		boolean result = false;
		if (type == 0) {
			for (Integer a : phaseOrders) {
				if(a == phaseOrderApprover){
					return true;
				}
			}
		} else if (type == 1) {
			for (Integer a : phaseOrders) {
				if(a > phaseOrderApprover){
					return true;
				}
			}
		}

		return result;
	}
	@Override
	public List<ApproveRootStatusForEmpExport> getApprovalByEmplAndDate(GeneralDate startDate, GeneralDate endDate,
			String employeeID, String companyID, Integer rootType) {
		List<ApproveRootStatusForEmpExport> result = new ArrayList<>();
		// 対象者と期間から承認ルートインスタンスを取得する
		List<ApprovalRootState> approvalRootSates = this.approvalRootStateRepository.findAppByEmployeeIDRecordDate(startDate, endDate, employeeID, rootType);
		
		//承認ルート状況を取得する
		result = this.getApproveRootStatusForEmpExport(approvalRootSates);
		return result;
	}
	//承認ルート状況を取得する
	private List<ApproveRootStatusForEmpExport> getApproveRootStatusForEmpExport(List<ApprovalRootState> approvalRootSates){
		List<ApproveRootStatusForEmpExport> result = new ArrayList<>();
		for(ApprovalRootState approvalRoot : approvalRootSates){
			ApproveRootStatusForEmpExport approveRootStatusForEmpExport = new ApproveRootStatusForEmpExport();
			int status = ApprovalStatusForEmployee.UNAPPROVED.value;
			boolean unapprovedPhasePresent = false;
			List<ApprovalPhaseState> listApprovalPhaseState = approvalRoot.getListApprovalPhaseState();
			listApprovalPhaseState.sort((a,b) -> b.getPhaseOrder().compareTo(a.getPhaseOrder()));
			for(ApprovalPhaseState approvalPhaseState : listApprovalPhaseState){
				//1.承認フェーズ毎の承認者を取得する(getApproverFromPhase)
				List<String> approverFromPhases = judgmentApprovalStatusService.getApproverFromPhase(approvalPhaseState);
				// ループ中の承認フェーズに承認者がいる
				if(!CollectionUtil.isEmpty(approverFromPhases)){
					// ループ中のドメインモデル「承認フェーズインスタンス」．承認区分 == 承認済
					if(approvalPhaseState.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
						//未承認フェーズあり=true
						if(unapprovedPhasePresent == true){
							status = ApprovalStatusForEmployee.DURING_APPROVAL.value;
							break;
						}else{
							// 未承認フェーズあり=false
							status = ApprovalStatusForEmployee.APPROVED.value;
							break;
						}
					}else{
						unapprovedPhasePresent = true;
						if(checkApproverOfFrame(approvalPhaseState.getListApprovalFrame())){
							status = approvalPhaseState.getApprovalAtr().value;
							break;
						}
					}
				}
			}
			approveRootStatusForEmpExport.setAppDate(approvalRoot.getApprovalRecordDate());
			approveRootStatusForEmpExport.setEmployeeID(approvalRoot.getEmployeeID());
			approveRootStatusForEmpExport.setApprovalStatus(EnumAdaptor.valueOf(status, ApprovalStatusForEmployee.class));
			result.add(approveRootStatusForEmpExport);
		}
		return result;
	}
	
	private boolean checkApproverOfFrame(List<ApprovalFrame> listApprovalFrame){
		for(ApprovalFrame approvalFrame : listApprovalFrame){
			if(approvalFrame.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean checkDataApproveed(GeneralDate startDate, GeneralDate endDate, String approverID, Integer rootType,
			String companyID) {
		List<ApprovalRootState> approvalRootStates = new ArrayList<>();
		if(rootType == null){
			// xử lí 承認者と期間から承認ルートインスタンスを取得する（ルート種類指定なし）
			 approvalRootStates = this.approvalRootStateRepository
					.findEmployeeAppByApprovalRecordDateAndNoRootType(startDate, endDate, approverID);
			 
		}else{
			// 承認者と期間から承認ルートインスタンスを取得する
			 approvalRootStates = this.approvalRootStateRepository
					.findEmployeeAppByApprovalRecordDate(startDate, endDate, approverID, rootType);
		}
		// ドメインモデル「代行承認」を取得する
		List<Agent> agents = this.agentRepository.findByApproverAndDate(companyID, approverID, startDate, endDate);
		List<String> employeeApproverID = new ArrayList<>();
		employeeApproverID.add(approverID);
		if (!CollectionUtil.isEmpty(agents)) {
			for (Agent agent : agents) {
				// ドメインモデル「承認ルートインスタンス」を取得する
				employeeApproverID.add(agent.getEmployeeId());
				List<ApprovalRootState> approvalRootStateAgents = this.approvalRootStateRepository
						.findEmployeeAppByApprovalRecordDate(startDate, endDate, agent.getEmployeeId(), rootType);
				if (!CollectionUtil.isEmpty(approvalRootStateAgents)) {
					for (ApprovalRootState approver : approvalRootStateAgents) {
						approvalRootStates.add(approver);
					}
				}
			}
		}
		if(CollectionUtil.isEmpty(approvalRootStates)){
			return false;
		}
		boolean result = false;
		for(ApprovalRootState approval : approvalRootStates){
			ApproverPersonExport ApproverPersonExport = this.judgmentTargetPersonCanApprove(companyID,approval.getRootStateID(),approverID);
			if(ApproverPersonExport.getAuthorFlag() && ApproverPersonExport.getApprovalAtr().equals(ApprovalBehaviorAtrExport.UNAPPROVED) && !ApproverPersonExport.getExpirationAgentFlag()){
				result = true;
				break;
			}else{
				result = false;
			}
		}
		
		
		
		return result;
	}
	@Override
	// RequestList229
	public List<ApproveRootStatusForEmpExport> getApprovalByListEmplAndDate(GeneralDate startDate, GeneralDate endDate,
			List<String> employeeIDs, String companyID, Integer rootType) {
		List<ApproveRootStatusForEmpExport> result = new ArrayList<>();
		// 対象者と期間から承認ルートインスタンスを取得する
		List<ApprovalRootState> approvalRootSates = this.approvalRootStateRepository.findAppByListEmployeeIDRecordDate(startDate, endDate, employeeIDs, rootType);
		
		//承認ルート状況を取得する
		result = this.getApproveRootStatusForEmpExport(approvalRootSates);
		return result;
	}
	@Override
	public AppRootStateConfirmExport getApprovalRootState(String companyID, String employeeID, Integer confirmAtr,
			Integer appType, GeneralDate date) {
		AppRootStateConfirmOutput appRootStateConfirmOutput = generateApprovalRootStateService.getApprovalRootState(
				companyID, 
				employeeID, 
				EnumAdaptor.valueOf(confirmAtr-1, ConfirmationRootType.class), 
				appType == null ? null : EnumAdaptor.valueOf(appType, ApplicationType.class), 
				date);
		return new AppRootStateConfirmExport(
				appRootStateConfirmOutput.getIsError(), 
				appRootStateConfirmOutput.getRootStateID(), 
				appRootStateConfirmOutput.getErrorMsg());
	}
	@Override
	// requestList155
	public List<ApproveRootStatusForEmpExport> getApprovalByListEmplAndListApprovalRecordDate(
			List<GeneralDate> approvalRecordDates, List<String> employeeIDs, Integer rootType) {
		List<ApproveRootStatusForEmpExport> result = new ArrayList<>();
		// 対象者リストと日付リストから承認ルートインスタンスを取得する
		List<ApprovalRootState> approvalRootSates = this.approvalRootStateRepository.findAppByListEmployeeIDAndListRecordDate(approvalRecordDates, employeeIDs, rootType);
		
		//承認ルート状況を取得する
		result = this.getApproveRootStatusForEmpExport(approvalRootSates);
		return result;
	}
	@Override
	// requestList347
	public void RegisterApproval(String approverID, List<GeneralDate> approvalRecordDates, List<String> employeeIDs,
			Integer rootType,String companyID) {
		// 対象者リストと日付リストから承認ルートインスタンスを取得する
		List<ApprovalRootState> approvalRootSates = this.approvalRootStateRepository.findAppByListEmployeeIDAndListRecordDate(approvalRecordDates, employeeIDs, rootType);
		if(!CollectionUtil.isEmpty(approvalRootSates)){
			for(ApprovalRootState approvalRootState : approvalRootSates){
				 this.doApprove(companyID, approvalRootState.getRootStateID(), approverID, false, 0, null, null);
			}
		}
	}
	@Override
	// requestList356
	public boolean ReleaseApproval(String approverID, List<GeneralDate> approvalRecordDates, List<String> employeeIDs,
			Integer rootType, String companyID) {
		boolean result = true;
		// 対象者リストと日付リストから承認ルートインスタンスを取得する
		List<ApprovalRootState> approvalRootSates = this.approvalRootStateRepository.findAppByListEmployeeIDAndListRecordDate(approvalRecordDates, employeeIDs, rootType);
		if(approvalRootSates != null){
			for(ApprovalRootState approvalRootState : approvalRootSates){
				result = this.doRelease(companyID, approvalRootState.getRootStateID(), approverID);
				if(!result){
					return result;
				}
			}
		}
		return result;
	}
}
