package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalMaxValue;
import nts.arc.primitive.constraint.DecimalMinValue;

@DecimalMaxValue("9999999")
@DecimalMinValue("0")
public class StaffNo extends DecimalPrimitiveValue<StaffNo> {

	public StaffNo(BigDecimal rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
