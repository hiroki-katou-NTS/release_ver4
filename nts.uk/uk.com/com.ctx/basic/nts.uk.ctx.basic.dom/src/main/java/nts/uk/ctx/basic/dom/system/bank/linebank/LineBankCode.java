package nts.uk.ctx.basic.dom.system.bank.linebank;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

@StringMaxLength(2)
@StringCharType(CharType.NUMERIC)
//@StringRegEx("^[1-9][0-9]?$")
/**
 * 
 * @author sonnh1
 *
 */
public class LineBankCode extends CodePrimitiveValue<LineBankCode> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public LineBankCode(String rawValue) {
		super(rawValue);
	}
}
