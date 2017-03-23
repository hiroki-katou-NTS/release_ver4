package nts.uk.ctx.pr.core.dom.itemmaster.itemsalary;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalMaxValue;
import nts.arc.primitive.constraint.DecimalMinValue;

@DecimalMinValue("0")
@DecimalMaxValue("9999999999")
public class AlRangeHigh extends DecimalPrimitiveValue<AlRangeHigh> {
	public AlRangeHigh(BigDecimal rawValue) {

		super(rawValue);
	}

}
