package nts.uk.ctx.pr.formula.dom.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(2000)
public class FromTable extends StringPrimitiveValue<FromTable>{

	public FromTable(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;

}
