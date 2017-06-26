package nts.uk.ctx.pr.core.dom.itemmaster.itemsalary;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalRange;
@DecimalRange(max = "9999999999", min = "0")
public class ErrRangeHigh extends DecimalPrimitiveValue<ErrRangeHigh> {
	public ErrRangeHigh(BigDecimal rawValue) {
		super(rawValue);
	}
}
	