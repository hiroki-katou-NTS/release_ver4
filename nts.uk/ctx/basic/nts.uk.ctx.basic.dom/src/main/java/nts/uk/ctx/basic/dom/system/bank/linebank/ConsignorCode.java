package nts.uk.ctx.basic.dom.system.bank.linebank;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

@StringMaxLength(10)
@StringCharType(CharType.NUMERIC)
/**
 * 
 * @author sonnh1
 *
 */
public class ConsignorCode extends CodePrimitiveValue<ConsignorCode> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public ConsignorCode(String rawValue) {
		super(rawValue);
	}
}
