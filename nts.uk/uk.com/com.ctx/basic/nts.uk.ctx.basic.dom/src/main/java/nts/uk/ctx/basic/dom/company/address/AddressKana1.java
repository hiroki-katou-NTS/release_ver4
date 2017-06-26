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
@StringMaxLength(60)
@StringCharType(CharType.ALPHABET)
public class AddressKana1 extends StringPrimitiveValue<AddressKana1>{

	/**serialVersionUID*/
	private static final long serialVersionUID = 1L;
	/**
	 * contructors
	 * @param rawValue
	 */
	public AddressKana1(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}


}
