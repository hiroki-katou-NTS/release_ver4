package nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(20)
@StringCharType(CharType.ALPHA_NUMERIC)
public class PersonalUnitPriceName extends StringPrimitiveValue<PersonalUnitPriceName> {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param rawValue
	 */
	public PersonalUnitPriceName(String rawValue) {
		super(rawValue);
	}

}
