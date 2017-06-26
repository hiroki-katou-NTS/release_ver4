package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.paymentdata.BonusDetailCode;
import nts.uk.ctx.pr.core.dom.paymentdata.PaymentDetailCode;

public class CompanyAllotSetting extends AggregateRoot {


	@Getter
	private final CompanyCode companyCode;
	
	@Getter
	private String historyId;

	@Getter
	private final YearMonth startDate;
	
	@Getter
	@Setter
	private YearMonth endDate;
	
	@Getter
	private PaymentDetailCode paymentDetailCode;
	
	@Getter
	private BonusDetailCode bonusDetailCode;

	

	public CompanyAllotSetting(CompanyCode companyCode,String historyId, YearMonth startYearMonth, YearMonth endYearMonth, PaymentDetailCode paymentDetailCode, BonusDetailCode bonusDetailCode) {
		super();
		this.companyCode = companyCode;
		this.historyId = historyId;
		this.startDate = startYearMonth;
		this.endDate = endYearMonth;
		this.paymentDetailCode = paymentDetailCode;
		this.bonusDetailCode = bonusDetailCode;
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
	public static CompanyAllotSetting createFromJavaType(String companyCode, String historyId,int startYearMonth, int endYearMonth, String paymentDetailCode,String bonusDetailCode) {
		return new CompanyAllotSetting(
					new CompanyCode(companyCode), 
					historyId,
					new YearMonth(startYearMonth), 
					new YearMonth(endYearMonth), 
					new PaymentDetailCode(paymentDetailCode),
					new BonusDetailCode(bonusDetailCode));
	}
}
