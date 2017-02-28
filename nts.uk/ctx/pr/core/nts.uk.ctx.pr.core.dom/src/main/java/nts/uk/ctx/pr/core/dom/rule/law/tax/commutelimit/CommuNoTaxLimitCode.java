package nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * @author tuongvc
 *
 */
@StringMaxLength(2)
public class CommuNoTaxLimitCode extends StringPrimitiveValue<CommuNoTaxLimitCode> {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor CommuNoTaxLimitCode class
	 * 
	 * @param rawValue
	 */
	public CommuNoTaxLimitCode(String rawValue) {
		super(rawValue);
	}

}
