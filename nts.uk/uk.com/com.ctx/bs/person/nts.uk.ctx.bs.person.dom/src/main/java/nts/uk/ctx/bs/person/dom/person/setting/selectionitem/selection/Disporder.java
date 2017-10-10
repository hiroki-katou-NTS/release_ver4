package nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

@IntegerRange(min = 1, max = 9999)
public class Disporder extends IntegerPrimitiveValue<Disporder>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Disporder(int rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
