package find.person.info.item;

import lombok.Value;
import nts.uk.ctx.bs.person.dom.person.info.item.PersonInfoItemDefinition;

@Value
public class PersonInfoItemDefDto {
	private String id;
	private String perInfoCtgId;
	private String itemName;
	private int isAbolition;
	private int systemRequired;

	public static PersonInfoItemDefDto fromDomain(PersonInfoItemDefinition domain) {

		return new PersonInfoItemDefDto(domain.getPerInfoItemDefId(),
				domain.getPerInfoCategoryId(),domain.getItemName().v(),
				domain.getIsAbolition().value, domain.getSystemRequired().value);

	}

}
