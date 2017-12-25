package nts.uk.ctx.pereg.app.find.copysetting.item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pereg.app.find.common.MappingFactory;
import nts.uk.ctx.pereg.app.find.initsetting.item.SettingItemDto;
import nts.uk.ctx.pereg.app.find.initsetting.item.SettingItemDtoMapping;
import nts.uk.ctx.pereg.app.find.processor.LayoutingProcessor;
import nts.uk.ctx.pereg.dom.copysetting.item.EmpCopySettingItem;
import nts.uk.ctx.pereg.dom.copysetting.item.EmpCopySettingItemRepository;
import nts.uk.ctx.pereg.dom.person.info.singleitem.DataTypeValue;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.find.PeregQuery;
import nts.uk.shr.pereg.app.find.dto.PeregDto;

@Stateless
public class CopySettingItemFinder {

	@Inject
	private EmpCopySettingItemRepository empCopyItemRepo;

	@Inject
	private LayoutingProcessor layoutProc;

	@Inject
	private SettingItemDtoMapping settingItemMap;

	public List<SettingItemDto> getAllCopyItemByCtgCode(boolean isSetText, String categoryCd, String employeeId,
			GeneralDate baseDate) {

		String companyId = AppContexts.user().companyId();

		List<SettingItemDto> result = new ArrayList<SettingItemDto>();

		// check empployeeId
		boolean isSelf = employeeId == AppContexts.user().employeeId() ? true : false;

		List<EmpCopySettingItem> itemList = this.empCopyItemRepo.getAllItemFromCategoryCd(categoryCd, companyId,
				isSelf);

		if (CollectionUtil.isEmpty(itemList)) {
			boolean isPersonnelRepresentative = true;

			if (isPersonnelRepresentative) {
				throw new BusinessException(new RawErrorMessage("Msg_347"));
			} else {
				throw new BusinessException(new RawErrorMessage("Msg_348"));
			}
		}

		itemList.forEach(x -> {
			result.add(SettingItemDto.createFromJavaType(x.getCategoryCode(), x.getItemDefId(), x.getItemCode(),
					x.getItemName(), x.getIsRequired().value, 1, GeneralDate.min(), BigDecimal.valueOf(0), "",
					x.getDataType(), x.getSelectionItemRefType(), x.getItemParentCd(), x.getDateType().value,
					x.getSelectionItemRefCd()));
		});

		PeregQuery query = new PeregQuery(categoryCd, employeeId, null, baseDate);

		PeregDto dto = this.layoutProc.findSingle(query);

		if (dto != null) {
			Map<String, Object> dataMap = MappingFactory.getFullDtoValue(dto);

			dataMap.forEach((k, v) -> {

				Optional<SettingItemDto> itemDtoOpt = result.stream().filter(x -> x.getItemCode().equals(k))
						.findFirst();

				if (itemDtoOpt.isPresent()) {
					SettingItemDto itemInfo = itemDtoOpt.get();

					itemInfo.setData(v != null ? v.toString() : "");
				}

			});
		}

		setDataForSetItem(result);
		if (isSetText) {
			this.settingItemMap.setTextForSelectionItem(result);
		}
		return result;

	}

	public void setDataForSetItem(List<SettingItemDto> result) {
		List<SettingItemDto> childList = result.stream().filter(x -> !StringUtils.isEmpty(x.getItemParentCd()))
				.collect(Collectors.toList());

		if (!CollectionUtil.isEmpty(childList)) {
			List<String> itemSetCdLst = new ArrayList<String>();
			childList.forEach(child -> {

				if (!itemSetCdLst.contains(child.getItemParentCd())) {
					itemSetCdLst.add(child.getItemParentCd());
				}

			});

			itemSetCdLst.forEach(itemCd -> {

				Optional<SettingItemDto> itemSetOpt = result.stream().filter(item -> item.getItemCode().equals(itemCd))
						.findFirst();
				if (itemSetOpt.isPresent()) {

					SettingItemDto itemSet = itemSetOpt.get();
					String itemValue = genItemvalue(result, itemCd);
					itemSet.setData(itemValue);
				}

			});
		}
	}

	private String genItemvalue(List<SettingItemDto> result, String itemCd) {

		String itemValue = "";
		List<SettingItemDto> childItems = result.stream()
				.filter(item -> String.valueOf(item.getItemParentCd()).equals(itemCd)).collect(Collectors.toList());

		for (SettingItemDto childItem : childItems) {

			if (!StringUtils.isEmpty(childItem.getSaveData().getValue())) {
				if (itemValue == "") {

					itemValue = childItem.getSaveData().getValue();
				} else {
					itemValue = String.join(getBetweenChar(childItem.getDataType()), itemValue,
							childItem.getSaveData().getValue());
				}
			}
		}

		return itemValue;
	}

	private String getBetweenChar(DataTypeValue dataType) {

		switch (dataType) {
		case DATE:
		case TIME:
		case TIMEPOINT:
			return "~";

		default:
			return " ";
		}

	}

	public List<CopySettingItemDto> getPerInfoDefById(String perInfoCategoryId) {
		String companyId = AppContexts.user().companyId();
		String contractId = AppContexts.user().contractCode();
		List<CopySettingItemDto> listData = empCopyItemRepo
				.getPerInfoItemByCtgId(perInfoCategoryId, companyId, contractId).stream().map(item -> {
					return CopySettingItemDto.createFromDomain(item);
				}).collect(Collectors.toList());

		return listData;
	}

}
