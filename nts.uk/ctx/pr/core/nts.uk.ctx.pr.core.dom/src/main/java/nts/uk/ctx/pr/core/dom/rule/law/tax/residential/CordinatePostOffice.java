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
@StringMaxLength(24)
public class CordinatePostOffice extends StringPrimitiveValue<CordinatePostOffice>{
/**
 * contructors
 * @param rawValue
 */
	public CordinatePostOffice(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	/**	serialVersionUID */
	private static final long serialVersionUID = 1L;

}
