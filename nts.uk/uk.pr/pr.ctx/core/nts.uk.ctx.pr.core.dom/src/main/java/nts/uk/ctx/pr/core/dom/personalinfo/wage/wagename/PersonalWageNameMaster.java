package nts.uk.ctx.pr.core.dom.personalinfo.wage.wagename;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;

public class PersonalWageNameMaster extends AggregateRoot {

	@Getter
	private CompanyCode companyCode;

	@Getter
	private CategoryAtr categoryAtr;

	@Getter
	private PersonalWageCode personalWageCode;

	@Getter
	private PersonalWageName personalWageName;

	public PersonalWageNameMaster(CompanyCode companyCode
			, CategoryAtr categoryAtr
			, PersonalWageCode personalWageCode,
			PersonalWageName personalWageName) {
		super();
		this.companyCode = companyCode;
		this.categoryAtr = categoryAtr;
		this.personalWageCode = personalWageCode;
		this.personalWageName = personalWageName;
	}

	public static PersonalWageNameMaster createFromJavaType(
			String companyCode,
			int categoryAtr,
			String personalWageCode,
			String personalWageName){
		return new PersonalWageNameMaster(
				new CompanyCode(companyCode),
				EnumAdaptor.valueOf(categoryAtr, CategoryAtr.class),
				new PersonalWageCode(personalWageCode),
				new PersonalWageName(personalWageName));
	}
}
