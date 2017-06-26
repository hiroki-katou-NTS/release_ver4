package nts.uk.ctx.pr.formula.dom.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(4)
public class ReferenceItemCode extends StringPrimitiveValue<ReferenceItemCode>{
	public ReferenceItemCode(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;
}
