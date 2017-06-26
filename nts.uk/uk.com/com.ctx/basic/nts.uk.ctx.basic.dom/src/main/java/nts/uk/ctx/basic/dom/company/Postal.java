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
@StringMaxLength(10)
@StringCharType(CharType.NUMERIC)
public class Postal extends StringPrimitiveValue<Postal>{
	/**serialVersionUID	 */
	private static final long serialVersionUID = 1L;
	/**
	 * constructors
	 * @param rawValue raw value
	 */
	public Postal(String rawValue) {
		super(rawValue);
	}



}
