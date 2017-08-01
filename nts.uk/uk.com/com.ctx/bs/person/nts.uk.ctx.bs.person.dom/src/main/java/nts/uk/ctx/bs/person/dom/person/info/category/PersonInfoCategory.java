package nts.uk.ctx.bs.person.dom.person.info.category;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.text.IdentifierUtil;

@Getter
public class PersonInfoCategory extends AggregateRoot {
	private String personInfoCategoryId;
	private String companyId;
	private CategoryCode categoryCode;
	private CategoryCode categoryParentCode;
	private CategoryName categoryName;
	private PersonEmployeeType personEmployeeType;
	private IsUsed isUsed;
	private CategoryType categoryType;
	private IsFixed isFixed;

	private PersonInfoCategory(String companyId, String categoryCode, String categoryParentCode, String categoryName,
			int personEmployeeType, int isUsed, int categoryType, int isFixed) {
		super();
		this.personInfoCategoryId = IdentifierUtil.randomUniqueId();
		this.companyId = companyId;
		this.categoryCode = new CategoryCode(categoryCode);
		this.categoryParentCode = new CategoryCode(categoryParentCode);
		this.categoryName = new CategoryName(categoryName);
		this.personEmployeeType = EnumAdaptor.valueOf(personEmployeeType, PersonEmployeeType.class);
		this.isUsed = EnumAdaptor.valueOf(isUsed, IsUsed.class);
		this.categoryType = EnumAdaptor.valueOf(categoryType, CategoryType.class);
		this.isFixed = EnumAdaptor.valueOf(isFixed, IsFixed.class);
	}

	public static PersonInfoCategory createFromJavaType(String companyId, String categoryCode,
			String categoryParentCode, String categoryName, int personEmployeeType, int isUsed, int categoryType,
			int isFixed) {
		return new PersonInfoCategory(companyId, categoryCode, categoryParentCode, categoryName, personEmployeeType,
				isUsed, categoryType, isFixed);
	}

}
