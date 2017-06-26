package nts.uk.ctx.pr.formula.dom.primitive;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalMaxValue;

@DecimalMaxValue("9999999999")
public class MaxValue extends DecimalPrimitiveValue<MaxValue> {
	public MaxValue(BigDecimal rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;
}
