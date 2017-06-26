package nts.uk.ctx.pr.core.dom.paymentdata;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(2)
public class BonusDetailCode extends StringPrimitiveValue<BonusDetailCode> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs.
	 * 
	 * @param rawValue raw value
	 */
	public BonusDetailCode(String rawValue) {
		super(rawValue);
	}
}
