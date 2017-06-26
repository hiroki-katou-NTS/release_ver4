package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

/**
 * 
 * @author Duc
 * code of pay class
 */
@StringCharType(CharType.NUMERIC)
@StringMaxLength(4)
public class PayClassCode  extends CodePrimitiveValue<PayClassCode>{

	public PayClassCode(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

}
