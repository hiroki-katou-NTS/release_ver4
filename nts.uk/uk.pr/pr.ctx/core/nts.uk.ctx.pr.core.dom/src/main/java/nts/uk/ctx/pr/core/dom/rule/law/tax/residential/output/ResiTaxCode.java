package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output;

import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

@StringMaxLength(6)
public class ResiTaxCode extends CodePrimitiveValue<ResiTaxCode> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResiTaxCode(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
