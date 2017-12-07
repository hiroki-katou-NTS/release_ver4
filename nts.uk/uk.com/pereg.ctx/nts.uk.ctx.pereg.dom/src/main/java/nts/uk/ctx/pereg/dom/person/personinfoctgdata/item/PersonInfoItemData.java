package nts.uk.ctx.pereg.dom.person.personinfoctgdata.item;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pereg.dom.person.info.item.IsRequired;
import nts.uk.ctx.pereg.dom.person.info.item.ItemCode;
import nts.uk.shr.pereg.app.find.dto.DataStateType;
import nts.uk.shr.pereg.app.find.dto.PersonOptionalDto;

@NoArgsConstructor
@Getter
@Setter
public class PersonInfoItemData extends AggregateRoot {

	private ItemCode itemCode;

	private String perInfoCtgId;

	private String perInfoCtgCd;

	private String itemName;

	private String perInfoItemDefId;

	private String recordId;

	private DataState dataState;

	private IsRequired isRequired;

	public PersonInfoItemData(ItemCode itemCode, String perInfoDefId, String recordId, String perInfoCtgId,
			String perInfoCtgCd, String itemName, IsRequired isRequired, DataState dataState) {
		super();
		this.itemCode = itemCode;
		this.perInfoCtgId = perInfoCtgId;
		this.perInfoCtgCd = perInfoCtgCd;
		this.itemName = itemName;
		this.perInfoItemDefId = perInfoDefId;
		this.recordId = recordId;
		this.dataState = dataState;
		this.isRequired = isRequired;
	}

	public PersonInfoItemData(String perInfoItemDefId, String recordId, DataState dataState) {
		super();
		this.perInfoItemDefId = perInfoItemDefId;
		this.recordId = recordId;
		this.dataState = dataState;
	}

	public static PersonInfoItemData createFromJavaType(String itemCode, String perInfoDefId, String recordId,
			String perInfoCtgId, String perInfoCtgCd, String itemName, int isRequired, int dataStateType,
			String stringValue, BigDecimal intValue, GeneralDate dateValue) {

		return new PersonInfoItemData(new ItemCode(itemCode), perInfoDefId, recordId, perInfoCtgId, perInfoCtgCd,
				itemName, EnumAdaptor.valueOf(isRequired, IsRequired.class), createDataState(
						EnumAdaptor.valueOf(dataStateType, DataStateType.class), stringValue, intValue, dateValue));

	}

	private static DataState createDataState(DataStateType dataStateType, String stringValue, BigDecimal intValue,
			GeneralDate dateValue) {

		DataState resultState = new DataState();

		switch (dataStateType) {
		case String:
			resultState = DataState.createFromStringValue(stringValue.trim());
			break;

		case Numeric:
			resultState = DataState.createFromNumberValue(intValue);
			break;

		case Date:
			resultState = DataState.createFromDateValue(dateValue);
			break;

		}
		return resultState;
	}

	public PersonOptionalDto genToPeregDto() {
		PersonOptionalDto dto = new PersonOptionalDto();
		dto.setItemCode(this.itemCode.v());
		dto.setPerInfoCtgId(this.perInfoCtgCd);
		dto.setPerInfoCtgCd(perInfoCtgCd);
		dto.setItemName(itemName);
		dto.setPerInfoItemDefId(perInfoItemDefId);
		dto.setRecordId(recordId);
		dto.setDataType(this.dataState.getDataStateType().value);
		switch (dataState.getDataStateType()) {
		case String:
			dto.setValue(dataState.getStringValue());
			break;
		case Numeric:
			dto.setValue(dataState.getNumberValue());
			break;
		case Date:
			dto.setValue(dataState.getDateValue());
			break;
		}
		dto.setRequired(isRequired == IsRequired.REQUIRED);
		return dto;
	}
}
