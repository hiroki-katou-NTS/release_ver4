package nts.uk.ctx.pr.formula.dom.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(20)
public class EasyFormulaName extends StringPrimitiveValue<EasyFormulaName> {
	public EasyFormulaName(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;
}
