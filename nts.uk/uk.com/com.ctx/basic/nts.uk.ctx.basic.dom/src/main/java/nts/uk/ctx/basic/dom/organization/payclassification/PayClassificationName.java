package nts.uk.ctx.basic.dom.organization.payclassification;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(20)
public class PayClassificationName extends StringPrimitiveValue<PayClassificationName> {

	public PayClassificationName(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
