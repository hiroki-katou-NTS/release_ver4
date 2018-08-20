package nts.uk.ctx.pereg.app.command.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pereg.app.find.employee.category.EmpCtgFinder;
import nts.uk.ctx.pereg.app.find.processor.ItemDefFinder;
import nts.uk.ctx.pereg.dom.person.info.category.CategoryType;
import nts.uk.ctx.pereg.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.pereg.dom.person.info.category.dto.DateRangeDto;
import nts.uk.ctx.sys.auth.app.find.user.GetUserByEmpFinder;
import nts.uk.ctx.sys.auth.app.find.user.UserAuthDto;
import nts.uk.ctx.sys.log.app.command.pereg.PersonCategoryCorrectionLogParameter;
import nts.uk.ctx.sys.log.app.command.pereg.PersonCategoryCorrectionLogParameter.PersonCorrectionItemInfo;
import nts.uk.ctx.sys.log.app.command.pereg.PersonCorrectionLogParameter;
import nts.uk.shr.com.security.audittrail.correction.DataCorrectionContext;
import nts.uk.shr.com.security.audittrail.correction.content.TargetDataKey;
import nts.uk.shr.com.security.audittrail.correction.content.TargetDataKey.CalendarKeyType;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.InfoOperateAttr;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.PersonInfoProcessAttr;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.ReviseInfo;
import nts.uk.shr.com.security.audittrail.correction.processor.CorrectionProcessorId;
import nts.uk.shr.pereg.app.ComboBoxObject;
import nts.uk.shr.pereg.app.ItemValue;
import nts.uk.shr.pereg.app.ItemValueType;
import nts.uk.shr.pereg.app.command.ItemsByCategory;
import nts.uk.shr.pereg.app.command.PeregAddCommandHandler;
import nts.uk.shr.pereg.app.command.PeregCommandHandlerCollector;
import nts.uk.shr.pereg.app.command.PeregDeleteCommand;
import nts.uk.shr.pereg.app.command.PeregDeleteCommandHandler;
import nts.uk.shr.pereg.app.command.PeregInputContainer;
import nts.uk.shr.pereg.app.command.PeregUpdateCommandHandler;
import nts.uk.shr.pereg.app.command.userdef.PeregUserDefAddCommand;
import nts.uk.shr.pereg.app.command.userdef.PeregUserDefAddCommandHandler;
import nts.uk.shr.pereg.app.command.userdef.PeregUserDefDeleteCommand;
import nts.uk.shr.pereg.app.command.userdef.PeregUserDefDeleteCommandHandler;
import nts.uk.shr.pereg.app.command.userdef.PeregUserDefUpdateCommand;
import nts.uk.shr.pereg.app.command.userdef.PeregUserDefUpdateCommandHandler;
import nts.uk.shr.pereg.app.find.PeregQuery;

@ApplicationScoped
public class PeregCommandFacade {
	
	@Inject 
	private PerInfoCategoryRepositoty ctgRepo;

	@Inject
	private PeregCommandHandlerCollector handlerCollector;

	/** Command handlers to add */
	private Map<String, PeregAddCommandHandler<?>> addHandlers;

	/** Command handlers to update */
	private Map<String, PeregUpdateCommandHandler<?>> updateHandlers;

	/** Command handlers to delete */
	private Map<String, PeregDeleteCommandHandler<?>> deleteHandlers;

	/** this handles command to add data defined by user. */
	@Inject
	private PeregUserDefAddCommandHandler userDefAdd;

	/** this handles command to update data defined by user. */
	@Inject
	private PeregUserDefUpdateCommandHandler userDefUpdate;

	/** this handles command to delete data defined by user. */
	@Inject
	private PeregUserDefDeleteCommandHandler userDefDelete;

	@Inject
	private ItemDefFinder itemDefFinder;
	
	@Inject
	private EmpCtgFinder empCtgFinder;

	@Inject
	private GetUserByEmpFinder userFinder;
	
	private final static String nameEndate = "終了日";
	
	private final static String valueEndate = "9999/12/31";
	
	/* employeeCode, stardCardNo */
	private final static List<String> specialItemCode = Arrays.asList("IS00001", "IS00779");
	
	/* target Key: null */
	private final static List<String> singleCategories = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("CS00002");
			add("CS00022");
			add("CS00023");
			add("CS00024");
			add("CS00035");
			add("CS00036");
		}
	};

	/* target Key : code */
	private static final Map<String, String> specialItemCodes = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("CS00001", "IS00001");
			put("CS00025", "1");
			put("CS00026", "2");
			put("CS00027", "3");
			put("CS00028", "4");
			put("CS00029", "5");
			put("CS00030", "6");
			put("CS00031", "7");
			put("CS00032", "8");
			put("CS00033", "9");
			put("CS00034", "10");
			put("CS00049", "11");
			put("CS00050", "12");
			put("CS00051", "13");
			put("CS00052", "14");
			put("CS00053", "15");
			put("CS00054", "16");
			put("CS00055", "17");
			put("CS00056", "18");
			put("CS00057", "19");
			put("CS00058", "20");
			put("CS00069", "IS00779");
		}
	};

	/**
	 * return List Category Code
	 */
	public List<String> getAddCategoryCodeList() {
		return this.addHandlers.keySet().stream().collect(Collectors.toList());
	}

	/**
	 * Initializes.
	 */
	public void init(@Observes @Initialized(ApplicationScoped.class) Object event) {

		this.addHandlers = this.handlerCollector.collectAddHandlers().stream()
				.collect(Collectors.toMap(h -> h.targetCategoryCd(), h -> h));

		this.updateHandlers = this.handlerCollector.collectUpdateHandlers().stream()
				.collect(Collectors.toMap(h -> h.targetCategoryCd(), h -> h));

		this.deleteHandlers = this.handlerCollector.collectDeleteHandlers().stream()
				.collect(Collectors.toMap(h -> h.targetCategoryCd(), h -> h));
	}
	
	public Object registerHandler(PeregInputContainer inputContainer) {
		DataCorrectionContext.transactionBegun(CorrectionProcessorId.PEREG_REGISTER, -99);
		
		String recordId = null;
		PersonCorrectionLogParameter target  = null;
		List<DateRangeDto> ctgCode = ctgRepo.dateRangeCode();
		String employeeId = inputContainer.getEmployeeId();
		UserAuthDto user = new UserAuthDto("", "", "", employeeId, "", "");
		List<UserAuthDto> userAuth = this.userFinder.getByListEmp(Arrays.asList(employeeId));
		
		if (userAuth.size() > 0) {
			user = userAuth.get(0);
		}
		
		List<ItemsByCategory> updateInput = inputContainer.getInputs().stream()
				.filter(p -> !StringUtils.isEmpty(p.getRecordId())).collect(Collectors.toList());
		
		List<ItemsByCategory> deleteInputs = inputContainer.getInputs().stream().filter(p -> p.isDelete()).collect(Collectors.toList());
		if (updateInput.size() > 0) {
			target = new PersonCorrectionLogParameter(user.getUserID(), employeeId, user.getUserName(),
					PersonInfoProcessAttr.UPDATE, null);
		} else {
			target = new PersonCorrectionLogParameter(user.getUserID(), employeeId, user.getUserName(),
					PersonInfoProcessAttr.ADD, null);
		}
		
		if(deleteInputs.size() == 0) {
			// ADD COMMAND
			recordId = this.add(inputContainer, target, user);

			// UPDATE COMMAND
			this.update(inputContainer, target, user);
		}

		// DELETE COMMAND
		this.delete(inputContainer, ctgCode);

		DataCorrectionContext.transactionFinishing(-99);
		
		return new Object[] { recordId };
	}
	
	public void deleteHandler(PeregDeleteCommand command) {
		DataCorrectionContext.transactionBegun(CorrectionProcessorId.PEREG_REGISTER, -88);

		List<DateRangeDto> ctgCode = ctgRepo.dateRangeCode();
		List<UserAuthDto> userAuth = this.userFinder.getByListEmp(Arrays.asList(command.getEmployeeId()));
		UserAuthDto user = new UserAuthDto("", "", "", command.getEmployeeId(), "", "");

		if (userAuth.size() > 0) {
			user = userAuth.get(0);
		}

		PersonCorrectionLogParameter target = new PersonCorrectionLogParameter(user.getUserID(), command.getEmployeeId(),
				user.getUserName(), PersonInfoProcessAttr.UPDATE, null);
		
		DataCorrectionContext.setParameter(target.getHashID(), target);

		this.delete(command, ctgCode);
		DataCorrectionContext.transactionFinishing(-88);
	}

	/**
	 * hàm này viết cho cps001 Handles add commands.
	 * 
	 * @param container
	 *            inputs
	 */
	@Transactional
	public String add(PeregInputContainer container, PersonCorrectionLogParameter target, UserAuthDto user) {
		return addNonTransaction(container, false, target, user);	
	}

	@Transactional
	public String addForCPS002(PeregInputContainer container, PersonCorrectionLogParameter target) {
		return addNonTransaction(container, true, target, null);
	}

	private String addNonTransaction(PeregInputContainer container, boolean isCps002, PersonCorrectionLogParameter target, UserAuthDto user) {
		// Filter input category
		List<ItemsByCategory> addInputs = container.getInputs().stream()
				.filter(p -> StringUtils.isEmpty(p.getRecordId())).collect(Collectors.toList());

		List<String> recordIds = new ArrayList<String>();
		String personId = container.getPersonId();
		String employeeId = container.getEmployeeId();
		
		if (isCps002 == false) {
			if (addInputs.size() > 0) {
				DataCorrectionContext.transactionBegun(CorrectionProcessorId.PEREG_REGISTER);
				updateInputForAdd(addInputs);
				setParamsForCPS001(employeeId, personId, PersonInfoProcessAttr.ADD, addInputs, target);
				DataCorrectionContext.transactionFinishing();
			}
		}
		
		addInputs.forEach(itemsByCategory -> {
			val handler = this.addHandlers.get(itemsByCategory.getCategoryCd());
			// In case of optional category fix category doesn't exist
			String recordId = null;
			
			if (handler != null && itemsByCategory.isHaveSomeSystemItems()) {
				val result = handler.handlePeregCommand(personId, employeeId, itemsByCategory);
				// pass new record ID that was generated by add domain command
				recordId = result.getAddedRecordId();
			}

			// pass new record ID that was generated by add domain command
			// handler
			val commandForUserDef = new PeregUserDefAddCommand(personId, employeeId, recordId, itemsByCategory);
			this.userDefAdd.handle(commandForUserDef);

			// Keep record id to focus in UI
			recordIds.add(recordId);
		});
		
		if (recordIds.size() == 1) {
			return recordIds.get(0);
		}

		return null;
	}

	/**
	 * Handles update commands.
	 * 
	 * @param container
	 *            inputs
	 */
	@Transactional
	public void update(PeregInputContainer container, PersonCorrectionLogParameter target, UserAuthDto user) {
		List<ItemsByCategory> updateInputs = container.getInputs().stream()
				.filter(p -> !StringUtils.isEmpty(p.getRecordId())).collect(Collectors.toList());

		if (updateInputs != null && !updateInputs.isEmpty()) {
			DataCorrectionContext.transactionBegun(CorrectionProcessorId.PEREG_REGISTER);
			setParamsForCPS001(container.getEmployeeId(), container.getPersonId(), PersonInfoProcessAttr.UPDATE, updateInputs, target);
			DataCorrectionContext.transactionFinishing();
			updateInputCategories(container, updateInputs);
		}

		updateInputs.forEach(itemsByCategory -> {
			val handler = this.updateHandlers.get(itemsByCategory.getCategoryCd());

			// In case of optional category fix category doesn't exist
			if (handler != null) {
				handler.handlePeregCommand(container.getPersonId(), container.getEmployeeId(), itemsByCategory);
			}

			val commandForUserDef = new PeregUserDefUpdateCommand(container.getPersonId(), container.getEmployeeId(),
					itemsByCategory);

			this.userDefUpdate.handle(commandForUserDef);
			
			
		});
		
		
	}

	//update input for case ADD
	private void updateInputForAdd(List<ItemsByCategory> inputs) {
		// Add item invisible to list
		for (ItemsByCategory itemByCategory : inputs) {
			itemByCategory.getItems().stream().forEach(c -> {
				c.setValueBefore(null);
				c.setContentBefore(null);
			});
		}
	}

	/**
	 * set Params cho trường hợp update, add màn cps001
	 * @param sid
	 * @param pid
	 * @param isAdd
	 * @param inputs
	 * @param target
	 */
	private void setParamsForCPS001(String sid, String pid, PersonInfoProcessAttr isAdd, List<ItemsByCategory> inputs, PersonCorrectionLogParameter target) {
		if (target != null) {
			
			String stringKey = null;
			ReviseInfo reviseInfo = null;
			List<DateRangeDto> ctgCode = this.ctgRepo.dateRangeCode();

			for (ItemsByCategory input : inputs) {
				DateRangeDto dateRange = null;
				CategoryType ctgType = EnumAdaptor.valueOf(input.getCategoryType(), CategoryType.class);
				List<PersonCorrectionItemInfo> lstItemInfo = new ArrayList<>();
				PeregQuery query = PeregQuery.createQueryCategory(input.getRecordId(), input.getCategoryCd(),sid, pid);
				List<ItemValue> invisibles = this.getItemInvisibles(query, input, isAdd);
				Optional<DateRangeDto> dateRangeOp = ctgCode.stream().filter(c -> c.getCtgCode().equals(input.getCategoryCd())).findFirst();
				boolean isHistory = ctgType == CategoryType.DUPLICATEHISTORY
						|| ctgType == CategoryType.CONTINUOUSHISTORY || ctgType == CategoryType.NODUPLICATEHISTORY;

				if(input.getCategoryCd().equals("CS00003")) {
					dateRange = new DateRangeDto(input.getCategoryCd(), "IS00020", "IS00021");
				} else {
					dateRange = isHistory == true? dateRangeOp.get(): null;
				}

				List<ItemValue> itemLogs = input.getItems() == null ?
						new ArrayList<>() :  input.getItems().stream().filter(distinctByKey(p -> p.itemCode())).collect(Collectors.toList());
				for (ItemValue c : invisibles) {
					switch (ctgType) {
					case SINGLEINFO:
					case MULTIINFO:
						if (specialItemCode.contains(c.itemCode())) {
							itemLogs.add(c);
						}
						break;
					case DUPLICATEHISTORY:
					case CONTINUOUSHISTORY:
					case NODUPLICATEHISTORY:
						if (c.itemCode().equals(dateRange.getStartDateCode())) {
							itemLogs.add(c);
						}
						break;
					default:
						break;
					}
				}
				
				InfoOperateAttr info = InfoOperateAttr.ADD;
				for (ItemValue item : itemLogs) {
					// kiểm tra các item của  category nghỉ đặc biệt, employee, lịch sử 
					if (specialItemCode.contains(item.itemCode())
							|| (isHistory && item.logType() == ItemValueType.DATE.value
								&& (item.itemCode().equals(dateRange.getStartDateCode()) || item.itemCode().equals(dateRange.getEndDateCode())))) {

						// lấy target Key
						if (specialItemCode.contains(item.itemCode()) || (item.itemCode().equals(dateRange.getStartDateCode()))) {
							stringKey = item.valueAfter();
						}
						
						// nếu startDate newValue != afterValue;
						if(ctgType == CategoryType.CONTINUOUSHISTORY || ctgType == CategoryType.MULTIINFO) {
							query.setCategoryId(input.getCategoryId());
							List<ComboBoxObject> historyLst =  this.empCtgFinder.getListInfoCtgByCtgIdAndSid(query);
							/**
							 * trường hợp update :lấy ra danh sách các lịch sử của category liên tục, tìm ra
							 * itemEndDate bị ảnh hưởng để thêm vào domain ReviseInfo trường hợp tạo mới set
							 * EndDate = "9999/12/31"
							 **/
							switch(ctgType) {
							case CONTINUOUSHISTORY:
								// trường hợp category lịch sử không có history nào
								if(historyLst.size() == 1) {
									if (item.itemCode().equals(dateRange.getEndDateCode())) {
										item.setValueAfter(valueEndate);
										item.setContentAfter(valueEndate);
									}
									
								}else {
									PersonCorrectionLogParameter correctedLog =  new PersonCorrectionLogParameter(target.userId, target.employeeId, target.userName,
											PersonInfoProcessAttr.UPDATE, null);
									target =  new PersonCorrectionLogParameter(correctedLog.userId, correctedLog.employeeId, correctedLog.userName,
											PersonInfoProcessAttr.UPDATE, null);
									
									// trường hợp tạo mới hoàn toàn category
									for (ComboBoxObject c : historyLst) {
										if (c.getOptionValue() != null) {
											// optionText có kiểu giá trị 2018/12/01 ~ 2018/12/31
											String[] history = c.getOptionText().split("~");
											switch (isAdd) {
											case ADD:
												info = InfoOperateAttr.ADD_HISTORY;
												//nếu thêm lịch sử thì endCode sẽ có giá trị 9999/12/31
												if (item.itemCode().equals(dateRange.getEndDateCode())) {
													item.setValueAfter(valueEndate);
													item.setContentAfter(valueEndate);
													break;
												}else {
													reviseInfo = new ReviseInfo(nameEndate,
														Optional.ofNullable(GeneralDate.fromString(item.valueAfter(), "yyyy/MM/dd").addDays(-1)),
														Optional.empty(), Optional.empty());
													break;
												}
												
												
											case UPDATE:
												info = InfoOperateAttr.UPDATE;
												if (!history[1].equals(" ")) {
													GeneralDate oldEnd = GeneralDate.fromString(history[1].substring(1), "yyyy/MM/dd");
													GeneralDate oldStart = GeneralDate.fromString(item.valueBefore(), "yyyy/MM/dd");
													if (oldStart.addDays(-1).equals(oldEnd)) {
														reviseInfo = new ReviseInfo(nameEndate,
																Optional.ofNullable(GeneralDate.fromString(item.valueAfter(), "yyyy/MM/dd").addDays(-1)),
																Optional.empty(), Optional.empty());
														break;
													}
												} else {
													break;
												}

											default:
												break;
											
											}

										}
									}
									
								}
								break;
							case MULTIINFO:
								if(historyLst.size() > 1 && isAdd == PersonInfoProcessAttr.ADD) {
									PersonCorrectionLogParameter correctedLog =  new PersonCorrectionLogParameter(target.userId, target.employeeId, target.userName,
											PersonInfoProcessAttr.UPDATE, null);
									target =  new PersonCorrectionLogParameter(correctedLog.userId, correctedLog.employeeId, correctedLog.userName,
											PersonInfoProcessAttr.UPDATE, null);
									info = InfoOperateAttr.ADD_HISTORY;
									
								}
								break;
							default:
								break;
							
							}
						}
						
					}
					
					if (ItemValue.filterItem(item) != null) {
						input.getItems().stream().forEach(c ->{
							if(item.itemCode().equals(c.itemCode())) {
								lstItemInfo.add(PersonCorrectionItemInfo.createItemInfoToItemLog(item));
							}
						});
						
					}
				}
				

				// Add category correction data
				PersonCategoryCorrectionLogParameter ctgTarget = null;
				
				if (isAdd == PersonInfoProcessAttr.ADD) {
					ctgTarget = setCategoryTarget(ctgType, ctgTarget, input, lstItemInfo, reviseInfo, stringKey, info);
				} else {
					ctgTarget = setCategoryTarget(ctgType, ctgTarget, input, lstItemInfo, reviseInfo, stringKey, info != InfoOperateAttr.ADD ? info: InfoOperateAttr.UPDATE);
				}
				
				if (ctgTarget != null && lstItemInfo.size() > 0) {

					DataCorrectionContext.setParameter(target.getHashID(), target);
					DataCorrectionContext.setParameter(ctgTarget.getHashID(), ctgTarget);
				}
				stringKey = null;
				reviseInfo = null;
			}
		}
	}
	
	/**
	 *  trường hợp update :lấy ra danh sách các lịch sử của category liên tục, 
	 *  tìm ra  itemEndDate bị ảnh hưởng để thêm vào domain ReviseInfo
	 *  trường hợp tạo mới set EndDate = "9999/12/31" 
	 * @param sid
	 * @param pid
	 * @param isAdd
	 * @param input
	 * @param itemCode
	 * @param item
	 * @return
	 */

	private PersonCategoryCorrectionLogParameter setCategoryTarget(CategoryType ctgType, PersonCategoryCorrectionLogParameter ctgTarget,
			ItemsByCategory input, List<PersonCorrectionItemInfo> lstItemInfo, ReviseInfo reviseInfo, String stringKey,
			InfoOperateAttr infoOperateAttr) {

		switch (ctgType) {

		case SINGLEINFO:

			if (singleCategories.contains(input.getCategoryCd())) {
				ctgTarget = new PersonCategoryCorrectionLogParameter(input.getCategoryId(), input.getCategoryName(), 
						infoOperateAttr, lstItemInfo,
						new TargetDataKey(CalendarKeyType.NONE, null, null),  Optional.ofNullable(reviseInfo));

			} else {
				String code = specialItemCodes.get(input.getCategoryCd());
				ctgTarget = new PersonCategoryCorrectionLogParameter(input.getCategoryId(), input.getCategoryName(), 
						infoOperateAttr, lstItemInfo,
						new TargetDataKey(CalendarKeyType.NONE, null,
						code.equals(specialItemCode.get(0)) == true  || code.equals(specialItemCode.get(1)) == true? stringKey : code), Optional.ofNullable(reviseInfo));
			}
			return ctgTarget;

		case MULTIINFO:
			ctgTarget = new PersonCategoryCorrectionLogParameter(input.getCategoryId(), input.getCategoryName(), infoOperateAttr, lstItemInfo,
					new TargetDataKey(CalendarKeyType.NONE, null, stringKey), Optional.ofNullable(reviseInfo));
			return ctgTarget;
		case NODUPLICATEHISTORY:
		case DUPLICATEHISTORY:
		case CONTINUOUSHISTORY:
			if(stringKey != null) {
				ctgTarget = new PersonCategoryCorrectionLogParameter(input.getCategoryId(), input.getCategoryName(), infoOperateAttr, lstItemInfo,
						TargetDataKey.of(GeneralDate.fromString(stringKey, "yyyy/MM/dd")), Optional.ofNullable(reviseInfo));
			}
			return ctgTarget;
		default:
			return null;
		}
	}

	private void updateInputCategories(PeregInputContainer container, List<ItemsByCategory> updateInputs) {
		// Add item invisible to list
		for (ItemsByCategory itemByCategory : updateInputs) {
			
			PeregQuery query = PeregQuery.createQueryCategory(itemByCategory.getRecordId(),
					itemByCategory.getCategoryCd(), container.getEmployeeId(), container.getPersonId());
			List<ItemValue> invisibles = this.getItemInvisibles(query, itemByCategory, PersonInfoProcessAttr.UPDATE);
			itemByCategory.getItems().addAll(invisibles);
		}
	}
	
	/**
	 * lấy ra những item không được hiển thị trên màn hình layouts
	 * @param query
	 * @param itemByCategory
	 * @return
	 */
	private List<ItemValue> getItemInvisibles(PeregQuery query, ItemsByCategory itemByCategory, PersonInfoProcessAttr isAdd){
		if(isAdd == PersonInfoProcessAttr.UPDATE) {
			List<ItemValue> fullItems = itemDefFinder.getFullListItemDef(query);
			
			List<String> visibleItemCodes = itemByCategory.getItems().stream().map(ItemValue::itemCode)
					.collect(Collectors.toList());
			return fullItems.stream().filter(i -> {
				return i.itemCode().indexOf("O") == -1 && !visibleItemCodes.contains(i.itemCode());
			}).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}


	@Transactional
	public void updateForCPS002(PeregInputContainer container) {
		List<ItemsByCategory> updateInputs = container.getInputs().stream()
				.filter(p -> !StringUtils.isEmpty(p.getRecordId())).collect(Collectors.toList());

		if (updateInputs != null && !updateInputs.isEmpty()) {
			// Add item invisible to list
			for (ItemsByCategory itemByCategory : updateInputs) {
				PeregQuery query = PeregQuery.createQueryCategory(itemByCategory.getRecordId(),
						itemByCategory.getCategoryCd(), container.getEmployeeId(), container.getPersonId());

				itemByCategory.getItems().addAll(this.getItemInvisibles(query, itemByCategory, PersonInfoProcessAttr.ADD));
			}
		}

		updateInputs.forEach(itemsByCategory -> {
			val handler = this.updateHandlers.get(itemsByCategory.getCategoryCd());
			
			// In case of optional category fix category doesn't exist
			if (handler != null) {
				handler.handlePeregCommand(container.getPersonId(), container.getEmployeeId(), itemsByCategory);
			}
			
			val commandForUserDef = new PeregUserDefUpdateCommand(container.getPersonId(), container.getEmployeeId(),
					itemsByCategory);
			this.userDefUpdate.handle(commandForUserDef);
		});
	}
	
	/**
	 * 
	 * @param keyExtractor
	 * @return
	 */
	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	/**
	 * @param inputContainer
	 *            delete data when click register cps001
	 */
	private void delete(PeregInputContainer inputContainer, List<DateRangeDto> ctgCode) {
		List<PeregDeleteCommand> deleteInputs = inputContainer.getInputs().stream().filter(p -> p.isDelete())
				.map(x -> new PeregDeleteCommand(inputContainer.getPersonId(), inputContainer.getEmployeeId(),
						x.getCategoryId(), x.getCategoryType(), x.getCategoryCd(), x.getCategoryName(), x.getRecordId(), x.getItems()))
				.collect(Collectors.toList());

		deleteInputs.forEach(deleteCommand -> delete(deleteCommand, ctgCode));
	}

	/**
	 * Handles delete command.
	 * 
	 * @param command
	 *            command
	 */
	@Transactional
	private void delete(PeregDeleteCommand command, List<DateRangeDto> ctgCode) {
		DataCorrectionContext.transactionBegun(CorrectionProcessorId.PEREG_REGISTER);

		val handler = this.deleteHandlers.get(command.getCategoryCode());
		if (handler != null) {
			handler.handlePeregCommand(command);
		}

		val commandForUserDef = new PeregUserDefDeleteCommand(command);
		this.userDefDelete.handle(commandForUserDef);

		/*
		 * SINGLEINFO(1), MULTIINFO(2), CONTINUOUSHISTORY(3), NODUPLICATEHISTORY(4),
		 * DUPLICATEHISTORY(5), CONTINUOUS_HISTORY_FOR_ENDDATE(6);
		 */
		TargetDataKey dKey = TargetDataKey.of("");
		List<PersonCorrectionItemInfo> itemInfo = new ArrayList<PersonCorrectionItemInfo>();
		Optional<ReviseInfo> rInfo = Optional.ofNullable(null);
		switch (command.getCategoryType()) {
		case 2:
			Optional<ItemValue> itemValue = command.getInputs().stream().findFirst();

			if (itemValue.isPresent()) {
				ItemValue _itemValue = itemValue.get();
				String valueAfter = Optional.ofNullable(_itemValue.valueAfter()).orElse(""),
						viewAfter = Optional.ofNullable(_itemValue.contentAfter()).orElse("");

				if (!valueAfter.trim().isEmpty()) {
					_itemValue.setValueAfter(null);
				}

				if (!viewAfter.trim().isEmpty()) {
					_itemValue.setContentAfter(null);
				}

				dKey = TargetDataKey.of(_itemValue.valueBefore());
				itemInfo.add(PersonCorrectionItemInfo.createItemInfoToItemLog(_itemValue));
			}			
			break;
		case 3:
		case 4:
		case 5:
		case 6:
			Optional<DateRangeDto> ddto = ctgCode.stream().filter(f -> f.getCtgCode().equals(command.getCategoryCode()))
					.findFirst();

			if (ddto.isPresent()) {
				Optional<ItemValue> startDate = command.getInputs().stream()
						.filter(f -> f.itemCode().equals(ddto.get().getStartDateCode())).findFirst();
				Optional<ItemValue> endDate = command.getInputs().stream()
						.filter(f -> f.itemCode().equals(ddto.get().getEndDateCode())).findFirst();

				if (startDate.isPresent()) {
					ItemValue _startDate = startDate.get();
					String valueAfter = Optional.ofNullable(_startDate.valueAfter()).orElse(""),
							viewAfter = Optional.ofNullable(_startDate.contentAfter()).orElse("");
					
					if(!valueAfter.trim().isEmpty()) {
						_startDate.setValueAfter(null);
					}

					if(!viewAfter.trim().isEmpty()) {
						_startDate.setContentAfter(null);
					}
					
					dKey = TargetDataKey.of(GeneralDate.fromString(_startDate.valueBefore(), "yyyy/MM/dd"));
					itemInfo.add(PersonCorrectionItemInfo.createItemInfoToItemLog(_startDate));
				}

				int ctype = command.getCategoryType();
				// save revise for continue, noduplicate history
				if ((ctype == 3 || ctype == 4 || ctype == 6) && endDate.isPresent()) {
					ItemValue _endDate = endDate.get();
					rInfo = startDate.map(m -> {
						GeneralDate date = GeneralDate.fromString(m.valueBefore(), "yyyy/MM/dd").addDays(-1);
						return new ReviseInfo(_endDate.itemName(), Optional.ofNullable(date), Optional.empty(),
								Optional.empty());
					});
				}
			}
			break;
		}

		// Add category correction data
		PersonCategoryCorrectionLogParameter ctgTarget = new PersonCategoryCorrectionLogParameter(
				command.getCategoryId(), command.getCategoryName(), InfoOperateAttr.deleteOf(command.getCategoryType()),
				itemInfo, dKey, rInfo);

		DataCorrectionContext.setParameter(ctgTarget.getHashID(), ctgTarget);

		DataCorrectionContext.transactionFinishing();
	}

}
