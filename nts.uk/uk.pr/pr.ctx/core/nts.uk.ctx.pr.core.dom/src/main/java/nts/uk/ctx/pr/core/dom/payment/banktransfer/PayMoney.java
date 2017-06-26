package nts.uk.ctx.pr.core.dom.payment.banktransfer;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalMaxValue;

@DecimalMaxValue("9999999999")
public class PayMoney extends DecimalPrimitiveValue<PayMoney> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PayMoney(BigDecimal rawValue) {
		super(rawValue);
	}


}
