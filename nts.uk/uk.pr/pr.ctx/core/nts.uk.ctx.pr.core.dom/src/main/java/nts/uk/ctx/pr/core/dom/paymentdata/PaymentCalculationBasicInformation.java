package nts.uk.ctx.pr.core.dom.paymentdata;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;

public class PaymentCalculationBasicInformation extends AggregateRoot {

	/** company code */
	@Getter
	private CompanyCode companyCode;

	/** Base Days */
	@Getter
	private BigDecimal baseDays;

	/** Base Hours */
	@Getter
	private BigDecimal baseHours;

	public PaymentCalculationBasicInformation(CompanyCode companyCode, BigDecimal baseDays, BigDecimal baseHours) {
		super();
		this.companyCode = companyCode;
		this.baseDays = baseDays;
		this.baseHours = baseHours;
	}
	
	/**
	 * 
	 * @param companyCode
	 * @param baseDays
	 * @param baseHours
	 * @return
	 */
	public static PaymentCalculationBasicInformation createFromJavaType(String companyCode, BigDecimal baseDays, BigDecimal baseHours) {
		return new PaymentCalculationBasicInformation(new CompanyCode(companyCode), baseDays, baseHours);
	}

}
