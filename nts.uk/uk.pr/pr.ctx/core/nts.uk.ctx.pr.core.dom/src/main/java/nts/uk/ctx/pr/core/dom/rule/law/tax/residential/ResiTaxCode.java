/**
 * 
 */
package nts.uk.ctx.pr.core.dom.rule.law.tax.residential;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * @author lanlt
 *
 */
@StringMaxLength(6)
@StringCharType(CharType.NUMERIC)
public class ResiTaxCode extends StringPrimitiveValue<ResiTaxCode>{
	/**serialVersionUID	 */
	private static final long serialVersionUID = 1L;
	/**
	 * contructors
	 * @param rawValue
	 */
	public ResiTaxCode(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
