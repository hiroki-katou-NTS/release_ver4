package nts.uk.ctx.bs.person.dom.person.setting.selection;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

@IntegerRange(min = 1, max = 99)
public class SelectionName extends IntegerPrimitiveValue<SelectionName>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SelectionName(int rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
