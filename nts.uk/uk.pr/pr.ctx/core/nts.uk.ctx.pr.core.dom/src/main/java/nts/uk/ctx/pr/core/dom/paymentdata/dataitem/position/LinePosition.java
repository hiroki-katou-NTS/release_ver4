package nts.uk.ctx.pr.core.dom.paymentdata.dataitem.position;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.IntegerRange;
import nts.arc.primitive.constraint.StringCharType;

/**
 * 行表示位置
 */
@StringCharType(CharType.NUMERIC)
@IntegerRange(min = 0, max = 99)
public class LinePosition extends IntegerPrimitiveValue<LinePosition> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public LinePosition(Integer rawValue) {
		super(rawValue);
	}

}
