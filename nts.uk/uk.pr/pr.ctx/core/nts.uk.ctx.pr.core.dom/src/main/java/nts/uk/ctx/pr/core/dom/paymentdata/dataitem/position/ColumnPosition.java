package nts.uk.ctx.pr.core.dom.paymentdata.dataitem.position;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.IntegerRange;
import nts.arc.primitive.constraint.StringCharType;

/**
 * 項目位置（列）
 */
@StringCharType(CharType.NUMERIC)
@IntegerRange(min = 0, max = 99)
public class ColumnPosition extends IntegerPrimitiveValue<ColumnPosition> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public ColumnPosition(Integer rawValue) {
		super(rawValue);
	}

}
