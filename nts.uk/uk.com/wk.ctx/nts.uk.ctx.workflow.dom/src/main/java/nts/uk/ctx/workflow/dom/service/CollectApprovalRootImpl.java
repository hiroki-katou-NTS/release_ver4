package nts.uk.ctx.workflow.dom.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import nts.uk.ctx.workflow.dom.adapter.bs.EmployeeAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.SyJobTitleAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.ConcurrentEmployeeImport;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.JobTitleImport;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.SimpleJobTitleImport;
import nts.uk.ctx.workflow.dom.adapter.workplace.WorkplaceApproverAdapter;
import nts.uk.ctx.workflow.dom.adapter.workplace.WorkplaceImport;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.ApprovalSettingRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.JobAssignSetting;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.JobAssignSettingRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.PrincipalApprovalFlg;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApplicationType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalAtr;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhase;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhaseRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.Approver;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmPerson;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.EmploymentRootAtr;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.JobtitleSearchSetRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApproverState;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRepresenterOutput;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRootContentOutput;
import nts.uk.ctx.workflow.dom.service.output.ApproverInfo;
import nts.uk.ctx.workflow.dom.service.output.ErrorFlag;
import nts.uk.ctx.workflow.dom.service.output.RepresenterInforOutput;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class CollectApprovalRootImpl implements CollectApprovalRootService {
	
	@Inject
	private PersonApprovalRootRepository perApprovalRootRepository;
	
	@Inject
	private EmployeeAdapter employeeAdapter;
	
	@Inject
	private JobAssignSettingRepository jobAssignSetRepository;
	
	@Inject
	private WorkplaceApproverAdapter wkApproverAdapter;
	
	@Inject
	private JobtitleSearchSetRepository jobtitleSearchSetRepository;
	
	@Inject
	private SyJobTitleAdapter syJobTitleAdapter;
	
	@Inject
	private WorkplaceApprovalRootRepository wkpApprovalRootRepository;
	
	@Inject
	private CompanyApprovalRootRepository comApprovalRootRepository;
	
	@Inject
	private ApprovalPhaseRepository approvalPhaseRepository;
	
	@Inject
	private CollectApprovalAgentInforService collectApprovalAgentInforService;
	
	@Inject
	private ApprovalSettingRepository approvalSettingRepository;
	
	@Override
	public ApprovalRootContentOutput getApprovalRootOfSubjectRequest(String companyID, String employeeID, 
			EmploymentRootAtr rootAtr, ApplicationType appType, GeneralDate standardDate) {
		// ドメインモデル「個人別就業承認ルート」を取得する(lấy thông tin domain「個人別就業承認ルート」)
		// ①個人別の該当申請の承認ルートを取得、なかったら②へ
		Optional<PersonApprovalRoot> opPerAppRoot = perApprovalRootRepository.findByBaseDate(companyID, employeeID, standardDate, appType, rootAtr);
		if(opPerAppRoot.isPresent()){
			List<ApprovalPhase> listApprovalPhaseBefore = approvalPhaseRepository.getAllIncludeApprovers(companyID, opPerAppRoot.get().getBranchId());
			List<ApprovalPhase> listApprovalPhaseAfter = this.adjustmentData(companyID, employeeID, standardDate, listApprovalPhaseBefore);
			ErrorFlag errorFlag = this.checkApprovalRoot(listApprovalPhaseBefore, listApprovalPhaseAfter);
			ApprovalRootState approvalRootState = this.createFromApprovalPhaseList(listApprovalPhaseAfter, 
					opPerAppRoot.get().getEmploymentAppHistoryItems().get(0).getHistoryId());
			return new ApprovalRootContentOutput(approvalRootState, errorFlag);
		}
		
		// ドメインモデル「個人別就業承認ルート」を取得する(lấy thông tin domain 「個人別就業承認ルート」)
		// ②個人別の共通の承認ルートを取得、なかったら③へ
		Optional<PersonApprovalRoot> opPerAppRootsOfCommon = perApprovalRootRepository.findByBaseDateOfCommon(companyID, employeeID, standardDate);
		if(opPerAppRootsOfCommon.isPresent()){
			List<ApprovalPhase> listApprovalPhaseBefore = approvalPhaseRepository.getAllIncludeApprovers(companyID, opPerAppRootsOfCommon.get().getBranchId());
			List<ApprovalPhase> listApprovalPhaseAfter = this.adjustmentData(companyID, employeeID, standardDate, listApprovalPhaseBefore);
			ErrorFlag errorFlag = this.checkApprovalRoot(listApprovalPhaseBefore, listApprovalPhaseAfter);
			ApprovalRootState approvalRootState = this.createFromApprovalPhaseList(listApprovalPhaseAfter, 
					opPerAppRootsOfCommon.get().getEmploymentAppHistoryItems().get(0).getHistoryId());
			return new ApprovalRootContentOutput(approvalRootState, errorFlag);
		}
		
		List<String> wpkList = employeeAdapter.findWpkIdsBySid(companyID, employeeID, standardDate);
		for (String wｋｐId : wpkList) {
			Optional<WorkplaceApprovalRoot> opWkpAppRoot = wkpApprovalRootRepository.findByBaseDate(companyID, wｋｐId, standardDate, appType, rootAtr);
			if(opWkpAppRoot.isPresent()){
				List<ApprovalPhase> listApprovalPhaseBefore = approvalPhaseRepository.getAllIncludeApprovers(companyID, opWkpAppRoot.get().getBranchId());
				List<ApprovalPhase> listApprovalPhaseAfter = this.adjustmentData(companyID, employeeID, standardDate, listApprovalPhaseBefore);
				ErrorFlag errorFlag = this.checkApprovalRoot(listApprovalPhaseBefore, listApprovalPhaseAfter);
				ApprovalRootState approvalRootState = this.createFromApprovalPhaseList(listApprovalPhaseAfter,
						opWkpAppRoot.get().getEmploymentAppHistoryItems().get(0).getHistoryId());
				return new ApprovalRootContentOutput(approvalRootState, errorFlag);
			}
			Optional<WorkplaceApprovalRoot> opWkpAppRootsOfCom = wkpApprovalRootRepository.findByBaseDateOfCommon(companyID, wｋｐId, standardDate);
			if(opWkpAppRootsOfCom.isPresent()){
				List<ApprovalPhase> listApprovalPhaseBefore = approvalPhaseRepository.getAllIncludeApprovers(companyID, opWkpAppRootsOfCom.get().getBranchId());
				List<ApprovalPhase> listApprovalPhaseAfter = this.adjustmentData(companyID, employeeID, standardDate, listApprovalPhaseBefore);
				ErrorFlag errorFlag = this.checkApprovalRoot(listApprovalPhaseBefore, listApprovalPhaseAfter);
				ApprovalRootState approvalRootState = this.createFromApprovalPhaseList(listApprovalPhaseAfter,
						opWkpAppRootsOfCom.get().getEmploymentAppHistoryItems().get(0).getHistoryId());
				return new ApprovalRootContentOutput(approvalRootState, errorFlag);
			}
		}
		
		Optional<CompanyApprovalRoot> opComAppRoot = comApprovalRootRepository.findByBaseDate(companyID, standardDate, appType, rootAtr);
		if(opComAppRoot.isPresent()){
			List<ApprovalPhase> listApprovalPhaseBefore = approvalPhaseRepository.getAllIncludeApprovers(companyID, opComAppRoot.get().getBranchId());
			List<ApprovalPhase> listApprovalPhaseAfter = this.adjustmentData(companyID, employeeID, standardDate, listApprovalPhaseBefore);
			ErrorFlag errorFlag = this.checkApprovalRoot(listApprovalPhaseBefore, listApprovalPhaseAfter);
			ApprovalRootState approvalRootState = this.createFromApprovalPhaseList(listApprovalPhaseAfter,
					opComAppRoot.get().getEmploymentAppHistoryItems().get(0).getHistoryId());
			return new ApprovalRootContentOutput(approvalRootState, errorFlag);
		}
		
		Optional<CompanyApprovalRoot> opCompanyAppRootsOfCom = comApprovalRootRepository.findByBaseDateOfCommon(companyID, standardDate);
		if(opCompanyAppRootsOfCom.isPresent()){
			List<ApprovalPhase> listApprovalPhaseBefore = approvalPhaseRepository.getAllIncludeApprovers(companyID, opCompanyAppRootsOfCom.get().getBranchId());
			List<ApprovalPhase> listApprovalPhaseAfter = this.adjustmentData(companyID, employeeID, standardDate, listApprovalPhaseBefore);
			ErrorFlag errorFlag = this.checkApprovalRoot(listApprovalPhaseBefore, listApprovalPhaseAfter);
			ApprovalRootState approvalRootState = this.createFromApprovalPhaseList(listApprovalPhaseAfter,
					opCompanyAppRootsOfCom.get().getEmploymentAppHistoryItems().get(0).getHistoryId());
			return new ApprovalRootContentOutput(approvalRootState, errorFlag);
		}
		return new ApprovalRootContentOutput(ApprovalRootState.builder().listApprovalPhaseState(Collections.emptyList()).build(), ErrorFlag.NO_APPROVER);
	}
	
	@Override
	public List<ApprovalPhase> adjustmentData(String companyID, String employeeID, GeneralDate baseDate,  List<ApprovalPhase> listApprovalPhase) {
		listApprovalPhase.stream().forEach(approvalPhase -> {
			if(CollectionUtil.isEmpty(approvalPhase.getApprovers())){
				return;
			}

			// ドメインモデル「承認フェーズ」．承認形態をメモリ上保持する(luu thong tin 「承認フェーズ」．承認形態 vao cache) ??
			
			List<Approver> listApprover = new ArrayList<>();
			approvalPhase.getApprovers().sort(Comparator.comparing(Approver::getOrderNumber));
			approvalPhase.getApprovers().forEach(approver -> {
				
				// 承認者IDリストをクリアする（初期化）(clear thong tin cua list ID nguoi xac nhan)
				
				if(approver.getApprovalAtr().equals(ApprovalAtr.PERSON)){
					if(listApprover.stream().filter(x -> x.getEmployeeId().equals(approver.getEmployeeId())).findAny().isPresent()){
						return;
					}
					listApprover.add(approver);
					return;
				}
				List<Approver> listApproverJob = this.convertPositionToApprover(companyID, employeeID, baseDate, approver.getJobTitleId())
								.stream().map(x -> new Approver(
										approver.getCompanyId(), 
										approver.getBranchId(), 
										approver.getApprovalPhaseId(), 
										approver.getApproverId(), 
										x.getJobId(), 
										x.getSid(), 
										approver.getOrderNumber(), 
										approver.getApprovalAtr(), 
										approver.getConfirmPerson()))
								.collect(Collectors.toList());
				if(CollectionUtil.isEmpty(listApproverJob)){
					return;
				}
				listApproverJob.forEach(x -> {
					if(listApprover.stream().filter(y -> y.getEmployeeId().equals(x.getEmployeeId())).findAny().isPresent()){
						return;
					}
					listApprover.add(x);
				});
			});
			approvalPhase.getApprovers().clear();
			if(CollectionUtil.isEmpty(listApprover)){
				return;
			}
			List<Approver> listRemoveApprover = new ArrayList<>();
			List<String> listApproverID = listApprover.stream().map(x -> x.getEmployeeId()).collect(Collectors.toList());
			ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, listApproverID);
			listApprover.stream().forEach(x -> {
				approvalRepresenterOutput.getListApprovalAgentInfor().stream().filter(y -> y.getApprover().equals(x.getEmployeeId())).findAny().ifPresent(z -> {
					if(z.getRepresenter().getValue().equals(RepresenterInforOutput.Path_Information)){
						listRemoveApprover.add(x);
					}
				});;
			});
			listApprover.removeAll(listRemoveApprover);
			PrincipalApprovalFlg principalApprovalFlg = approvalSettingRepository.getPrincipalByCompanyId(companyID).orElse(PrincipalApprovalFlg.NOT_PRINCIPAL);
			if(principalApprovalFlg.equals(PrincipalApprovalFlg.NOT_PRINCIPAL)){
				List<Approver> listDeleteApprover = listApprover.stream().filter(x -> x.getEmployeeId().equals(employeeID)).collect(Collectors.toList());
				listApprover.removeAll(listDeleteApprover);
			}
			approvalPhase.addApproverList(listApprover);
		});
		return listApprovalPhase.stream().filter(x -> !CollectionUtil.isEmpty(x.getApprovers())).collect(Collectors.toList());
	}
	
	@Override
	public List<ApproverInfo> convertPositionToApprover(String companyID, String employeeID, GeneralDate baseDate, String jobTitleId) {
		// 共通アルゴリズム「申請者の職位の序列は承認者のと比較する」を実行する
		Boolean isApper = compareHierarchyTargetPerson(companyID, employeeID, jobTitleId, baseDate);
		if(isApper.equals(Boolean.FALSE)){
			return Collections.emptyList();
		}
		WorkplaceImport wkpInfor = wkApproverAdapter.findBySid(employeeID, baseDate);
		// thực hiện xử lý 「職場に指定する職位の対象者を取得する」
		List<ApproverInfo> approvers = this.getPersonByWorkplacePosition(companyID, wkpInfor.getWkpId(), baseDate, jobTitleId);
		if (!CollectionUtil.isEmpty(approvers)) {
			return approvers;
		}

		// lấy domain 「職位別のサーチ設定」
		Boolean needsSearch = this.jobtitleSearchSetRepository.finById(companyID, jobTitleId)
					.map(s -> s.needsSearch())
						.orElse(false);
		if (needsSearch.equals(Boolean.FALSE)) {
			return Collections.emptyList();
		}
		List<String> wkpIds = this.employeeAdapter.findWpkIdsBySid(companyID, employeeID, baseDate);
		wkpIds.remove(0);

		// 上位職場が存在する(not exist wkpId 上位)
		if (CollectionUtil.isEmpty(wkpIds)) {
			return Collections.emptyList();
		}

		// 上位職場の先頭から最後ループ
		for (String id : wkpIds) {
			// thực hiện xử lý 「職場に指定する職位の対象者を取得する」
			List<ApproverInfo> approversByWkp = this.getPersonByWorkplacePosition(companyID, id, baseDate, jobTitleId);
			// If exist break and return
			if (!CollectionUtil.isEmpty(approversByWkp)) {
				return approversByWkp;
			}
		}
		return Collections.emptyList();
	}
	
	@Override
	public Boolean compareHierarchyTargetPerson(String companyID, String targetPersonID, String positionID,
			GeneralDate date) {
		JobTitleImport jobOfEmp = syJobTitleAdapter.findJobTitleBySid(targetPersonID, date);
		SimpleJobTitleImport approverInfo = null;
		SimpleJobTitleImport requestInfo = null;
		// 承認者の
		List<SimpleJobTitleImport> approverInfoList = syJobTitleAdapter.findByIds(companyID, Arrays.asList(positionID), date);
		if(!CollectionUtil.isEmpty(approverInfoList)){
			approverInfo = approverInfoList.get(0);
		}		
		if (approverInfo != null) {
			// 申請の
			List<SimpleJobTitleImport> requestInfoList = syJobTitleAdapter
					.findByIds(companyID, Arrays.asList(jobOfEmp.getPositionId()), date);
			if(!CollectionUtil.isEmpty(requestInfoList)){
				requestInfo = requestInfoList.get(0);
			}
			if (requestInfo == null) {
				return false;
			}
			if (requestInfo.getDisporder() > approverInfo.getDisporder()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<ApproverInfo> getPersonByWorkplacePosition(String cid, String wkpId, GeneralDate baseDate, String jobTitleId) {
		// 承認者の
		List<ConcurrentEmployeeImport> employeeList = employeeAdapter.getConcurrentEmployee(cid, jobTitleId,
				baseDate);
		Optional<JobAssignSetting> assignSet = jobAssignSetRepository.findById();
		if (assignSet.get().getIsConcurrently()) {
			// 本務兼務区分が兼務の対象者を除く
			employeeList.removeIf(x -> x.isConcurrent());
		}
		
		List<ApproverInfo> approvers = new ArrayList<>();
		for (ConcurrentEmployeeImport emp : employeeList) {
			WorkplaceImport wkpIdOfEmp = wkApproverAdapter.findBySid(emp.getEmployeeId(), baseDate);
			if (wkpId.equals(wkpIdOfEmp.getWkpId())) {
				// truyền tạm approvalAtr = 1
				approvers.add(ApproverInfo.create(emp));
			}
		}
		return approvers;
	}
	
	@Override
	public ErrorFlag checkApprovalRoot(List<ApprovalPhase> listApprovalPhaseBefore, List<ApprovalPhase> listApprovalPhaseAfter) {
		if(CollectionUtil.isEmpty(listApprovalPhaseBefore)|CollectionUtil.isEmpty(listApprovalPhaseAfter)){
			return ErrorFlag.NO_APPROVER;
		}
		for(int i = 0; i < listApprovalPhaseBefore.size(); i++){
			ApprovalPhase approvalPhaseBefore = listApprovalPhaseBefore.get(i);
			ApprovalPhase approvalPhaseAfter = listApprovalPhaseBefore.get(i);
			if(CollectionUtil.isEmpty(approvalPhaseBefore.getApprovers())){
				continue;
			}
			if(approvalPhaseAfter.getApprovers().size() > 10){
				return ErrorFlag.APPROVER_UP_10;
			}
			if(approvalPhaseAfter.getApprovers().size() < 0){
				return ErrorFlag.NO_APPROVER;
			}
			if(approvalPhaseBefore.getApprovalForm().equals(ApprovalForm.EVERYONE_APPROVED)){
				continue;
			}
			for(Approver approver : approvalPhaseBefore.getApprovers()){
				if(approver.getConfirmPerson().equals(ConfirmPerson.NOT_CONFIRM)){
					continue;
				}
				if(CollectionUtil.isEmpty(approvalPhaseAfter.getApprovers())){
					return ErrorFlag.NO_CONFIRM_PERSON;
				}
			}
		}
		return ErrorFlag.NO_ERROR;
	}
	
	@Override
	public void getApprovalRootBy36AppEmployee(String companyID, String employeeID, GeneralDate date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> organizeBrowsingPhase(String companyID, String employeeID, GeneralDate date, ApprovalPhase approvalPhase) {
		List<String> viewList = new ArrayList<>();
		if(approvalPhase.getBrowsingPhase()==0){
			return viewList;
		}
		approvalPhase.getApprovers().forEach(approver -> {
			if(approver.getApprovalAtr().equals(ApprovalAtr.PERSON)){
				viewList.add(approver.getEmployeeId());
			} else {
				List<String> approvers = this.convertPositionToApprover(companyID, employeeID, date, approver.getJobTitleId())
											.stream().map(x -> x.getSid()).collect(Collectors.toList());
				viewList.addAll(approvers);
			}
		});
		return viewList.stream().distinct().collect(Collectors.toList());
	}

	private ApprovalRootState createFromApprovalPhaseList(List<ApprovalPhase> listApprovalPhase, String histotyID) {
		List<ApprovalPhaseState> listApprovalPhaseState = listApprovalPhase.stream().map(approvalPhase -> {
			List<ApprovalFrame> resultApprovalFrame = new ArrayList<>();
			List<ApprovalFrame> listApprovalFrameByPerson = approvalPhase.getApprovers().stream()
			.filter(approver -> Strings.isBlank(approver.getJobTitleId()))
			.map(approver -> {
				List<ApproverState> listApproverState = new ArrayList<>();
				listApproverState.add(new ApproverState(null, approvalPhase.getOrderNumber(), approver.getOrderNumber()+1, approver.getEmployeeId()));
				return ApprovalFrame.firstCreate(null, approvalPhase.getOrderNumber(), approver.getOrderNumber()+1, approver.getConfirmPerson(), listApproverState);
			}).collect(Collectors.toList());
			resultApprovalFrame.addAll(listApprovalFrameByPerson);
			
			List<Approver> allListApproverByJob = approvalPhase.getApprovers().stream()
			.filter(approver -> !Strings.isBlank(approver.getJobTitleId()))
			.collect(Collectors.toList());
			Map<String, List<Approver>> findMap = allListApproverByJob.stream()
					.collect(Collectors.groupingBy(Approver::getJobTitleId));
			findMap.forEach((k,v) -> {
				List<ApproverState> listApproverStateByJob = v.stream()
						.map(approver -> new ApproverState(null, approvalPhase.getOrderNumber(), approver.getOrderNumber()+1, approver.getEmployeeId()))
						.collect(Collectors.toList()); 
						if(!CollectionUtil.isEmpty(listApproverStateByJob)){
							ApprovalFrame approvalFrameByJob = 
									ApprovalFrame.firstCreate(
											null, 
											approvalPhase.getOrderNumber(), 
											listApproverStateByJob.get(0).getFrameOrder(), 
											v.get(0).getConfirmPerson(), 
											listApproverStateByJob);
							
							resultApprovalFrame.add(approvalFrameByJob);
						}
			});
			resultApprovalFrame.sort((a,b)-> a.getFrameOrder().compareTo(b.getFrameOrder()));
			ApprovalPhaseState approvalPhaseState = new ApprovalPhaseState(
					null, 
					approvalPhase.getOrderNumber(), 
					ApprovalBehaviorAtr.UNAPPROVED, 
					EnumAdaptor.valueOf(approvalPhase.getApprovalForm().value, ApprovalForm.class), 
					resultApprovalFrame);
			return approvalPhaseState;
		}).sorted(Comparator.comparing(ApprovalPhaseState::getPhaseOrder))
				.collect(Collectors.toList());
		return ApprovalRootState.builder()
				.historyID(histotyID)
				.listApprovalPhaseState(listApprovalPhaseState)
				.build();
	}
}
