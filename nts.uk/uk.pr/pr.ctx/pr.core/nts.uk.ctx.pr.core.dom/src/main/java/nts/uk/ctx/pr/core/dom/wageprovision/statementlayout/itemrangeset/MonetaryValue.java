package nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.itemrangeset;

import nts.arc.primitive.LongPrimitiveValue;
import nts.arc.primitive.constraint.LongMaxValue;
import nts.arc.primitive.constraint.LongMinValue;

/**
 * 金額値
 */
@LongMinValue(-9999999999L)
@LongMaxValue(9999999999L)
public class MonetaryValue extends LongPrimitiveValue<MonetaryValue>{

	public MonetaryValue(long rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L; 
}
