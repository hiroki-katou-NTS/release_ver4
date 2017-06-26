package nts.uk.ctx.basic.dom.system.bank.linebank;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(7)
@StringCharType(CharType.NUMERIC)
/**
 * Account Number
 * @author sonnh1
 *
 */
public class AccountNo extends StringPrimitiveValue<AccountNo> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public AccountNo(String arg0) {
		super(arg0);
	}
}
