package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.payday;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalRange;

@DecimalRange(min = "0", max = "31")
public class NeededWorkDay extends DecimalPrimitiveValue<NeededWorkDay> {
	private static final long serialVersionUID = 1L;

	public NeededWorkDay(BigDecimal rawValue) {
		super(rawValue);
	}

}
