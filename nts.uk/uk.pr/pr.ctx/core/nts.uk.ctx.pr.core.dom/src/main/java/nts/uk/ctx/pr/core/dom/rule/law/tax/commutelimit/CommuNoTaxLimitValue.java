package nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalRange;

/**
 * @author tuongvc
 *
 */
@DecimalRange(max = "9999999999", min = "0")
public class CommuNoTaxLimitValue extends DecimalPrimitiveValue<CommuNoTaxLimitValue> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor CommuNoTaxLimitValue class.
	 * 
	 * @param rawValue
	 */
	public CommuNoTaxLimitValue(BigDecimal rawValue) {
		super(rawValue);
	}

}
