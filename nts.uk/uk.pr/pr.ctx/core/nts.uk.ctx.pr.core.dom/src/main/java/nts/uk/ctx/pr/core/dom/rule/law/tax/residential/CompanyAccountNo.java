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
@StringMaxLength(15)
/*@StringCharType(CharType.ALPHABET)*/
public class CompanyAccountNo extends StringPrimitiveValue<CompanyAccountNo>{
/**
 * contructors
 * @param rawValue
 */
	public CompanyAccountNo(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	/**	serialVersionUID */
	private static final long serialVersionUID = 1L;

}
