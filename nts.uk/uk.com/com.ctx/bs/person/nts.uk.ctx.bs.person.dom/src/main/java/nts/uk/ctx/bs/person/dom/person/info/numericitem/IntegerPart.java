package nts.uk.ctx.bs.person.dom.person.info.numericitem;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

@IntegerRange(max = 30, min = 0)
public class IntegerPart extends IntegerPrimitiveValue<IntegerPart>{

	private static final long serialVersionUID = 1L;

	public IntegerPart(Integer rawValue) {
		super(rawValue);
	}

}
