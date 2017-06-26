package nts.uk.ctx.pr.core.dom.paymentdata.insure;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

/**
 * 厚生年金保険等級
 */
@IntegerRange(min = 0, max = 99)
public class PensionInsuranceGrade extends IntegerPrimitiveValue<PensionInsuranceGrade> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public PensionInsuranceGrade(Integer rawValue) {
		super(rawValue);
	}

}
