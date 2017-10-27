package find.person.info.item;

import lombok.Getter;
import nts.uk.ctx.bs.person.dom.person.info.singleitem.DataTypeValue;

@Getter
public class DateItemDto extends DataTypeStateDto {

	private int dateItemType;

	private DateItemDto(int dateItemType) {
		super();
		this.dataTypeValue = DataTypeValue.DATE.value;
		this.dateItemType = dateItemType;
	}

	public static DateItemDto createFromJavaType(int dateItemType) {
		return new DateItemDto(dateItemType);
	}
}
