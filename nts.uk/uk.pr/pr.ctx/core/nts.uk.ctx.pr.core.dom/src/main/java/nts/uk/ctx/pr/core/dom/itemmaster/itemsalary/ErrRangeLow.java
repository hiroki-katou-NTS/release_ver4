package nts.uk.ctx.pr.core.dom.itemmaster.itemsalary;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalMaxValue;
import nts.arc.primitive.constraint.DecimalMinValue;
import nts.arc.primitive.constraint.DecimalRange;

@DecimalRange(max = "9999999999", min = "0")
public class ErrRangeLow extends DecimalPrimitiveValue<ErrRangeLow> {
	public ErrRangeLow(BigDecimal rawValue) {
		super(rawValue);
	}
}
