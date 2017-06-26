package nts.uk.ctx.sys.portal.dom.placement.primitive;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerMinValue;

/**
 * @author LamDT
 */
@IntegerMinValue(1)
public class Column extends IntegerPrimitiveValue<Column> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public Column(int rawValue) {
		super(rawValue);
	}

}
