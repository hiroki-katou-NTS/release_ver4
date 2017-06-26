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
@StringMaxLength(13)
@StringCharType(CharType.NUMERIC)
public class CorporateMyNumber extends StringPrimitiveValue<CorporateMyNumber>{
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/**
	 * contructors
	 * @param rawValue
	 */
	public CorporateMyNumber(String rawValue) {
		super(rawValue);
	}


	
}
