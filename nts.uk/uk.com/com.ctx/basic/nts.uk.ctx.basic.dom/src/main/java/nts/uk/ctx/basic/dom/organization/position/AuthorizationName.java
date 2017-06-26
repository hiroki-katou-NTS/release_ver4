package nts.uk.ctx.basic.dom.organization.position;


import nts.arc.primitive.PrimitiveValue;
import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(20)
public class AuthorizationName  extends StringPrimitiveValue<PrimitiveValue<String>>{
	/**
	 * AUTH_NAME
	 */
	private static final long serialVersionUID = 1L;

	public AuthorizationName(String rawValue) {
		super(rawValue);
	}

}