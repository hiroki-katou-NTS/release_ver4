package nts.uk.ctx.pr.core.dom.itemmaster.itemsalary;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerMaxValue;
import nts.arc.primitive.constraint.IntegerMinValue;

@IntegerMinValue(1900)
@IntegerMaxValue(9999)
public class Year extends IntegerPrimitiveValue<Year> {
	public Year(int value) {
		super(value);
	}
}
