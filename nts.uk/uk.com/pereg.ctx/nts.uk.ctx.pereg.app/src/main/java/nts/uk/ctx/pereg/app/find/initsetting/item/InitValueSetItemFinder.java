package nts.uk.ctx.pereg.app.find.initsetting.item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.pereg.app.find.additionaldata.item.EmpInfoItemDataFinder;
import nts.uk.ctx.pereg.app.find.common.MappingFactory;
import nts.uk.ctx.pereg.app.find.processor.LayoutingProcessor;
import nts.uk.ctx.pereg.dom.person.info.singleitem.DataTypeValue;
import nts.uk.ctx.pereg.dom.person.setting.init.item.PerInfoInitValueSetItem;
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

	List<PerInfoInitValueSetItem> itemList;

	String employeeId;

	String categoryCd;

	GeneralDate baseDate;

	// sonnlb
	public List<SettingItemDto> getAllInitItemByCtgCode(findInitItemDto command) {

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

		return result;
	}

	private void setItemSameLogin(List<PerInfoInitValueSetItem> itemList, List<SettingItemDto> result) {
		if (isHaveItemRefType(itemList, ReferenceMethodType.SAMEASLOGIN)) {

			if (categoryCd.charAt(1) == 'S') {

				setSystemLoginData(itemList, result);

			}

			setOptinalLoginData(itemList, result);
		}
	}

	private void setDataByRefType(List<PerInfoInitValueSetItem> itemList, List<SettingItemDto> result,
			ReferenceMethodType methodType, String value) {
		if (isHaveItemRefType(itemList, methodType)) {
			itemList.stream().filter(x -> x.getRefMethodType().equals(methodType)).collect(Collectors.toList())
					.forEach(x -> {

						Optional<SettingItemDto> itemDtoOpt = result.stream()
								.filter(item -> item.getItemCode().equals(x.getItemCode())).findFirst();
						if (itemDtoOpt.isPresent()) {
							itemDtoOpt.get().setData(value);
						}

					});
		}

	}

	private void setDataByRefType(List<PerInfoInitValueSetItem> itemList, List<SettingItemDto> result,
			ReferenceMethodType methodType, GeneralDate value) {
		if (isHaveItemRefType(itemList, methodType)) {
			itemList.stream().filter(x -> x.getRefMethodType().equals(methodType)).collect(Collectors.toList())
					.forEach(x -> {

						Optional<SettingItemDto> itemDtoOpt = result.stream()
								.filter(item -> item.getItemCode().equals(x.getItemCode())).findFirst();
						if (itemDtoOpt.isPresent()) {
							itemDtoOpt.get().setData(value);
						}

					});
		}
	}

	private void setOptinalLoginData(List<PerInfoInitValueSetItem> itemList, List<SettingItemDto> result) {

		List<SettingItemDto> optList = this.infoItemDataFinder.loadInfoItemDataList(categoryCd,
				AppContexts.user().companyId(), employeeId);

		itemList.stream().filter(x -> x.getRefMethodType().equals(ReferenceMethodType.SAMEASLOGIN))
				.collect(Collectors.toList()).forEach(x -> {
					Optional<SettingItemDto> itemDtoOpt = result.stream()
							.filter(item -> item.getItemCode().equals(x.getItemCode())).findFirst();
					if (itemDtoOpt.isPresent()) {
						Optional<SettingItemDto> itemDataOpt = optList.stream()
								.filter(item -> item.getItemCode().equals(x.getItemCode())).findFirst();
						if (itemDataOpt.isPresent()) {
							itemDtoOpt.get().setData(itemDataOpt.get().getValueAsString());
						}

					}
				});

	}

	private void setSystemLoginData(List<PerInfoInitValueSetItem> itemList, List<SettingItemDto> result) {
		PeregQuery query = new PeregQuery(categoryCd, employeeId, null, baseDate);

		PeregDto dto = this.layoutProc.findSingle(query);

		if (dto != null) {
			Map<String, Object> dataMap = MappingFactory.getFullDtoValue(dto);
			if (!dataMap.isEmpty()) {

				itemList.stream().filter(x -> x.getRefMethodType().equals(ReferenceMethodType.SAMEASLOGIN))
						.collect(Collectors.toList()).forEach(x -> {
							Optional<SettingItemDto> itemDtoOpt = result.stream()
									.filter(item -> item.getItemCode().equals(x.getItemCode())).findFirst();
							if (itemDtoOpt.isPresent()) {
								Object value = dataMap.get(itemDtoOpt.get().getItemCode());
								if (value != null) {
									itemDtoOpt.get().setData(value.toString());
								}
							}
						});

			}
		}
	}

	private SettingItemDto fromInitValuetoDto(PerInfoInitValueSetItem domain) {

		int dataType = domain.getDataType();

		SettingItemDto itemDto = SettingItemDto.createFromJavaType(domain.getCtgCode(), domain.getPerInfoItemDefId(),
				domain.getItemCode(), domain.getItemName(), domain.getIsRequired().value,
				domain.getSaveDataType().value, domain.getDateValue(), domain.getIntValue().v(),
				domain.getStringValue().v(), dataType, BigDecimal.valueOf(domain.getSelectionItemRefType()),
				domain.getItemParentCd());

		if (dataType == DataTypeValue.SELECTION.value) {

		}
		return itemDto;
	}

	private boolean isHaveItemRefType(List<PerInfoInitValueSetItem> listItem, ReferenceMethodType methodType) {
		if (!listItem.isEmpty()) {

			return listItem.stream().filter(obj -> obj.getRefMethodType().equals(methodType)).findFirst().isPresent();

		} else {

			return false;

		}
	}

	// sonnlb

}
