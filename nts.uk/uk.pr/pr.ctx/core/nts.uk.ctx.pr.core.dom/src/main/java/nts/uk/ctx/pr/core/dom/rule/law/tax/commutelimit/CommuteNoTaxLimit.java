package nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;

/**
 * @author tuongvc
 *
 */
@Getter
@Setter
public class CommuteNoTaxLimit extends AggregateRoot {

	/**
	 * companyCode
	 */
	private String companyCode;

	/**
	 * commuNoTaxLimitCode
	 */
	private CommuNoTaxLimitCode commuNoTaxLimitCode;

	/**
	 * commuNoTaxLimitName
	 */
	private CommuNoTaxLimitName commuNoTaxLimitName;

	/**
	 * commuNoTaxLimitValue
	 */
	private CommuNoTaxLimitValue commuNoTaxLimitValue;
	
	

	public CommuteNoTaxLimit() {
		super();
	}

	/**
	 * Constructor CommuNoTaxLimitValue class.
	 * 
	 * @param companyCode
	 * @param commuNoTaxLimitCode
	 * @param commuNoTaxLimitName
	 * @param commuNoTaxLimitValue
	 */
	public CommuteNoTaxLimit(String companyCode, CommuNoTaxLimitCode commuNoTaxLimitCode,
			CommuNoTaxLimitName commuNoTaxLimitName, CommuNoTaxLimitValue commuNoTaxLimitValue) {
		super();
		this.companyCode = companyCode;
		this.commuNoTaxLimitCode = commuNoTaxLimitCode;
		this.commuNoTaxLimitName = commuNoTaxLimitName;
		this.commuNoTaxLimitValue = commuNoTaxLimitValue;
	}

	public static CommuteNoTaxLimit createFromJavaType(String companyCode, String commuNoTaxLimitCode,
			String commuNoTaxLimitName, BigDecimal commuNoTaxLimitValue) {
		return new CommuteNoTaxLimit(
				companyCode, 
				new CommuNoTaxLimitCode(commuNoTaxLimitCode),
				new CommuNoTaxLimitName(commuNoTaxLimitName),
				new CommuNoTaxLimitValue(commuNoTaxLimitValue));

	}

}
