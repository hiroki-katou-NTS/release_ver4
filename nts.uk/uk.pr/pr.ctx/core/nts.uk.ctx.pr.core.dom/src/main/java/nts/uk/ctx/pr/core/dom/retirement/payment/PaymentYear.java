package nts.uk.ctx.pr.core.dom.retirement.payment;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerMaxValue;
import nts.arc.primitive.constraint.IntegerMinValue;

/**
 * 
 * @author Doan Duy Hung
 *
 */

@IntegerMaxValue(99)
@IntegerMinValue(0)
public class PaymentYear extends IntegerPrimitiveValue<PaymentYear>{
	
	public PaymentYear(int value) {
		super(value);
	}
}
