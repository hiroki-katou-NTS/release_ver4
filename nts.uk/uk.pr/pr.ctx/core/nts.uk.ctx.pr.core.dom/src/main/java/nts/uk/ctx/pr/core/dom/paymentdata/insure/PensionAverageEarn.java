package nts.uk.ctx.pr.core.dom.paymentdata.insure;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

/**
 * 厚年標準報酬
 */
@IntegerRange(min = 0, max = 99999999)
public class PensionAverageEarn extends IntegerPrimitiveValue<PensionAverageEarn> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public PensionAverageEarn(Integer rawValue) {
		super(rawValue);
	}

}
