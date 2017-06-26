package nts.uk.ctx.pr.formula.dom.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(36)
public class VariableId extends StringPrimitiveValue<VariableId> {

	public VariableId(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;

}
