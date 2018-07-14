package nts.uk.ctx.at.shared.dom.specialholidaynew.grantcondition;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.PrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

/**
 * 周期
 * 
 * @author tanlv
 *
 */
@IntegerRange(min = 0, max = 150)
public class AgeLimit extends IntegerPrimitiveValue<PrimitiveValue<Integer>> {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public AgeLimit(int rawValue) {
		super(rawValue);
	}
}
