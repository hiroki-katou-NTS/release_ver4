package nts.uk.shr.com.primitive.sample;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerMaxValue;
import nts.arc.primitive.constraint.IntegerMinValue;

/**
 * 処理日番号
 */
@IntegerMinValue(1)
@IntegerMaxValue(5)
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
