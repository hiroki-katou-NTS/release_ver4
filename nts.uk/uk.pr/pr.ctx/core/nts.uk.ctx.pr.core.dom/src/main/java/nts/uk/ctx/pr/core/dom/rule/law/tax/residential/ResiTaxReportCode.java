package nts.uk.ctx.pr.core.dom.rule.law.tax.residential;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * 
 * @author lanlt
 *
 */
@StringMaxLength(6)
public class ResiTaxReportCode extends StringPrimitiveValue<ResiTaxReportCode> {
/**
 * contructors
 * @param rawValue
 */
	public ResiTaxReportCode(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	/**serialVersionUID	 */
	private static final long serialVersionUID = 1L;

}
