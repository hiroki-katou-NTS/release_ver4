package nts.uk.ctx.at.shared.dom.specialholiday.yearserviceset.primitives;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.PrimitiveValue;

public class Date extends IntegerPrimitiveValue<PrimitiveValue<Integer>>{
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	public Date(Integer rawValue){
		super(rawValue);
	}
}
