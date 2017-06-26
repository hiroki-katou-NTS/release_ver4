package nts.uk.ctx.basic.dom.organization.position;

import nts.arc.primitive.PrimitiveValue;
import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

@StringCharType(CharType.ALPHABET)
@StringMaxLength(2)
public class AuthScopeAtr extends StringPrimitiveValue<PrimitiveValue<String>>{
	/**
	 * AUTH_SCOPE_ATR
	 */
	private static final long serialVersionUID = 1L;

	public AuthScopeAtr(String rawValue) {
		super(rawValue);
	}

}