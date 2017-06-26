package nts.uk.ctx.pr.core.dom.rule.law.tax.residential;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(15)
public class ResiTaxAutonomyKana extends StringPrimitiveValue<ResiTaxAutonomyKana> {
	
	/**serialVersionUID	 */
	private static final long serialVersionUID = 1L;

	/**
	 * contructors
	 * 
	 * @param rawValue
	 */
	public ResiTaxAutonomyKana(String rawValue) {
		super(rawValue);
	}

}
