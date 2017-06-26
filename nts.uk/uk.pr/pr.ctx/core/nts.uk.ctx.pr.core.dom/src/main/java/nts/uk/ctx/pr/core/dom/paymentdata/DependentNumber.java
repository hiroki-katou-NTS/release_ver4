package nts.uk.ctx.pr.core.dom.paymentdata;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

/**
 * 扶養人数（その月時点）　※計算項目
 */
@IntegerRange(min = 0, max = 99)
public class DependentNumber extends IntegerPrimitiveValue<DependentNumber> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public DependentNumber(Integer rawValue) {
		super(rawValue);
	}

}
