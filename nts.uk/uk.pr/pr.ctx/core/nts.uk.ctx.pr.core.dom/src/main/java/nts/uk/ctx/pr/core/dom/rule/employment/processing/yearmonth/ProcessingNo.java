package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerMaxValue;
import nts.arc.primitive.constraint.IntegerMinValue;
import nts.arc.primitive.constraint.IntegerRange;

@IntegerRange(max = 5, min = 1)
public class ProcessingNo extends IntegerPrimitiveValue<ProcessingNo> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public ProcessingNo(Integer rawValue) {
		super(rawValue);
	}

}
