package nts.uk.ctx.pereg.app.find.initsetting.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pereg.app.find.additionaldata.item.EmpInfoItemDataFinder;
import nts.uk.ctx.pereg.app.find.common.MappingFactory;
import nts.uk.ctx.pereg.app.find.processor.LayoutingProcessor;
import nts.uk.ctx.pereg.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.pereg.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.pereg.dom.person.setting.init.item.PerInfoInitValueSetItemDetail;
import nts.uk.ctx.pereg.dom.person.setting.init.item.PerInfoInitValueSetItemRepository;
import nts.uk.ctx.pereg.dom.person.setting.init.item.ReferenceMethodType;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.find.PeregQuery;
import nts.uk.shr.pereg.app.find.dto.PeregDto;

/**
 * @author sonnlb
 *
 */
@Stateless
public class InitValueSetItemFinder {


	@Inject
	private PerInfoInitValueSetItemRepository settingItemRepo;

	@Inject
	private LayoutingProcessor layoutProc;
	@Inject
	private EmpInfoItemDataFinder infoItemDataFinder;

	@Inject
	private SettingItemDtoMapping settingItemMap;

	List<PerInfoInitValueSetItemDetail> itemList;
	
	@Inject 
	private PerInfoCategoryRepositoty perInfoCategoryRepositoty;

	String employeeId;

	String categoryCd;

	GeneralDate baseDate;

	// sonnlb
	public List<SettingItemDto> getAllInitItemByCtgCode(boolean isScreenC, FindInitItemDto command) {

		List<SettingItemDto> result = new ArrayList<SettingItemDto>();

		this.categoryCd = command.getCategoryCd();

		this.baseDate = command.getBaseDate();

		employeeId = AppContexts.user().employeeId();

		itemList = this.settingItemRepo.getAllInitItem(command.getInitSettingId(), categoryCd);

		result.addAll(itemList.stream().map(x -> fromInitValuetoDto(x)).collect(Collectors.toList()));

		// set item SAMEASLOGIN

		setItemSameLogin(itemList, result);

		// set item

		setDataByRefType(itemList, result, ReferenceMethodType.SAMEASEMPLOYEECODE, command.getEmployeeCode());

		setDataByRefType(itemList, result, ReferenceMethodType.SAMEASNAME, command.getEmployeeName());

		setDataByRefType(itemList, result, ReferenceMethodType.SAMEASEMPLOYMENTDATE, command.getHireDate());

		setDataByRefType(itemList, result, ReferenceMethodType.SAMEASSYSTEMDATE, GeneralDate.today());

		boolean isSetSameLoginSuccess = setItemSameLogin(itemList, result);

		if (isScreenC) {
			// Get perInfoCategory
			Optional<PersonInfoCategory> perInfoCategory = perInfoCategoryRepositoty
					.getPerInfoCategoryByCtgCD(command.getCategoryCd(), AppContexts.user().companyId());
			
			if (!perInfoCategory.isPresent()) {
				throw new RuntimeException("invalid PersonInfoCategory");
			}
			
			this.settingItemMap.setTextForSelectionItem(result, employeeId, command.getBaseDate(), perInfoCategory.get());
			
		} else {
			if (!isSetSameLoginSuccess) {
				boolean isAllItemIsSameAsLogin = isAllItemIsSameAsLogin(itemList, ReferenceMethodType.SAMEASLOGIN);
				if (isAllItemIsSameAsLogin) {
					return Collections.emptyList();
				}
			}
		}

		return result;
	}

	private boolean isAllItemIsSameAsLogin(List<PerInfoInitValueSetItemDetail> itemList, ReferenceMethodType sameaslogin) {
		if (!CollectionUtil.isEmpty(itemList)) {
			List<PerInfoInitValueSetItemDetail> sameAsLoginItems = itemList.stream()
					.filter(obj -> obj.getRefMethodType() == sameaslogin.value).collect(Collectors.toList());
			if (sameAsLoginItems.size() == itemList.size()) {
				return true;
			}
		}
		return false;
	}

	private boolean setItemSameLogin(List<PerInfoInitValueSetItemDetail> itemList, List<SettingItemDto> result) {
		if (isHaveItemRefType(itemList, ReferenceMethodType.SAMEASLOGIN)) {

			if (categoryCd.charAt(1) == 'S') {

				return setSystemCtgData(itemList, result);

			}

			return setOptinalCtgData(itemList, result);
		}
		return true;
	}

	private void setDataByRefType(List<PerInfoInitValueSetItemDetail> itemList, List<SettingItemDto> result,
			ReferenceMethodType methodType, String value) {
		if (isHaveItemRefType(itemList, methodType)) {
			itemList.stream().filter(x -> x.getRefMethodType() == methodType.value).collect(Collectors.toList())
					.forEach(x -> {

						Optional<SettingItemDto> itemDtoOpt = result.stream()
								.filter(item -> item.getItemCode().equals(x.getItemCode())).findFirst();
						if (itemDtoOpt.isPresent()) {
							itemDtoOpt.get().setData(value);
						}

					});
		}

	}

	private void setDataByRefType(List<PerInfoInitValueSetItemDetail> itemList, List<SettingItemDto> result,
			ReferenceMethodType methodType, GeneralDate value) {
		if (isHaveItemRefType(itemList, methodType)) {
			itemList.stream().filter(x -> x.getRefMethodType() == methodType.value).collect(Collectors.toList())
					.forEach(x -> {

						Optional<SettingItemDto> itemDtoOpt = result.stream()
								.filter(item -> item.getItemCode().equals(x.getItemCode())).findFirst();
						if (itemDtoOpt.isPresent()) {
							itemDtoOpt.get().setData(value);
						}

					});
		}
	}

	private boolean setOptinalCtgData(List<PerInfoInitValueSetItemDetail> itemList, List<SettingItemDto> result) {

		List<SettingItemDto> optList = this.infoItemDataFinder.loadInfoItemDataList(categoryCd,
				AppContexts.user().companyId(), employeeId);
		if (!CollectionUtil.isEmpty(optList)) {
			itemList.stream().filter(x -> x.getRefMethodType() == ReferenceMethodType.SAMEASLOGIN.value)
					.collect(Collectors.toList()).forEach(x -> {
						Optional<SettingItemDto> itemDtoOpt = result.stream()
								.filter(item -> item.getItemCode().equals(x.getItemCode())).findFirst();
						if (itemDtoOpt.isPresent()) {
							Optional<SettingItemDto> itemDataOpt = optList.stream()
									.filter(item -> item.getItemCode().equals(x.getItemCode())).findFirst();
							if (itemDataOpt.isPresent()) {
								itemDtoOpt.get().setData(itemDataOpt.get().getSaveData().getValue());
							}

						}
					});

			return true;
		} else {

			return false;
		}

	}

	private boolean setSystemCtgData(List<PerInfoInitValueSetItemDetail> itemList, List<SettingItemDto> result) {
		PeregQuery query = PeregQuery.createQueryLayout(categoryCd, employeeId, null, baseDate);

		PeregDto dto = this.layoutProc.findSingle(query);

		if (dto != null) {
			Map<String, Object> dataMap = MappingFactory.getFullDtoValue(dto);
			if (!dataMap.isEmpty()) {

				itemList.stream().filter(x -> x.getRefMethodType() == (ReferenceMethodType.SAMEASLOGIN.value))
						.collect(Collectors.toList()).forEach(x -> {
							Optional<SettingItemDto> itemDtoOpt = result.stream()
									.filter(item -> item.getItemCode().equals(x.getItemCode())).findFirst();
							if (itemDtoOpt.isPresent()) {
								Object value = dataMap.get(itemDtoOpt.get().getItemCode());
								if (value != null) {
									itemDtoOpt.get().setData(value);
								}
							}
						});

			}
			return true;
		} else {
			return false;

		}
	}

	private SettingItemDto fromInitValuetoDto(PerInfoInitValueSetItemDetail domain) {

		SettingItemDto itemDto = SettingItemDto.createFromJavaType(domain.getCtgCode(), domain.getPerInfoItemDefId(),
				domain.getItemCode(), domain.getItemName(), domain.getIsRequired(),
				domain.getSaveDataType(), domain.getDateValue(), domain.getIntValue(),
				domain.getStringValue(), domain.getDataType(), domain.getSelectionItemRefType(),
				domain.getItemParentCd(), domain.getDateType(), domain.getSelectionItemRefCd());

		return itemDto;
	}

	private boolean isHaveItemRefType(List<PerInfoInitValueSetItemDetail> listItem, ReferenceMethodType methodType) {
		if (!listItem.isEmpty()) {

			return listItem.stream().filter(obj -> obj.getRefMethodType() == methodType.value).findFirst().isPresent();

		} else {

			return false;

		}
	}

	// sonnlb

}
