package nts.uk.ctx.sys.auth.pubimp.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.auth.dom.adapter.employee.JobTitleAdapter;
import nts.uk.ctx.sys.auth.dom.adapter.employee.employeeinfo.EmpInfoImport;
import nts.uk.ctx.sys.auth.dom.adapter.employee.employeeinfo.EmployeeInfoAdapter;
import nts.uk.ctx.sys.auth.dom.adapter.person.EmployeeBasicInforAuthImport;
import nts.uk.ctx.sys.auth.dom.adapter.person.PersonAdapter;
import nts.uk.ctx.sys.auth.dom.adapter.workplace.AffWorkplaceHistImport;
import nts.uk.ctx.sys.auth.dom.adapter.workplace.AffiliationWorkplace;
import nts.uk.ctx.sys.auth.dom.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.sys.auth.dom.algorithm.AcquireListWorkplaceByEmpIDService;
import nts.uk.ctx.sys.auth.dom.algorithm.AcquireUserIDFromEmpIDService;
import nts.uk.ctx.sys.auth.dom.algorithm.CanApprovalOnBaseDateService;
import nts.uk.ctx.sys.auth.dom.algorithm.DetermineEmpCanReferService;
import nts.uk.ctx.sys.auth.dom.algorithm.EmpReferenceRangeService;
import nts.uk.ctx.sys.auth.dom.employee.dto.JobTitleValueImport;
import nts.uk.ctx.sys.auth.dom.grant.roleindividual.RoleIndividualGrant;
import nts.uk.ctx.sys.auth.dom.grant.roleindividual.RoleIndividualGrantRepository;
import nts.uk.ctx.sys.auth.dom.grant.rolesetjob.RoleSetGrantedJobTitleRepository;
import nts.uk.ctx.sys.auth.dom.grant.rolesetperson.RoleSetGrantedPerson;
import nts.uk.ctx.sys.auth.dom.grant.rolesetperson.RoleSetGrantedPersonRepository;
import nts.uk.ctx.sys.auth.dom.grant.service.RoleIndividualService;
import nts.uk.ctx.sys.auth.dom.role.EmployeeReferenceRange;
import nts.uk.ctx.sys.auth.dom.role.Role;
import nts.uk.ctx.sys.auth.dom.role.RoleType;
import nts.uk.ctx.sys.auth.dom.roleset.RoleSet;
import nts.uk.ctx.sys.auth.dom.roleset.RoleSetRepository;
import nts.uk.ctx.sys.auth.dom.user.User;
import nts.uk.ctx.sys.auth.dom.user.UserRepository;
import nts.uk.ctx.sys.auth.dom.wkpmanager.EmpInfoAdapter;
import nts.uk.ctx.sys.auth.dom.wkpmanager.WorkplaceManager;
import nts.uk.ctx.sys.auth.dom.wkpmanager.WorkplaceManagerRepository;
import nts.uk.ctx.sys.auth.dom.wplmanagementauthority.WorkPlaceAuthority;
import nts.uk.ctx.sys.auth.dom.wplmanagementauthority.WorkPlaceAuthorityRepository;
import nts.uk.ctx.sys.auth.pub.employee.EmpWithRangeLogin;
import nts.uk.ctx.sys.auth.pub.employee.EmployeePublisher;
import nts.uk.ctx.sys.auth.pub.employee.NarrowEmpByReferenceRange;
import nts.uk.ctx.sys.auth.pub.role.RoleExportRepo;
import nts.uk.ctx.sys.auth.pub.user.UserExport;
import nts.uk.ctx.sys.auth.pub.user.UserPublisher;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class EmployeePublisherImpl implements EmployeePublisher {

	@Inject
	private PersonAdapter personAdapter;

	@Inject
	private UserPublisher userPublisher;

	@Inject
	private EmpReferenceRangeService empReferenceRangeService;

	@Inject
	private WorkplaceManagerRepository workplaceManagerRepository;

	@Inject
	private WorkplaceAdapter workplaceAdapter;

	@Inject
	private EmployeeInfoAdapter employeeInfoAdapter;

	@Inject
	private DetermineEmpCanReferService determineEmpCanRefer;

	@Inject
	private CanApprovalOnBaseDateService canApprovalOnBaseDateService;

	@Inject
	private AcquireListWorkplaceByEmpIDService acquireListWorkplace;
	
	@Inject
	private AcquireUserIDFromEmpIDService acquireUserIDFromEmpIDService;
	
	@Inject
	private RoleIndividualService roleIndividualService;

	@Inject
	private RoleExportRepo roleExportRepo;
	
	@Inject
	private WorkPlaceAuthorityRepository workPlaceAuthorityRepository;
	
	@Inject
	private RoleSetRepository roleSetRepository;
	
	@Inject
	private RoleSetGrantedJobTitleRepository roleSetGrantedJobTitleRepository;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private RoleIndividualGrantRepository roleIndividualGrantRepository;
	
	@Inject
	private RoleSetGrantedPersonRepository roleSetGrantedPersonRepository;
	
	@Inject
	private JobTitleAdapter jobTitleAdapter;
	
	@Inject
	private EmpInfoAdapter empInfoAdapter;
	
	@Override
	public Optional<NarrowEmpByReferenceRange> findByEmpId(List<String> sID, int roleType) {
		// imported（権限管理）「社員」を取得する Request No1
		// employeeID = employeeID login
		String employeeIDLogin = AppContexts.user().employeeId();
		List<String> result = new ArrayList<>();
		Optional<EmployeeBasicInforAuthImport> employeeImport = personAdapter.getPersonInfor(employeeIDLogin);
		// List<String> listEmployeeID = listEmployeeImport.stream().map(c ->
		// c.getEmployeeId()).collect(Collectors.toList());
		if (!employeeImport.isPresent()) {
			return Optional.empty();
		} else {
			// アルゴリズム「紐付け先個人IDからユーザを取得する」を実行する
			// Execute algorithm "Acquire user from tied personal ID"
			Optional<UserExport> useExport = userPublisher.getUserByAssociateId(employeeImport.get().getPid());
			if (!useExport.isPresent()) {
				return Optional.empty();
			} else {
				Optional<Role> role = empReferenceRangeService.getByUserIDAndReferenceDate(useExport.get().getUserID(), roleType, GeneralDate.today());
				if (!role.isPresent()) {
					if (sID.contains(employeeIDLogin)) {
						result.add(employeeIDLogin);
					}
					return Optional.of(new NarrowEmpByReferenceRange(result));
				}

				EmployeeReferenceRange referenceRange = role.get().getEmployeeReferenceRange();
				if (referenceRange == EmployeeReferenceRange.ALL_EMPLOYEE) {
					return Optional.of(new NarrowEmpByReferenceRange(sID));
				} else if (referenceRange == EmployeeReferenceRange.ONLY_MYSELF) {
					if (sID.contains(employeeIDLogin)) {
						result.add(employeeIDLogin);
					}
					return Optional.of(new NarrowEmpByReferenceRange(result));
				} else {
					// ドメインモデル「職場管理者」をすべて取得する
					// (Lấy all domain 「職場管理者」)
					List<WorkplaceManager> listWorkplaceManager = workplaceManagerRepository.findListWkpManagerByEmpIdAndBaseDate(employeeIDLogin, GeneralDate.today());
					List<String> listWorkPlaceID2 = listWorkplaceManager.stream().map(c -> c.getWorkplaceId()).collect(Collectors.toList());
					// imported（権限管理）「所属職場履歴」を取得する
					// (Lấy imported（権限管理）「所属職場履歴」) Lay RequestList No.30
					Optional<AffWorkplaceHistImport> workPlace = workplaceAdapter.findWkpByBaseDateAndEmployeeId(GeneralDate.today(), employeeIDLogin);
					String workPlaceID1 = workPlace.get().getWorkplaceId();
					List<String> listWorkPlaceID3 = new ArrayList<>();
					if (referenceRange == EmployeeReferenceRange.DEPARTMENT_AND_CHILD) {
						// 配下の職場をすべて取得する
						// Lay RequestList No.154
						listWorkPlaceID3 = workplaceAdapter.findListWorkplaceIdByCidAndWkpIdAndBaseDate(AppContexts.user().companyId(), workPlaceID1, GeneralDate.today());
					}
					// 社員ID（List）と基準日から所属職場IDを取得 Lay request 227
					List<AffiliationWorkplace> lisAfiliationWorkplace = workplaceAdapter.findByListEmpIDAndDate(sID, GeneralDate.today());
					// 取得した所属職場履歴項目（List）を参照可能職場ID（List）で絞り込む
					List<String> listtWorkID = new ArrayList<>();
					listtWorkID.add(workPlaceID1);
					listtWorkID.addAll(listWorkPlaceID2);
					listtWorkID.addAll(listWorkPlaceID3);
					// 取得した所属職場履歴項目（List）を参照可能職場ID（List）で絞り込む
					result = lisAfiliationWorkplace.stream().filter(c -> listtWorkID.contains(c.getWorkplaceId())).map(x -> x.getEmployeeId()).collect(Collectors.toList());
					
				}
			}

		}
		return Optional.of(new NarrowEmpByReferenceRange(result));
	}

	@Override
	public Optional<EmpWithRangeLogin> findByCompanyIDAndEmpCD(String companyID, String employeeCD) {
		// imported（権限管理）「社員」を取得する
		Optional<EmpInfoImport> empInfor = employeeInfoAdapter.getByComnyIDAndEmployeeCD(companyID, employeeCD);
		if (empInfor.isPresent()) {
			// 参照可能な社員かを判定する（職場）
			boolean result = determineEmpCanRefer.checkDetermineEmpCanRefer(GeneralDate.today(), empInfor.get().getEmployeeId(), RoleType.EMPLOYMENT.value);
			if (result == true) {
				return Optional.of((new EmpWithRangeLogin(empInfor.get().getPerName(), empInfor.get().getCompanyId(), empInfor.get().getPersonId(), empInfor.get().getEmployeeCode(), empInfor.get().getEmployeeId())));
			} else
				return Optional.empty();
		}
		return Optional.empty();
	}

	@Override
	public Optional<EmpWithRangeLogin> getByComIDAndEmpCD(String companyID, String employeeCD, GeneralDate referenceDate) {
		// imported（権限管理）「社員」を取得する Lấy request List No.18
		Optional<EmpInfoImport> empInfor = employeeInfoAdapter.getByComnyIDAndEmployeeCD(companyID, employeeCD);
		if (empInfor.isPresent()) {
			// 指定社員が基準日に承認権限を持っているかチェックする Lay request 305 tu domain service
			boolean result = canApprovalOnBaseDateService.canApprovalOnBaseDate(empInfor.get().getCompanyId(), empInfor.get().getEmployeeId(), referenceDate);
			if (result == true) {
				return Optional.of((new EmpWithRangeLogin(empInfor.get().getPerName(), empInfor.get().getCompanyId(), empInfor.get().getPersonId(), empInfor.get().getEmployeeCode(), empInfor.get().getEmployeeId())));
			} else
				return Optional.empty();
		}
		return Optional.empty();
	}

	@Override
	public List<String> getListWorkPlaceID(String employeeID, GeneralDate referenceDate) {
		// 社員IDからユーザIDを取得する
		// (Lấy userID từ employeeID)
		Optional<String> userID = acquireUserIDFromEmpIDService.getUserIDByEmpID(employeeID);
		if (!userID.isPresent()) {
			return new ArrayList<>();
		} else {
			// ユーザIDからロールを取得する
			// (lấy role từ userID)
			String roleID = roleIndividualService.getRoleFromUserId(userID.get(), RoleType.EMPLOYMENT.value, referenceDate);
			// 社員参照範囲を取得する
			// (Lấy phạm vi tham chiếu employee)
			OptionalInt optEmpRange = roleExportRepo.findEmpRangeByRoleID(roleID);
			// 指定社員が参照可能な職場リストを取得する
			// (Lấy list workplace của employee chỉ định)
			List<String> listWorkPlaceID = acquireListWorkplace.getListWorkPlaceID(employeeID, optEmpRange.getAsInt(), referenceDate);
			if (listWorkPlaceID.isEmpty()) {
				return new ArrayList<>();
			} else {
				return listWorkPlaceID;
			}
		}
	}

	@Override
	public List<String> getListEmployeeId(String wkpID, GeneralDate date) {
		String companyID = AppContexts.user().companyId();
		List<String> resultLst = new ArrayList<>();
		
		// ドメインモデル「所属職場権限」を取得する
		List<WorkPlaceAuthority> workPlaceAuthorityLst = workPlaceAuthorityRepository.getByFunctionAndAvailable(companyID, 3, true);
		
		List<String> roleIDLst = workPlaceAuthorityLst.stream().map(x -> x.getRoleId()).collect(Collectors.toList());
		
		// 職場、ロールIDから社員リストを取得する
		List<String> empLst2 = this.getEmpByWkpAndRole(wkpID, roleIDLst, date);
		
		// ドメインモデル「職場管理者」を取得する
		List<String> empLst3 = workplaceManagerRepository.findByWkpDateAndManager(wkpID, date, roleIDLst)
				.stream().map(x -> x.getEmployeeId()).collect(Collectors.toList());
		
		// (2)と(3)の社員リストを、重複を除いてマージする
		resultLst.addAll(empLst2);
		resultLst.addAll(empLst3);
		
		return resultLst.stream().distinct().collect(Collectors.toList());
	}
	
	/**
	 * 職場、ロールIDから社員リストを取得する
	 * @param wkpID
	 * @param roleIDs
	 * @param date
	 * @return
	 */
	private List<String> getEmpByWkpAndRole(String wkpID, List<String> roleIDs, GeneralDate date){
		String companyID = AppContexts.user().companyId();
		
		// output社員リストを初期化する
		List<String> result = new ArrayList<>();
		
		// ドメインモデル「ロールセット」を取得する
		List<RoleSet> roleSetLst = roleSetRepository.findByCIDAndEmpRoleLst(companyID, roleIDs);
		
		// ドメインモデル「ロールセット職位別付与」を取得する
		List<String> jobTitleLst = roleSetGrantedJobTitleRepository.findJobTitleByRoleCDLst(companyID, roleSetLst.stream().map(x -> x.getRoleSetCd().v()).collect(Collectors.toList()));
		
		// imported（権限管理）「所属職場履歴」を取得する (request list 120)
		List<String> employeeLst = workplaceAdapter.findListSIdByCidAndWkpIdAndPeriod(wkpID, date, date)
				.stream().map(x -> x.getEmployeeId()).collect(Collectors.toList());
		
		// 社員ID(List)から個人社員基本情報を取得 (request list 61)
		List<EmpInfoImport> empInforLst = empInfoAdapter.getEmpInfo(employeeLst);
		
		// 取得した社員リストでループする
		empInforLst.forEach(employee -> {
			String employeeID = employee.getEmployeeId();
			// ドメインモデル「ユーザ」を取得する
			Optional<User> opUser = userRepository.getByAssociatedPersonId(employee.getPersonId());
			if(opUser.isPresent()){
				User user = opUser.get();
				// ドメインモデル「ロール個人別付与」を取得する
				Optional<RoleIndividualGrant> opRoleIndividualGrant = roleIndividualGrantRepository.findByDetail(user.getUserID(), companyID, RoleType.EMPLOYMENT.value, roleIDs, date);
				if(opRoleIndividualGrant.isPresent()){
					result.add(employeeID);
					return;
				}
			}
			// ドメインモデル「ロールセット個人別付与」を取得する
			Optional<RoleSetGrantedPerson> opRoleSetGrantedPerson = roleSetGrantedPersonRepository.findByDetail(companyID, employeeID, roleIDs, date);
			if(opRoleSetGrantedPerson.isPresent()){
				result.add(employeeID);
				return;
			}
			// 社員の職位を取得する
			JobTitleValueImport jobTitleValueImport = jobTitleAdapter.findJobTitleBySid(employeeID, date);
			// 取得した職位をチェックする
			if((jobTitleValueImport!=null)&&jobTitleLst.contains(jobTitleValueImport.getPositionId())){
				result.add(employeeID);
				return;
			}
		});
		return result;
	}
	
}
