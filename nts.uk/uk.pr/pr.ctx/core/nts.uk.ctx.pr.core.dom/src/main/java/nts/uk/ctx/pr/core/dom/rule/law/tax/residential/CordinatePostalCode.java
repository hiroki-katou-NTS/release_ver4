/**
 * 
 */
package nts.uk.ctx.pr.core.dom.rule.law.tax.residential;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * @author lanlt
 *
 */
@StringMaxLength(7)
/*@StringCharType(CharType.ALPHABET)*/
public class CordinatePostalCode extends StringPrimitiveValue<CordinatePostOffice>{
/**
 * contructors
 * @param rawValue
 */
	public CordinatePostalCode(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	/**	 serialVersionUID*/
	private static final long serialVersionUID = 1L;

}
