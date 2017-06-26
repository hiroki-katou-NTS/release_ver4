package nts.uk.ctx.pr.core.dom.personalinfo.wage.wagename;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(40)
public class PersonalWageName extends StringPrimitiveValue<PersonalWageName> {
	public PersonalWageName(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;
}
