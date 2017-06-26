/**
 * 
 */
package nts.uk.ctx.basic.dom.company;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * @author lanlt
 *
 */
@StringMaxLength(40)
public class CompanyNameGlobal  extends StringPrimitiveValue<CompanyNameGlobal>{
	/**
	 * 
	 * @param rawValue
	 */
	public CompanyNameGlobal(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
