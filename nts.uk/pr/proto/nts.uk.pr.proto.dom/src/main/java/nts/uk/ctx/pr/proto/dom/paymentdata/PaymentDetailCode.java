package nts.uk.ctx.pr.proto.dom.paymentdata;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLengh;

@StringMaxLengh(2)
public class PaymentDetailCode extends StringPrimitiveValue<PaymentDetailCode> {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs.
	 * 
	 * @param rawValue raw value
	 */
	public PaymentDetailCode(String rawValue) {
		super(rawValue);
	}
}
