package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalMaxValue;
import nts.arc.primitive.constraint.DecimalMinValue;
/**
 * 
 * @author sonnh1
 *
 */
@DecimalMaxValue("9999999999")
@DecimalMinValue("0")
public class MonthlyResidenceTax extends DecimalPrimitiveValue<MonthlyResidenceTax> {

	public MonthlyResidenceTax(BigDecimal rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
	
}
