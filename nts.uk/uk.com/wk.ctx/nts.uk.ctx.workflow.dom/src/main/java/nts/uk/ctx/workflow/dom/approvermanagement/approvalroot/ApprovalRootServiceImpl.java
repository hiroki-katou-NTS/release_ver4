package nts.uk.ctx.workflow.dom.approvermanagement.approvalroot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.adapter.bs.EmployeeAdapter;
import nts.uk.ctx.workflow.dom.agent.ApprovalAgencyInfoService;
import nts.uk.ctx.workflow.dom.agent.output.ApprovalAgencyInfoOutput;
import nts.uk.ctx.workflow.dom.approvermanagement.approvalroot.output.ApprovalPhaseOutput;
import nts.uk.ctx.workflow.dom.approvermanagement.approvalroot.output.ApprovalRootOutput;
import nts.uk.ctx.workflow.dom.approvermanagement.approvalroot.output.ApproverInfo;
import nts.uk.ctx.workflow.dom.approvermanagement.approvalroot.output.ErrorFlag;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.ApprovalSettingRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.PrincipalApprovalFlg;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalAtr;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhase;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhaseRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.Approver;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmPerson;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRootRepository;

/**
 * 1.社員の対象申請の承認ルートを取得する
 * 
 * @author vunv
 *
 */
@Stateless
public class ApprovalRootServiceImpl implements ApprovalRootService {

	@Inject
	private CompanyApprovalRootRepository comApprovalRootRepository;

	@Inject
	private PersonApprovalRootRepository perApprovalRootRepository;

	@Inject
	private WorkplaceApprovalRootRepository wkpApprovalRootRepository;

	@Inject
	private ApprovalPhaseRepository approvalPhaseRepository;

	@Inject
	private EmployeeAdapter employeeAdapter;

	@Inject
	private JobtitleToApproverService jobtitleToAppService;

	/**
	 * 3-1.承認代行情報の取得処理
	 */
	@Inject
	private ApprovalAgencyInfoService appAgencyInfoService;

	/** 承認設定 */
	@Inject
	private ApprovalSettingRepository approvalSettingRepository;

	/**
	 * 1.社員の対象申請の承認ルートを取得する
	 * 
	 * @param cid
	 *            会社ID
	 * @param sid
	 *            社員ID（申請本人の社員ID）
	 * @param employmentRootAtr
	 *            就業ルート区分
	 * @param subjectRequest
	 *            対象申請
	 * @param baseDate
	 *            基準日
	 */
	@Override
	public List<ApprovalRootOutput> getApprovalRootOfSubjectRequest(String cid, String sid, int employmentRootAtr,
			int appType, GeneralDate baseDate) {
		List<ApprovalRootOutput> result = new ArrayList<>();
		// get 個人別就業承認ルート from workflow
		List<PersonApprovalRoot> perAppRoots = this.perApprovalRootRepository.findByBaseDate(cid, sid, baseDate,
				appType);
		if (CollectionUtil.isEmpty(perAppRoots)) {
			// get 個人別就業承認ルート from workflow by other conditions
			List<PersonApprovalRoot> perAppRootsOfCommon = this.perApprovalRootRepository.findByBaseDateOfCommon(cid,
					sid, baseDate);
			if (CollectionUtil.isEmpty(perAppRootsOfCommon)) {
				// 所属職場を含む上位職場を取得
				List<String> wpkList = this.employeeAdapter.findWpkIdsBySid(cid, sid, baseDate);
				for (String wｋｐId : wpkList) {
					List<WorkplaceApprovalRoot> wkpAppRoots = this.wkpApprovalRootRepository.findByBaseDate(cid, wｋｐId,
							baseDate, appType);
					if (!CollectionUtil.isEmpty(wkpAppRoots)) {
						// 2.承認ルートを整理する
						result = wkpAppRoots.stream().map(x -> ApprovalRootOutput.convertFromWkpData(x))
								.collect(Collectors.toList());
						this.adjustmentData(cid, sid, baseDate, result);
						break;
					}

					List<WorkplaceApprovalRoot> wkpAppRootsOfCom = this.wkpApprovalRootRepository
							.findByBaseDateOfCommon(cid, wｋｐId, baseDate);
					if (!CollectionUtil.isEmpty(wkpAppRootsOfCom)) {
						// 2.承認ルートを整理する
						result = wkpAppRoots.stream().map(x -> ApprovalRootOutput.convertFromWkpData(x))
								.collect(Collectors.toList());
						this.adjustmentData(cid, sid, baseDate, result);
						break;
					}
				}

				// ドメインモデル「会社別就業承認ルート」を取得する
				List<CompanyApprovalRoot> comAppRoots = this.comApprovalRootRepository.findByBaseDate(cid, baseDate,
						appType);
				if (CollectionUtil.isEmpty(comAppRoots)) {
					List<CompanyApprovalRoot> companyAppRootsOfCom = this.comApprovalRootRepository
							.findByBaseDateOfCommon(cid, baseDate);
					if (!CollectionUtil.isEmpty(companyAppRootsOfCom)) {
						// 2.承認ルートを整理する
						result = companyAppRootsOfCom.stream().map(x -> ApprovalRootOutput.convertFromCompanyData(x))
								.collect(Collectors.toList());
						this.adjustmentData(cid, sid, baseDate, result);
					}
				} else {
					// 2.承認ルートを整理する
					result = comAppRoots.stream().map(x -> ApprovalRootOutput.convertFromCompanyData(x))
							.collect(Collectors.toList());
					this.adjustmentData(cid, sid, baseDate, result);
				}

			} else {
				// 2.承認ルートを整理する
				result = perAppRoots.stream().map(x -> ApprovalRootOutput.convertFromPersonData(x))
						.collect(Collectors.toList());
				this.adjustmentData(cid, sid, baseDate, result);
			}

		} else {
			// 2.承認ルートを整理する
			result = perAppRoots.stream().map(x -> ApprovalRootOutput.convertFromPersonData(x))
					.collect(Collectors.toList());
			this.adjustmentData(cid, sid, baseDate, result);
		}

		return result;
	}

	/**
	 * 2.承認ルートを整理する
	 * 
	 * @param cid
	 * @param sid
	 * @param baseDate
	 * @param branchIds
	 */
	@Override
	public List<ApprovalRootOutput> adjustmentData(String cid, String sid, GeneralDate baseDate,
			List<ApprovalRootOutput> appDatas) {
		appDatas.stream().forEach(x -> {
			List<ApprovalPhase> appPhase = this.approvalPhaseRepository.getAllIncludeApprovers(cid, x.getBranchId())
					.stream().filter(f -> f.getBrowsingPhase() == 0).collect(Collectors.toList());
			x.setBeforePhases(appPhase);
			List<ApprovalPhaseOutput> phases = this.adjustmentApprovalRootData(cid, sid, baseDate, appPhase);
			x.setAdjustedPhases(phases);
			// 7.承認ルートの異常チェック
			ErrorFlag errorFlag = ErrorFlag.NO_ERROR;
			if (CollectionUtil.isEmpty(appPhase)) {
				errorFlag = ErrorFlag.NO_APPROVER;
			} else {
				errorFlag = x.getAfterPhases().checkError(appPhase);
			}

			x.setErrorFlag(errorFlag);
		});
		return appDatas;
	}

	/**
	 * 2.承認ルートを整理する call this activity fo every branchId
	 */
	private List<ApprovalPhaseOutput> adjustmentApprovalRootData(String cid, String sid, GeneralDate baseDate,
			List<ApprovalPhase> appPhases) {
		List<ApprovalPhaseOutput> phaseResults = new ArrayList<>();

		for (ApprovalPhase phase : appPhases) {
			ApprovalPhaseOutput phaseResult = ApprovalPhaseOutput.convertToOutputData(phase);

			List<Approver> approvers = phase.getApprovers();
			if (CollectionUtil.isEmpty(approvers)) {
				break;
			}

			List<ApproverInfo> approversResult = new ArrayList<>();
			approvers.stream().forEach(x -> {
				// 個人の場合
				if (x.getApprovalAtr() == ApprovalAtr.PERSON) {
					approversResult.add(ApproverInfo.create(x, employeeAdapter.getEmployeeName(x.getEmployeeId())));
				} else {
					// 職位の場合
					List<ApproverInfo> approversOfJob = this.jobtitleToAppService.convertToApprover(cid, sid, baseDate,
							x.getJobTitleId());
					approversResult.addAll(approversOfJob);
				}
			});

			// 承認者IDリストに承認者がいるかチェックする
			if (CollectionUtil.isEmpty(approversResult)) {
				break;
			}

			List<String> approverIds = approversResult.stream().map(x -> x.getSid()).collect(Collectors.toList());
			// 3-1.承認代行情報の取得処理
			ApprovalAgencyInfoOutput agency = this.appAgencyInfoService.getApprovalAgencyInformation(cid, approverIds);
			// remove approvers with agency is PASS
			List<String> agencyAppIds = agency.getListApproverAndRepresenterSID().stream()
					.filter(x -> x.getRepresenter().equals("Pass")).map(x -> x.getApprover())
					.collect(Collectors.toList());
			approverIds.removeAll(agencyAppIds);

			// get 承認設定
			PrincipalApprovalFlg appSetting = this.approvalSettingRepository.getPrincipalByCompanyId(cid);
			if (!Objects.isNull(appSetting)) {
				if (appSetting == PrincipalApprovalFlg.NOT_PRINCIPAL) {
					// 申請本人社員IDを承認者IDリストから消す
					approverIds.remove(sid);
				}
			}

			// remove duplicate data
			phaseResult.addApproverList(removeDuplicateSid(approversResult.stream().filter(x -> {
				return approverIds.contains(x);
			}).collect(Collectors.toList())));

			// add to result
			phaseResults.add(phaseResult);
		}
		return phaseResults;
	}

	/**
	 * 承認者IDリストに重複の社員IDを消す(xóa ID của nhân viên bị trùng trong List ID người xác
	 * nhận)
	 * 
	 * @param approvers
	 *            承認者IDリスト
	 * @return ApproverInfos
	 */
	private List<ApproverInfo> removeDuplicateSid(List<ApproverInfo> approvers) {
		List<ApproverInfo> result = new ArrayList<>();

		Map<String, List<ApproverInfo>> approversBySid = approvers.stream()
				.collect(Collectors.groupingBy(x -> x.getSid()));
		for (Map.Entry<String, List<ApproverInfo>> info : approversBySid.entrySet()) {
			List<ApproverInfo> values = info.getValue();
			values.sort((a, b) -> Integer.compare(a.getOrderNumber(), b.getOrderNumber()));
			Optional<ApproverInfo> value = values.stream().filter(x -> x.getIsConfirmPerson()).findFirst();
			if (value.isPresent()) {
				result.add(value.get());
			} else {
				result.add(values.get(0));
			}
		}
		return result;
	}

}
