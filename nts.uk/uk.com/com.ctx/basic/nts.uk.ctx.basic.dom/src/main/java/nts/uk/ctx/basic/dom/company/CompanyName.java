package nts.uk.ctx.basic.dom.company;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;
/**
 * @author lanlt
 *
 */
@StringMaxLength(40)
/*@StringCharType(CharType.ALPHA_NUMERIC)*/
public class CompanyName extends StringPrimitiveValue<CompanyName> {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/**
	 * Constructs.
	 * 
	 * @param rawValue raw value
	 */
	public CompanyName(String rawValue) {
		super(rawValue);
	}

}
