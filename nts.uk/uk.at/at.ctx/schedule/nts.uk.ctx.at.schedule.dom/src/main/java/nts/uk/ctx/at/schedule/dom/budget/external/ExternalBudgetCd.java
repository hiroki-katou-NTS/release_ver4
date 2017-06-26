package nts.uk.ctx.at.schedule.dom.budget.external;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(3)
public class ExternalBudgetCd extends CodePrimitiveValue<ExternalBudgetCd> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ExternalBudgetCd(String rawValue) {
		super(rawValue);
	}
}
