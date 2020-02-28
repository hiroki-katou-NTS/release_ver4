package nts.uk.ctx.pr.core.dom.wageprovision.formula;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;
import nts.uk.shr.com.primitive.ZeroPaddedCode;

/**
 * 
 * @author HungTT - 計算式名
 *
 */

@StringMaxLength(30)
public class FormulaName extends StringPrimitiveValue<FormulaName> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FormulaName(String rawValue) {
		super(rawValue);
	}

}
