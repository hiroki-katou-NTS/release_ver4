package nts.uk.shr.com.primitive.sample;

import nts.arc.primitive.HalfIntegerPrimitiveValue;
import nts.arc.primitive.constraint.HalfIntegerRange;

@HalfIntegerRange(min=1, max=9.5)
public class SampleHalfInt extends HalfIntegerPrimitiveValue<SampleHalfInt> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public SampleHalfInt(Double rawValue) {
		super(rawValue);
	}

}
