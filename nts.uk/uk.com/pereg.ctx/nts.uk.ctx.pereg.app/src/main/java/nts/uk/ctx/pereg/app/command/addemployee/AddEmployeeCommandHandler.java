package nts.uk.ctx.pereg.app.command.addemployee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EnumType;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.gul.security.hash.password.PasswordHash;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.shared.app.command.shortworktime.AddShortWorkTimeCommand;
import nts.uk.ctx.at.shared.app.command.workingcondition.AddWorkingConditionCommand;
import nts.uk.ctx.at.shared.app.command.workingcondition.AddWorkingConditionCommandAssembler;
import nts.uk.ctx.bs.employee.dom.empfilemanagement.EmpFileManagementRepository;
import nts.uk.ctx.bs.employee.dom.empfilemanagement.PersonFileManagement;
import nts.uk.ctx.bs.employee.dom.empfilemanagement.TypeFile;
import nts.uk.ctx.pereg.dom.person.info.category.CategoryType;
import nts.uk.ctx.pereg.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.pereg.dom.person.info.item.PersonInfoItemDefinitionSimple;
import nts.uk.ctx.pereg.dom.reghistory.EmpRegHistory;
import nts.uk.ctx.pereg.dom.reghistory.EmpRegHistoryRepository;
import nts.uk.ctx.sys.auth.dom.user.User;
import nts.uk.ctx.sys.auth.dom.user.UserRepository;
import nts.uk.ctx.sys.log.app.command.pereg.KeySetCorrectionLog;
import nts.uk.ctx.sys.log.app.command.pereg.PersonCategoryCorrectionLogParameter;
import nts.uk.ctx.sys.log.app.command.pereg.PersonCategoryCorrectionLogParameter.CategoryCorrectionTarget;
import nts.uk.ctx.sys.log.app.command.pereg.PersonCategoryCorrectionLogParameter.PersonCorrectionItemInfo;
import nts.uk.ctx.sys.log.app.command.pereg.PersonCorrectionLogParameter;
import nts.uk.ctx.sys.log.app.command.pereg.PersonCorrectionLogParameter.PersonCorrectionTarget;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.security.audittrail.correction.DataCorrectionContext;
import nts.uk.shr.com.security.audittrail.correction.content.TargetDataKey;
import nts.uk.shr.com.security.audittrail.correction.content.TargetDataKey.CalendarKeyType;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.InfoOperateAttr;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.PersonInfoProcessAttr;
import nts.uk.shr.com.security.audittrail.correction.processor.CorrectionProcessorId;
import nts.uk.shr.pereg.app.ItemValue;
import nts.uk.shr.pereg.app.SaveDataType;
import nts.uk.shr.pereg.app.command.ItemsByCategory;

/**
 * @author sonnlb
 *
 */
@Stateless
public class AddEmployeeCommandHandler extends CommandHandlerWithResult<AddEmployeeCommand, String> {

	@Inject
	private AddEmployeeCommandHelper helper;

	@Inject
	private AddEmployeeCommandFacade commandFacade;

	@Inject
	private AddWorkingConditionCommandAssembler wkCodAs;
	
	@Inject
	private PerInfoItemDefRepositoty perInfoItemRepo;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private EmpFileManagementRepository perFileManagementRepository;
	
	@Inject
	private EmpRegHistoryRepository empHisRepo;
	
	
	private static final List<String> historyCategoryCodeList = Arrays.asList("CS00004", "CS00014", "CS00016", "CS00017", "CS00018",
			"CS00019", "CS00020", "CS00021");
			       
	private static final Map<String, String> startDateItemCodes;
	static {
		Map<String, String> aMap = new HashMap<>();
		// 分類１
		aMap.put("CS00004", "IS00026");
		// 雇用
		aMap.put("CS00014", "IS00066");
		// 職位本務
		aMap.put("CS00016", "IS00077");
		// 職場
		aMap.put("CS00017", "IS00082");
		// 休職休業
		aMap.put("CS00018", "IS00087");
		// 短時間勤務
		aMap.put("CS00019", "IS00102");
		// 労働条件
		aMap.put("CS00020", "IS00119");
		//勤務種別
		aMap.put("CS00021", "IS00255");

		startDateItemCodes = Collections.unmodifiableMap(aMap);
	}
	
	private static final Map<String, String> endDateItemCodes;
	static {
		Map<String, String> aMap = new HashMap<>();
		// 分類１
		aMap.put("CS00004", "IS00027");
		// 雇用
		aMap.put("CS00014", "IS00067");
		// 職位本務
		aMap.put("CS00016", "IS00078");
		// 職場
		aMap.put("CS00017", "IS00083");
		// 休職休業
		aMap.put("CS00018", "IS00088");
		// 短時間勤務
		aMap.put("CS00019", "IS00103");
		// 労働条件
		aMap.put("CS00020", "IS00120");
		//勤務種別
		aMap.put("CS00021", "IS00256");

		endDateItemCodes = Collections.unmodifiableMap(aMap);
	}
	
	@Override
	protected String handle(CommandHandlerContext<AddEmployeeCommand> context) {
		DataCorrectionContext.transactionBegun(CorrectionProcessorId.PEREG_REGISTER, -98);
		val command = context.getCommand();
		String employeeId = IdentifierUtil.randomUniqueId();
		String userId = IdentifierUtil.randomUniqueId();
		String personId = IdentifierUtil.randomUniqueId();
		String companyId = AppContexts.user().companyId();
		String comHistId = IdentifierUtil.randomUniqueId();

		List<ItemsByCategory> inputs = commandFacade.createData(command);

		validateTime(inputs, employeeId, personId);
		checkRequiredInputs(inputs, employeeId, personId, companyId);
		
		processHistoryPeriod(inputs, command.getHireDate());

		helper.addBasicData(command, personId, employeeId, comHistId, companyId);
		commandFacade.addNewFromInputs(personId, employeeId, comHistId, inputs);
		
		addNewUser(personId, command, userId);
		
		addAvatar(personId, command.getAvatarId());
		
		updateEmployeeRegHist(companyId, employeeId);
		
		setParamsForCorrection(command, inputs, employeeId, userId);
		DataCorrectionContext.transactionFinishing(-98);
		return employeeId;

	}
	
	private void setParamsForCorrection(AddEmployeeCommand command, List<ItemsByCategory> inputs, String employeeId,String userId) {
		// set PeregCorrectionLogParameter
		PersonCorrectionTarget target = new PersonCorrectionTarget(
				userId,
				employeeId, 
				command.getEmployeeName(),
			    PersonInfoProcessAttr.ADD, null);

		// set correction log
		PersonCorrectionLogParameter correction = new PersonCorrectionLogParameter(Arrays.asList(target));
		DataCorrectionContext.setParameter(String.valueOf(KeySetCorrectionLog.PERSON_CORRECTION_LOG.value), correction);

		List<CategoryCorrectionTarget> ctgTargets = new ArrayList<>();

		for (ItemsByCategory input : inputs) {
			// prepare data
			GeneralDate startDateItemCode = null;
			String itemCode = null;
			if (historyCategoryCodeList.contains(input.getCategoryCd())) {
				itemCode = startDateItemCodes.get(input.getCategoryCd());
			}
			
			List<PersonCorrectionItemInfo> lstItemInfo = new ArrayList<>();
			for (ItemValue item : input.getItems()) {
				if(item.itemCode().equals(itemCode)) {
					startDateItemCode = item.value();
				}
				lstItemInfo.add(new PersonCorrectionItemInfo(item.definitionId(), item.itemName(), null, item.stringValue(),
						item.saveDataType().value));
			}
			CategoryType ctgType = EnumAdaptor.valueOf(input.getCategoryType(), CategoryType.class);
			
			//Add category correction data
			CategoryCorrectionTarget ctgTarget = null;
			switch (ctgType) {
			case SINGLEINFO:
				ctgTarget = new CategoryCorrectionTarget(input.getCategoryName(), InfoOperateAttr.ADD, lstItemInfo, new TargetDataKey(CalendarKeyType.NONE, null, null), null);
				break;
			case MULTIINFO:
				ctgTarget = new CategoryCorrectionTarget(input.getCategoryName(), InfoOperateAttr.ADD, lstItemInfo, new TargetDataKey(CalendarKeyType.NONE, null, command.getCardNo()), null);
				break;
			case CONTINUOUSHISTORY:
			case NODUPLICATEHISTORY:
			case DUPLICATEHISTORY:
				ctgTarget = new CategoryCorrectionTarget(input.getCategoryName(), InfoOperateAttr.ADD_HISTORY, lstItemInfo, new TargetDataKey(CalendarKeyType.DATE, startDateItemCode, null), null);
				break;
			default:
				break;
			}
			ctgTargets.add(ctgTarget);
			
		}
		
		PersonCategoryCorrectionLogParameter personCtg = new PersonCategoryCorrectionLogParameter(ctgTargets);
		DataCorrectionContext.setParameter(String.valueOf(KeySetCorrectionLog.CATEGORY_CORRECTION_LOG.value), personCtg);

	}


	private void checkRequiredInputs(List<ItemsByCategory> inputs, String employeeId, String personId,
			String companyId) {

		List<String> ctgCodes = inputs.stream().map(x -> x.getCategoryCd()).collect(Collectors.toList());

		// làm phẳng data truyền vào để dễ thao tác
		List<ItemValue> items = new ArrayList<ItemValue>();

		inputs.forEach(ctg -> {

			items.addAll(ctg.getItems());

		});
		// lấy item system required để so sánh
		List<PersonInfoItemDefinitionSimple> requiredItems = perInfoItemRepo
				.getRequiredItemFromCtgCdLst(AppContexts.user().contractCode(), companyId, ctgCodes);

		List<String> nodataItems = new ArrayList<String>();
		requiredItems.forEach(item -> {
			Optional<ItemValue> requiredItemOpt = items.stream()
					.filter(x -> x.itemCode().equals(item.getItemCode().v())).findFirst();
			// kiểm tra item đó có trong data list truyền vào không
			if (requiredItemOpt.isPresent()) {
				ItemValue requiredItem = requiredItemOpt.get();
				// kiểm tra xem giá trị của nó có bị null không
				if (requiredItem.value() == null) {
					// nếu null thì thêm nó vào list lỗi
					nodataItems.add(item.getItemName().v());
				}

			} else {
				nodataItems.add(item.getItemName().v());
			}
		});
		// kiểm tra list lỗi để trả về thông báo
		if (!CollectionUtil.isEmpty(nodataItems)) {
			throw new BusinessException("Msg_925", String.join(",", nodataItems));

		}

	}

	private void validateTime(List<ItemsByCategory> inputs, String employeeId, String personId) {
		Optional<ItemsByCategory> shortWkOpt = inputs.stream().filter(ctg -> ctg.getCategoryCd().equals("CS00019"))
				.findFirst();
		if (shortWkOpt.isPresent()) {
			AddShortWorkTimeCommand shortWk = (AddShortWorkTimeCommand) shortWkOpt.get()
					.createCommandForSystemDomain(personId, employeeId, AddShortWorkTimeCommand.class);
			shortWk.getLstTimeSlot();
		}

		Optional<ItemsByCategory> wkCodOpt = inputs.stream().filter(ctg -> ctg.getCategoryCd().equals("CS00020"))
				.findFirst();
		if (wkCodOpt.isPresent()) {
			AddWorkingConditionCommand wkCod = (AddWorkingConditionCommand) wkCodOpt.get()
					.createCommandForSystemDomain(personId, employeeId, AddWorkingConditionCommand.class);
			wkCodAs.fromDTO(null, wkCod);
		}

	}
	
	private void processHistoryPeriod(List<ItemsByCategory> inputs, GeneralDate hireDate) {
		inputs.forEach( category -> {
			if (historyCategoryCodeList.contains(category.getCategoryCd())) {
				String startDateItemCode = startDateItemCodes.get(category.getCategoryCd());
				String endDateItemCode = endDateItemCodes.get(category.getCategoryCd());
				
				if (!category.getItems().stream().anyMatch(item -> item.itemCode().equals(startDateItemCode))) {
					category.getItems().add(new ItemValue("", startDateItemCode,"", hireDate.toString(), 3));
				} 
				
				if (!category.getItems().stream().anyMatch(item -> item.itemCode().equals(endDateItemCode))) {
					category.getItems().add(new ItemValue("", endDateItemCode,"", GeneralDate.max().toString(), 3));
				} 
					
			}
		});
	}
	
	private void addNewUser(String personId, AddEmployeeCommand command, String userId) {
		// add new user
		String passwordHash = PasswordHash.generate(command.getPassword(), userId);
		User newUser = User.createFromJavatype(userId, false, passwordHash, command.getLoginId(),
				AppContexts.user().contractCode(), GeneralDate.max(), 0, 0, "",
				command.getEmployeeName(), personId, 1);

		this.userRepository.addNewUser(newUser);

	}
	
	private void addAvatar(String personId, String avatarId) {
		if (avatarId != "") {
			PersonFileManagement perFile = PersonFileManagement.createFromJavaType(personId, avatarId,
					TypeFile.AVATAR_FILE.value, null);

			this.perFileManagementRepository.insert(perFile);
		}

	}
	
	private void updateEmployeeRegHist(String companyId, String employeeId) {

		String currentEmpId = AppContexts.user().employeeId();

		Optional<EmpRegHistory> optRegHist = this.empHisRepo.getRegHistById(currentEmpId);

		EmpRegHistory newEmpRegHistory = EmpRegHistory.createFromJavaType(currentEmpId, companyId,
				GeneralDateTime.now(), employeeId, "");

		if (optRegHist.isPresent()) {

			this.empHisRepo.update(newEmpRegHistory);

		} else {

			this.empHisRepo.add(newEmpRegHistory);

		}

	}

}
