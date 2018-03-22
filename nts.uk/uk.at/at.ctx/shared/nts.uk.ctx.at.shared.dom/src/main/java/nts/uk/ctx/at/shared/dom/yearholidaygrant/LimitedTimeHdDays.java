package nts.uk.ctx.at.shared.dom.yearholidaygrant;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

@IntegerRange(min = 0, max = 99)
public class LimitedTimeHdDays extends IntegerPrimitiveValue<LimitedTimeHdDays> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LimitedTimeHdDays(Integer rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
