package nts.uk.ctx.basic.dom.company.address;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
/**
 * 
 * @author lanlt
 *
 */
@StringMaxLength(40)
@StringCharType(CharType.ALPHABET)
public class AddressKana2 extends StringPrimitiveValue<AddressKana2>{
	/**serialVersionUID	 */
	private static final long serialVersionUID = 1L;
	/**
	 * contructors
	 * @param rawValue
	 * @return AddressKana2
	 */
	public AddressKana2(String rawValue) {
		super(rawValue);
	}

	

}
