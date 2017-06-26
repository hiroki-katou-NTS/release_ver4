package nts.uk.ctx.pr.core.dom.retirement.payitem;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
/**
 * 退職金項目印刷名称
 * @author Doan Duy Hung
 *
 */

@StringMaxLength(12)
public class RetirementPayItemPrintName extends StringPrimitiveValue<RetirementPayItemPrintName>{
	public RetirementPayItemPrintName(String value) {
		super(value);
	}
}
