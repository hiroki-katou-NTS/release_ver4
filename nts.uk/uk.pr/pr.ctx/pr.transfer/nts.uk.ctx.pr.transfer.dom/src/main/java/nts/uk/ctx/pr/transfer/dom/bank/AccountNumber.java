package nts.uk.ctx.pr.transfer.dom.bank;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * 
 * @author HungTT - 口座番号
 *
 */

@StringMaxLength(7)
@StringCharType(CharType.NUMERIC)
public class AccountNumber extends StringPrimitiveValue<AccountNumber> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public AccountNumber(String arg0) {
		super(arg0);
	}
}
