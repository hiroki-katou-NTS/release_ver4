package nts.uk.ctx.bs.person.dom.person.info.category;

import java.util.List;
import java.util.stream.Collectors;

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
	private IsAbolition isAbolition;
	private CategoryType categoryType;
	private IsFixed isFixed;
	private boolean alreadyCopy;

	public final static String ROOT_COMPANY_ID = "000000000000-0000";

	private PersonInfoCategory(String companyId, String categoryCode, String categoryName, int categoryType) {
		super();
		this.personInfoCategoryId = IdentifierUtil.randomUniqueId();
		this.companyId = companyId;
		this.categoryCode = new CategoryCode(categoryCode);
		this.categoryParentCode = null;
		this.categoryName = new CategoryName(categoryName);
		this.personEmployeeType = PersonEmployeeType.EMPLOYEE;
		this.isAbolition = IsAbolition.NOT_ABOLITION;
		this.categoryType = EnumAdaptor.valueOf(categoryType, CategoryType.class);
		this.isFixed = IsFixed.NOT_FIXED;
	}

	private PersonInfoCategory(String personInfoCategoryId, String companyId, String categoryCode,
			String categoryParentCode, String categoryName, int personEmployeeType, int isAbolition, int categoryType,
			int isFixed) {
		super();
		this.personInfoCategoryId = personInfoCategoryId;
		this.categoryCode = new CategoryCode(categoryCode);
		this.categoryParentCode = new CategoryCode(categoryParentCode);
		this.categoryName = new CategoryName(categoryName);
		this.personEmployeeType = EnumAdaptor.valueOf(personEmployeeType, PersonEmployeeType.class);
		this.isAbolition = EnumAdaptor.valueOf(isAbolition, IsAbolition.class);
		this.categoryType = EnumAdaptor.valueOf(categoryType, CategoryType.class);
		this.isFixed = EnumAdaptor.valueOf(isFixed, IsFixed.class);
	}

	private PersonInfoCategory(String personInfoCategoryId, String companyId, int categoryType) {
		super();
		this.personInfoCategoryId = personInfoCategoryId;
		this.companyId = companyId;
		this.categoryType = EnumAdaptor.valueOf(categoryType, CategoryType.class);
	}

	public static PersonInfoCategory createFromJavaType(String companyId, String categoryCode, String categoryName,
			int categoryType) {
		return new PersonInfoCategory(companyId, categoryCode, categoryName, categoryType);
	}

	public static PersonInfoCategory createFromEntity(String personInfoCategoryId, String companyId,
			String categoryCode, String categoryParentCode, String categoryName, int personEmployeeType,
			int isAbolition, int categoryType, int isFixed) {
		return new PersonInfoCategory(personInfoCategoryId, companyId, categoryCode, categoryParentCode, categoryName,
				personEmployeeType, isAbolition, categoryType, isFixed);
	}

	public static PersonInfoCategory createFromJavaTypeUpdate(String personInfoCategoryId, String companyId,
			int categoryType) {
		return new PersonInfoCategory(personInfoCategoryId, companyId, categoryType);
	}
	
	public static List<PersonInfoCategory> getAllPerInfoCategoryWithCondition(List<PersonInfoCategory> lstObj){
		return lstObj.stream().filter(obj -> 
			obj.getPersonEmployeeType() == PersonEmployeeType.EMPLOYEE
			&& obj.getIsAbolition() == IsAbolition.NOT_ABOLITION
			&& (obj.getCategoryType() != CategoryType.MULTIINFO
			|| obj.getCategoryType() != CategoryType.DUPLICATEHISTORY)
		).collect(Collectors.toList());
	}

	public void setCategoryName(String name) {
		this.categoryName = new CategoryName(name);
	}
	
	public void setCategoryType(int categoryType) {
		this.categoryType = EnumAdaptor.valueOf(categoryType, CategoryType.class);
	}
}
