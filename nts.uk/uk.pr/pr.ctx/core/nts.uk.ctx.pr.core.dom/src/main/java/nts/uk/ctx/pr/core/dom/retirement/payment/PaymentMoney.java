package nts.uk.ctx.pr.core.dom.retirement.payment;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerMaxValue;
import nts.arc.primitive.constraint.IntegerMinValue;

/**
 * 
 * @author Doan Duy Hung
 *
 */

@IntegerMaxValue(999999999)
@IntegerMinValue(0)
public class PaymentMoney extends IntegerPrimitiveValue<PaymentMoney>{
	
	public PaymentMoney(int value) {
		super(value);
	}

}
