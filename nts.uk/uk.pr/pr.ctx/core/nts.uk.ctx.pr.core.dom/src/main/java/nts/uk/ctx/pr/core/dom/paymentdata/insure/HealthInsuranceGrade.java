package nts.uk.ctx.pr.core.dom.paymentdata.insure;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

/**
 * 健康保険等級
 */
@IntegerRange(min = 0, max = 99)
public class HealthInsuranceGrade extends IntegerPrimitiveValue<HealthInsuranceGrade> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public HealthInsuranceGrade(Integer rawValue) {
		super(rawValue);
	}

}
