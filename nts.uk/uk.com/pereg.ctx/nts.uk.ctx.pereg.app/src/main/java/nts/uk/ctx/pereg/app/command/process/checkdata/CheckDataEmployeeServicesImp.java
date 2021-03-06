package nts.uk.ctx.pereg.app.command.process.checkdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.task.data.TaskDataSetter;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfo;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfoRepository;
import nts.uk.ctx.pereg.app.find.layoutdef.classification.GridLayoutPersonInfoClsDto;
import nts.uk.ctx.pereg.app.find.processor.PeregProcessor;
import nts.uk.ctx.pereg.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.pereg.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.pereg.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.pereg.dom.person.info.item.PersonInfoItemDefinition;
import nts.uk.ctx.pereg.dom.person.setting.validatecheck.PerInfoValidChkCtgRepository;
import nts.uk.ctx.pereg.dom.person.setting.validatecheck.PerInfoValidateCheckCategory;
import nts.uk.query.model.employee.RegulationInfoEmployee;
import nts.uk.query.model.employee.RegulationInfoEmployeeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.system.config.InstalledProduct;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;
import nts.uk.shr.pereg.app.find.PeregEmpInfoQuery;

@Stateless
public class CheckDataEmployeeServicesImp implements CheckDataEmployeeServices {

	@Inject
	private RegulationInfoEmployeeRepository regulationInfoEmployeeRepo;
	@Inject
	private PerInfoCategoryRepositoty perInfoCtgRepo;
	@Inject
	private PerInfoValidChkCtgRepository perInfoCheckCtgRepo;
	@Inject
	private PerInfoItemDefRepositoty perInfotemDfRepo;
	@Inject
	private PeregProcessor peregProcessor;
	@Inject
	I18NResourcesForUK ukResouce;
	@Inject 
	private EmployeeDataMngInfoRepository empDataMngInfoRepo;
	@Inject 
	private CheckPersonInfoProcess checkPersonInfoProcess;
	@Inject 
	private CheckMasterProcess checkMasterProcess;
	
	/** The Constant TIME_DAY_START. */
	public static final String TIME_DAY_START = " 00:00:00";

	/** The Constant DATE_TIME_FORMAT. */
	public static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
	/** The Constant DEFAULT_VALUE. */
	private static final int DEFAULT_VALUE = 0;
	final String cancelRequest = TextResource.localize("CPS013_51");

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public <C> void manager(CheckDataFromUI excuteCommand, AsyncCommandHandlerContext<C> async) {

		// ???????????? ????????????
		val dataSetter = async.getDataSetter();
		// ????????????????????????????????? (l???y system date)
		dataSetter.setData("startTime", GeneralDateTime.now().toString());
		
		// ????????????????????????????????????????????????????????????????????????????????????????????????
		// (th???c thi thu???t to??n ???Search employee theo ??i???u ki???n th??ng tin c?? nh??n, v?? thay ?????i th??? t??????
		List<EmployeeResultDto> listEmp = this.findEmployeesInfo(excuteCommand);
		if(CollectionUtil.isEmpty(listEmp)) {
			throw new BusinessException("Msg_1564");
		}
		
		Map<String, String> mapSIdWthBussinessName = listEmp.stream().collect(Collectors.toMap(e -> e.sid, e -> e.bussinessName));
		
		List<String> sids = listEmp.stream().map(mapper -> mapper.sid).collect(Collectors.toList());
		
		List<EmployeeDataMngInfo> listEmpData = this.empDataMngInfoRepo.findByListEmployeeId(sids);
		
		List<EmployeeDataMngInfo> listEmpDataOrder = listEmpData.stream()
				.sorted(Comparator.comparing(EmployeeDataMngInfo::getEmployeeCode))
				.collect(Collectors.toList());
		
		// ????????????????????????????????????????????????????????????????????? (Th???c hi???n thu???t to??n ???L???y PersonInfoCategory???)
		Map<PersonInfoCategory, List<PersonInfoItemDefinition>> mapCategoryWithListItemDf = this.getAllCategory(AppContexts.user().companyId());
		
		List<ErrorInfoCPS013> listError = new ArrayList<>();
		
		int countError = DEFAULT_VALUE;
		dataSetter.setData("numberEmpChecked", 0);
		dataSetter.setData("countEmp", listEmpData.size());
		dataSetter.setData("statusCheck", ExecutionStatusCps013.PROCESSING.name);
		
		//?????????????????????????????????????????????????????????????????? (Th???c hi???n thu???t to??n ???X??? l?? check t??nh h???p l??????)
		for (int i = 0; i < listEmpDataOrder.size(); i++) {
			
			// check request cancel
			if (async.hasBeenRequestedToCancel()) {
				dataSetter.updateData("statusCheck", cancelRequest);
				dataSetter.setData("endTimeForRequestedToCancel", GeneralDateTime.now().toString());
				async.finishedAsCancelled();
				return ;
			}
			
			EmployeeDataMngInfo employee = listEmpDataOrder.get(i);
			
			String bussinessName = mapSIdWthBussinessName.get(employee.getEmployeeId());
			
			PeregEmpInfoQuery empCheck = new PeregEmpInfoQuery(employee.getPersonId(), employee.getEmployeeId(), null);
			
			//????????????????????????????????? (L???y data ?????i t?????ng check)
			Map<String, List<GridLayoutPersonInfoClsDto>> dataOfEmployee =  this.peregProcessor.getFullCategoryDetailByListEmp(Arrays.asList(empCheck), mapCategoryWithListItemDf);
			
			checkDataOfEmp(empCheck, dataOfEmployee, excuteCommand, mapCategoryWithListItemDf, employee, bussinessName, dataSetter, listError);
			
			countError += 1;
			
			dataSetter.updateData("numberEmpChecked", countError);
		}
		
		if (listError.isEmpty()) {
			dataSetter.updateData("statusCheck", ExecutionStatusCps013.DONE.name);
		} else {
			dataSetter.updateData("statusCheck", ExecutionStatusCps013.DONE_WITH_ERROR.name);
		}
	}

	private void checkDataOfEmp(PeregEmpInfoQuery empCheck, Map<String, List<GridLayoutPersonInfoClsDto>> dataOfEmployee, 
			CheckDataFromUI excuteCommand,Map<PersonInfoCategory, List<PersonInfoItemDefinition>> mapCategoryWithListItemDf, EmployeeDataMngInfo employee, String bussinessName, TaskDataSetter dataSetter, List<ErrorInfoCPS013> listError) {
		
		if (excuteCommand.isMasterCheck() && excuteCommand.isPerInfoCheck()) {
			checkMasterAndPersonInfo(empCheck, dataOfEmployee, excuteCommand, mapCategoryWithListItemDf, employee, bussinessName, dataSetter, listError);
		} else if (excuteCommand.isPerInfoCheck()) {
			this.checkPersonInfoProcess.checkPersonInfo(empCheck, dataOfEmployee, excuteCommand, mapCategoryWithListItemDf, employee, bussinessName, dataSetter, listError);
		} else if (excuteCommand.isMasterCheck()) {
			this.checkMasterProcess.checkMaster(empCheck, dataOfEmployee, excuteCommand, mapCategoryWithListItemDf, employee, bussinessName, dataSetter, listError);
		}
	}

	private void checkMasterAndPersonInfo(PeregEmpInfoQuery empCheck, Map<String, List<GridLayoutPersonInfoClsDto>> dataOfEmployee,
			CheckDataFromUI excuteCommand, Map<PersonInfoCategory, List<PersonInfoItemDefinition>> mapCategoryWithListItemDf, EmployeeDataMngInfo employee, String bussinessName,  TaskDataSetter dataSetter, List<ErrorInfoCPS013> listError) {
		
		// ?????????????????????????????? (Check th??ng tin c?? nh??n c?? b???n)
		this.checkPersonInfoProcess.checkPersonInfo(empCheck, dataOfEmployee, excuteCommand, mapCategoryWithListItemDf, employee, bussinessName, dataSetter, listError);

		// ?????????????????????????????????(Filter Category ?????i t?????ng check)
		/**
		 * Loai bo category doi tuong duoi day ra khoi list category doi tuongTruong hop [Personal information integrity check category].
		 * [Employment system required]:True or 
		 * [Human Resources System Required]:True or 
		 * [Salary System Required]:True
		 * 
		 * ???Vi viec kiem tra System required category da duoc check trong???Personal Basic Information Check???, nen khong can hien thi error message 2 lan nua
		 */
		
		List<PersonInfoCategory> listCategory = new ArrayList<>(mapCategoryWithListItemDf.keySet()); 
		
		List<String> listCategoryCode = listCategory.stream().map(m -> m.getCategoryCode().v()).collect(Collectors.toList());
		
		List<PerInfoValidateCheckCategory> lstCtgSetting = this.perInfoCheckCtgRepo.getListPerInfoValidByListCtgId(listCategoryCode, AppContexts.user().contractCode());
		
		List<PerInfoValidateCheckCategory> listCategorySystemFilter = lstCtgSetting.stream().filter(ctg -> {
			if ((ctg.getHumanSysReq().value == NotUseAtr.USE.value) || (ctg.getPaySysReq().value == NotUseAtr.USE.value)
			|| (ctg.getJobSysReq().value == NotUseAtr.USE.value)) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());
		
		List<PerInfoValidateCheckCategory> listCategoryNotSystemFilter = lstCtgSetting.stream()
                .filter(i -> !listCategorySystemFilter.contains(i))
                .collect (Collectors.toList());
		
		List<String> listCtgNotSystemFilterCode = listCategoryNotSystemFilter.stream().map(m -> m.getCategoryCd().v()).collect(Collectors.toList());
		
		Map<PersonInfoCategory, List<PersonInfoItemDefinition>> mapCategoryWithListItemDfNew = mapCategoryWithListItemDf.entrySet().stream()
				.filter(x -> listCtgNotSystemFilterCode.contains(x.getKey().getCategoryCode().v()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		
		// ????????????????????? (check master)
		this.checkMasterProcess.checkMaster(empCheck, dataOfEmployee, excuteCommand, mapCategoryWithListItemDfNew, employee, bussinessName, dataSetter, listError);
		
	}

	// ????????????????????????????????????????????????????????????????????????????????????????????????
	// (th???c thi thu???t to??n ???Search employee theo ??i???u ki???n th??ng tin c?? nh??n, v?? thay ?????i th??? t??????)
	private List<EmployeeResultDto> findEmployeesInfo(CheckDataFromUI query) {
		EmpQueryDto queryDto = new EmpQueryDto();
		GeneralDateTime baseDate = GeneralDateTime.fromString(query.getDateTime() + TIME_DAY_START, DATE_TIME_FORMAT);
		return this.regulationInfoEmployeeRepo.find(AppContexts.user().companyId(), queryDto.toQueryModel(baseDate))
				.stream().map(model -> this.toEmployeeDto(model)).collect(Collectors.toList());
	}

	private EmployeeResultDto toEmployeeDto(RegulationInfoEmployee model) {
		return new EmployeeResultDto(model.getEmployeeID(), model.getEmployeeCode(), model.getName().orElse(""));
	}

	// ????????????????????????????????????????????????????????????????????? (Th???c hi???n thu???t to??n ???L???y PersonInfoCategory???)
	public Map<PersonInfoCategory, List<PersonInfoItemDefinition>> getAllCategory(String cid) {
		// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		// (Th???c hi???n thu???t to??n ???T??? SystemUseAtr, l???y to??n b???
		// PersonInfoCategory c?? th??? s??? d???ng???)
		int forAttendance = nts.uk.ctx.at.schedule.dom.plannedyearholiday.frame.NotUseAtr.NOT_USE.value;
		int forPayroll = nts.uk.ctx.at.schedule.dom.plannedyearholiday.frame.NotUseAtr.NOT_USE.value;
		int forPersonnel = nts.uk.ctx.at.schedule.dom.plannedyearholiday.frame.NotUseAtr.NOT_USE.value;
		List<InstalledProduct> installProduct = AppContexts.system().getInstalledProducts();
		for (InstalledProduct productType : installProduct) {
			switch (productType.getProductType()) {
			case ATTENDANCE:
				forAttendance = NotUseAtr.USE.value;
				break;
			case PAYROLL:
				forPayroll = NotUseAtr.USE.value;
				break;
			case PERSONNEL:
				forPersonnel = NotUseAtr.USE.value;
				break;
			default:
				break;
			}
		}

		String contracCd = AppContexts.user().contractCode();

		// trong c??u query ???? l???c ra nh???ng category kh??ng c?? item n??o r???i.
		List<PersonInfoCategory> lstCtg = perInfoCtgRepo.getAllCategoryForCPS013(cid, forAttendance, forPayroll,
				forPersonnel);

		// ?????????????????????????????????????????????????????????????????????????????????????????????
		// (Get to??n b??? domain model ???PerInfoValidChkCtg???)
		// ???????????????????????????????????????????????????
		// (Check s??? data item ?????i t?????ng check)
		if (lstCtg.isEmpty()) {
			throw new BusinessException("Msg_930");
		}

		List<String> listCategoryCode = lstCtg.stream().map(i -> i.getCategoryCode().toString())
				.collect(Collectors.toList());

		List<PerInfoValidateCheckCategory> lstCtgCheck = this.perInfoCheckCtgRepo
				.getListPerInfoValidByListCtgId(listCategoryCode, contracCd);

		Map<PersonInfoCategory, List<PersonInfoItemDefinition>> mapCategoryWithListItemDf = new HashMap<>();

		lstCtgCheck.forEach(ctg -> {
			PersonInfoCategory category = lstCtg.stream()
					.filter(i -> i.getCategoryCode().toString().equals(ctg.getCategoryCd().toString())).findFirst()
					.get();
			List<PersonInfoItemDefinition> listItemDf = this.perInfotemDfRepo.getAllPerInfoItemDefByCategoryIdCPS013(
					category.getPersonInfoCategoryId(), AppContexts.user().contractCode());
			if (!listItemDf.isEmpty()) {
				mapCategoryWithListItemDf.put(category, listItemDf);
			}
		});

		// ?????????????????????????????????????????????????????? (filter category l?? ?????i t?????ng check)
		filterCategory(lstCtgCheck, mapCategoryWithListItemDf);

		return mapCategoryWithListItemDf;
	}

	public void filterCategory(List<PerInfoValidateCheckCategory> listCtgSetting, Map<PersonInfoCategory, List<PersonInfoItemDefinition>> mapCategoryWithListItemDf) {
		mapCategoryWithListItemDf.entrySet().forEach(ctg -> {
			PerInfoValidateCheckCategory ctgSetting = listCtgSetting.stream()
					.filter(i -> i.getCategoryCd().toString().equals(ctg.getKey().getCategoryCode().toString())).findFirst()
					.get();
			if (!checkCategory(ctgSetting)) {
				mapCategoryWithListItemDf.remove(ctg);
			}
		});
	}

	private boolean checkCategory(PerInfoValidateCheckCategory ctgSetting) {
		if ((ctgSetting.getHumanSysReq() == NotUseAtr.USE) || (ctgSetting.getJobSysReq() == NotUseAtr.USE)
				|| (ctgSetting.getPaySysReq() == NotUseAtr.USE) || (ctgSetting.getPayMngReq() == NotUseAtr.USE)
				|| (ctgSetting.getMonthCalcMngReq() == NotUseAtr.USE)
				|| (ctgSetting.getMonthActualMngReq() == NotUseAtr.USE)
				|| (ctgSetting.getBonusMngReq() == NotUseAtr.USE) || (ctgSetting.getScheduleMngReq() == NotUseAtr.USE)
				|| (ctgSetting.getDailyActualMngReq() == NotUseAtr.USE)
				|| (ctgSetting.getYearMngReq() == NotUseAtr.USE))
			return true;
		return false;
	}
}
