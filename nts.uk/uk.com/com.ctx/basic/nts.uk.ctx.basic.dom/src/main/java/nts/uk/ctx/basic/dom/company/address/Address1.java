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
@StringMaxLength(120)
/*@StringCharType(CharType.ALPHA_NUMERIC)*/
public class Address1 extends StringPrimitiveValue<Address1> {
	/**serialVersionUID	 */
	private static final long serialVersionUID = 1L;
	/**
	 * contructors
	 * @param rawValue raw value
	 */
	public Address1(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	

}
