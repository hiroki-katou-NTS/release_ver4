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
public class FaxNo extends StringPrimitiveValue<FaxNo>{
	/**serialVersionUID	 */
	private static final long serialVersionUID = 1L;
	/**
	 * contructor
	 * @param rawValue raw value
	 *  */
	public FaxNo(String rawValue) {
		super(rawValue);
	}
}
