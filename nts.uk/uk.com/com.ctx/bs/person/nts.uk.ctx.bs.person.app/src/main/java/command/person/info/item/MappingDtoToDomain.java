package command.person.info.item;

import java.util.List;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.bs.person.dom.person.info.category.IsAbolition;
import nts.uk.ctx.bs.person.dom.person.info.category.IsFixed;
import nts.uk.ctx.bs.person.dom.person.info.item.IsRequired;
import nts.uk.ctx.bs.person.dom.person.info.item.ItemType;
import nts.uk.ctx.bs.person.dom.person.info.item.ItemTypeState;
import nts.uk.ctx.bs.person.dom.person.info.item.PersonInfoItemDefinition;
import nts.uk.ctx.bs.person.dom.person.info.selectionitem.ReferenceTypeState;
import nts.uk.ctx.bs.person.dom.person.info.selectionitem.ReferenceTypes;
import nts.uk.ctx.bs.person.dom.person.info.singleitem.DataTypeState;
import nts.uk.ctx.bs.person.dom.person.info.singleitem.DataTypeValue;

public class MappingDtoToDomain {

	private final static String ITEM_NAME_START_DATE = "開始日";
	private final static String ITEM_NAME_END_DATE = "終了日";
	private final static String ITEM_NAME_PERIOD = "期間";

	public static PersonInfoItemDefinition mappingFromDomaintoDtoForStartDate(AddItemCommand addItemCommand) {
		PersonInfoItemDefinition itemDef = PersonInfoItemDefinition.createFromJavaType(addItemCommand.getPerInfoCtgId(),
				addItemCommand.getItemCode(), addItemCommand.getItemParentCode(), ITEM_NAME_START_DATE,
				IsAbolition.NOT_ABOLITION.value, IsFixed.FIXED.value, IsRequired.REQUIRED.value);
		itemDef.setItemTypeState(createItemTypeState(addItemCommand, ItemType.SINGLE_ITEM, DataTypeValue.DATE, null));
		return itemDef;
	}

	public static PersonInfoItemDefinition mappingFromDomaintoDtoForEndtDate(AddItemCommand addItemCommand) {
		PersonInfoItemDefinition itemDef = PersonInfoItemDefinition.createFromJavaType(addItemCommand.getPerInfoCtgId(),
				addItemCommand.getItemCode(), addItemCommand.getItemParentCode(), ITEM_NAME_END_DATE,
				IsAbolition.NOT_ABOLITION.value, IsFixed.FIXED.value, IsRequired.NONE_REQUIRED.value);
		itemDef.setItemTypeState(createItemTypeState(addItemCommand, ItemType.SINGLE_ITEM, DataTypeValue.DATE, null));
		return itemDef;
	}

	public static PersonInfoItemDefinition mappingFromDomaintoDtoForPeriod(AddItemCommand addItemCommand, List<String> items) {
		PersonInfoItemDefinition itemDef = PersonInfoItemDefinition.createFromJavaType(addItemCommand.getPerInfoCtgId(),
				addItemCommand.getItemCode(), addItemCommand.getItemParentCode(), ITEM_NAME_PERIOD,
				IsAbolition.NOT_ABOLITION.value, IsFixed.FIXED.value, IsRequired.REQUIRED.value);
		itemDef.setItemTypeState(createItemTypeState(addItemCommand, ItemType.SET_ITEM, null, items));
		return itemDef;
	}

	public PersonInfoItemDefinition mappingFromDomaintoDto(AddItemCommand addItemCommand) {
		PersonInfoItemDefinition itemDef = PersonInfoItemDefinition.createFromJavaType(addItemCommand.getPerInfoCtgId(),
				addItemCommand.getItemCode(), addItemCommand.getItemParentCode(), addItemCommand.getItemName(),
				IsAbolition.NOT_ABOLITION.value, IsFixed.NOT_FIXED.value, IsRequired.REQUIRED.value);
		itemDef.setItemTypeState(createItemTypeState(addItemCommand, null, null, null));
		return itemDef;
	}

	private static ItemTypeState createItemTypeState(AddItemCommand addItemCommand, ItemType itemType,
			DataTypeValue dataTypeValue, List<String> items) {

		itemType = itemType != null ? itemType : EnumAdaptor.valueOf(addItemCommand.getItemType(), ItemType.class);
		if (itemType == ItemType.SINGLE_ITEM) {
			return ItemTypeState.createSingleItem(createDataTypeState(addItemCommand.getSingleItem(), dataTypeValue));
		} else {
			return ItemTypeState.createSetItem(items);
		}
	}

	private static DataTypeState createDataTypeState(SingleItemCommand singleI, DataTypeValue dataTypeValue) {
		dataTypeValue = dataTypeValue != null ? dataTypeValue
				: EnumAdaptor.valueOf(singleI.getDataType(), DataTypeValue.class);
		switch (dataTypeValue.value) {
		case 1:
			return DataTypeState.createStringItem(singleI.getStringItemLength(), singleI.getStringItemType(),
					singleI.getStringItemDataType());
		case 2:
			return DataTypeState.createNumericItem(singleI.getNumericItemMinus(), singleI.getNumericItemAmount(),
					singleI.getIntegerPart(), singleI.getDecimalPart(), singleI.getNumericItemMin(),
					singleI.getNumericItemMax());
		case 3:
			return DataTypeState.createDateItem(singleI.getDateItemType());
		case 4:
			return DataTypeState.createTimeItem(singleI.getTimeItemMax(), singleI.getTimeItemMin());
		case 5:
			return DataTypeState.createTimePointItem(singleI.getTimePointItemMin(), singleI.getTimePointItemMax());
		case 6:
			ReferenceTypeState referenceType = null;
			if (singleI.getReferenceType() == ReferenceTypes.DESIGNATED_MASTER.value) {
				referenceType = ReferenceTypeState.createMasterReferenceCondition(singleI.getReferenceCode());
			} else if (singleI.getReferenceType() == ReferenceTypes.CODE_NAME.value) {
				referenceType = ReferenceTypeState.createCodeNameReferenceType(singleI.getReferenceCode());
			} else {
				referenceType = ReferenceTypeState.createEnumReferenceCondition(singleI.getReferenceCode());
			}
			return DataTypeState.createSelectionItem(referenceType);
		default:
			return null;
		}
	}
}
