package nts.uk.ctx.basic.dom.organization.position;


import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;


@StringCharType(CharType.ALPHABET)
@StringMaxLength(5)
public class AuthorizationCode extends CodePrimitiveValue<AuthorizationCode>{
	/**
	 * AUTHCD
	 */
	private static final long serialVersionUID = 1L;

	public AuthorizationCode(String rawValue) {
		super(rawValue);
	}

}