package nts.uk.ctx.workflow.app.find.approvermanagement.workroot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.text.StringUtil;
import nts.uk.ctx.workflow.dom.adapter.bs.EmployeeAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.PersonAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.SyJobTitleAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.JobTitleImport;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.PersonImport;
import nts.uk.ctx.workflow.dom.adapter.employee.EmployeeWithRangeAdapter;
import nts.uk.ctx.workflow.dom.adapter.employee.EmployeeWithRangeLoginImport;
import nts.uk.ctx.workflow.dom.adapter.workplace.WorkplaceApproverAdapter;
import nts.uk.ctx.workflow.dom.adapter.workplace.WorkplaceImport;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhase;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhaseRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.Approver;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApproverRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmPerson;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRootRepository;
import nts.uk.shr.com.company.CompanyAdapter;
import nts.uk.shr.com.company.CompanyInfor;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author hoatt
 *
 */
@Stateless
public class CommonApprovalRootFinder {
	@Inject
	private CompanyAdapter comAdapter;
	@Inject
	private PersonApprovalRootRepository repo;
	@Inject
	private CompanyApprovalRootRepository repoCom;
	@Inject
	private WorkplaceApprovalRootRepository repoWorkplace;
	@Inject
	private ApprovalPhaseRepository repoAppPhase;
	@Inject
	private ApproverRepository repoApprover;
	@Inject
	private PersonAdapter adapterPerson;
	@Inject
	private WorkplaceApproverAdapter adapterWp;
	@Inject
	private SyJobTitleAdapter adapterJobtitle;
	@Inject
	private EmployeeAdapter employeeAdapter;
	@Inject
	private EmployeeWithRangeAdapter employeeWithRangeAdapter;
	private static final int COMPANY = 0;
	private static final int WORKPLACE = 1;
	/**
	 * getAllCommonApprovalRoot (grouping by history)
	 * まとめて登録モード
	 * @param param
	 * @return
	 */
	public DataFullDto getAllCommonApprovalRoot(ParamDto param){
		//get all data by param
		CommonApprovalRootDto data = getPrivate(param);
		//TH: company - domain 会社別就業承認ルート
		if(param.getRootType() == COMPANY){
			return this.getAllComApprovalRoot(data);
		}
		//TH: work place - domain 職場別就業承認ルート
		if(param.getRootType() == WORKPLACE){
			return this.getAllWpApprovalRoot(data);
		}
		//TH: person - domain 個人別就業承認ルート
		else{
			return this.getAllPsApprovalRoot(data);
		}
	}
	/**
	 * get all data from db
	 * 申請個別登録モード
	 * getPrivate (not grouping)
	 * @param param
	 * @return
	 */
	public CommonApprovalRootDto getPrivate(ParamDto param){
		//get name company
		Optional<CompanyInfor> companyCurrent = comAdapter.getCurrentCompany();
		String companyName = companyCurrent == null ? "" : companyCurrent.get().getCompanyName();
		//TH: company - domain 会社別就業承認ルート
		if(param.getRootType() == COMPANY){
			return this.getDataComApprovalRoot(param, companyName);
		}
		//TH: workplace - domain 職場別就業承認ルート
		if(param.getRootType() == WORKPLACE){
			return this.getDataWpApprovalRoot(param, companyName);
		}
		//TH: person - domain 個人別就業承認ルート
		else{
			return this.getDataPsApprovalRoot(param, companyName);
		}
	}
	/**
	 * get work place info (id, code, name)
	 * @param workplaceId
	 * @return
	 */
	public WorkplaceImport getWpInfo(String workplaceId){
		GeneralDate baseDate = GeneralDate.today();
		Optional<WorkplaceImport> wpInfo = adapterWp.findByWkpId(workplaceId, baseDate);
		String name = "";
		String code = "";
		WorkplaceImport wpResult = null;
		if(wpInfo.isPresent()){
			code = wpInfo.get().getWkpCode();
			name = wpInfo.get().getWkpName();
		}
		wpResult = new WorkplaceImport(workplaceId, code, name);
		return wpResult;
	}
	/**
	 * get All Company Approval Root(grouping by history)
	 * @param data
	 * @return
	 */
	private DataFullDto getAllComApprovalRoot(CommonApprovalRootDto data){
		//list company get from db
		List<CompanyAppRootDto> lstCompanyRoot = data.getLstCompanyRoot();
		List<ObjectDate> lstObjDate = new ArrayList<>();
		lstCompanyRoot.forEach(item ->{
			lstObjDate.add(new ObjectDate(item.getCompany().getApprovalId(), item.getCompany().getStartDate(), item.getCompany().getEndDate(),false));
		});
		//grouping history
		ObjGrouping result = this.groupingMapping(lstObjDate);
		List<ObjectDate> lstRootCheckOvl = result.getLstRootCheckOvl();
		List<ObjDate> lstTrung = result.getLstDate();
		List<DataDisplayComDto> lstComFinal = new ArrayList<>();
		int index = 0;
		//grouping companyRoot by history
		for (ObjectDate objDate : lstRootCheckOvl) {
			List<CompanyAppRootDto> lstItem = new ArrayList<>();
			ObjDate date = new ObjDate(objDate.getStartDate(), objDate.getEndDate());
			//TH: grouping
			if(lstTrung.contains(date)){
				for (CompanyAppRootDto com : lstCompanyRoot) {
					if(com.getCompany().getStartDate().compareTo(objDate.getStartDate())==0 && com.getCompany().getEndDate().compareTo(objDate.getEndDate())==0){
						lstItem.add(com);
					}
				}
			}
			//TH: not grouping
			else{
				for (CompanyAppRootDto com : lstCompanyRoot) {
					if(com.getCompany().getApprovalId().compareTo(objDate.getApprovalId())==0){
						lstItem.add(com);
					}
				}
			}
			lstComFinal.add(new DataDisplayComDto(index,objDate.isOverlap(),data.getCompanyName(), lstItem));
			index++;
		}
		return new DataFullDto("",lstComFinal, null, null);
	}
	/**
	 * get All Work place Approval Root(grouping by history)
	 * @param data
	 * @return
	 */
	private DataFullDto getAllWpApprovalRoot(CommonApprovalRootDto data){
		//list work place get from db
		List<WorkPlaceAppRootDto> lstWorkplaceRoot = data.getLstWorkplaceRoot();
		List<ObjectDate> lstObjDate = new ArrayList<>();
		lstWorkplaceRoot.forEach(item ->{
			lstObjDate.add(new ObjectDate(item.getWorkplace().getApprovalId(), item.getWorkplace().getStartDate(), item.getWorkplace().getEndDate(),false));
		});
		//grouping history
		ObjGrouping result = this.groupingMapping(lstObjDate);
		List<ObjectDate> lstRootCheckOvl = result.getLstRootCheckOvl();
		List<ObjDate> lstTrung = result.getLstDate();
		List<DataDisplayWpDto> lstWpFinal = new ArrayList<>();
		int index = 0;
		//grouping WorkplaceRoot by history
		for (ObjectDate objDate : lstRootCheckOvl) {
			List<WorkPlaceAppRootDto> lstItem = new ArrayList<>();
			ObjDate date = new ObjDate(objDate.getStartDate(), objDate.getEndDate());
			//TH: grouping
			if(lstTrung.contains(date)){
				for (WorkPlaceAppRootDto wp : lstWorkplaceRoot) {
					if(wp.getWorkplace().getStartDate().compareTo(objDate.getStartDate())==0 && wp.getWorkplace().getEndDate().compareTo(objDate.getEndDate())==0){
						lstItem.add(wp);
					}
				}
			}
			//TH: not grouping
			else{
				for (WorkPlaceAppRootDto wp : lstWorkplaceRoot) {
					if(wp.getWorkplace().getApprovalId().compareTo(objDate.getApprovalId())==0){
						lstItem.add(wp);
					}
				}
			}
			lstWpFinal.add(new DataDisplayWpDto(index,objDate.isOverlap(), lstItem));
			index++;
		}
		return new DataFullDto(data.getWorkplaceId(),null, lstWpFinal, null);
	}
	/**
	 * get All Person Approval Root(grouping by history)
	 * @param data
	 * @return
	 */
	private DataFullDto getAllPsApprovalRoot(CommonApprovalRootDto data){
		//list person get from db
		List<PersonAppRootDto> lstPersonRoot = data.getLstPersonRoot();
		List<ObjectDate> lstObjDate = new ArrayList<>();
		lstPersonRoot.forEach(item ->{
			lstObjDate.add(new ObjectDate(item.getPerson().getApprovalId(), item.getPerson().getStartDate(), item.getPerson().getEndDate(),false));
		});
		//grouping history
		ObjGrouping result = this.groupingMapping(lstObjDate);
		List<ObjectDate> lstRootCheckOvl = result.getLstRootCheckOvl();
		List<ObjDate> lstTrung = result.getLstDate();
		List<DataDisplayPsDto> lstPsFinal = new ArrayList<>();
		int index = 0;
		//grouping PersonRoot by history
		for (ObjectDate objDate : lstRootCheckOvl) {
			List<PersonAppRootDto> lstItem = new ArrayList<>();
			ObjDate date = new ObjDate(objDate.getStartDate(), objDate.getEndDate());
			//TH: grouping
			if(lstTrung.contains(date)){
				for (PersonAppRootDto ps : lstPersonRoot) {
					if(ps.getPerson().getStartDate().compareTo(objDate.getStartDate())==0 && ps.getPerson().getEndDate().compareTo(objDate.getEndDate())==0){
						lstItem.add(ps);
					}
				}
			}
			//TH: not grouping
			else{
				for (PersonAppRootDto ps : lstPersonRoot) {
					if(ps.getPerson().getApprovalId().compareTo(objDate.getApprovalId())==0){
						lstItem.add(ps);
					}
				}
			}
			lstPsFinal.add(new DataDisplayPsDto(index, objDate.isOverlap(), lstItem));
			index++;
		}
		return new DataFullDto("",null, null, lstPsFinal);
	}
	/**
	 * get Data Company Approval Root
	 * @param param
	 * @param companyName
	 * @return
	 */
	private CommonApprovalRootDto getDataComApprovalRoot(ParamDto param, String companyName){
		//user contexts
		String companyId = AppContexts.user().companyId();
		List<CompanyAppRootDto> lstComRoot = new ArrayList<>();
		//get all data from ComApprovalRoot (会社別就業承認ルート)
		List<ComApprovalRootDto> lstCom = this.repoCom.getAllComApprovalRoot(companyId)
							.stream()
							.map(c->ComApprovalRootDto.fromDomain(c))
							.collect(Collectors.toList());
		for (ComApprovalRootDto companyApprovalRoot : lstCom) {
			List<ApprovalPhaseDto> lstApprovalPhase = new ArrayList<>();
			List<ApproverDto> lstApproverDto = new ArrayList<ApproverDto>();
			List<Approver> listApprover = new ArrayList<Approver>();
			//get All Approval Phase by BranchId
			List<ApprovalPhase> lstAppPhase = this.repoAppPhase.getAllApprovalPhasebyCode(companyId, companyApprovalRoot.getBranchId());
			for (ApprovalPhase approvalPhase : lstAppPhase) {
				//get All Approver By ApprovalPhaseId
				listApprover = this.repoApprover.getAllApproverByCode(companyId, approvalPhase.getApprovalPhaseId());
				lstApproverDto = listApprover.stream()
							.map(c->{
								String name = c.getApprovalAtr().value == 0 ? 
										getPersonInfo(c.getEmployeeId()) == null ? "" : getPersonInfo(c.getEmployeeId()).getEmployeeName() : 	
										getJobTitleInfo(c.getJobTitleId()) == null ? "" : getJobTitleInfo(c.getJobTitleId()).getPositionName();
								String confirmName = c.getConfirmPerson() == ConfirmPerson.CONFIRM ? "(確定)" : "";
								return ApproverDto.fromDomain(c,name, confirmName);
								})
							.collect(Collectors.toList());
				//lst (ApprovalPhase + lst Approver)
				lstApprovalPhase.add(new ApprovalPhaseDto(lstApproverDto, approvalPhase.getBranchId(),approvalPhase.getApprovalPhaseId(),
						approvalPhase.getApprovalForm().value, approvalPhase.getApprovalForm().getName(), approvalPhase.getBrowsingPhase(), approvalPhase.getOrderNumber()));
			}
			//add in lstAppRoot
			lstComRoot.add(new CompanyAppRootDto(companyApprovalRoot,lstApprovalPhase));
		}
		return new CommonApprovalRootDto(companyName,"","",lstComRoot, null, null);
	}
	/**
	 * get Data Work place Approval Root
	 * @param param
	 * @param companyName
	 * @return
	 */
	private CommonApprovalRootDto getDataWpApprovalRoot(ParamDto param, String companyName){
		//user contexts
		String companyId = AppContexts.user().companyId();
		GeneralDate baseDate = GeneralDate.today();
		List<WorkPlaceAppRootDto> lstWpRoot = new ArrayList<>();
		String workplaceId = param.getWorkplaceId();
		if(workplaceId == ""){
			WorkplaceImport workplace = adapterWp.findBySid(AppContexts.user().employeeId(), baseDate);
			if(workplace != null){
				workplaceId = workplace.getWkpId();
			}
		}
		//get all data from WorkplaceApprovalRoot (職場別就業承認ルート)
		List<WpApprovalRootDto> lstWp = this.repoWorkplace.getAllWpApprovalRoot(companyId, workplaceId)
				.stream()
				.map(c->WpApprovalRootDto.fromDomain(c))
				.collect(Collectors.toList());
		for (WpApprovalRootDto workplaceApprovalRoot : lstWp) {
			List<ApprovalPhaseDto> lstApprovalPhase = new ArrayList<>();
			List<ApproverDto> lstApproverDto = new ArrayList<>();
			List<Approver> lstApprover = new ArrayList<>();
			//get All Approval Phase by BranchId
			List<ApprovalPhase> lstAppPhase = this.repoAppPhase.getAllApprovalPhasebyCode(companyId, workplaceApprovalRoot.getBranchId());
			for (ApprovalPhase approvalPhase : lstAppPhase) {
				//get All Approver By ApprovalPhaseId
				lstApprover = this.repoApprover.getAllApproverByCode(companyId, approvalPhase.getApprovalPhaseId());
				lstApproverDto = lstApprover.stream()
						.map(c->{
							String name = c.getApprovalAtr().value == 0 ? 
									getPersonInfo(c.getEmployeeId()) == null ? "" : getPersonInfo(c.getEmployeeId()).getEmployeeName() : 	
									getJobTitleInfo(c.getJobTitleId()) == null ? "" : getJobTitleInfo(c.getJobTitleId()).getPositionName();
							String confirmName = c.getConfirmPerson() == ConfirmPerson.CONFIRM ? "(確定)" : "";
							return ApproverDto.fromDomain(c,name,confirmName);
							})
						.collect(Collectors.toList());
				//lst (ApprovalPhase + lst Approver)
				lstApprovalPhase.add(new ApprovalPhaseDto(lstApproverDto, approvalPhase.getBranchId(),approvalPhase.getApprovalPhaseId(),
						approvalPhase.getApprovalForm().value, approvalPhase.getApprovalForm().getName(), approvalPhase.getBrowsingPhase(), approvalPhase.getOrderNumber()));
			}
			//add in lstAppRoot
			lstWpRoot.add(new WorkPlaceAppRootDto(workplaceApprovalRoot,lstApprovalPhase));
		}
		return new CommonApprovalRootDto(companyName, workplaceId,"", null, lstWpRoot, null);
	}
	/**
	 * get Data Person Approval Root
	 * @param param
	 * @param companyName
	 * @return
	 */
	private CommonApprovalRootDto getDataPsApprovalRoot(ParamDto param, String companyName){
		//user contexts
		String companyId = AppContexts.user().companyId();
		List<PersonAppRootDto> lstPsRoot = new ArrayList<>();
		String employeeId = StringUtil.isNullOrEmpty(param.getEmployeeId(), true) ? AppContexts.user().employeeId() : param.getEmployeeId();
		//get all data from PersonApprovalRoot (個人別就業承認ルート)
		List<PsApprovalRootDto> lstPs = this.repo.getAllPsApprovalRoot(companyId,employeeId )
				.stream()
				.map(c->PsApprovalRootDto.fromDomain(c))
				.collect(Collectors.toList());
		for (PsApprovalRootDto personApprovalRoot : lstPs) {
			List<ApprovalPhaseDto> lstApprovalPhase = new ArrayList<>();
			List<Approver> lstApprover = new ArrayList<>();
			List<ApproverDto> lstApproverDto = new ArrayList<>();
			//get All Approval Phase by BranchId
			List<ApprovalPhase> lstAppPhase = this.repoAppPhase.getAllApprovalPhasebyCode(companyId, personApprovalRoot.getBranchId());
			for (ApprovalPhase approvalPhase : lstAppPhase) {
				//get All Approver By ApprovalPhaseId
				lstApprover = this.repoApprover.getAllApproverByCode(companyId, approvalPhase.getApprovalPhaseId());
				lstApproverDto = lstApprover.stream()
						.map(c->{
							String name = c.getApprovalAtr().value == 0 ? 
									getPersonInfo(c.getEmployeeId()) == null ? "" : getPersonInfo(c.getEmployeeId()).getEmployeeName() : 	
									getJobTitleInfo(c.getJobTitleId()) == null ? "" : getJobTitleInfo(c.getJobTitleId()).getPositionName();
							String confirmName = c.getConfirmPerson() == ConfirmPerson.CONFIRM ? "(確定)" : "";
							return ApproverDto.fromDomain(c,name,confirmName);
							})
						.collect(Collectors.toList());
				//lst (ApprovalPhase + lst Approver)
				lstApprovalPhase.add(new ApprovalPhaseDto(lstApproverDto, approvalPhase.getBranchId(),approvalPhase.getApprovalPhaseId(),
						approvalPhase.getApprovalForm().value, approvalPhase.getApprovalForm().getName(),approvalPhase.getBrowsingPhase(), approvalPhase.getOrderNumber()));
			}
			//add in lstAppRoot
			lstPsRoot.add(new PersonAppRootDto(personApprovalRoot,lstApprovalPhase));
		}
		return new CommonApprovalRootDto(companyName, "",employeeId, null, null, lstPsRoot);
	}
	/**
	 * grouping history
	 * @param lstRoot(List<ObjectDate>)
	 * @return ObjGrouping
	 */
	private ObjGrouping groupingMapping(List<ObjectDate> lstRoot){
		List<ObjDate> result = new ArrayList<ObjDate>();
		List<ObjectDate> lstRootCheckOvl = new ArrayList<>();
		List<ObjDate> lstDate = new ArrayList<>();
		boolean check = true;
		for (ObjectDate date1 : lstRoot) {
			for (ObjectDate date2 : lstRoot) {
				if (date1.getApprovalId() != date2.getApprovalId() && isOverlap(date1,date2)){//overlap
					check = false;
					break;
				}
				check = true;
			}
			//TH: not overlap
			if(check){
				ObjDate date = new ObjDate(date1.getStartDate(), date1.getEndDate());
				if(!result.contains(date)){//exist
					result.add(new ObjDate(date1.getStartDate(), date1.getEndDate()));
					lstRootCheckOvl.add(new ObjectDate(date1.getApprovalId(),date1.getStartDate(), date1.getEndDate(), false));
				}else{
					if(!lstDate.contains(date)){
						lstDate.add(new ObjDate(date1.getStartDate(), date1.getEndDate()));
					}
				}
			}
			//TH: overlap
			else{
				result.add(new ObjDate(date1.getStartDate(), date1.getEndDate()));
				lstRootCheckOvl.add(new ObjectDate(date1.getApprovalId(),date1.getStartDate(), date1.getEndDate(), true));
			}
		}
		return new ObjGrouping(lstDate, lstRootCheckOvl);
	}
	
	/**
	 * check if date1 isOverlap date2 ? 
	 * @param date1
	 * @param date2
	 * @return true, if date1 isOverlap date2
	 */
	private boolean isOverlap(ObjectDate date1, ObjectDate date2){
		/**
		 * date 1.........|..............]..........
		 * date 2............|......................
		 * sDate1<sDate2<eDate1
		 */
		if (date2.getStartDate().compareTo(date1.getStartDate()) > 0
				&& date2.getStartDate().compareTo(date1.getEndDate()) < 0) {
			return true;
		}
		/**
		 * date 1.........|..............]..........
		 * date 2.........|..........]..............
		 * sDate1<=sDate2 && eDate<eDate1
		 */
		if (date2.getStartDate().compareTo(date1.getStartDate()) == 0
				&& date2.getEndDate().compareTo(date1.getEndDate()) < 0) {
			return true;
		}
		
		/**
		 * date 1.........|..............]..........
		 * date 2.....|........]....................
		 * sDate2 < sDate1 and eDate2 > sDate1
		 */
		if (date2.getStartDate().compareTo(date1.getStartDate()) < 0
				&& date2.getEndDate().compareTo(date1.getStartDate()) > 0) {
			return true;
		}
		/**
		 * date 1.........|..............]..........
		 * date 2.............]....................
		 * sDate1 < eDate2 < eDate1
		 */
		if (date1.getStartDate().compareTo(date2.getEndDate()) < 0
				&& date2.getEndDate().compareTo(date1.getEndDate()) < 0) {
			return true;
		}
		/**
		 * date 1.........|..............]..........
		 * date 2..........|...................].....
		 * eDate2 > eDate1 && sDate2 < eDate1
		 */
		if(date2.getEndDate().compareTo(date1.getEndDate()) > 0
				&& date2.getStartDate().compareTo(date1.getEndDate()) <0){
			return true;
		}
		return false;
	}
	/**
	 * get Person Info
	 * @param employeeId
	 * @return
	 */
	private PersonImport getPersonInfo(String employeeId){
		return adapterPerson.getPersonInfo(employeeId);
	}
	/**
	 * get JobTitle Info
	 * @param jobTitleId
	 * @return
	 */
	private JobTitleImport getJobTitleInfo(String jobTitleId){
		String companyId = AppContexts.user().companyId();
		GeneralDate baseDate = GeneralDate.today();
		return adapterJobtitle.findJobTitleByPositionId(companyId, jobTitleId, baseDate);
	}

	/**
	 * 社員コードを入力する
	 * @param employeeCode
	 * @return
	 */
	public EmployeeWithRangeLoginImport getEmployeeInfoByCode(Cmm053EmployeeSearchParam param){
		String companyId     = AppContexts.user().companyId();
		String employeeCode  = param.getEmployeeCode();
		GeneralDate baseDate = Objects.isNull(param.getBaseDate()) ? GeneralDate.today() : param.getBaseDate();
		Optional<EmployeeWithRangeLoginImport> employeeWithRange = Optional.empty();

		//社員コードから承認権限ありの社員のみ取得する
		// RequestList315
		employeeWithRange = this.employeeWithRangeAdapter.findByEmployeeByLoginRange(companyId, employeeCode, baseDate);

		if (!employeeWithRange.isPresent())
			throw new BusinessException("Msg_1078");

		return employeeWithRange.map(x -> {
			return new EmployeeWithRangeLoginImport(x.getBusinessName(), x.getEmployeeCD(),
					x.getEmployeeID());
		}).orElse(null);
	}

	/**
	 * Get 過去履歴
	 * @param employeeId
	 * @return
	 */
	public List<PastHistoryDto> getPastHistory(String employeeId) {
		String companyId = AppContexts.user().companyId();
		List<PersonApprovalRoot> getAllPsApprovalRoot = repo.getPastHistory(companyId, employeeId);

		Map<GeneralDate, List<PersonApprovalRoot>> grouped = getAllPsApprovalRoot.stream().collect(
				Collectors.groupingBy(item -> item.getEmploymentAppHistoryItems().get(0).start()));

		List<PastHistoryDto> itemModel = grouped.entrySet().stream().map(item -> {
			GeneralDate startDate    = null;
			GeneralDate endDate      = null;
			String departmentCode    = null;
			String departmentName    = null;
			String dailyApprovalCode = null;
			String dailyApprovalName = null;

			if (!item.getValue().isEmpty()) {
				for(PersonApprovalRoot psAppRoot: item.getValue()){
					startDate = psAppRoot.getEmploymentAppHistoryItems().get(0).start();
					endDate   = psAppRoot.getEmploymentAppHistoryItems().get(0).end();
					Optional<ApprovalPhase> psAppPhase = this.repoAppPhase.getApprovalFirstPhase(companyId, psAppRoot.getBranchId());
					if(psAppPhase.isPresent()){
						Optional<Approver> firstApprover = psAppPhase.get().getApprovers().stream().filter(x-> x.getOrderNumber() == 0).findFirst();
						if(firstApprover.isPresent()){
							String sId = firstApprover.get().getEmployeeId();
							PersonImport person = this.employeeAdapter.getEmployeeInformation(sId);
							if (psAppRoot.getEmploymentRootAtr().value == 0
									&& Objects.isNull(psAppRoot.getApplicationType())) {
								dailyApprovalCode = person.getEmployeeCode();
								dailyApprovalName = person.getEmployeeName();
							} else if (psAppRoot.getEmploymentRootAtr().value == 2
									&& psAppRoot.getConfirmationRootType().value == 1) {
								departmentCode = person.getEmployeeCode();
								departmentName = person.getEmployeeName();
							}
						}
					}
				}
			}
			return new PastHistoryDto(startDate, endDate, departmentCode, departmentName, dailyApprovalCode, dailyApprovalName);
		}).collect(Collectors.toList());
		return itemModel;
	}
	/**
	 * get work place info (id, code, name)
	 * @param workplaceId
	 * @return
	 */
	public WorkplaceImport getWpInfoLogin(){
		return adapterWp.findBySid(AppContexts.user().employeeId(), GeneralDate.today());
	}
}
