/**
 * 
 */
package nts.uk.ctx.pr.core.dom.rule.law.tax.residential;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(15)
/*@StringCharType(CharType.ALPHABET)*/
public class CompanySpecifiedNo extends StringPrimitiveValue<CompanySpecifiedNo> {
/**
 * contructors
 * @param rawValue
 */
	public CompanySpecifiedNo(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	/**	 serialVersionUID*/
	private static final long serialVersionUID = 1L;

}
