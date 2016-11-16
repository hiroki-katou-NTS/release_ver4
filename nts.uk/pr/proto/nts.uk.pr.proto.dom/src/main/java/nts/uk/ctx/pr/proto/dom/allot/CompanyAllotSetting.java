package nts.uk.ctx.pr.proto.dom.allot;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.proto.dom.paymentdata.BonusDetailCode;
import nts.uk.ctx.pr.proto.dom.paymentdata.Payment;
import nts.uk.ctx.pr.proto.dom.paymentdata.PaymentDetailCode;

public class CompanyAllotSetting extends AggregateRoot {

	@Getter
	private final CompanyCode companyCode;

	@Getter
	private final YearMonth startDate;

	@Getter
	private BonusDetailCode bonusDetailCode;

	@Getter
	private YearMonth endDate;

	@Getter
	private PaymentDetailCode paymentDetailCode;

	public CompanyAllotSetting(CompanyCode companyCode, YearMonth startDate, BonusDetailCode bonusDetailCode,PaymentDetailCode paymentDetailCode) {
		super();
		this.companyCode = companyCode;
		this.startDate = startDate;
		this.bonusDetailCode = bonusDetailCode;
		this.paymentDetailCode = paymentDetailCode;
	}

	/**
	 * Create instance using Java type parameters.
	 * 
	 * @param companyCode
	 *            company code
	 * @param startDate
	 *            start date
	 * @return CompanyPaymentSetting
	 */
	public static CompanyAllotSetting createFromJavaType(String companyCode, int startDate, String bonusDetailCode, String paymentDetailCode) {
		return new CompanyAllotSetting(new CompanyCode(companyCode), new YearMonth(startDate), new BonusDetailCode(bonusDetailCode), new PaymentDetailCode(paymentDetailCode));
	}
}
