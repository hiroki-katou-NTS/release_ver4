package nts.uk.ctx.pereg.app.command.addemployee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.gul.security.hash.password.PasswordHash;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.bs.employee.dom.empfilemanagement.EmpFileManagementRepository;
import nts.uk.ctx.bs.employee.dom.empfilemanagement.PersonFileManagement;
import nts.uk.ctx.bs.employee.dom.empfilemanagement.TypeFile;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHist;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistByEmployee;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistItem;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistRepository;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyInfo;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyInfoRepository;
import nts.uk.ctx.bs.employee.dom.employee.history.RecruitmentClassification;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfo;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfoRepository;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDeletionAttr;
import nts.uk.ctx.bs.person.dom.person.info.BloodType;
import nts.uk.ctx.bs.person.dom.person.info.GenderPerson;
import nts.uk.ctx.bs.person.dom.person.info.Person;
import nts.uk.ctx.bs.person.dom.person.info.PersonRepository;
import nts.uk.ctx.pereg.app.command.facade.PeregCommandFacade;
import nts.uk.ctx.pereg.app.find.initsetting.item.SettingItemDto;
import nts.uk.ctx.pereg.app.find.layout.RegisterLayoutFinder;
import nts.uk.ctx.pereg.dom.reghistory.EmpRegHistory;
import nts.uk.ctx.pereg.dom.reghistory.EmpRegHistoryRepository;
import nts.uk.ctx.sys.auth.dom.user.User;
import nts.uk.ctx.sys.auth.dom.user.UserRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.ItemValue;
import nts.uk.shr.pereg.app.command.ItemsByCategory;
import nts.uk.shr.pereg.app.command.PeregInputContainer;

/**
 * @author sonnlb
 *
 */
@RequestScoped
public class AddEmployeeCommandHandler extends CommandHandlerWithResult<AddEmployeeCommand, String> {
	@Inject
	private RegisterLayoutFinder layoutFinder;

	@Inject
	private PeregCommandFacade commandFacade;

	@Inject
	private UserRepository userRepository;

	@Inject
	private EmpFileManagementRepository perFileManagementRepository;

	@Inject
	private EmpRegHistoryRepository empHisRepo;

	@Inject
	private AffCompanyHistRepository companyHistRepo;

	@Inject
	private AffCompanyInfoRepository companyInfoRepo;

	@Inject
	private EmployeeDataMngInfoRepository empDataRepo;

	@Inject
	private PersonRepository personRepo;

	AddEmployeeCommand command;
	String employeeId;
	String userId;
	String personId;
	List<ItemsByCategory> inputs;
	String companyId;
	String comHistId;

	List<String> fixedCtgList = Arrays.asList("CS00001", "CS00002", "CS00003");

	@Override
	protected String handle(CommandHandlerContext<AddEmployeeCommand> context) {

		command = context.getCommand();

		employeeId = IdentifierUtil.randomUniqueId();

		userId = IdentifierUtil.randomUniqueId();

		personId = IdentifierUtil.randomUniqueId();

		companyId = AppContexts.user().companyId();

		comHistId = IdentifierUtil.randomUniqueId();

		addBasicData();

		// call commandFacade

		inputsProcess();

		return employeeId;

	}

	@Transactional
	private void addBasicData() {

		// add newPerson

		addNewPerson();

		// addmngInfo

		addEmployeeDataMngInfo();

		// add AffCompanyHist

		addAffCompanyHist();

		// add new User
		addNewUser();

		// register avatar

		addAvatar();

		// Update employee registration history
		updateEmployeeRegHist();
	}

	private void addNewPerson() {

		Person newPerson = Person.createFromJavaType(GeneralDate.min(), BloodType.Unselected.value,
				GenderPerson.Male.value, personId, "", "", command.getEmployeeName(), "", "", "", "", "", "", "", "",
				"", "", "");

		this.personRepo.addNewPerson(newPerson);

	}

	@Transactional
	private void inputsProcess() {

		List<SettingItemDto> dataServer = this.layoutFinder.itemListByCreateType(command);

		// merge data from client with dataServer
		mergeData(dataServer, command.getInputs());

		inputs = new ArrayList<ItemsByCategory>();
		List<String> categoryCodeList = commandFacade.getAddCategoryCodeList();
		dataServer.forEach(x -> {

			if (categoryCodeList.indexOf(x.getCategoryCode()) == -1 && x.getCategoryCode().charAt(1) == 'O') {

				categoryCodeList.add(x.getCategoryCode());

			}
		});

		categoryCodeList.forEach(categoryCd -> {

			ItemsByCategory newCtg = createNewItemsByCategoryCode(dataServer, categoryCd);
			if (newCtg != null) {

				inputs.add(newCtg);
			}

		});

		// update data
		List<ItemsByCategory> fixedInputs = inputs.stream().filter(x -> fixedCtgList.indexOf(x.getCategoryCd()) != -1)
				.collect(Collectors.toList());

		if (!CollectionUtil.isEmpty(fixedInputs)) {

			addOptinalInputs(fixedInputs);

			PeregInputContainer updateContainer = new PeregInputContainer(personId, employeeId, fixedInputs);

			this.commandFacade.update(updateContainer);

		}
		inputs = inputs.stream().filter(x -> fixedCtgList.indexOf(x.getCategoryCd()) == -1)
				.collect(Collectors.toList());
		// call add commandFacade
		PeregInputContainer addContainer = new PeregInputContainer(personId, employeeId, inputs);

		this.commandFacade.add(addContainer);
	}

	@Transactional
	private void addOptinalInputs(List<ItemsByCategory> fixedInputs) {
		List<ItemsByCategory> addInputs = new ArrayList<ItemsByCategory>();
		addInputs = fixedInputs;

		addInputs.forEach(ctg -> ctg.setItems(
				ctg.getItems().stream().filter(item -> item.itemCode().charAt(1) == 'O').collect(Collectors.toList())));

		PeregInputContainer addContainer = new PeregInputContainer(personId, employeeId, addInputs);

		this.commandFacade.add(addContainer);

	}

	private void addEmployeeDataMngInfo() {
		// check duplicate employeeCode
		List<EmployeeDataMngInfo> infoList = this.empDataRepo
				.getEmployeeNotDeleteInCompany(AppContexts.user().companyId(), command.getEmployeeCode());

		if (!CollectionUtil.isEmpty(infoList)) {
			throw new BusinessException("Msg_345");
		}
		// add system data
		this.empDataRepo.add(EmployeeDataMngInfo.createFromJavaType(companyId, personId, employeeId,
				command.getEmployeeCode(), EmployeeDeletionAttr.NOTDELETED.value, GeneralDateTime.min(), "", ""));

	}

	private void addAffCompanyHist() {
		List<AffCompanyHistByEmployee> comHistList = new ArrayList<AffCompanyHistByEmployee>();

		List<AffCompanyHistItem> comHistItemList = new ArrayList<AffCompanyHistItem>();

		comHistItemList.add(
				new AffCompanyHistItem(comHistId, false, new DatePeriod(command.getHireDate(), GeneralDate.max())));

		comHistList.add(new AffCompanyHistByEmployee(employeeId, comHistItemList));

		AffCompanyHist newComHist = new AffCompanyHist(personId, comHistList);

		this.companyHistRepo.add(newComHist);

		AffCompanyInfo newComInfo = AffCompanyInfo.createFromJavaType(comHistId, "", GeneralDate.max(),
				GeneralDate.max());

		this.companyInfoRepo.add(newComInfo);

	}

	private void addAvatar() {
		if (command.getAvatarId() != "") {
			PersonFileManagement perFile = PersonFileManagement.createFromJavaType(personId, command.getAvatarId(),
					TypeFile.AVATAR_FILE.value, null);

			this.perFileManagementRepository.insert(perFile);
		}

	}

	private void updateEmployeeRegHist() {

		String currentEmpId = AppContexts.user().employeeId();

		Optional<EmpRegHistory> optRegHist = this.empHisRepo.getLastRegHistory(currentEmpId);

		EmpRegHistory newEmpRegHistory = EmpRegHistory.createFromJavaType(currentEmpId, companyId, GeneralDate.today(),
				employeeId, "");

		if (optRegHist.isPresent()) {

			this.empHisRepo.update(newEmpRegHistory);

		} else {

			this.empHisRepo.add(newEmpRegHistory);

		}

	}

	private void addNewUser() {
		// add new user
		String passwordHash = PasswordHash.generate(command.getPassword(), userId);
		User newUser = User.createFromJavatype(userId, false, passwordHash, command.getLoginId(),
				AppContexts.user().contractCode(), GeneralDate.fromString("9999/12/31", "yyyy/MM/dd"), 0, 0, "",
				command.getEmployeeName(), employeeId);

		this.userRepository.addNewUser(newUser);

	}

	private void mergeData(List<SettingItemDto> dataList, List<ItemsByCategory> inputs) {

		dataList.forEach(x -> {

			String StringData = getItemValueById(inputs, x.getItemCode());

			if (StringData != null) {
				x.setSaveData(SettingItemDto.createSaveDataDto(x.getSaveData().getSaveDataType().value,
						getItemValueById(inputs, x.getItemCode())));
			}
		});

	}

	private String getItemValueById(List<ItemsByCategory> inputs, String itemCode) {
		String returnString = null;

		for (ItemsByCategory ctg : inputs) {

			Optional<ItemValue> optItem = ctg.getItems().stream().filter(x -> x.itemCode().equals(itemCode))
					.findFirst();
			if (optItem.isPresent()) {
				if (optItem.get().value() != null) {
					returnString = optItem.get().value().toString();
				}
				break;
			}

		}
		return returnString;

	}

	private ItemsByCategory createNewItemsByCategoryCode(List<SettingItemDto> dataList, String categoryCd) {

		List<ItemValue> items = new ArrayList<ItemValue>();
		getAllItemInCategoryByCode(dataList, categoryCd).forEach(item -> {
			items.add(new ItemValue(item.getItemDefId(), item.getItemCode(), item.getValueAsString(),
					item.getDataType()));
		});
		if (CollectionUtil.isEmpty(items)) {
			return null;
		}
		String recordId = null;

		if (categoryCd == "CS00001") {

			recordId = employeeId;
		}

		if (categoryCd == "CS00002") {
			recordId = personId;
		}

		if (categoryCd == "CS00003") {
			recordId = comHistId;

		}

		return new ItemsByCategory(categoryCd, recordId, items);
	}

	private List<SettingItemDto> getAllItemInCategoryByCode(List<SettingItemDto> sourceList, String categoryCode) {
		return sourceList.stream().filter(x -> x.getCategoryCode().equals(categoryCode)).collect(Collectors.toList());
	}

}
