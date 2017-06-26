package nts.uk.ctx.pr.formula.dom.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(10)
public class ReferenceMasterCode extends StringPrimitiveValue<ReferenceMasterCode> {
	public ReferenceMasterCode(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;
}
