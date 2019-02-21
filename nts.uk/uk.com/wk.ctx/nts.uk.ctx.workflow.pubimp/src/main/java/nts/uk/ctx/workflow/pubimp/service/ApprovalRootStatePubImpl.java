package nts.uk.ctx.workflow.pubimp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import nts.uk.ctx.workflow.dom.approvermanagement.setting.ApprovalSettingRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.PrincipalApprovalFlg;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApplicationType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmPerson;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmationRootType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.EmploymentRootAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootStateRepository;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApproverState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.RootType;
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
import nts.uk.ctx.workflow.pub.service.export.ApprovalPhaseStateParam;
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
import nts.uk.ctx.workflow.pub.service.export.ApproverRemandExport;
import nts.uk.ctx.workflow.pub.service.export.ApproverStateExport;
import nts.uk.ctx.workflow.pub.service.export.ApproverWithFlagExport;
import nts.uk.ctx.workflow.pub.service.export.ErrorFlagExport;
import nts.uk.ctx.workflow.pub.service.export.ReleasedProprietyDivision;
import nts.uk.shr.com.context.AppContexts;
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
	@Inject
	private ApprovalSettingRepository repoApprSet;
	
	@Override
	public Map<String,List<ApprovalPhaseStateExport>> getApprovalRoots(List<String> appIDs, String companyID){ 
		String approverID = AppContexts.user().employeeId();
		Map<String,List<ApprovalPhaseStateExport>> mapApprPhsStateEx = new LinkedHashMap<>();
		ApprovalRootContentOutput apprRootContentOut =  null;
		//ドメインモデル「承認ルートインスタンス」を取得する
		List<ApprovalRootState> approvalRootStates = approvalRootStateRepository.findEmploymentApps(appIDs, approverID);
		if(approvalRootStates.isEmpty()){
			return mapApprPhsStateEx;
		}
		//hoatt 2018.12.14
		//EA修正履歴 No.3009
		//ドメインモデル「承認設定」を取得する
		Optional<PrincipalApprovalFlg> flg = repoApprSet.getPrincipalByCompanyId(companyID);
		List<ApprovalRootState> lstApprRootSttFil = new ArrayList<>();
		if(!flg.isPresent() || flg.get().equals(PrincipalApprovalFlg.NOT_PRINCIPAL)){//本人による承認＝falseの場合
			//申請者ID＝ログイン社員IDのデータを取得した一覧から削除する(applicantID = loginID -> remove app)
			lstApprRootSttFil = approvalRootStates.stream().filter(c -> !c.getEmployeeID().equals(approverID))
					.collect(Collectors.toList());
		}else{
			lstApprRootSttFil = approvalRootStates;
		}
		if(lstApprRootSttFil.isEmpty()){
			return mapApprPhsStateEx;
		}
		for(ApprovalRootState approvalRootState :  lstApprRootSttFil){
			apprRootContentOut = new ApprovalRootContentOutput(approvalRootState, ErrorFlag.NO_ERROR);
			
			List<ApprovalPhaseStateExport> lstApprPhsStateEx = apprRootContentOut.getApprovalRootState().getListApprovalPhaseState()
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
												y.getApprovalReason(), y.getConfirmAtr().value);
									}).collect(Collectors.toList()));
						}).collect(Collectors.toList());
			mapApprPhsStateEx.put(approvalRootState.getRootStateID(), lstApprPhsStateEx);
		}
		return mapApprPhsStateEx;
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
											y.getApprovalReason(), y.getConfirmAtr().value);
								}).collect(Collectors.toList()));
					}).collect(Collectors.toList())
				), 
				EnumAdaptor.valueOf(approvalRootContentOutput.getErrorFlag().value, ErrorFlagExport.class));
		
		
	}
	
	@Override
	public void insertAppRootType(String companyID, String employeeID, 
			Integer appTypeValue, GeneralDate date, String appID, Integer rootType) {
		approvalRootStateService.insertAppRootType(
				companyID, 
				employeeID, 
				EnumAdaptor.valueOf(appTypeValue, ApplicationType.class), 
				date, 
				appID,
				rootType);
	}

	@Override
	public List<String> getNextApprovalPhaseStateMailList(String companyID, String rootStateID,
			Integer approvalPhaseStateNumber, Boolean isCreate, String employeeID, Integer appTypeValue,
			GeneralDate appDate, Integer rootType) {
		return approveService.getNextApprovalPhaseStateMailList(
				companyID, 
				rootStateID, 
				approvalPhaseStateNumber, 
				isCreate, 
				employeeID, 
				EnumAdaptor.valueOf(appTypeValue, ApplicationType.class), 
				appDate,
				rootType);
	}

	@Override
	public Integer doApprove(String companyID, String rootStateID, String employeeID, Boolean isCreate, 
			Integer appTypeValue, GeneralDate appDate, String memo, Integer rootType) {
		return approveService.doApprove(
				companyID, 
				rootStateID, 
				employeeID, 
				isCreate, 
				EnumAdaptor.valueOf(appTypeValue, ApplicationType.class), 
				appDate, 
				memo,
				rootType);
	}

	@Override
	public Boolean isApproveAllComplete(String companyID, String rootStateID, String employeeID, Boolean isCreate,
			Integer appTypeValue, GeneralDate appDate, Integer rootType) {
		return approveService.isApproveAllComplete(
				companyID, 
				rootStateID, 
				employeeID, 
				isCreate, 
				EnumAdaptor.valueOf(appTypeValue, ApplicationType.class), 
				appDate,
				rootType);
	}

	@Override
	public void doReleaseAllAtOnce(String companyID, String rootStateID, Integer rootType) {
		releaseAllAtOnceService.doReleaseAllAtOnce(companyID, rootStateID, rootType);
	}

	@Override
	public ApproverApprovedExport getApproverApproved(String rootStateID, Integer rootType) {
		ApproverApprovedOutput approverApprovedOutput = releaseAllAtOnceService.getApproverApproved(rootStateID, rootType);
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
	public List<String> getMailNotifierList(String companyID, String rootStateID, Integer rootType) {
		return collectMailNotifierService.getMailNotifierList(companyID, rootStateID, rootType);
	}

	@Override
	public void deleteApprovalRootState(String rootStateID, Integer rootType) {
		approvalRootStateRepository.delete(rootStateID, rootType);
	}

	@Override
	public Boolean doRelease(String companyID, String rootStateID, String employeeID, Integer rootType) {
		return releaseService.doRelease(companyID, rootStateID, employeeID, rootType);
	}

	@Override
	public Boolean doDeny(String companyID, String rootStateID, String employeeID, String memo, Integer rootType) {
		return denyService.doDeny(companyID, rootStateID, employeeID, memo, rootType);
	}

	@Override
	public Boolean judgmentTargetPersonIsApprover(String companyID, String rootStateID, String employeeID, Integer rootType) {
		return judgmentApprovalStatusService.judgmentTargetPersonIsApprover(companyID, rootStateID, employeeID, rootType);
	}

	@Override
	public ApproverPersonExport judgmentTargetPersonCanApprove(String companyID, String rootStateID,
			String employeeID, Integer rootType) {
		ApproverPersonOutput approverPersonOutput = judgmentApprovalStatusService
				.judgmentTargetPersonCanApprove(companyID, rootStateID, employeeID, rootType);
		return new ApproverPersonExport(
				approverPersonOutput.getAuthorFlag(), 
				EnumAdaptor.valueOf(approverPersonOutput.getApprovalAtr().value, ApprovalBehaviorAtrExport.class) , 
				approverPersonOutput.getExpirationAgentFlag());
	}

	@Override
	public List<String> doRemandForApprover(String companyID, String rootStateID, Integer order, Integer rootType) {
		return remandService.doRemandForApprover(companyID, rootStateID, order, rootType);
	}

	@Override
	public void doRemandForApplicant(String companyID, String rootStateID, Integer rootType) {
		remandService.doRemandForApplicant(companyID, rootStateID, rootType);
	}
	@Override
	public ApprovalRootOfEmployeeExport getApprovalRootOfEmloyee(GeneralDate startDate, GeneralDate endDate,
			String approverID,String companyID,Integer rootType) {
		List<ApprovalRootState> approvalRootStates = new ArrayList<>();
		// 承認者と期間から承認ルートインスタンスを取得する
		long start = System.currentTimeMillis();
		List<ApprovalRootState> resultApprovalRootState = this.approvalRootStateRepository.findByApprover(companyID, startDate, endDate, approverID, rootType);
		approvalRootStates.addAll(resultApprovalRootState);
		long end = System.currentTimeMillis();
		System.out.println("Thời gian chạy đoạn lệnh: " + (end - start) + "Millis");
		// ドメインモデル「代行承認」を取得する
		List<Agent> agents = this.agentRepository.findByApproverAndDate(companyID, approverID, startDate, endDate);
		List<String> employeeApproverID = new ArrayList<>();
		employeeApproverID.add(approverID);
		
		if (!CollectionUtil.isEmpty(agents)) {
			for(Agent agent : agents){
				// ドメインモデル「承認ルートインスタンス」を取得する
				employeeApproverID.add(agent.getEmployeeId());
			}
		}
		long end1 = System.currentTimeMillis();
		System.out.println("Thời gian chạy đoạn lệnh: " + (end1 - end) + "Millis");
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
			boolean statusFrame  = false;
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
					if(approvalFrame.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
						statusFrame = true;
					}
				}
				//1.承認フェーズ毎の承認者を取得する(getApproverFromPhase)
				List<String> approverFromPhases = judgmentApprovalStatusService.getApproverFromPhase(listApprovalPhaseState.get(i));
				if(!CollectionUtil.isEmpty(approverFromPhases)){
					if(!listApprovalPhaseState.get(i).getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
						// 承認中のフェーズ＝ループ中のフェーズ．順序
//						int approverPhase = i;
						// フェーズ承認区分＝ループ中のフェーズ．承認区分
//						int approverPhaseIndicator = listApprovalPhaseState.get(i).getApprovalAtr().value;
					}
					//1.承認状況の判断
					ApprovalStatusOutput approvalStatusOutput = judgmentApprovalStatusService.judmentApprovalStatusNodataDatabaseAcess(
							companyID, listApprovalPhaseState.get(i), approverID,
							agents.stream().map(x -> x.getAgentSid1()).collect(Collectors.toList()));
					if(listApprovalPhaseState.get(i).getPhaseOrder().equals(5) && listApprovalPhaseState.get(i).getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED) ){
						checkStatusFrame(approvalStatusOutput,approvalStatus);
						employeephase = i;
						break;
					}
					if(approverPhaseFlag == true && listApprovalPhaseState.get(i).getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
						break;
					}
					if(approvalStatusOutput.getApprovalFlag() == true && approvalStatusOutput.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)){
						approverPhaseFlag = true;
					}
					checkStatusFrame(approvalStatusOutput,approvalStatus);
					//基準社員のフェーズ＝ループ中のフェーズ．順序
					if(approvalStatusOutput.getApprovalFlag() == true){
						employeephase = i;
					}
					if(approvalStatusOutput.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
						break;
					}
					if(statusFrame){
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
		long end2 = System.currentTimeMillis();
		System.out.println("Thời gian chạy đoạn lệnh2: " + (end2 - end1) + "Millis");
		result.setEmployeeStandard(approverID);
		result.setApprovalRootSituations(approvalRootSituations);
		return result;
	}
	private void checkStatusFrame(ApprovalStatusOutput approvalStatusOutput,ApprovalStatus approvalStatus ){
		// output「ルート状況」をセットする
		if(approvalStatusOutput.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED) && approvalStatusOutput.getApprovableFlag() == true){
			approvalStatus.setReleaseDivision(EnumAdaptor.valueOf(ReleasedProprietyDivision.RELEASE.value, ReleasedProprietyDivision.class));
		}else{
			approvalStatus.setReleaseDivision(EnumAdaptor.valueOf(ReleasedProprietyDivision.NOT_RELEASE.value, ReleasedProprietyDivision.class));
		}
		//承認状況．基準社員の承認アクション
		if(approvalStatusOutput.getApprovableFlag() == false){
			approvalStatus.setApprovalActionByEmpl(EnumAdaptor.valueOf(ApprovalActionByEmpl.NOT_APPROVAL.value, ApprovalActionByEmpl.class));
		}else if(approvalStatusOutput.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)){
			approvalStatus.setApprovalActionByEmpl(EnumAdaptor.valueOf(ApprovalActionByEmpl.APPROVAL_REQUIRE.value, ApprovalActionByEmpl.class));
		}else{
			approvalStatus.setApprovalActionByEmpl(EnumAdaptor.valueOf(ApprovalActionByEmpl.APPROVALED.value, ApprovalActionByEmpl.class));
		}
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
							status = ApprovalStatusForEmployee.DURING_APPROVAL.value;
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
					.findEmployeeAppByApprovalRecordDateAndNoRootType(companyID, startDate, endDate, approverID);
			 
		}else{
			// 承認者と期間から承認ルートインスタンスを取得する
			 approvalRootStates = this.approvalRootStateRepository
					.findByApprover(companyID, startDate, endDate, approverID, rootType);
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
						.findByApprover(companyID, startDate, endDate, agent.getEmployeeId(), rootType);
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
		System.out.println(approvalRootStates.stream().map(x -> x.getRootStateID()).collect(Collectors.toList()));
		boolean result = false;
		for(ApprovalRootState approval : approvalRootStates){
			ApproverPersonExport ApproverPersonExport = this.judgmentTargetPersonCanApprove(companyID,approval.getRootStateID(),approverID, rootType);
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
	public void registerApproval(String approverID, List<GeneralDate> approvalRecordDates, List<String> employeeIDs,
			Integer rootType,String companyID) {
		// 対象者リストと日付リストから承認ルートインスタンスを取得する
		List<ApprovalRootState> approvalRootSates = this.approvalRootStateRepository.findAppByListEmployeeIDAndListRecordDate(approvalRecordDates, employeeIDs, rootType);
		if(!CollectionUtil.isEmpty(approvalRootSates)){
			for(ApprovalRootState approvalRootState : approvalRootSates){
				 this.doApprove(companyID, approvalRootState.getRootStateID(), approverID, false, 0, null, null, rootType);
			}
		}
	}
	@Override
	// requestList356
	public boolean releaseApproval(String approverID, List<GeneralDate> approvalRecordDates, List<String> employeeIDs,
			Integer rootType, String companyID) {
		boolean result = true;
		// 対象者リストと日付リストから承認ルートインスタンスを取得する
		List<ApprovalRootState> approvalRootSates = this.approvalRootStateRepository.findAppByListEmployeeIDAndListRecordDate(approvalRecordDates, employeeIDs, rootType);
		if(approvalRootSates != null){
			for(ApprovalRootState approvalRootState : approvalRootSates){
				result = this.doRelease(companyID, approvalRootState.getRootStateID(), approverID, rootType);
				if(!result){
					return result;
				}
			}
		}
		return result;
	}
	@Override
	public void cleanApprovalRootState(String rootStateID, Integer rootType) {
		Optional<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findByID(rootStateID, rootType);
		if(!opApprovalRootState.isPresent()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = opApprovalRootState.get();
		approvalRootState.getListApprovalPhaseState().sort(Comparator.comparing(ApprovalPhaseState::getPhaseOrder).reversed());
		approvalRootState.getListApprovalPhaseState().stream().forEach(approvalPhaseState -> {
			approvalPhaseState.getListApprovalFrame().forEach(approvalFrame -> {
				approvalFrame.setApprovalAtr(ApprovalBehaviorAtr.UNAPPROVED);
				approvalFrame.setApproverID(null);
				approvalFrame.setRepresenterID(null);
				approvalFrame.setApprovalDate(null);
				approvalFrame.setApprovalReason(null);
			});
			approvalPhaseState.setApprovalAtr(ApprovalBehaviorAtr.UNAPPROVED);
		});
		approvalRootStateRepository.update(approvalRootState, rootType);
		
	}
	@Override
	public void deleteConfirmDay(String employeeID, GeneralDate date) {
		approvalRootStateRepository.deleteConfirmDay(employeeID, date);
	}
	/**
	 * RequestList No.483
	 * 1.承認フェーズ毎の承認者を取得する
	 * @param phase
	 * @return
	 */
	@Override
	public List<String> getApproverFromPhase(ApprovalPhaseStateParam param) {
		ApprovalPhaseState phase = ApprovalPhaseState.createFormTypeJava(param.getRootStateID(), param.getPhaseOrder(),
				param.getApprovalAtr(),param.getApprovalForm(), 
				param.getListApprovalFrame().stream().map(c->new ApprovalFrame(c.getRootStateID(),
					c.getPhaseOrder(),
					c.getFrameOrder(),
					EnumAdaptor.valueOf(c.getApprovalAtr(), ApprovalBehaviorAtr.class),
					EnumAdaptor.valueOf(c.getConfirmAtr(), ConfirmPerson.class),
					c.getListApproverState().stream().map(x -> new ApproverState(x.getRootStateID(),
						x.getPhaseOrder(),
						x.getFrameOrder(),
						x.getApproverID(),
						x.getCompanyID(),
						x.getDate())).collect(Collectors.toList()),
					c.getApproverID(),
					c.getRepresenterID(),
					c.getApprovalDate(),
					c.getApprovalReason())).collect(Collectors.toList()));
		return judgmentApprovalStatusService.getApproverFromPhase(phase);
	}
	/**
	 * RequestList 479
	 * 差し戻し対象者一覧を取得
	 * @param appID
	 * @return
	 */
	@Override
	public List<ApproverRemandExport> getListApproverRemand(String appID) {
		List<ApproverRemandExport> lstResult = new ArrayList<>();
		String companyID = AppContexts.user().companyId();
		//ドメインモデル「承認フェーズインスタンス」から最大の承認済フェーズを取得-(Lấy phase đã approve có order lớn nhất từ domain 「承認フェーズインスタンス」)
		List<ApprovalPhaseState> phaseMax = approvalRootStateRepository.findPhaseApprovalMax(appID);
		for (ApprovalPhaseState phase : phaseMax) {
			//アルゴリズム「1.承認フェーズ毎の承認者を取得する」 - RequestList No.483
			List<String> lstApprover = judgmentApprovalStatusService.getApproverFromPhase(phase);
			for (String approver : lstApprover) {
				lstResult.add(new ApproverRemandExport(phase.getPhaseOrder(), approver, false));
			}
			//アルゴリズム「承認代行情報の取得処理」を実行する-(3-1) (Thực hiện "xử lý lấy thông tin đại diện approval")
			AgentPubExport agent = this.getApprovalAgentInfor(companyID, lstApprover);
			//「承認者の代行情報リスト」に存在する承認者に代行を付加する-(thêm thằng đại diện vào list -承認者の代行情報リスト」)
			for (String agentId : agent.getListRepresenterSID()) {
				lstResult.add(new ApproverRemandExport(phase.getPhaseOrder(), agentId, true));
			}
		}
		return lstResult;
	}
	@Override
	public Boolean isApproveApprovalPhaseStateComplete(String companyID, String rootStateID, Integer phaseNumber) {
		Optional<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findByID(rootStateID, RootType.EMPLOYMENT_APPLICATION.value);
		if(!opApprovalRootState.isPresent()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = opApprovalRootState.get();
		Optional<ApprovalPhaseState> opCurrentPhase = approvalRootState.getListApprovalPhaseState().stream()
				.filter(x -> x.getPhaseOrder()==phaseNumber).findAny();
		if(!opCurrentPhase.isPresent()){
			return false;
		}
		return approveService.isApproveApprovalPhaseStateComplete(companyID, opCurrentPhase.get());
	}
}
