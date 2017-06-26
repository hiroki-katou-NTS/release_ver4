package nts.uk.ctx.pr.formula.dom.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(20)
public class VariableName extends StringPrimitiveValue<VariableName>{

	public VariableName(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;

}
