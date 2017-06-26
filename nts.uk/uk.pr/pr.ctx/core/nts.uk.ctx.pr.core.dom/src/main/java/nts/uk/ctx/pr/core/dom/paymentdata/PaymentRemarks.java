package nts.uk.ctx.pr.core.dom.paymentdata;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * 備考
 */
@StringMaxLength(200)
public class PaymentRemarks extends StringPrimitiveValue<PaymentRemarks> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public PaymentRemarks(String rawValue) {
		super(rawValue);
	}

}
