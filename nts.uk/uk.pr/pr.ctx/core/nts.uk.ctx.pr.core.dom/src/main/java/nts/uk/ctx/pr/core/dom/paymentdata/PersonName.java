package nts.uk.ctx.pr.core.dom.paymentdata;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(50)
public class PersonName extends StringPrimitiveValue<PersonName> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public PersonName(String rawValue) {
		super(rawValue);
	}

}
