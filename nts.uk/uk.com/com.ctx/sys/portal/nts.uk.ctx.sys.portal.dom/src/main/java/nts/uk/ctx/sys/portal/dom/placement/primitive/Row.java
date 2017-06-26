package nts.uk.ctx.sys.portal.dom.placement.primitive;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerMinValue;

/**
 * @author LamDT
 */
@IntegerMinValue(1)
public class Row extends IntegerPrimitiveValue<Column> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public Row(int rawValue) {
		super(rawValue);
	}

}
