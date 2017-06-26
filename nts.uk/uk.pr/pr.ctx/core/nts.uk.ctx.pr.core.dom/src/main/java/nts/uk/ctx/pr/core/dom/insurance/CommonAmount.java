package nts.uk.ctx.pr.core.dom.insurance;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalRange;

@DecimalRange(min = "0", max = "9999999999")
public class CommonAmount extends DecimalPrimitiveValue<CommonAmount>{

	public CommonAmount(BigDecimal rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;

}
