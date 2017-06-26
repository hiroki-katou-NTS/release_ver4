package nts.uk.ctx.pr.formula.dom.primitive;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalMantissaMaxLength;

@DecimalMantissaMaxLength(4)
public class TimeNotationSetting extends DecimalPrimitiveValue<TimeNotationSetting>{

	public TimeNotationSetting(BigDecimal rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;

}
