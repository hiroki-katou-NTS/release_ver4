package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.standardday;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

@IntegerRange(min = 1, max = 31)
public class IncometaxStdDay extends IntegerPrimitiveValue<IncometaxStdDay> {

	private static final long serialVersionUID = 1L;

	public IncometaxStdDay(Integer rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
