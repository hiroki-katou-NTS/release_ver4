package nts.uk.ctx.bs.person.dom.person.setting.selectionitem;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;


@IntegerRange(min = 1, max = 10)
public class SelectionCodeLength extends IntegerPrimitiveValue<SelectionCodeLength> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SelectionCodeLength(int rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
