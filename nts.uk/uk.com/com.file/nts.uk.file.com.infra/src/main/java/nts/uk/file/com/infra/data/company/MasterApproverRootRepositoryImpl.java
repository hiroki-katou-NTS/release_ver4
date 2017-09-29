package nts.uk.file.com.infra.data.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.workflow.dom.adapter.workplace.WorkplaceImport;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApplicationType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhase;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhaseRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.Approver;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.EmploymentRootAtr;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.ApprovalForApplication;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.ApprovalRootCommonOutput;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.ApprovalRootMaster;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.CompanyApprovalInfor;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.EmployeeApproverOutput;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.MasterApproverRootOutput;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.PersonApproverOutput;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.WorkplaceApproverOutput;
import nts.uk.file.com.app.company.approval.MasterApproverRootRepository;
import nts.uk.shr.com.company.CompanyAdapter;
import nts.uk.shr.com.company.CompanyInfor;

@Stateless
public class MasterApproverRootRepositoryImpl implements MasterApproverRootRepository {

	@Inject
	private CompanyApprovalRootRepository comRootRepository;
	@Inject
	private WorkplaceApprovalRootRepository wpRootRepository;
	@Inject
	private PersonApprovalRootRepository psRootRepository;
	@Inject
	private CompanyAdapter comAdapter;
	@Inject
	private WorkplaceAdapter wpAdapter;
	@Inject
	private ApprovalPhaseRepository phaseRepository;

	private final String rootCommon = "共通ルート";

	@Override
	public MasterApproverRootOutput getMasterInfo(String companyID, GeneralDate baseDate, boolean isCompany,
			boolean isWorkplace, boolean isPerson) {
		// MasterApproverRootOutput masterInfor = null;
		CompanyApprovalInfor comMasterInfor = new CompanyApprovalInfor(null, null);
		Map<String, WorkplaceApproverOutput> mapWpRootInfor = new HashMap<>();
		Map<String, PersonApproverOutput> mapPsRootInfor = new HashMap<>();
		// 出力対象に会社別がある(có 会社別 trong đối tượng output)
		if (isCompany) {
			comMasterInfor = getComApprovalInfor(companyID, baseDate);
		}
		// 出力対象に職場別がある(có 職場別 trong đối tượng output)
		if (isWorkplace) {
			// ドメインモデル「職場別就業承認ルート」を取得する(lấy dữ liệu domain 「職場別就業承認ルート」)
			List<WorkplaceApprovalRoot> lstWps = wpRootRepository.findAllByBaseDate(companyID, baseDate);
			// データが１件以上取得した場合(có 1 data trở lên)
			if (!CollectionUtil.isEmpty(lstWps)) {
				mapWpRootInfor = getWpApproverInfor(lstWps, companyID, baseDate);
			}
		}
		// 出力対象に個人別がある(có 個人別 trong đối tượng output)
		if (isPerson) {
			// ドメインモデル「個人別就業承認ルート」を取得する(lấy dữ liệu domain「個人別就業承認ルート」)
			List<PersonApprovalRoot> lstPss = psRootRepository.findAllByBaseDate(companyID, baseDate);
			// データが１件以上取得した場合(có 1 data trở lên)
			if (!CollectionUtil.isEmpty(lstPss)) {
				mapPsRootInfor = getPsRootInfor(lstPss, companyID);
			}
		}
		MasterApproverRootOutput masterInfor = new MasterApproverRootOutput(comMasterInfor, mapWpRootInfor,
				mapPsRootInfor);
		return masterInfor;
	}

	/**
	 * get all approval of employee
	 * 
	 * @param lstPss
	 * @param companyID
	 * @return
	 */
	private Map<String, PersonApproverOutput> getPsRootInfor(List<PersonApprovalRoot> lstPss, String companyID) {
		Map<String, PersonApproverOutput> mapPsRootInfor = new HashMap<>();
		for (PersonApprovalRoot root : lstPss) {
			List<ApprovalForApplication> psWootInfor = new ArrayList<>();
			ApprovalRootCommonOutput psRoot = new ApprovalRootCommonOutput(root.getCompanyId(), root.getApprovalId(),
					root.getEmployeeId(), "", root.getHistoryId(), root.getApplicationType().value,
					root.getPeriod().getEndDate(), root.getPeriod().getEndDate(), root.getBranchId(),
					root.getAnyItemApplicationId(), root.getConfirmationRootType().value,
					root.getEmploymentRootAtr().value);
			// Neu da co person roi
			if (!mapPsRootInfor.isEmpty() && mapPsRootInfor.containsKey(root.getEmployeeId())) {
				PersonApproverOutput psApp = mapPsRootInfor.get(root.getEmployeeId());
				psWootInfor = psApp.getPsRootInfo();
				psWootInfor = getAppInfors(psRoot, psWootInfor, companyID);
				continue;
			}
			// ドメインモデル「社員」を取得する(lấy dữ liệu domain「社員」)
			// TODO Viet sau khi QA duoc tra loi
			EmployeeApproverOutput empInfor = new EmployeeApproverOutput("", "");

			psWootInfor = getAppInfors(psRoot, psWootInfor, companyID);
			PersonApproverOutput psOutput = new PersonApproverOutput(empInfor, psWootInfor);
			mapPsRootInfor.put(root.getEmployeeId(), psOutput);
		}

		// Traversing Map
		Set set = mapPsRootInfor.entrySet();// Converting to Set so that we can traverse
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			// Converting to Map.Entry so that we can get key and value separately
			Map.Entry entry = (Map.Entry) itr.next();
			System.out.println(entry.getKey() + " " + entry.getValue());
		}

		return mapPsRootInfor;
	}

	/**
	 * get all approval of workplace
	 * 
	 * @param lstWps
	 * @param companyID
	 * @param baseDate
	 * @return
	 */
	private Map<String, WorkplaceApproverOutput> getWpApproverInfor(List<WorkplaceApprovalRoot> lstWps,
			String companyID, GeneralDate baseDate) {
		Map<String, WorkplaceApproverOutput> mapWpRootInfor = new HashMap<>();
		for (WorkplaceApprovalRoot root : lstWps) {
			List<ApprovalForApplication> wpRootInfor = new ArrayList<>();
			ApprovalRootCommonOutput wpRoot = new ApprovalRootCommonOutput(root.getCompanyId(), root.getApprovalId(),
					"", root.getWorkplaceId(), root.getHistoryId(), root.getApplicationType().value,
					root.getPeriod().getEndDate(), root.getPeriod().getEndDate(), root.getBranchId(),
					root.getAnyItemApplicationId(), root.getConfirmationRootType().value,
					root.getEmploymentRootAtr().value);
			// Neu da co workplace roi
			if (!mapWpRootInfor.isEmpty() && mapWpRootInfor.containsKey(root.getWorkplaceId())) {
				WorkplaceApproverOutput wpApp = mapWpRootInfor.get(root.getWorkplaceId());
				wpRootInfor = wpApp.getWpRootInfor();
				wpRootInfor = getAppInfors(wpRoot, wpRootInfor, companyID);
				continue;
			}

			// ドメインモデル「職場」を取得する(lấy dữ liệu domain 「職場」) tra ra 1 list nhung thuc chat chi
			// co 1 du lieu
			WorkplaceImport wpInfors = wpAdapter.findByWkpId(root.getWorkplaceId(), baseDate).get();
			wpRootInfor = getAppInfors(wpRoot, wpRootInfor, companyID);
			WorkplaceApproverOutput wpOutput = new WorkplaceApproverOutput(wpInfors, wpRootInfor);
			mapWpRootInfor.put(root.getWorkplaceId(), wpOutput);
		}

		// Traversing Map
		Set set = mapWpRootInfor.entrySet();// Converting to Set so that we can traverse
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			// Converting to Map.Entry so that we can get key and value separately
			Map.Entry entry = (Map.Entry) itr.next();
			System.out.println(entry.getKey() + " " + entry.getValue());
		}

		return mapWpRootInfor;
	}

	private List<ApprovalForApplication> getAppInfors(ApprovalRootCommonOutput root,
			List<ApprovalForApplication> wpRootInfor, String companyID) {
		// ApprovalForApplication wpAppInfo = null;
		// neu la 就業ルート区分 la 共通(common)
		String appName = "";
		int appId = 0;
		if (root.getEmploymentRootAtr() == EmploymentRootAtr.COMMON.value) {
			appName = rootCommon;
		} else if (root.getEmploymentRootAtr() == EmploymentRootAtr.APPLICATION.value) {
			appId = root.getApplicationType() + 1;
			appName = EnumAdaptor.valueOf(root.getApplicationType(), ApplicationType.class).nameId;
		}
		List<ApprovalRootMaster> lstAppInfo = new ArrayList<>();
		// 承認フェーズ, 承認者
		lstAppInfo = getPhaseApprover(companyID, root.getBranchId());
		ApprovalForApplication wpAppInfo = new ApprovalForApplication(appId, appName, root.getStartDate(),
				root.getEndDate(), lstAppInfo);
		wpRootInfor.add(wpAppInfo);
		Collections.sort(wpRootInfor, Comparator.comparing(ApprovalForApplication::getAppType));
		return wpRootInfor;
	}

	/**
	 * get phase, approver of company
	 * 
	 * @param approvalForApplication
	 * @param comRoot
	 * @param companyID
	 * @return
	 */
	private ApprovalForApplication getApproval(ApprovalForApplication approvalForApplication,
			CompanyApprovalRoot comRoot, String companyID) {
		approvalForApplication.setStartDate(comRoot.getPeriod().getStartDate());
		approvalForApplication.setEndDate(comRoot.getPeriod().getEndDate());

		List<ApprovalRootMaster> lstMatter = new ArrayList<>();
		// 承認フェーズ, 承認者
		lstMatter = getPhaseApprover(companyID, comRoot.getBranchId());
		approvalForApplication.setLstApproval(lstMatter);
		return approvalForApplication;
	}

	private List<ApprovalRootMaster> getPhaseApprover(String companyID, String branchId) {
		List<ApprovalRootMaster> lstMatter = new ArrayList<>();
		// 承認フェーズ, 承認者
		List<ApprovalPhase> getAllIncludeApprovers = phaseRepository.getAllIncludeApprovers(companyID, branchId);
		for (ApprovalPhase phase : getAllIncludeApprovers) {
			List<String> lstApprovers = new ArrayList<>();
			for (Approver approver : phase.getApprovers()) {
				// TODO chuyen sang ten khi co request
				lstApprovers.add(approver.getEmployeeId());
			}
			ApprovalRootMaster appRoot = new ApprovalRootMaster(phase.getOrderNumber(), phase.getApprovalForm().name,
					lstApprovers);
			lstMatter.add(appRoot);
		}
		Collections.sort(lstMatter, Comparator.comparing(ApprovalRootMaster::getPhaseNumber));
		return lstMatter;
	}

	/**
	 * get all approval of company
	 * 
	 * @param companyID
	 * @param baseDate
	 * @return
	 */
	private CompanyApprovalInfor getComApprovalInfor(String companyID, GeneralDate baseDate) {
		// CompanyApprovalInfor comMasterInfor = null;
		// ドメインモデル「会社別就業承認ルート」を取得する(lấy thông tin domain 「会社別就業承認ルート」)
		List<CompanyApprovalRoot> lstComs = comRootRepository.findByBaseDateOfCommon(companyID, baseDate);
		Optional<CompanyInfor> comInfo = comAdapter.getCurrentCompany();
		List<ApprovalForApplication> comApproverRoot = new ArrayList<>();
		ApprovalForApplication approvalForApplication = new ApprovalForApplication(0, rootCommon, null, null, null);
		if (!lstComs.isEmpty()) {
			// lay du lieu voi truong hop 0: 共通(common)
			Optional<CompanyApprovalRoot> opComCommon = lstComs.stream()
					.filter(x -> x.getEmploymentRootAtr() == EmploymentRootAtr.COMMON).findAny();
			if (opComCommon.isPresent()) {
				approvalForApplication = getApproval(approvalForApplication, opComCommon.get(), companyID);
			}
		}

		for (int i = 0; i < 5; i++) {
			comApproverRoot.add(approvalForApplication);
		}

		// Lap de lay du lieu theo app type
		for (ApplicationType appType : ApplicationType.values()) {
			approvalForApplication = new ApprovalForApplication(appType.value + 1, appType.nameId, null, null, null);
			// 「会社別就業承認ルート」
			if (!lstComs.isEmpty()) {
				Optional<CompanyApprovalRoot> comApps = lstComs.stream().filter(x -> x.getApplicationType() == appType)
						.findAny();
				if (comApps.isPresent()) {
					approvalForApplication = getApproval(approvalForApplication, comApps.get(), companyID);
				}
			}
			// thong tin các application da duoc set approver
			for (int i = 0; i < 5; i++) {
				comApproverRoot.add(approvalForApplication);
			}

		}

		List<ApprovalForApplication> a = new ArrayList<>();
		for(int i = 0; i < 5 ; i++) {
		a.addAll(comApproverRoot);
		}
		
		Collections.sort(a, Comparator.comparing(ApprovalForApplication::getAppType));
		CompanyApprovalInfor comMasterInfor = new CompanyApprovalInfor(comInfo, a);

		return comMasterInfor;
	}

}
