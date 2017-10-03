package nts.uk.ctx.bs.person.dom.person.setting.selection;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(2)
@StringCharType(CharType.ALPHA_NUMERIC)
public class IntegrationCode extends StringPrimitiveValue<IntegrationCode> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IntegrationCode(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
