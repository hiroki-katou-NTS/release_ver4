package nts.uk.ctx.pr.formula.dom.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(2000)
public class WhereCondition extends StringPrimitiveValue<WhereCondition>{

	public WhereCondition(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;

}
