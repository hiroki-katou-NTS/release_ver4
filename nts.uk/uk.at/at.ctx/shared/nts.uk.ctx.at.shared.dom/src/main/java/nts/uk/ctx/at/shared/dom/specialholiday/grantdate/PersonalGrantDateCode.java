package nts.uk.ctx.at.shared.dom.specialholiday.grantdate;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(2)
public class PersonalGrantDateCode extends CodePrimitiveValue<PersonalGrantDateCode> {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public PersonalGrantDateCode(String rawValue) {
		super(rawValue);
	}

}
