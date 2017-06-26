package nts.uk.ctx.basic.dom.system.bank;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

@StringMaxLength(4)
@StringCharType(CharType.NUMERIC)
public class BankCode extends CodePrimitiveValue<BankCode> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BankCode(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
