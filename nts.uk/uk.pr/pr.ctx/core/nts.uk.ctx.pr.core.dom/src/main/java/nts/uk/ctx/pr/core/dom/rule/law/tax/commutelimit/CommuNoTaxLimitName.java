package nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * @author tuongvc
 *
 */
@StringMaxLength(20)
public class CommuNoTaxLimitName extends StringPrimitiveValue<CommuNoTaxLimitName> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor CommuNoTaxLimitName class.
	 * 
	 * @param rawValue
	 */
	public CommuNoTaxLimitName(String rawValue) {
		super(rawValue);
	}

}
