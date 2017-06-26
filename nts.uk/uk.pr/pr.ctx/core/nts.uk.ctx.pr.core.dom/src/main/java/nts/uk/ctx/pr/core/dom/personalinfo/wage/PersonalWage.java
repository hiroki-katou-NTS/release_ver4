package nts.uk.ctx.pr.core.dom.personalinfo.wage;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.personalinfo.wage.wagename.PersonalWageCode;
import nts.uk.shr.com.primitive.PersonId;

public class PersonalWage extends AggregateRoot {
	/** person wage code **/
	@Getter
	private PersonalWageCode wageCode;
	
	/**
	 * Person wage value.
	 */
	@Getter
	private BigDecimal wageValue;

	/**
	 * Personal Id
	 */
	@Getter
	private PersonId personId;

	@Getter
	private CompanyCode companyCode;

	@Getter
	private CategoryAtr categoryAtr;

	@Getter
	private YearMonth startYearMonth;

	@Getter
	private YearMonth endYearMonth;

	public PersonalWage(BigDecimal wageValue, PersonId personId, CompanyCode companyCode, CategoryAtr categoryAtr,
			YearMonth startYearMonth, YearMonth endYearMonth, PersonalWageCode wageCode) {
		super();
		this.wageValue = wageValue;
		this.personId = personId;
		this.companyCode = companyCode;
		this.categoryAtr = categoryAtr;
		this.startYearMonth = startYearMonth;
		this.endYearMonth = endYearMonth;
		this.wageCode = wageCode;
	}

	/**
	 * Create instance using Java type parameters.
	 * 
	 * @return PersonalWage
	 */
	public static PersonalWage createFromJavaType(BigDecimal wageValue, String personId, String companyCode,
			int categoryAtr, int startYearMonth, int endYearMonth, String wageCode) {
		return new PersonalWage(wageValue, new PersonId(personId), new CompanyCode(companyCode),
				EnumAdaptor.valueOf(categoryAtr, CategoryAtr.class), YearMonth.of(startYearMonth), YearMonth.of(endYearMonth), new PersonalWageCode(wageCode));
	}

}
