package nts.uk.ctx.pr.core.dom.paymentdata;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerMaxValue;
import nts.arc.primitive.constraint.IntegerMinValue;

/**
 * 賞与税率
 */
@IntegerMinValue(0)
@IntegerMaxValue(99999)
public class BonusTaxRate extends IntegerPrimitiveValue<BonusTaxRate> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public BonusTaxRate(Integer rawValue) {
		super(rawValue);
	}

}
