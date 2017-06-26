package nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal;

import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

@StringMaxLength(10)
public class UniteCode extends CodePrimitiveValue<UniteCode> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UniteCode(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
