package nts.uk.ctx.workflow.app.find.approvermanagement.workroot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.text.StringUtil;
import nts.uk.ctx.workflow.dom.adapter.bs.EmployeeAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.PersonAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.EmpInfoRQ18;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.PersonImport;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.StatusOfEmployment;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.StatusOfEmploymentImport;
import nts.uk.ctx.workflow.dom.adapter.employee.EmployeeWithRangeAdapter;
import nts.uk.ctx.workflow.dom.adapter.employee.EmployeeWithRangeLoginImport;
import nts.uk.ctx.workflow.dom.adapter.workplace.WorkplaceApproverAdapter;
import nts.uk.ctx.workflow.dom.adapter.workplace.WorkplaceImport;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.ApprovalSetting;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.ApprovalSettingRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.PrincipalApprovalFlg;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalAtr;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhase;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhaseRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.Approver;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmPerson;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmationRootType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.ApprovalRootCommonService;
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
	private PersonAdapter adapterPerson;
	@Inject
	private WorkplaceApproverAdapter adapterWp;
	@Inject
	private ApprovalRootCommonService appRootCm;
	@Inject
	private EmployeeAdapter employeeAdapter;
	@Inject
	private EmployeeWithRangeAdapter empWithRanAd;
	@Inject
	private ApprovalSettingRepository appSetRepo;
	
	
	private static final int COMPANY = 0;
	private static final int WORKPLACE = 1;
	/**
	 * getAllCommonApprovalRoot (grouping by history)
	 * ???????????????????????????
	 * @param param
	 * @return
	 */
	public DataFullDto getAllCommonApprovalRoot(ParamDto param){
		//get all data by param
		CommonApprovalRootDto data = this.getPrivate(param);
		//TH: company - domain ??????????????????????????????
		if(param.getRootType() == COMPANY){
			return this.getAllComApprovalRoot(data);
		}
		//TH: work place - domain ??????????????????????????????
		if(param.getRootType() == WORKPLACE){
			return this.getAllWpApprovalRoot(data);
		}
		//TH: person - domain ??????????????????????????????
		else{
			return this.getAllPsApprovalRoot(data);
		}
	}
	/**
	 * get all data from db
	 * ???????????????????????????
	 * getPrivate (not grouping)
	 * @param param
	 * @return
	 */
	public CommonApprovalRootDto getPrivate(ParamDto param){
		//get name company
		Optional<CompanyInfor> companyCurrent = comAdapter.getCurrentCompany();
		String companyName = companyCurrent == null ? "" : companyCurrent.get().getCompanyName();
		//TH: company - domain ??????????????????????????????
		if(param.getRootType() == COMPANY){
			return this.getDataComApprovalRoot(param, companyName);
		}
		//TH: workplace - domain ??????????????????????????????
		if(param.getRootType() == WORKPLACE){
			return this.getDataWpApprovalRoot(param, companyName);
		}
		//TH: person - domain ??????????????????????????????
		else{
			return this.getDataPsApprovalRoot(param, companyName);
		}
	}

	/**
	 * UKDesign.UniversalK.??????.CMM_???????????????????????????.CMM018_??????????????????.CMM018_???????????????????????????????????????.AB:???????????????????????????????????????????????????.??????????????????."02.????????????(???????????????????????????)"
	 * get All Company Approval Root(grouping by history)
	 * @param data
	 * @return
	 */
	private DataFullDto getAllComApprovalRoot(CommonApprovalRootDto data){
		// list company get from db
		// ??????????????????????????????????????????????????????????????????
		List<CompanyAppRootDto> lstCompanyRoot = data.getLstCompanyRoot();
		List<ObjectDate> lstObjDate = new ArrayList<>();
		lstCompanyRoot.forEach(item ->{
			lstObjDate.add(new ObjectDate(item.getCompany().getApprovalId(), item.getCompany().getStartDate(), item.getCompany().getEndDate(),false));
		});
		// grouping history
		
		// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		ObjGrouping result = this.groupingMapping(lstObjDate);
		List<ObjectDate> lstRootCheckOvl = result.getLstRootCheckOvl();
		List<ObjDate> lstTrung = result.getLstDate();
		List<DataDisplayComDto> lstComFinal = new ArrayList<>();
		int index = 0;
		// ?????????????????????????????????
		if(lstRootCheckOvl.isEmpty()){
			return new DataFullDto("", data.getCompanyName(),lstComFinal, null, null);
		}
		//grouping companyRoot by history
		
		// ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
			lstComFinal.add(new DataDisplayComDto(index,objDate.isOverlap(),lstItem));
			index++;
		}
		
		// -> ????????????????????????????????????????????????????????? (UI)
		return new DataFullDto("", data.getCompanyName(), lstComFinal, null, null);
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
		return new DataFullDto(data.getWorkplaceId(), "", null, lstWpFinal, null);
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
		return new DataFullDto("", "", null, null, lstPsFinal);
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
		
		//get all data from ComApprovalRoot (??????????????????????????????)
		List<ComApprovalRootDto> lstCom = this.repoCom.getComRootStart(companyId, 
				param.getSystemAtr(), 
				param.getLstAppType(), 
				param.getLstNoticeID(), 
				param.getLstEventID()).stream()
									  .map(c->ComApprovalRootDto.fromDomain(c))
									  .collect(Collectors.toList());
		
		for (ComApprovalRootDto rootCom : lstCom) {
			//get All Approval Phase by approvalId
			List<ApprovalPhaseDto> lstPhaseDto = this.convertDto(rootCom.getApprovalId());
			//add in lstAppRoot
			lstComRoot.add(new CompanyAppRootDto(rootCom, lstPhaseDto));
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
		String wkpDepId = param.getWorkplaceId();
		if(Strings.isBlank(wkpDepId)){
			if(param.getSystemAtr() == 0) {
				wkpDepId = adapterWp.getWorkplaceIDByEmpDate(AppContexts.user().employeeId(), baseDate);
			}else {
				wkpDepId = adapterWp.getDepartmentIDByEmpDate(AppContexts.user().employeeId(), baseDate);
			}
			
		}
		if(Strings.isBlank(wkpDepId)) {//TH data setting chua co
			return new CommonApprovalRootDto(companyName, wkpDepId,"", null, lstWpRoot, null);
		}
		//get all data from WorkplaceApprovalRoot (??????????????????????????????)
		List<WpApprovalRootDto> lstWp = this.repoWorkplace.getWpRootStart(companyId, wkpDepId, param.getSystemAtr(),
				param.getLstAppType(), param.getLstNoticeID(), param.getLstEventID())
				.stream()
				.map(c->WpApprovalRootDto.fromDomain(c))
				.collect(Collectors.toList());
		for (WpApprovalRootDto rootWkp : lstWp) {
			//get All Approval Phase by ApprovalId
			List<ApprovalPhaseDto> lstPhaseDto = this.convertDto(rootWkp.getApprovalId());
			//add in lstAppRoot
			lstWpRoot.add(new WorkPlaceAppRootDto(rootWkp, lstPhaseDto));
		}
		return new CommonApprovalRootDto(companyName, wkpDepId,"", null, lstWpRoot, null);
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
		//get all data from PersonApprovalRoot (??????????????????????????????)
		List<PsApprovalRootDto> lstPs = this.repo.getPsRootStart(companyId,employeeId, param.getSystemAtr(),
				param.getLstAppType(), param.getLstNoticeID(), param.getLstEventID())
				.stream()
				.map(c->PsApprovalRootDto.fromDomain(c))
				.collect(Collectors.toList());
		for (PsApprovalRootDto rootPs : lstPs) {
			//get All Approval Phase by ApprovalId
			List<ApprovalPhaseDto> lstPhaseDto = this.convertDto(rootPs.getApprovalId());
			//add in lstAppRoot
			lstPsRoot.add(new PersonAppRootDto(rootPs, lstPhaseDto));
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
		if(date1.getStartDate().compareTo(date2.getStartDate()) == 0
				&& date1.getEndDate().compareTo(date2.getEndDate()) == 0) {
			return false;
			
		}
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
		/**
		 * date 1.........|..............]..........
		 * date 2....|....].........................
		 * eDate2 = sDate1 or eDate1 = sDate2
		 */
		if(date2.getEndDate().compareTo(date1.getStartDate()) == 0
				|| date2.getStartDate().compareTo(date1.getEndDate()) == 0){
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
	 * 05.??????????????????????????????
	 * @param employeeCode
	 * @return
	 */
	public EmployeeWithRangeLoginImport getEmployeeInfoByCode(Cmm053EmployeeSearchParam param){
		String companyId     = AppContexts.user().companyId();
		String employeeCode  = param.getEmployeeCode();
		GeneralDate baseDate = Objects.isNull(param.getBaseDate()) ? GeneralDate.today() : param.getBaseDate();
		Optional<EmployeeWithRangeLoginImport> employeeWithRange = Optional.empty();
		//2019.03.11 hoatt
		//?????????ID????????????????????????????????????????????????????????? - RQ18
		Optional<EmpInfoRQ18> empInfo = employeeAdapter.getEmpInfoByScd(companyId, employeeCode);
		if(!empInfo.isPresent()){//???????????????????????????
			//????????????????????????	???Msg_1512???
			throw new BusinessException("Msg_1512");
		}
		//?????????????????????
		StatusOfEmploymentImport sttEmp = employeeAdapter.getStatusOfEmployment(empInfo.get().getEmployeeId(), baseDate);
		if(sttEmp != null && (sttEmp.getStatusOfEmployment().equals(StatusOfEmployment.RETIREMENT)||
				sttEmp.getStatusOfEmployment().equals(StatusOfEmployment.LEAVE_OF_ABSENCE)||
				sttEmp.getStatusOfEmployment().equals(StatusOfEmployment.HOLIDAY))){
			//????????????????????????OR????????????OR??????????????????
			//????????????????????????	???Msg_1511???
			throw new BusinessException("Msg_1511");
		}
		//??????????????????????????????????????????????????????????????????
		// RequestList315
		employeeWithRange = this.empWithRanAd.findByEmployeeByLoginRange(companyId, employeeCode, baseDate);

		if (!employeeWithRange.isPresent())
			throw new BusinessException("Msg_1078");

		return employeeWithRange.map(x -> {
			return new EmployeeWithRangeLoginImport(x.getBusinessName(), x.getEmployeeCD(),
					x.getEmployeeID());
		}).orElse(null);
	}

	/**
	 * Get ????????????
	 * @param employeeId
	 * @return
	 */
	public List<PastHistoryDto> getPastHistory(String employeeId) {
		String companyId = AppContexts.user().companyId();
		List<PersonApprovalRoot> getAllPsApprovalRoot = repo.getPastHistory(companyId, employeeId);

		Map<GeneralDate, List<PersonApprovalRoot>> grouped = getAllPsApprovalRoot.stream().collect(
				Collectors.groupingBy(item -> item.getApprRoot().getHistoryItems().get(0).start()));

		List<PastHistoryDto> itemModel = grouped.entrySet().stream().map(item -> {
			GeneralDate startDate    = null;
			GeneralDate endDate      = null;
			String codeB17  = null;
			String nameB18  = null;
			String codeB110 = null;
			String nameB111 = null;
			String codeB112 = null;
			String nameB113 = null;
			if (!item.getValue().isEmpty()) {
				for(PersonApprovalRoot psAppRoot: item.getValue()){
					startDate = psAppRoot.getApprRoot().getHistoryItems().get(0).start();
					endDate   = psAppRoot.getApprRoot().getHistoryItems().get(0).end();
					Optional<ApprovalPhase> psAppPhase = this.repoAppPhase.getApprovalFirstPhase(psAppRoot.getApprovalId());
					if(psAppPhase.isPresent()){
						Optional<Approver> approver1 = psAppPhase.get().getApprovers().stream().filter(x-> x.getApproverOrder() == 1).findFirst();
						PersonImport person = null;
						if(approver1.isPresent()){
							person = this.employeeAdapter.getEmployeeInformation(approver1.get().getEmployeeId());
						}
						if(psAppRoot.isCommon()){
							if(person != null){
								codeB110 = person.getEmployeeCode();
								nameB111 = person.getEmployeeName();
							}
							Optional<Approver> approver2 = psAppPhase.get().getApprovers().stream().filter(x-> x.getApproverOrder() == 2).findFirst();
							if(approver2.isPresent()){
								PersonImport person2 = this.employeeAdapter.getEmployeeInformation(approver2.get().getEmployeeId());
								if(person2 != null){
									codeB112 = person2.getEmployeeCode();
									nameB113 = person2.getEmployeeName();
								}
							}
						}
						if(psAppRoot.isConfirm() && psAppRoot.getApprRoot().getConfirmationRootType().equals(ConfirmationRootType.MONTHLY_CONFIRMATION)){
							if(person != null){
								codeB17 = person.getEmployeeCode();
								nameB18 = person.getEmployeeName();
							}
						}
					}
				}
			}
			return new PastHistoryDto(startDate, endDate, codeB17, nameB18, codeB110, nameB111, codeB112, nameB113);
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

	/**
	 * check before register cmm053
	 * @author hoatt
	 * 2019.03.11
	 * @param param
	 * @return
	 */
	public OutputCheckRegCmm053 checkReg(ParamCheckRegCmm053 param){
		String companyId     = AppContexts.user().companyId();
		GeneralDate baseDate = GeneralDate.fromString(param.getBaseDate(), "yyyy/MM/dd");
		String codeA27 = param.getCodeA27();
		String codeA210 = param.getCodeA210();
		String codeA16 = param.getCodeA16();
		boolean displayA210 = Strings.isNotBlank(codeA210);
		OutputCheckRegCmm053 result = new OutputCheckRegCmm053(false, false, false, null);
		//?????????ID????????????????????????????????????????????????????????? - RQ18
		//A2_7
		Optional<EmpInfoRQ18> empA27 = employeeAdapter.getEmpInfoByScd(companyId, codeA27);
		Optional<EmpInfoRQ18> empA210 = Optional.empty();
		if(displayA210){
			empA210 = employeeAdapter.getEmpInfoByScd(companyId, codeA210);
		}
		if(!empA27.isPresent() || (displayA210 && !empA210.isPresent())){//???????????????????????????
			//????????????????????????	???Msg_1512???
			result.setErrFlg(true);
			result.setErrA27(!empA27.isPresent());
			result.setErrA210(displayA210 && !empA210.isPresent());
			result.setMsgId("Msg_1512");
			return result;
		}
		//????????????????????? - RQ75
		StatusOfEmploymentImport sttEmpA27 = employeeAdapter.getStatusOfEmployment(empA27.get().getEmployeeId(), baseDate);
		StatusOfEmploymentImport sttEmpA210 = null;
		if(displayA210){
			sttEmpA210 = employeeAdapter.getStatusOfEmployment(empA210.get().getEmployeeId(), baseDate);
		}
		//check A27
		if(sttEmpA27 != null && (sttEmpA27.getStatusOfEmployment().equals(StatusOfEmployment.RETIREMENT)||
				sttEmpA27.getStatusOfEmployment().equals(StatusOfEmployment.LEAVE_OF_ABSENCE)||
				sttEmpA27.getStatusOfEmployment().equals(StatusOfEmployment.HOLIDAY))){
			//????????????????????????OR????????????OR??????????????????
			//????????????????????????	???Msg_1511???
			result.setErrFlg(true);
			result.setErrA27(true);
			result.setMsgId("Msg_1511");
		}
		//check A210
		if(displayA210 && sttEmpA27 != null && (sttEmpA210.getStatusOfEmployment().equals(StatusOfEmployment.RETIREMENT)||
				sttEmpA210.getStatusOfEmployment().equals(StatusOfEmployment.LEAVE_OF_ABSENCE)||
				sttEmpA210.getStatusOfEmployment().equals(StatusOfEmployment.HOLIDAY))){
			//????????????????????????OR????????????OR??????????????????
			//????????????????????????	???Msg_1511???
			result.setErrFlg(true);
			result.setErrA210(true);
			result.setMsgId("Msg_1511");
		}
		if(result.isErrFlg()){
			return result;
		}
		//?????????????????????????????????????????????????????????????????? - RQ315
		Optional<EmployeeWithRangeLoginImport> roleA27 =  empWithRanAd.findByEmployeeByLoginRange(companyId, codeA27, baseDate);
		Optional<EmployeeWithRangeLoginImport> roleA210 =  Optional.empty();
		if(displayA210){
			roleA210 =  empWithRanAd.findByEmployeeByLoginRange(companyId, codeA210, baseDate);
		}
		if(!roleA27.isPresent() || (displayA210 && !roleA210.isPresent())){//???????????????????????????
			//????????????????????????	???Msg_1078???
			result.setErrFlg(true);
			result.setErrA27(!roleA27.isPresent());
			result.setErrA210(displayA210 && !roleA210.isPresent());
			result.setMsgId("Msg_1078");
			return result;
		}
		//????????????????????????????????????????????????????????????????????????????????????
		Optional<ApprovalSetting> appSetOpt = appSetRepo.getApprovalByComId(companyId);
		if (!appSetOpt.isPresent()) {
			return result;
		}
		ApprovalSetting appSet = appSetOpt.get();
		if (appSet.getPrinFlg().equals(PrincipalApprovalFlg.NOT_PRINCIPAL)) {
			// ??????????????????????????????????????????????????????????????????false(domain ?????????????????????????????????????????? = false)
			if (codeA16.equals(codeA27) || (displayA210 && codeA16.equals(codeA210))) {
				//????????????????????????ID???A1_6???????????????????????????????????????ID???A2_7???	??????	????????????????????????ID???A1_6???????????????????????????????????????ID???A2_10???
				//????????????????????????	???Msg_1487???
				result.setErrFlg(true);
				result.setErrA27(codeA16.equals(codeA27));
				result.setErrA210(displayA210 && codeA16.equals(codeA210));
				result.setMsgId("Msg_1487");
				return result;
			}
		}
		return result;
	}
	
	private List<ApprovalPhaseDto> convertDto(String approvalId){
		//get All Approval Phase by ApprovalId
		List<ApprovalPhase> lstAppPhase = this.repoAppPhase.getAllApprovalPhasebyCode(approvalId);
		return lstAppPhase.stream()
				.map(c -> new ApprovalPhaseDto(c.getApprovers().stream()
						.map(d -> {
								String name = c.getApprovalAtr().equals(ApprovalAtr.PERSON) ? 
								this.getPersonInfo(d.getEmployeeId()) == null ? "" : getPersonInfo(d.getEmployeeId()).getEmployeeName() : 	
								appRootCm.getJobGInfo(d.getJobGCD()) == null ? "" : appRootCm.getJobGInfo(d.getJobGCD()).getName();
								String confirmName = d.getConfirmPerson() == ConfirmPerson.CONFIRM ? "(??????)" : "";
								String empCode = c.getApprovalAtr().equals(ApprovalAtr.PERSON) ? 
										this.getPersonInfo(d.getEmployeeId()) == null ? "" : getPersonInfo(d.getEmployeeId()).getEmployeeCode() : null;
								return ApproverDto.fromDomain(d, name, confirmName, empCode);
						}).collect(Collectors.toList()),
						c.getApprovalId(), c.getPhaseOrder(),
					 c.getApprovalForm().value, c.getApprovalForm().getName(), c.getBrowsingPhase(),
					 c.getApprovalAtr().value))
				.collect(Collectors.toList());
	}

}
