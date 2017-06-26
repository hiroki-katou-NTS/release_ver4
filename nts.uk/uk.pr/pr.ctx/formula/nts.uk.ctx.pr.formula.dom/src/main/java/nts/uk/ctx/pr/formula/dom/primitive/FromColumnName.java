package nts.uk.ctx.pr.formula.dom.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(30)
public class FromColumnName extends StringPrimitiveValue<FromColumnName>{

	public FromColumnName(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;

}
