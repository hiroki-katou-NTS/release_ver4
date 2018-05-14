package nts.uk.ctx.pereg.app.find.copysetting.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pereg.app.find.common.MappingFactory;
import nts.uk.ctx.pereg.app.find.initsetting.item.SettingItemDto;
import nts.uk.ctx.pereg.app.find.initsetting.item.SettingItemDtoMapping;
import nts.uk.ctx.pereg.app.find.processor.LayoutingProcessor;
import nts.uk.ctx.pereg.dom.copysetting.setting.EmpCopySettingRepository;
import nts.uk.ctx.pereg.dom.copysetting.setting.EmployeeCopyCategory;
import nts.uk.ctx.pereg.dom.copysetting.setting.EmployeeCopySetting;
import nts.uk.ctx.pereg.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.pereg.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.pereg.dom.person.info.item.ItemType;
import nts.uk.ctx.pereg.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.pereg.dom.person.info.item.PersonInfoItemDefinition;
import nts.uk.ctx.pereg.dom.person.info.singleitem.DataTypeState;
import nts.uk.ctx.pereg.dom.person.info.singleitem.DataTypeValue;
import nts.uk.ctx.pereg.dom.person.info.singleitem.SingleItem;
import nts.uk.ctx.pereg.dom.roles.auth.category.PersonInfoCategoryAuthRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.find.PeregQuery;
import nts.uk.shr.pereg.app.find.dto.PeregDto;

@Stateless
public class CopySettingItemFinder {

	@Inject
	private EmpCopySettingRepository empCopyRepo;

	@Inject
	private LayoutingProcessor layoutProcessor;

	@Inject
	private SettingItemDtoMapping settingItemMap;
	
	@Inject 
	private PerInfoCategoryRepositoty perInfoCategoryRepositoty;
	
	@Inject
	private PerInfoItemDefRepositoty itemDefRepo;
	
	@Inject
	private PersonInfoCategoryAuthRepository perInfoCtgRepo;
	
	public List<SettingItemDto> getAllCopyItemByCtgCode(String categoryCd, String selectedEmployeeId, GeneralDate baseDate) {

		String companyId = AppContexts.user().companyId();
		
		// Get perInfoCategory
		PersonInfoCategory perInfoCategory = getCategory(categoryCd, companyId);
		
		// get employee-copy-category
		EmployeeCopyCategory empCopyCategory = getEmpCopyCategory(companyId, perInfoCategory.getPersonInfoCategoryId());
		
		List<PersonInfoItemDefinition> itemDefList = itemDefRepo.getItemLstByListId(
				empCopyCategory.getItemDefIdList(), AppContexts.user().contractCode());
		
		// get data
		Map<String, Object> dataMap = getCategoryData(Arrays.asList(categoryCd), selectedEmployeeId, baseDate);
		
		List<SettingItemDto> copyItemList = convertToSettingItemDtoOfCategory(itemDefList, dataMap, categoryCd);
		
		this.settingItemMap.setTextForItem(copyItemList, selectedEmployeeId, baseDate, perInfoCategory);
		
		return copyItemList;

	}
	
	public List<SettingItemDto> getValueCopyItem(String employeeId, GeneralDate baseDate) {
		
		String companyId = AppContexts.user().companyId();
		
		// get employee-copy-setting
		Optional<EmployeeCopySetting> empCopySettingOpt = empCopyRepo.findSetting(companyId);
		if (!empCopySettingOpt.isPresent()) {
			return new ArrayList<>();
		}	
		List<String> copySettingCategoryIdList = empCopySettingOpt.get().getCopySettingCategoryIdList();
		List<String> copySettingItemIdList = empCopySettingOpt.get().getCopySettingItemIdList();
		
		Map<String, String> categoryIdvsCodeMap = perInfoCtgRepo
				.getAllCategoryByCtgIdList(companyId, copySettingCategoryIdList).stream()
				.collect(Collectors.toMap(x -> x.getCategoryId(), x -> x.getCategoryCode()));
		
		List<String> categoryCodes = new ArrayList<>(categoryIdvsCodeMap.values());
		
		// get item-definition-list
		List<PersonInfoItemDefinition> itemDefList = itemDefRepo.getItemLstByListId(copySettingItemIdList,
				AppContexts.user().contractCode());
		
		// get data
		Map<String, Object> dataMap = getCategoryData(categoryCodes, employeeId, baseDate);
		
		return convertToSettingItemDto(itemDefList, dataMap, categoryIdvsCodeMap);
	}
	
	private PersonInfoCategory getCategory(String categoryCd, String companyId) {
		// Get perInfoCategory
		Optional<PersonInfoCategory> perInfoCategory = perInfoCategoryRepositoty.getPerInfoCategoryByCtgCD(categoryCd,
				companyId);
		
		if (!perInfoCategory.isPresent()) {
			throw new RuntimeException("invalid PersonInfoCategory");
		}
		
		return perInfoCategory.get();
	}
	
	private EmployeeCopyCategory getEmpCopyCategory(String companyId, String categoryId) {
		Optional<EmployeeCopyCategory> empCopyCategoryOpt = empCopyRepo.findCopyCategory(companyId,
				categoryId);

		if (!empCopyCategoryOpt.isPresent() || CollectionUtil.isEmpty(empCopyCategoryOpt.get().getItemDefIdList())) {

			// TODO: ログイン者の権限（ロール）をチェックする (Kiểm tra quyền của người đăng nhập)
			boolean isPersonnelRepresentative = true;
			if (isPersonnelRepresentative) {
				throw new BusinessException(new RawErrorMessage("Msg_347"));
			} else {
				throw new BusinessException(new RawErrorMessage("Msg_348"));
			}

		}
		
		return empCopyCategoryOpt.get();
	}
	
	private List<SettingItemDto> convertToSettingItemDtoOfCategory(List<PersonInfoItemDefinition> itemDefList, 
			Map<String, Object> dataMap, String categoryCd) {
		List<SettingItemDto> copyItemList = new ArrayList<>();
		for (PersonInfoItemDefinition itemDef : itemDefList) {
			if (itemDef.getItemTypeState().getItemType() != ItemType.SINGLE_ITEM) {
				continue;
			}
			
			SingleItem singleItemTypeState = (SingleItem) itemDef.getItemTypeState();
			DataTypeState dataTypeState = singleItemTypeState.getDataTypeState();
			
			DataTypeValue dataType = dataTypeState.getDataTypeValue();
			
			if (dataType == DataTypeValue.READONLY
					|| dataType == DataTypeValue.RELATE_CATEGORY) {
				continue;
			}
			
			Object value  = dataMap.get(itemDef.getItemCode().v());
			
			copyItemList.add(SettingItemDto.createFromJavaType1(categoryCd,
					itemDef.getPerInfoItemDefId(), itemDef.getItemCode().v(), itemDef.getItemName().v(),
					itemDef.getIsRequired().value, value,
					dataTypeState.getDataTypeValue(), dataTypeState.getReferenceTypes(),
					itemDef.getItemParentCode().v(), null, dataTypeState.getReferenceCode()));
		}
		
		return copyItemList;
	}
	
	
	private Map<String, Object> getCategoryData(List<String> categoryCodes, String employeeId, GeneralDate baseDate) {
		Map<String, Object> dataMap = new HashMap<>();
		categoryCodes.forEach(categoryCode -> {
			PeregDto dto = this.layoutProcessor.findSingle(new PeregQuery(categoryCode, employeeId, null, baseDate));
			if ( dto != null ) {
				dataMap.putAll(MappingFactory.getFullDtoValue(dto));
			}
		});
		return dataMap;
	}
	
	private List<SettingItemDto> convertToSettingItemDto(List<PersonInfoItemDefinition> itemDefList,
			Map<String, Object> dataMap, Map<String, String> categoryIdvsCodeMap) {
		List<SettingItemDto> copyItemList = new ArrayList<>();
		for (PersonInfoItemDefinition itemDef : itemDefList) {
			if (itemDef.getItemTypeState().getItemType() != ItemType.SINGLE_ITEM) {
				continue;
			}

			if (dataMap.get(itemDef.getItemCode().v()) == null) {
				continue;
			}

			SingleItem singleItemTypeState = (SingleItem) itemDef.getItemTypeState();
			DataTypeState dataTypeState = singleItemTypeState.getDataTypeState();
			DataTypeValue dataType = dataTypeState.getDataTypeValue();

			if (dataType == DataTypeValue.READONLY || dataType == DataTypeValue.RELATE_CATEGORY) {
				continue;
			}

			Object value = dataMap.get(itemDef.getItemCode().v());

			copyItemList.add(SettingItemDto.createFromJavaType1(categoryIdvsCodeMap.get(itemDef.getPerInfoCategoryId()),
					itemDef.getPerInfoItemDefId(), itemDef.getItemCode().v(), itemDef.getItemName().v(),
					itemDef.getIsRequired().value, value, dataType, dataTypeState.getReferenceTypes(),
					itemDef.getItemParentCode().v(), null, dataTypeState.getReferenceCode()));

		}
		return copyItemList;
	}
	
	public List<CopySettingItemDto> getPerInfoDefById(String perInfoCategoryId) {
		String companyId = AppContexts.user().companyId();
		List<CopySettingItemDto> listData = empCopyRepo.getPerInfoItemByCtgId(companyId, perInfoCategoryId).stream()
				.map(item -> CopySettingItemDto.createFromDomain(item)).collect(Collectors.toList());
		return listData;
	}

}
