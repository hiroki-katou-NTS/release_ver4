package nts.uk.ctx.pr.core.dom.itemmaster.itemsalary;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalRange;

@DecimalRange(max = "9999999999", min = "1")
public class LimitMny extends DecimalPrimitiveValue<LimitMny> {
	public LimitMny(BigDecimal rawValue) {
		super(rawValue);

	}

}