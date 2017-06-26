package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.standardday;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

@IntegerRange(min = 1, max = 31)
public class SocialInsStdDay extends IntegerPrimitiveValue<SocialInsStdDay> {

	private static final long serialVersionUID = 1L;

	public SocialInsStdDay(Integer rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
