package nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(12)
public class PersonalUnitPriceShortName extends StringPrimitiveValue<PersonalUnitPriceShortName> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PersonalUnitPriceShortName(String rawValue) {
		super(rawValue);
	}

}
