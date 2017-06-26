package nts.uk.ctx.pr.core.dom.paymentdata.insure;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

/**
 * 健保標準報酬
 */
@IntegerRange(min = 0, max = 99999999)
public class HealthInsuranceAverageEarn extends IntegerPrimitiveValue<HealthInsuranceAverageEarn> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public HealthInsuranceAverageEarn(Integer rawValue) {
		super(rawValue);
	}

}
