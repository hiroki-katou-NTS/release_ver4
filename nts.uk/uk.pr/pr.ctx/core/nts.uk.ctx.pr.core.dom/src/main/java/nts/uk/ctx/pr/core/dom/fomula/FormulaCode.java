package nts.uk.ctx.pr.core.dom.fomula;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(3)
public class FormulaCode extends StringPrimitiveValue<FormulaCode> {

	public FormulaCode(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;

}
