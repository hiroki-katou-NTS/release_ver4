package nts.uk.ctx.basic.dom.company;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
/**
 * 
 * @author lanlt
 *
 */
@StringMaxLength(20)
@StringCharType(CharType.NUMERIC)
public class TelephoneNo extends StringPrimitiveValue<TelephoneNo>{
	/**serialVersionUID	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param rawValue
	 */
	public TelephoneNo(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	

}
