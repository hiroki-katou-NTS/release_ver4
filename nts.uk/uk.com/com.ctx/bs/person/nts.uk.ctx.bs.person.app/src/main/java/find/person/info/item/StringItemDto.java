package find.person.info.item;

import lombok.Getter;
import nts.uk.ctx.bs.person.dom.person.info.singleitem.DataTypeValue;

@Getter
public class StringItemDto extends DataTypeStateDto {

	private int stringItemLength;
	private int stringItemType;
	private int stringItemDataType;

	private StringItemDto(int stringItemLength, int stringItemType, int stringItemDataType) {
		super();
		this.dataTypeValue = DataTypeValue.STRING.value;
		this.stringItemLength = stringItemLength;
		this.stringItemType = stringItemType;
		this.stringItemDataType = stringItemDataType;
	}

	public static StringItemDto createFromJavaType(int stringItemLength, int stringItemType, int stringItemDataType) {
		return new StringItemDto(stringItemLength, stringItemType, stringItemDataType);
	}

}
