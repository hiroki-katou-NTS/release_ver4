package nts.uk.ctx.pr.core.dom.allot;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.paymentdata.BonusDetailCode;
import nts.uk.ctx.pr.core.dom.paymentdata.PaymentDetailCode;
import nts.uk.shr.com.primitive.PersonId;

public class PersonalAllotSetting extends AggregateRoot {

	@Getter
	private final CompanyCode companyCode;

	@Getter
	private final PersonId personId;

	@Getter
	private final YearMonth startDate;

	@Getter
	private BonusDetailCode bonusDetailCode;

	@Getter
	private YearMonth endDate;

	@Getter
	private PaymentDetailCode paymentDetailCode;

	public PersonalAllotSetting(CompanyCode companyCode, PersonId personId, YearMonth startDate, YearMonth endDate, BonusDetailCode bonusDetailCode, PaymentDetailCode paymentDetailCode) {
		super();
		this.companyCode = companyCode;
		this.personId = personId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.bonusDetailCode = bonusDetailCode;
		this.paymentDetailCode = paymentDetailCode;
	}

	/**
	 * Create instance using Java type parameters.
	 * 
	 * @param companyCode
	 *            company code
	 * @param personId
	 *            person id
	 * @param startDate
	 *            start date
	 * @return PersonalPaymentSetting
	 */
	public static PersonalAllotSetting createFromJavaType(String companyCode, String personId, int startYearMonth, int endYearMonth, String bonusDetailCode, String paymentDetailCode) {
		return new PersonalAllotSetting(new CompanyCode(companyCode), new PersonId(personId),
				new YearMonth(startYearMonth), new YearMonth(endYearMonth), new BonusDetailCode(bonusDetailCode), new PaymentDetailCode(paymentDetailCode));
	}
}
